package com.cms.studentportal.unittest.util;

import com.cms.studentportal.exception.AuthenticationException;
import com.cms.studentportal.utils.AuthenticateUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthenticateUtilTest {

	@Test
	public void isAuthenticate_without_session(){
		boolean  authenticate = AuthenticateUtil.isAuthenticate();
		assertThat(authenticate).isFalse();
	}

	@Test
	public void isAuthenticate_with_session(){
		boolean  authenticate = AuthenticateUtil.isAuthenticate();
		assertThat(authenticate).isTrue();
	}


	@Test
	public void getStudentId_without_session(){
		AuthenticationException exception = assertThrows(AuthenticationException.class, () -> AuthenticateUtil.getStudentId());
		assertThat(exception.getMessage()).isEqualTo("Student not found. Please authenticate before accessing resources.");
	}
}
