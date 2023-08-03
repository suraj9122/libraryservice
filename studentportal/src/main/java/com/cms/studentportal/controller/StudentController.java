package com.cms.studentportal.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.servlet.ServletException;

import com.cms.studentportal.exception.CourseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cms.studentportal.constant.Endpoint;
import com.cms.studentportal.domain.Student;
import com.cms.studentportal.exception.AuthenticationException;
import com.cms.studentportal.exception.UserAlreadyEnrollIntoCourseException;
import com.cms.studentportal.service.StudentServiceIfc;
import com.cms.studentportal.utils.AuthenticateUtil;;

@RestController
@RequestMapping(value = Endpoint.ROOT_API_V1 + Endpoint.STUDENT_URI)
public class StudentController {

	private StudentServiceIfc studentServiceIfc;

	StudentController(StudentServiceIfc studentServiceIfc) {
		this.studentServiceIfc = studentServiceIfc;
	}

	@PostMapping("/login")
	public @ResponseBody ResponseEntity<Student> loginApi(@RequestBody Student studentLogin) throws CourseNotFoundException, UserAlreadyEnrollIntoCourseException {

		if (!AuthenticateUtil.isAuthenticate()) {
			try {
				AuthenticateUtil.getHttpServletRequest().login(studentLogin.getUsername(), studentLogin.getPassword());
			} catch (ServletException e) {
				throw new AuthenticationException("Invalid username or password");
			}
		}

		Student student = new Student();
		student.setStudentId(AuthenticateUtil.getStudentId());

		student.add(linkTo(methodOn(StudentController.class).getStudent()).withRel("get_profile"));
		student.add(linkTo(methodOn(StudentController.class).updateStudent(new Student())).withRel("update_profile"));
		student.add(linkTo(methodOn(EnrollmentController.class).getEnrollments()).withRel("my_enrollments"));
		student.add(linkTo(methodOn(CourseController.class).getCourses()).withRel("all_courses"));

		return ResponseEntity.status(HttpStatus.OK).body(student);
	}

	@GetMapping
	public @ResponseBody ResponseEntity<Student> getStudent() {
		Student student = studentServiceIfc.getStudentByIdWithoutPassword(AuthenticateUtil.getStudentId());
		student.add(linkTo(methodOn(StudentController.class).updateStudent(new Student())).withRel("update_profile"));
		return ResponseEntity.status(HttpStatus.OK).body(student);
	}

	@PostMapping("/register")
	public @ResponseBody ResponseEntity<Student> registerStudent(@RequestBody Student studentRegister) throws CourseNotFoundException, UserAlreadyEnrollIntoCourseException {
		studentServiceIfc.createStudent(studentRegister);

		Student student = new Student();
		student.add(linkTo(methodOn(StudentController.class).loginApi(new Student())).withRel("login"));

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping
	public @ResponseBody ResponseEntity updateStudent(@RequestBody Student student) {
		studentServiceIfc.updateStudent(student);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
