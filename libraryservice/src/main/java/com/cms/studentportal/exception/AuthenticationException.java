package com.cms.studentportal.exception;

public class AuthenticationException extends org.springframework.security.core.AuthenticationException {

	public AuthenticationException(long studentId) {
		super("Credential does not match.");
	}

	public AuthenticationException() {
		super("User does not exist. Please register");
	}
}
