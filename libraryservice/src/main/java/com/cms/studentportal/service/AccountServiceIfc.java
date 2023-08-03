package com.cms.studentportal.service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cms.studentportal.exception.AuthenticationException;
import com.cms.studentportal.domain.Account;

public interface AccountServiceIfc {

	void createAccount(Account account);

	Account getAccountByStudentId(long studentId);

	Account loginStudent(@NotNull @NotEmpty long studentId, @NotNull @NotEmpty String password)
			throws AuthenticationException;


	
}
