package com.cms.financeservice.service.impl;

import java.util.List;

import com.cms.financeservice.doa.AccountRepositoryIfc;
import com.cms.financeservice.domain.Account;
import com.cms.financeservice.service.AccountServiceIfc;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountServiceIfc {

	private AccountRepositoryIfc accountRepositoryIfc;

	AccountServiceImpl(AccountRepositoryIfc accountRepositoryIfc) {
		this.accountRepositoryIfc = accountRepositoryIfc;
	}

	@Override
	public void createAccount(Account account) {
		accountRepositoryIfc.save(account);
	}

	@Override
	public Account getAccountByStudentId(long studentId) {
		return accountRepositoryIfc.findByStudentId(studentId);
	}

	@Override
	public List<Account> getAccounts() {
		return accountRepositoryIfc.findAll();
	}
	
	@Override
	public void deleteAccount(long studentId) {
		Account findByStudentId = accountRepositoryIfc.findByStudentId(studentId);
		accountRepositoryIfc.delete(findByStudentId);
	}
	
	
	
	
}
