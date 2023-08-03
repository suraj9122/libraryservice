package com.cms.financeservice.service;

import java.util.List;

import com.cms.financeservice.domain.Account;

public interface AccountServiceIfc {

	void createAccount(Account account);

	Account getAccountByStudentId(long studentId);

	List<Account> getAccounts();

	void deleteAccount(long studentId);
	
}
