package com.cms.studentportal.config;

import java.util.List;

import com.cms.studentportal.domain.Book;
import com.cms.studentportal.service.BookServiceIfc;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class BookInitializer {

	private BookServiceIfc bookServiceIfc;

	BookInitializer(BookServiceIfc bookServiceIfc) {
		this.bookServiceIfc = bookServiceIfc;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void appInit() {
		loadCourse();
	}

	private void loadCourse() {
		List<Book> books = bookServiceIfc.findAllBook();
		if (books.isEmpty()) {

			Book book = new Book("9780073523323", "Database System Concepts", "Abraham Silberschatz", 2010, 5);
			this.bookServiceIfc.createBook(book);

			Book book2 = new Book("9781449369415", "Introduction To Machine Learning With Python", "Andreas C. Muller", 2016, 2);
			this.bookServiceIfc.createBook(book2);
			
			Book book3 = new Book("9780321349606", "Java concurrency in practice", "Brian Goetz", 2015, 2);
			this.bookServiceIfc.createBook(book3);
			
			Book book4 = new Book("9780596516178", "The Ruby Programming Language", "David Flanagan", 2008, 1);
			this.bookServiceIfc.createBook(book4);
			
			Book book5 = new Book("9781491952023", "JavaScript: The Definitive Guide", "David Flanagan", 2020, 2);
			this.bookServiceIfc.createBook(book5);
			
			Book book6 = new Book("9780135957059", "The Pragmatic Programmer", "David Thomas", 2019, 1);
			this.bookServiceIfc.createBook(book6);
			
			Book book7 = new Book("9781937785499", "Programming Ruby 1.9 & 2.0 - The Pragmatic Programmers' Guide", "David Thomas", 2013, 2);
			this.bookServiceIfc.createBook(book7);
			
			Book book8 = new Book("9781593279288", "Python Crash Course, 2nd Edition", "Eric Mathes", 2019, 1);
			this.bookServiceIfc.createBook(book8);
			
			Book book9 = new Book("9783827330437", "Design Patterns - Elements Of Reusable Object-Oriented Software", "Erich Gamma", 1995, 1);
			this.bookServiceIfc.createBook(book9);
			
			Book book10 = new Book("9781430265337", "Introducing Spring Framework - A Primer", "Felipe Gutierrez", 2014, 1);
			this.bookServiceIfc.createBook(book10);
			
			Book book11 = new Book("9781491956250", "Microservice Architecture", "Irakli Nadareishvili", 2016, 2);
			this.bookServiceIfc.createBook(book11);
			
			Book book12 = new Book("9789813221871", "An Introduction To Component-Based Software Development	", "Lau Kung-Kiu", 2017, 0);
			this.bookServiceIfc.createBook(book12);
			
			Book book13 = new Book("9781617298691", "Spring Start Here - Learn What You Need And Learn It Well", "Laurentiu Spilca", 2021, 2);
			this.bookServiceIfc.createBook(book13);
			
			Book book14 = new Book("9781543057386", "Distributed systems (3rd edition)", "Maarten van Steen", 2017, 2);
			this.bookServiceIfc.createBook(book14);
			
		}
	}

}
