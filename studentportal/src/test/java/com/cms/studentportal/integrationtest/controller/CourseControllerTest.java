package com.cms.studentportal.integrationtest.controller;

import com.cms.studentportal.doa.StudentRepositoryIfc;
import com.cms.studentportal.domain.Student;
import com.cms.studentportal.utils.AuthenticateUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.ServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mvc;


    @Autowired
    private StudentRepositoryIfc studentRepositoryIfc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() throws ServletException {

        Student firstStudentSignUp = new Student();
        firstStudentSignUp.setStudentId(6666666);
        firstStudentSignUp.setPassword(passwordEncoder.encode("cms"));
        firstStudentSignUp.setEmail("cms@mailinator.com");
        firstStudentSignUp.setFullname("cms");
        firstStudentSignUp.setUsername("cms");
        firstStudentSignUp.setMobileNumber("1111222");
        studentRepositoryIfc.save(firstStudentSignUp);

        Student secondStudentSignUp = new Student();
        secondStudentSignUp.setStudentId(9999999);
        secondStudentSignUp.setPassword(passwordEncoder.encode("cms1"));
        secondStudentSignUp.setEmail("cmsn@mailinator.com");
        secondStudentSignUp.setFullname("cms1");
        secondStudentSignUp.setUsername("cms1");
        secondStudentSignUp.setMobileNumber("1122222");
        studentRepositoryIfc.save(secondStudentSignUp);

        AuthenticateUtil.getHttpServletRequest().login("cms", "cms1");

    }

    @Test
    public void getCourses() throws Exception {
        mvc.perform(get("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.course_id").value("COMP637"));
    }
}
