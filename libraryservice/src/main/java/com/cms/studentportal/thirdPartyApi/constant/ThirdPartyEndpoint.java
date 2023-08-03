package com.cms.studentportal.thirdPartyApi.constant;

/**
 * <h1>Third Party API</h1> This is the all api used to access third party
 * service within this system
 * 
 */
public class ThirdPartyEndpoint {

	private static String FINANCE = "http://localhost:8081/api/v1";

	public static String CREATE_FINANCE_INVOICE = FINANCE + "/invoices";

}
