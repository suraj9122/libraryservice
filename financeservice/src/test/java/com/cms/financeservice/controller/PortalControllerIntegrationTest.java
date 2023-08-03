package com.cms.financeservice.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.cms.financeservice.doa.AccountRepositoryIfc;
import com.cms.financeservice.doa.InvoiceRepositoryIfc;
import com.cms.financeservice.domain.Account;
import com.cms.financeservice.domain.Invoice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.cms.financeservice.constant.PaymentTypeEnum;
import com.cms.financeservice.constant.StatusEnum;

/**
 * these test case modified from provided finance services by Thalita Vergilio
 * */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class PortalControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepositoryIfc accountRepository;

    @Autowired
    private InvoiceRepositoryIfc invoiceRepository;

    private String invoiceReference;
    private Invoice outstandingInvoice;

    @BeforeEach
    public void setUp() {
        Account account = new Account();
        account.setStudentId(6666666);
        accountRepository.save(account);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        outstandingInvoice =  new Invoice(null,account, new BigDecimal(10.00), calendar.getTime(), PaymentTypeEnum.LIBRARY_FINE, StatusEnum.OUTSTANDING );
        Invoice outstandingInvoiceSaved = invoiceRepository.save(outstandingInvoice);
        invoiceReference = outstandingInvoiceSaved.getReferenceId();
    }

    @Test
    public void givenInvoice_whenPostFindInvoice_thenStatus200() throws Exception {
        mvc.perform(post("/portal/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reference\": \"" + invoiceReference + "\"}")
                        .flashAttr("invoice", outstandingInvoice))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(invoiceReference)));
    }

    @Test
    public void givenNoInvoice_whenPostFindInvoice_thenStatus404() throws Exception {
        outstandingInvoice.setReferenceId("BBBB9999");
        mvc.perform(post("/portal/invoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reference\": \"BBBB9999\"}")
                        .flashAttr("invoice", outstandingInvoice))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Could not find invoice for reference BBBB9999"));
    }

    @Test
    public void givenInvoice_whenPostPayInvoice_thenStatus200_andInvoicePaid() throws Exception {
        mvc.perform(post("/portal/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("invoice", outstandingInvoice))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("PAID")));
    }

    @Test
    public void givenInvoiceWithNullReference_whenPostPayInvoice_thenStatus404() throws Exception {
        outstandingInvoice.setReferenceId(null);
        mvc.perform(post("/portal/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("invoice", outstandingInvoice))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Could not find invoice."));
    }

    @Test
    public void givenInvoiceWithNonExistingReference_whenPostPayInvoice_thenStatus404() throws Exception {
        outstandingInvoice.setReferenceId("XXXXXXXX");
        mvc.perform(post("/portal/pay")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("invoice", outstandingInvoice))
                .andExpect(status().isNotFound())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Could not find invoice for reference XXXXXXXX"));
    }

    @Test
    public void whenGetPortal_thenStatus200() throws Exception {
        mvc.perform(get("/portal")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("Invoice Payment Portal")));
    }

    @Test
    public void whenGetPortalInvoice_thenStatus200() throws Exception {
        mvc.perform(get("/portal/invoice")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("Invoice Payment Portal")));
    }

    @Test
    public void whenGetRoot_thenRedirectToPortal_AndStatus302() throws Exception {
        mvc.perform(get("/")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/portal"));
    }

    @AfterEach
    public void tearDown() {
        invoiceRepository.deleteAll();
    }
}
