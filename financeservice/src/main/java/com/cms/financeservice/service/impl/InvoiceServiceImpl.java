package com.cms.financeservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cms.financeservice.constant.StatusEnum;
import com.cms.financeservice.doa.AccountRepositoryIfc;
import com.cms.financeservice.doa.InvoiceRepositoryIfc;
import com.cms.financeservice.domain.Account;
import com.cms.financeservice.domain.Invoice;
import com.cms.financeservice.service.InvoiceServiceIfc;
import com.cms.financeservice.utils.Util;

@Service
public class InvoiceServiceImpl implements InvoiceServiceIfc {

	private InvoiceRepositoryIfc invoiceRepositoryIfc;
	private AccountRepositoryIfc accountRepositoryIfc;

	InvoiceServiceImpl(InvoiceRepositoryIfc invoiceRepositoryIfc, AccountRepositoryIfc accountRepositoryIfc) {
		this.invoiceRepositoryIfc = invoiceRepositoryIfc;
		this.accountRepositoryIfc = accountRepositoryIfc;
	}

	@Override
	public Invoice getInvoiceByReferenceId(String referenceId) {
		return invoiceRepositoryIfc.findByReferenceId(referenceId);
	}
	
	@Override
	public void cancelInvoiceByReferenceId(String referenceId) {
		Invoice invoiceByReferenceId = getInvoiceByReferenceId(referenceId);
		invoiceByReferenceId.setStatus(StatusEnum.CANCELLED);
		invoiceRepositoryIfc.save(invoiceByReferenceId);
	}

	@Override
	public void payInvoiceByReferenceId(String referenceId) throws Exception {
		Invoice invoiceByReferenceId = getInvoiceByReferenceId(referenceId);
		if(invoiceByReferenceId.getStatus() == StatusEnum.PAID){
			throw new Exception("Already paid for reference "+referenceId);
		}
		invoiceByReferenceId.setStatus(StatusEnum.PAID);
		invoiceRepositoryIfc.save(invoiceByReferenceId);
	}
	
	@Override
	public void payInvoiceThroughPortal(String referenceId, Model model) throws Exception {
		payInvoiceByReferenceId(referenceId);
	}

	@Override
	public String createInvoice(Invoice invoice) {
		long studentId = invoice.getAccount().getStudentId();
		Account accountByStudentId = accountRepositoryIfc.findByStudentId(studentId);
		invoice.setAccount(accountByStudentId);
		
		invoice.setReferenceId(Util.generateInvoiceReferenceId());
		invoice.setStatus(StatusEnum.OUTSTANDING);
		invoiceRepositoryIfc.save(invoice);
		return invoice.getReferenceId();
	}

	@Override
	public List<Invoice> getInvoicesByAccount(Account account) {
		return invoiceRepositoryIfc.findByAccount(account);
	}
	
	
	@Override
	public List<Invoice> getAllInvoicesByStudentId(long studentId) {
		Account account = accountRepositoryIfc.findByStudentId(studentId);
		List<Invoice> invoicesByAccount = getInvoicesByAccount(account);
		return invoicesByAccount;
	}
	
	@Override
	public List<Invoice> getAllInvoices() {
		List<Invoice> invoicesByAccount = invoiceRepositoryIfc.findAll();
		return invoicesByAccount;
	}
	
	@Override
	public boolean hasOutstandingBalance(long studentId) {
		List<Invoice> allInvoicesByStudentId = getAllInvoicesByStudentId(studentId);
		long outstandingCount = allInvoicesByStudentId.stream().filter(data -> data.getStatus().equals(StatusEnum.OUTSTANDING)).count();
		boolean isOutstandingBalance = outstandingCount> 0;
		return isOutstandingBalance;
	}

}
