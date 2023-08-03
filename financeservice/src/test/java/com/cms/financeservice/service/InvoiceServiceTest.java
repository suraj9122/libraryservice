package com.cms.financeservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.cms.financeservice.doa.AccountRepositoryIfc;
import com.cms.financeservice.doa.InvoiceRepositoryIfc;
import com.cms.financeservice.domain.Account;
import com.cms.financeservice.domain.Invoice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.cms.financeservice.constant.PaymentTypeEnum;
import com.cms.financeservice.constant.StatusEnum;
import com.cms.financeservice.exception.InvoiceNotFoundException;

/**
 * these test case modified from provided finance services by Thalita Vergilio
 * */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class InvoiceServiceTest {

    private final LocalDate dueDate = LocalDate.of(2021, Month.DECEMBER, 25);
    private final LocalDate anotherDueDate = LocalDate.of(2018, Month.NOVEMBER, 23);
    private final PaymentTypeEnum type = PaymentTypeEnum.TUITION_FEES;
    private final PaymentTypeEnum anotherType = PaymentTypeEnum.LIBRARY_FINE;
    private final Long invoiceId = 1L;
    private final String invoiceReference = "1234ABCD";
    private Account account;
    private Account anotherAccount;
    private Invoice invoice;
    private Invoice anotherInvoice;

    @MockBean
    private AccountRepositoryIfc accountRepository;
    @MockBean
    private InvoiceRepositoryIfc invoiceRepository;
    @MockBean
    private Model model;
    @MockBean
    private BindingResult bindingResult;

    @Autowired
    private InvoiceServiceIfc invoiceService;

    @BeforeEach
    public void setUp() {
        final long studentId = 7777777;
        final long anotherStudentId = 3333333;
        final Long accountId = 1L;
        final Long anotherAccountId = 2L;
        final Long anotherInvoiceId = 2L;
        final BigDecimal amount = new BigDecimal(10.5);
        final BigDecimal anotherAmount = new BigDecimal(20.99);
        account = new Account(studentId);
        account.setId(accountId);
        anotherAccount = new Account(anotherStudentId);
        anotherAccount.setId(anotherAccountId);
        invoice = new Invoice(amount, dueDate, type, account);
        invoice.setId(invoiceId);
        invoice.setReferenceId(invoiceReference);
        anotherInvoice = new Invoice(anotherAmount, anotherDueDate, anotherType, anotherAccount);
        anotherInvoice.setId(anotherInvoiceId);
        Mockito.when(invoiceRepository.findById(invoiceId))
                .thenReturn(Optional.of(invoice));
        Mockito.when(invoiceRepository.findByReferenceId(invoiceReference))
                .thenReturn(invoice);
        Mockito.when(invoiceRepository.findAll())
                .thenReturn(Arrays.asList(invoice, anotherInvoice));
        Mockito.when(invoiceRepository.save(invoice))
                .thenReturn(invoice);
        Mockito.when(accountRepository.findByStudentId(studentId))
                .thenReturn(account);
        Mockito.doNothing().when(invoiceRepository).deleteById(isA(Long.class));
        Mockito.when(model.getAttribute("invoice"))
                .thenReturn(invoice);
    }


    @Test
    void testGetInvoiceByReference_withValidReference_returnsExistingInvoice() {
        Invoice result = invoiceService.getInvoiceByReferenceId(invoiceReference);
        assertEquals(invoiceReference, result.getReferenceId());
    }

    @Test
    void testGetInvoiceByReference_withInValidReference_throwsException() {
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.getInvoiceByReferenceId(""),
                "Exception was not thrown.");
    }

    @Test
    void testGetInvoiceByReference_withNullReference_throwsException() {
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.getInvoiceByReferenceId(null),
                "Exception was not thrown.");
    }

    @Test
    void testGetAllInvoices_returnsExistingInvoices() {
        List<Invoice> result = invoiceService.getAllInvoices();
        assertEquals(2, result.size());
        assertThat(result.containsAll(Arrays.asList(invoice, anotherInvoice)));

    }

    @Test
    void testCreateNewInvoice_withValidData_createsInvoice() {
    	String invoiceReferenceId = invoiceService.createInvoice(invoice);
    	Invoice invoiceByReferenceId = invoiceService.getInvoiceByReferenceId(invoiceReferenceId);
        assertEquals(invoiceReferenceId, invoiceByReferenceId.getReferenceId());
        assertEquals(invoice, invoiceByReferenceId);
    }

    @Test
    void testCancelInvoice_withValidId_cancelsInvoice() {
        invoice.setStatus(StatusEnum.OUTSTANDING);
        invoiceService.cancelInvoiceByReferenceId(invoiceReference);
        invoice.setStatus(StatusEnum.CANCELLED);
        
        Invoice invoiceByReferenceId = invoiceService.getInvoiceByReferenceId(invoiceReference);

        assertEquals(invoice, invoiceByReferenceId);
    }

    @Test
    void testPayInvoice_withValidId_paysInvoice() {
        invoice.setStatus(StatusEnum.OUTSTANDING);
        invoiceService.payInvoiceByReferenceId(invoiceReference);
        invoice.setStatus(StatusEnum.PAID);

        Invoice invoiceByReferenceId = invoiceService.getInvoiceByReferenceId(invoiceReference);

        assertEquals(invoice, invoiceByReferenceId);
    }

    @Test
    void testProcessPayment_withStatusOutstanding_updatesInvoice() {
        invoice.setStatus(StatusEnum.OUTSTANDING);
        invoiceService.payInvoiceByReferenceId(invoiceReference);
        
        Invoice invoiceByReferenceId = invoiceService.getInvoiceByReferenceId(invoiceReference);

        assertEquals(invoice, invoiceByReferenceId);
    }

    @Test
    void testProcessPayment_withStatusPaid_throwsUnsupportedOperationException() {
        invoice.setStatus(StatusEnum.PAID);
        assertThrows(UnsupportedOperationException.class, () -> invoiceService.payInvoiceByReferenceId(invoiceReference),
                "Exception was not thrown.");
    }

    @Test
    void testProcessPayment_withStatusCancelled_throwsUnsupportedOperationException() {
        invoice.setStatus(StatusEnum.CANCELLED);
        assertThrows(UnsupportedOperationException.class, () -> invoiceService.payInvoiceByReferenceId(invoiceReference),
                "Exception was not thrown.");
    }

    @Test
    void testFindInvoiceThroughPortal_withIncorrectReference_throwsException() {
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.getInvoiceByReferenceId("00000000"),
                "Exception was not thrown.");
    }

    @Test
    void testPayInvoiceThroughPortal_withCorrectReference_paysInvoice() {
        invoice.setStatus(StatusEnum.OUTSTANDING);
        invoiceService.payInvoiceThroughPortal(invoice.getReferenceId(), model);
        
        Invoice returned = invoiceService.getInvoiceByReferenceId(invoice.getReferenceId());
        assertEquals(StatusEnum.PAID, returned.getStatus());
    }

    @Test
    void testPayInvoiceThroughPortal_withIncorrectReference_throwsException() {
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.payInvoiceThroughPortal("00000000", model),
                "Exception was not thrown.");
    }

    @Test
    void testPayInvoiceThroughPortal_withNullReference_throwsException() {
        Invoice dummyInvoice = new Invoice();
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.payInvoiceThroughPortal(null, model),
                "Exception was not thrown.");
    }

    @Test
    void testPayInvoiceThroughPortal_withNullInvoice_throwsException() {
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.payInvoiceThroughPortal(null, model),
                "Exception was not thrown.");
    }

    @AfterEach
    public void tearDown() {
        account = null;
        anotherAccount = null;
        invoice = null;
        anotherInvoice = null;
    }
}