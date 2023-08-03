package com.cms.studentportal.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.cms.studentportal.exception.AlreadyBorrowedThisBookException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cms.studentportal.constant.Endpoint;
import com.cms.studentportal.domain.ManagesBook;
import com.cms.studentportal.exception.CourseNotFoundException;
import com.cms.studentportal.service.impl.ManagesBookServiceImpl;

@RestController
@RequestMapping(value = Endpoint.ROOT_API_V1 + Endpoint.MANAGE_BOOK_URI)
public class ManagesBookController {

	private ManagesBookServiceImpl managesBookServiceImpl;

	ManagesBookController(ManagesBookServiceImpl managesBookServiceImpl) {
		this.managesBookServiceImpl = managesBookServiceImpl;
	}

	@GetMapping
	public @ResponseBody ResponseEntity<CollectionModel<ManagesBook>> getEnrollments() {

		List<ManagesBook> enrolCourses = managesBookServiceImpl.getManagesBook();
		CollectionModel<ManagesBook> collectionModel = CollectionModel.of(enrolCourses);
		collectionModel.add(linkTo(methodOn(ManagesBookController.class).getEnrollments()).withSelfRel());

		return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
	}

	@PostMapping
	public @ResponseBody ResponseEntity enrollIntoCourse(@RequestParam("isbn") String isbn) throws CourseNotFoundException, AlreadyBorrowedThisBookException {
		managesBookServiceImpl.borrowBook(isbn);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
