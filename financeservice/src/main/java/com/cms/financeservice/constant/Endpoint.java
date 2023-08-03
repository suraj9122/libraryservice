package com.cms.financeservice.constant;

/**
 * <h1>Student portal API patterns</h1>
 */
public class Endpoint {

	public static final String ROOT_API_V1 = "/api/v1";

	public static final String ACCOUNT_CREATE_OR_GETS = "/accounts";
	public static final String GET_STUDENT_ACCOUNT = "/accounts/student/{studentId}";

	public static final String ACCOUNT_UPDATE_OR_DELETE_OR_GET = "/accounts/{id}";

	
	public static final String INVOICES_CREATE_OR_GETS = "/invoices";
	public static final String GET_INVOICES_BY_REFERENCE = "/invoices/reference/{reference}";
	
	public static final String PAY_INVOICE_BY_REFERENCE = "/invoices/{reference}/pay";
	public static final String CANCEL_INVOICE_BY_REFERENCE = "/invoices/{reference}/cancel";

	public static final String GET_INVOICE_BY_ID = "/invoices/{id}";

	

}
