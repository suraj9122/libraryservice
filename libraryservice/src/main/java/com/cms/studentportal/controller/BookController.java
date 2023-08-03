package com.cms.studentportal.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.cms.studentportal.exception.AlreadyBorrowedThisBookException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cms.studentportal.constant.Endpoint;
import com.cms.studentportal.domain.Book;
import com.cms.studentportal.exception.CourseNotFoundException;
import com.cms.studentportal.service.BookServiceIfc;

@RestController
@RequestMapping(value = Endpoint.ROOT_API_V1)
public class BookController {

	private BookServiceIfc bookServiceIfc;

	BookController(BookServiceIfc bookServiceIfc) {
		this.bookServiceIfc = bookServiceIfc;
	}

	@GetMapping(value = Endpoint.VIEW_BOOK_URI)
	public @ResponseBody ResponseEntity<CollectionModel<Book>> getBooks() throws CourseNotFoundException, AlreadyBorrowedThisBookException {
		List<Book> courseList = this.bookServiceIfc.findAllBook();

		for (Book course : courseList) {
			course.add(linkTo(methodOn(ManagesBookController.class).enrollIntoCourse(course.getIsbn()))
					.withRel("enroll_into_course"));
		}

		CollectionModel<Book> collectionModel = CollectionModel.of(courseList);
		collectionModel.add(linkTo(methodOn(BookController.class).getBooks()).withSelfRel());
		collectionModel.add(linkTo(methodOn(BookController.class).searchBooks("search_keyword_here")).withRel("search"));
		return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
	}

	@GetMapping(value = Endpoint.SEARCH_BOOK_URI)
	public @ResponseBody ResponseEntity<CollectionModel<Book>> searchBooks(@RequestParam String title) throws CourseNotFoundException, AlreadyBorrowedThisBookException {
		List<Book> courseList = this.bookServiceIfc.searchBooks(title);

		for (Book course : courseList) {
			course.add(linkTo(methodOn(ManagesBookController.class).enrollIntoCourse(course.getIsbn()))
					.withRel("enroll_into_course"));
		}

		CollectionModel<Book> collectionModel = CollectionModel.of(courseList);
		collectionModel.add(linkTo(methodOn(BookController.class).searchBooks(title)).withSelfRel());
		collectionModel
				.add(linkTo(methodOn(BookController.class).getBooks()).withRel(IanaLinkRelations.COLLECTION));
		return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
	}

}
