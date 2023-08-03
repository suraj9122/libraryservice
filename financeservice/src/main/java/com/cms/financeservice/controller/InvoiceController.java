package com.cms.financeservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.financeservice.domain.Invoice;
import com.cms.financeservice.service.InvoiceServiceIfc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cms.financeservice.constant.Endpoint;

@RestController
@RequestMapping(value = Endpoint.ROOT_API_V1)
public class InvoiceController {

	private InvoiceServiceIfc invoiceServiceIfc;
	
	InvoiceController(InvoiceServiceIfc invoiceServiceIfc) {
		this.invoiceServiceIfc = invoiceServiceIfc;
	}
	

	@PostMapping(Endpoint.INVOICES_CREATE_OR_GETS)
	public @ResponseBody ResponseEntity createInvoiceAccount(@RequestBody Invoice invoice) {
		invoiceServiceIfc.createInvoice(invoice);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@GetMapping("/accounts/student/{studentId}")
	public @ResponseBody ResponseEntity hasOutstandingBalance(@PathVariable String studentId) {
		boolean isOutstandingBalance = invoiceServiceIfc.hasOutstandingBalance(Long.valueOf(studentId));
		
		Map<String, Boolean> map= new HashMap();
		map.put("hasOutstandingBalance", isOutstandingBalance);
		
		return ResponseEntity.status(HttpStatus.OK).body(map);
	}
	

	@GetMapping(Endpoint.INVOICES_CREATE_OR_GETS)
	public @ResponseBody ResponseEntity getInvoiceAccount() {
		List<Invoice> allInvoices = invoiceServiceIfc.getAllInvoices();
		return ResponseEntity.status(HttpStatus.OK).body(allInvoices);
	}

	@GetMapping(Endpoint.GET_INVOICES_BY_REFERENCE)
	public @ResponseBody ResponseEntity getInvoiceAccountByRefId(@PathVariable String reference) {
		Invoice invoice = invoiceServiceIfc.getInvoiceByReferenceId(reference);
		return ResponseEntity.status(HttpStatus.OK).body(invoice);
	}

	@PutMapping(Endpoint.CANCEL_INVOICE_BY_REFERENCE)
	public @ResponseBody ResponseEntity cancelInvoiceByReferenceId(@PathVariable String reference) {
		invoiceServiceIfc.cancelInvoiceByReferenceId(reference);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping(Endpoint.GET_INVOICES_BY_REFERENCE)
	public @ResponseBody ResponseEntity payInvoiceAccountByRefId(@PathVariable String reference) throws Exception {
		invoiceServiceIfc.payInvoiceByReferenceId(reference);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
