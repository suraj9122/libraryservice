package com.cms.studentportal.exception;

public class AuthenticationException extends org.springframework.security.core.AuthenticationException {

	public AuthenticationException(String usernamee) {
		super("Credential does not match.");
	}

	public AuthenticationException(long studentid) {
		super("Student not found. Please authenticate before accessing resources.");
	}

	public AuthenticationException() {
		super("User does not exist. Please register");
	}
}
