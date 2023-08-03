package com.cms.studentportal.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import com.cms.studentportal.exception.AlreadyBorrowedThisBookException;
import com.cms.studentportal.exception.BookAlreadyReturnException;
import com.cms.studentportal.exception.BookHasNotBorrowedException;
import com.cms.studentportal.exception.CourseNotFoundException;
import com.cms.studentportal.thirdPartyApi.constant.PaymentType;
import com.cms.studentportal.thirdPartyApi.service.ThirdPartyAPIServiceIfc;
import org.springframework.stereotype.Service;

import com.cms.studentportal.doa.BookRepositoryIfc;
import com.cms.studentportal.doa.ManagesBookRepositoryIfc;
import com.cms.studentportal.domain.Book;
import com.cms.studentportal.domain.ManagesBook;
import com.cms.studentportal.service.ManagesBookServiceIfc;
import com.cms.studentportal.utils.AuthenticateUtil;

@Service
public class ManagesBookServiceImpl implements ManagesBookServiceIfc {

	private ManagesBookRepositoryIfc managesBookRepositoryIfc;

	private BookRepositoryIfc bookRepositoryIfc;

	private ThirdPartyAPIServiceIfc thirdPartyAPIServiceIfc;
	
	private final static int NUMBER_OF_DAYS_BOOK_BORROW = 10;

	ManagesBookServiceImpl(ManagesBookRepositoryIfc managesBookRepositoryIfc, BookRepositoryIfc bookRepositoryIfc,
			ThirdPartyAPIServiceIfc thirdPartyAPIServiceIfc) {
		this.managesBookRepositoryIfc = managesBookRepositoryIfc;
		this.bookRepositoryIfc = bookRepositoryIfc;
		this.thirdPartyAPIServiceIfc = thirdPartyAPIServiceIfc;
	}

	@Override
	public List<ManagesBook> getManagesBook() {
		long studentId = AuthenticateUtil.getStudentId();
		return managesBookRepositoryIfc.findByStudentId(studentId);
	}

	@Transactional
	@Override
	public void borrowBook(String isbn) throws CourseNotFoundException, AlreadyBorrowedThisBookException {
		Book book = bookRepositoryIfc.findByIsbn(isbn);
		if (book == null) {
			throw new CourseNotFoundException(isbn);
		}

		long studentId = AuthenticateUtil.getStudentId();

		// check student is already enroll into course
		ManagesBook alreadyEnroll = managesBookRepositoryIfc.findByStudentIdAndBook(studentId, book);
		if (alreadyEnroll != null && alreadyEnroll.getDateReturn() == null) {
			throw new AlreadyBorrowedThisBookException(book.getTitle());
		}

		ManagesBook managesBook = new ManagesBook();
		managesBook.setStudentId(studentId);
		managesBook.setDateBorrow(LocalDateTime.now());
		managesBook.setBook(book);
		managesBookRepositoryIfc.save(managesBook);

		thirdPartyAPIServiceIfc.createFinanceServiceInvoice(studentId, new BigDecimal(1.00), PaymentType.LIBRARY_FEES);
		
		int remainingCopies = book.getCopies() - 1;
		book.setCopies(remainingCopies);
		bookRepositoryIfc.save(book);

	}
	
	@Transactional
	@Override
	public void returnBook(String isbn) throws CourseNotFoundException, BookHasNotBorrowedException, BookAlreadyReturnException {
		Book book = bookRepositoryIfc.findByIsbn(isbn);
		if (book == null) {
			throw new CourseNotFoundException(isbn);
		}

		long studentId = AuthenticateUtil.getStudentId();

		// check student is already enroll into course
		ManagesBook alreadyEnroll = managesBookRepositoryIfc.findByStudentIdAndBook(studentId, book);
		if(alreadyEnroll == null) {
			throw new BookHasNotBorrowedException(book.getTitle());

		}
		
		if(alreadyEnroll.getDateReturn() != null) {
			throw new BookAlreadyReturnException(book.getTitle());
		}
		
		LocalDateTime todayDate = LocalDateTime.now();
		alreadyEnroll.setDateReturn(todayDate);
		managesBookRepositoryIfc.save(alreadyEnroll);

		//for fine purpose
		LocalDateTime dateBorrow = alreadyEnroll.getDateBorrow();
		
		// if return date - NUMBER_OF_DAYS_BOOK_BORROW > dateBorrow then books does not return in time
		if(todayDate.minusDays(NUMBER_OF_DAYS_BOOK_BORROW).isAfter(dateBorrow)) {
			thirdPartyAPIServiceIfc.createFinanceServiceInvoice(studentId, new BigDecimal(3.00), PaymentType.LIBRARY_FINE);
		}
		
		int remainingCopies = book.getCopies() + 1;
		book.setCopies(remainingCopies);
		bookRepositoryIfc.save(book);
	}

}
