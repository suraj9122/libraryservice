package com.cms.studentportal.utils;

import org.apache.commons.lang3.RandomUtils;

public class Util {

	/**
	 * <h1>Auto generate Student Id</h1> This method is used to generate student id
	 * for student record
	 */
	public static long generateStudentId() {
		Long studentId = RandomUtils.nextLong(0, 999999);
		return studentId;
	}

}
