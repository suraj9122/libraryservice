package com.cms.financeservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import com.cms.financeservice.doa.AccountRepositoryIfc;
import com.cms.financeservice.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * these test case modified from provided finance services by Thalita Vergilio
 * */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    private final long studentId = 7777777;
    private Account account;
    private Account anotherAccount;
    @MockBean
    private AccountRepositoryIfc accountRepository;
    @Autowired
    private AccountServiceIfc accountService;

    @BeforeEach
    public void setUp() {
        account = new Account();
        account.setStudentId(studentId);
        long anotherStudentId = 3333333;
        anotherAccount = new Account();
        anotherAccount.setStudentId(anotherStudentId);
        Long anotherId = 2L;
        anotherAccount.setId(anotherId);
        Mockito.when(accountRepository.findByStudentId(studentId))
                .thenReturn(account);
        Mockito.when(accountRepository.findAll())
                .thenReturn(Arrays.asList(account, anotherAccount));
        Mockito.when(accountRepository.save(account))
                .thenReturn(account);
        Mockito.doNothing().when(accountRepository).delete(account);
    }

    @Test
    void testGetAccountByStudentId_withValidID_ReturnsExistingAccount() {
        Account result = accountService.getAccountByStudentId(studentId);
        assertThat(studentId == result.getStudentId());
    }

    @Test
    void testGetAccountByStudentId_withInValidID_throwsException() {
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByStudentId(8752115),
                "Exception was not thrown.");
    }

    @Test
    void testGetAccountByStudentId_withEmptyID_throwsException() {
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByStudentId(0l),
                "Exception was not thrown.");
    }

    @Test
    void testGetAccountByStudentId_withNullID_throwsException() {
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountByStudentId(-125l),
                "Exception was not thrown.");
    }


    @Test
    void testGetAllAccounts_returnsExistingAccounts() {
    	List<Account> result = accountService.getAccounts();
        assertEquals(2, result.size());
        assertThat(result.containsAll(Arrays.asList(account, anotherAccount)));
    }

    @Test
    void testDeleteAccount_withValidId_deletesAccount() {
        accountService.deleteAccount(studentId);
        verify(accountRepository, times(1)).delete(account);
    }
}