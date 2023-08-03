package com.cms.studentportal.thirdPartyApi.service;

import java.math.BigDecimal;

import com.cms.studentportal.thirdPartyApi.constant.PaymentType;

/**
 * <h1>This class deal with all other microservice to communicate</h1>
 */
public interface ThirdPartyAPIServiceIfc {

	/**
	 * Finance microservice to create a invoice for student as per event
	 */
	void createFinanceServiceInvoice(long studentId, BigDecimal amount, PaymentType paymentType);

	/**
	 * Finance microservice to create a account within a finance department for
	 * student
	 */
	void createFinanceAccount(long studentId);

	/**
	 * Library microservice to create a account within a library department for
	 * student
	 */
	void createLibraryAccount(long studentId);

	/**
	 * Finance microservice to see if there are any outstanding invoices.
	 */
	boolean isEligibleGraduation(long studentId);

}
