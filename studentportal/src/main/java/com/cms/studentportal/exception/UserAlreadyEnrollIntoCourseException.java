package com.cms.studentportal.exception;

public class UserAlreadyEnrollIntoCourseException extends Exception{

	public UserAlreadyEnrollIntoCourseException(String courseTitle){
		super("User has already enrol into this course "+courseTitle);
	}
	
}
