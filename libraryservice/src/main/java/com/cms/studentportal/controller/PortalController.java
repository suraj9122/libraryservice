package com.cms.studentportal.controller;

import java.util.List;

import com.cms.studentportal.exception.AlreadyBorrowedThisBookException;
import com.cms.studentportal.exception.BookAlreadyReturnException;
import com.cms.studentportal.exception.BookHasNotBorrowedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cms.studentportal.domain.Account;
import com.cms.studentportal.domain.ManagesBook;
import com.cms.studentportal.exception.CourseNotFoundException;
import com.cms.studentportal.service.BookServiceIfc;
import com.cms.studentportal.service.ManagesBookServiceIfc;
import com.cms.studentportal.utils.AuthenticateUtil;

@Controller
@RequestMapping("")
public class PortalController {

	private BookServiceIfc bookServiceIfc;

	private ManagesBookServiceIfc managesBookServiceIfc;

	PortalController(BookServiceIfc bookServiceIfc, ManagesBookServiceIfc managesBookServiceIfc) {
		this.bookServiceIfc = bookServiceIfc;
		this.managesBookServiceIfc = managesBookServiceIfc;
	}

	@GetMapping("/")
	public String redirectToLogin() {
		return "redirect:/login";
	}

	@GetMapping({ "login" })
	public String login(Model model) {
		if (AuthenticateUtil.isAuthenticate()) {
			return "redirect:/dashboard";
		}
		model.addAttribute("student", new Account());
		return "index";
	}

	@GetMapping({ "/dashboard" })
	public String dashboardPortalPage(Model model) {
		model.addAttribute("student", new Account());
		return "dashboard";
	}

	@GetMapping({ "/books" })
	public String viewBooks(Model model) {
		model.addAttribute("books", bookServiceIfc.findAllBook());
		return "view-books-and-borrow";
	}

	@GetMapping({ "/logout" })
	public String logout(Model model) {
		return "redirect:/login";
	}
	
	@GetMapping({ "/my-books" })
	public String viewMyBooks(Model model) {
		List<ManagesBook> books = managesBookServiceIfc.getManagesBook();
		model.addAttribute("books", books);
		return "manage-my-books";
	}

	@GetMapping({ "/book/borrow/{isbn}" })
	public String borrowBook(@PathVariable("isbn") String isbn) throws CourseNotFoundException, AlreadyBorrowedThisBookException {
		managesBookServiceIfc.borrowBook(isbn);
		return "redirect:/my-books";
	}
	
	@GetMapping({ "/book/return/{isbn}" })
	public String returnBook(@PathVariable("isbn") String isbn) throws CourseNotFoundException, BookHasNotBorrowedException, BookAlreadyReturnException {
		managesBookServiceIfc.returnBook(isbn);
		return "redirect:/my-books";
	}

}
