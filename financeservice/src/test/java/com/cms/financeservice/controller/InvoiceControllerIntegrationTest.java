package com.cms.financeservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;

import com.cms.financeservice.doa.AccountRepositoryIfc;
import com.cms.financeservice.doa.InvoiceRepositoryIfc;
import com.cms.financeservice.domain.Account;
import com.cms.financeservice.domain.Invoice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
@TestMethodOrder(MethodOrderer.MethodName.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class InvoiceControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AccountRepositoryIfc accountRepository;

    @Autowired
    private InvoiceRepositoryIfc invoiceRepository;

    private String outstandingInvoiceReference;
    private String paidInvoiceReference;
    private String cancelledInvoiceReference;

    @BeforeEach
    public void setUp() {
        Account account = new Account();
        account.setStudentId(6666666);
        accountRepository.save(account);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        Invoice outstandingInvoice = new Invoice(null,account, new BigDecimal(10.50), calendar.getTime(), PaymentTypeEnum.TUITION_FEES, StatusEnum.OUTSTANDING );
        outstandingInvoice.setStatus(StatusEnum.OUTSTANDING);
        Invoice outstandingInvoiceSaved = invoiceRepository.save(outstandingInvoice);
        outstandingInvoiceReference = outstandingInvoiceSaved.getReferenceId();
        Invoice paidInvoice = new Invoice(new BigDecimal(5.30), LocalDate.of(2022, Month.JANUARY, 10), PaymentTypeEnum.LIBRARY_FINE, account);
        paidInvoice.setStatus(StatusEnum.PAID);
        Invoice paidInvoiceSaved = invoiceRepository.save(paidInvoice);
        paidInvoiceReference = paidInvoiceSaved.getReferenceId();
        Invoice cancelledInvoice = new Invoice(new BigDecimal(1.00), LocalDate.of(2022, Month.FEBRUARY, 28), PaymentTypeEnum.LIBRARY_FINE, account);
        cancelledInvoice.setStatus(StatusEnum.CANCELLED);
        Invoice cancelledInvoiceSaved = invoiceRepository.save(cancelledInvoice);
        cancelledInvoiceReference = cancelledInvoiceSaved.getReferenceId();
    }

    @Test
    public void a_givenInvoice_whenGetAccountById_thenStatus200() throws Exception {
        mvc.perform(get("/api/v1/invoices/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.studentId").value("6666666"))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void givenInvoice_whenDelete_thenStatus200_andInvoiceCancelled() throws Exception {
        mvc.perform(delete("/api/v1/invoices/" + outstandingInvoiceReference + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.reference").value(outstandingInvoiceReference));
    }

    @Test
    public void givenOutstandingInvoice_whenPay_thenStatus200_andInvoicePaid() throws Exception {
        mvc.perform(put("/api/v1/invoices/" + outstandingInvoiceReference + "/pay")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.reference").value(outstandingInvoiceReference));
    }

    @Test
    public void givenPaidInvoice_whenPay_thenStatus405() throws Exception {
        mvc.perform(put("/api/v1/invoices/" + paidInvoiceReference + "/pay")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Method not allowed"))
                .andExpect(jsonPath("$.detail").value("You can't pay an invoice that is in the PAID status"));
    }

    @Test
    public void givenCancelledInvoice_whenPay_thenStatus405() throws Exception {
        mvc.perform(put("/api/v1/invoices/" + cancelledInvoiceReference + "/pay")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Method not allowed"))
                .andExpect(jsonPath("$.detail").value("You can't pay an invoice that is in the CANCELLED status"));
    }

    @Test
    public void givenInvoices_whenGetInvoices_thenStatus200() throws Exception {
        mvc.perform(get("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.invoiceList[0].reference").value(outstandingInvoiceReference))
                .andExpect(jsonPath("$._embedded.invoiceList[1].reference").value(paidInvoiceReference))
                .andExpect(jsonPath("$._embedded.invoiceList[2].reference").value(cancelledInvoiceReference));
    }

    @Test
    public void givenNoInvoices_whenGetInvoices_thenStatus200_andLinkToSelf() throws Exception {
        invoiceRepository.deleteAll();
        mvc.perform(get("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links.self.href").isNotEmpty());
    }

    @Test
    public void givenNoInvoice_whenGetInvoiceById_thenStatus404() throws Exception {
        mvc.perform(get("/api/v1/invoices/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenInvoice_whenGetInvoiceByReference_thenStatus200() throws Exception {
        mvc.perform(get("/api/v1/invoices/reference/" + outstandingInvoiceReference)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$.reference").value(outstandingInvoiceReference));
    }

    @Test
    public void givenNoInvoice_whenGetInvoiceByReference_thenStatus404() throws Exception {
        mvc.perform(get("/api/v1/invoices/reference/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNoInvoice_whenPostNewInvoice_thenStatus201() throws Exception {
        mvc.perform(post("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 15.00, \"dueDate\": \"2021-11-06\",\"type\": \"LIBRARY_FINE\",\"account\": {\"studentId\": \"c6666666\"}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studentId").value("c6666666"))
                .andExpect(jsonPath("$.status").value("OUTSTANDING"));
    }

    @Test
    public void whenPostNewInvoice_withEmptyStudentIdValue_thenStatus422() throws Exception {
        mvc.perform(post("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 15.00, \"dueDate\": \"2021-11-06\",\"type\": \"LIBRARY_FINE\",\"account\": {\"studentId\": \"\"}}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void whenPostNewInvoice_withEmptyAccountValue_thenStatus422() throws Exception {
        mvc.perform(post("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 15.00, \"dueDate\": \"2021-11-06\",\"type\": \"LIBRARY_FINE\",\"account\": \"\"}"))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void whenPostNewInvoice_withEmptyJson_thenStatus400() throws Exception {
        mvc.perform(post("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNoInvoice_whenDelete_thenStatus404() throws Exception {
        mvc.perform(delete("/api/v1/invoices/999999/cancel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find invoice for reference 999999"));
    }

    @Test
    public void givenNoInvoice_whenPay_thenStatus405() throws Exception {
        mvc.perform(delete("/api/v1/invoices/999999/pay")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @AfterEach
    public void tearDown() {
        invoiceRepository.deleteAll();
    }
}
