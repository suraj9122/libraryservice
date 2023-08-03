package com.cms.studentportal.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cms.studentportal.doa.BookRepositoryIfc;
import com.cms.studentportal.domain.Book;
import com.cms.studentportal.service.BookServiceIfc;

@Service
public class BookServiceImpl implements BookServiceIfc {

	private BookRepositoryIfc bookRepositoryIfc;

	BookServiceImpl(BookRepositoryIfc bookRepositoryIfc) {
		this.bookRepositoryIfc = bookRepositoryIfc;
	}

	@Override
	public void createBook(Book course) {
		bookRepositoryIfc.save(course);
	}

	@Override
	public List<Book> findAllBook() {
		return bookRepositoryIfc.findAll();
	}

	@Override
	public List<Book> searchBooks(String name) {
		return bookRepositoryIfc.searchByTitleKeyword(name);
	}

}
