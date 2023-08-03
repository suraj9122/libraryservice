package com.cms.financeservice.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class Util {

	/**
	 * <h1>Auto generate Student Id</h1> This method is used to generate student id
	 * for student record
	 */
	public static String generateInvoiceReferenceId() {
		String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(6);
		return randomAlphanumeric;
	}

}
