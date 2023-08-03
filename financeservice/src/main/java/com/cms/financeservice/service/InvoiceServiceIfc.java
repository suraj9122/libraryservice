package com.cms.financeservice.service;

import java.util.List;

import org.springframework.ui.Model;

import com.cms.financeservice.domain.Account;
import com.cms.financeservice.domain.Invoice;

public interface InvoiceServiceIfc {
	
	
	/**
	 * <h1>This method get invoice by particular reference Id</h1>
	 * 
	 * @param referenceId hold particular id of invoice 
	 */
	Invoice getInvoiceByReferenceId(String referenceId);

	void payInvoiceByReferenceId(String referenceId) throws Exception;

	String createInvoice(Invoice invoice);
	
	List<Invoice> getInvoicesByAccount(Account account);

	List<Invoice> getAllInvoicesByStudentId(long studentId);

	boolean hasOutstandingBalance(long studentId);

	List<Invoice> getAllInvoices();

	void cancelInvoiceByReferenceId(String referenceId);

	void payInvoiceThroughPortal(String referenceId, Model model) throws Exception;

}
