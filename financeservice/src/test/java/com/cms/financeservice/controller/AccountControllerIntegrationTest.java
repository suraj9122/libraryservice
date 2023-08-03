package com.cms.financeservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.cms.financeservice.doa.AccountRepositoryIfc;
import com.cms.financeservice.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * these test case modified from provided finance services by Thalita Vergilio
 * */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepositoryIfc accountRepository;

    @BeforeEach
    public void setUp() {
        Account account1 = new Account();
        account1.setStudentId(6666666);
        Account account2 = new Account();
        account2.setStudentId(9999999);
        accountRepository.saveAll(List.of(account1, account2));
    }

    @Test
    public void a_givenAccount_whenGetAccountById_thenStatus200() throws Exception {
        mvc.perform(get("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.studentId").value("6666666"))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void b_givenAccount_whenDelete_thenStatus204() throws Exception {
        mvc.perform(delete("/api/v1/accounts/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void givenAccounts_whenGetAccounts_thenStatus200() throws Exception {
        mvc.perform(get("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.accountList[0].studentId").value("6666666"))
                .andExpect(jsonPath("$._embedded.accountList[1].studentId").value("9999999"));
    }

    @Test
    public void givenNoAccounts_whenGetAccounts_thenStatus200_andLinkToSelf() throws Exception {
        accountRepository.deleteAll();
        mvc.perform(get("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links.self.href").isNotEmpty());
    }


    @Test
    public void givenNoAccount_whenGetAccountById_thenStatus404() throws Exception {
        mvc.perform(get("/api/v1/accounts/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenAccount_whenGetAccountByStudentId_thenStatus200() throws Exception {
        mvc.perform(get("/api/v1/accounts/student/c6666666")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.studentId").value("6666666"));
    }

    @Test
    public void givenNoAccount_whenGetAccountByStudentId_thenStatus404() throws Exception {
        mvc.perform(get("/api/v1/accounts/student/c0000000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNoAccount_whenPostNewAccount_thenStatus201() throws Exception {
        mvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\": \"3429928\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value("3429928"))
                .andExpect(jsonPath("$.hasOutstandingBalance").value(false));
    }

    @Test
    public void givenExistingAccount_whenPostNewAccount_thenStatus422() throws Exception {
        mvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\": \"6666666\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("An account already exists for student ID 6666666."));
    }

    @Test
    public void whenPostNewAccount_withEmptyAccountValue_thenStatus422() throws Exception {
        mvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\": \"\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void whenPostNewAccount_withEmptyJson_thenStatus400() throws Exception {
        mvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNoAccount_whenDelete_thenStatus404() throws Exception {
        mvc.perform(delete("/api/v1/accounts/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find account 1000"));
    }

    @AfterEach
    public void tearDown() {
        accountRepository.deleteAll();
    }
}
