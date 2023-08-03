package com.cms.financeservice.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.cms.financeservice.constant.PaymentTypeEnum;
import com.cms.financeservice.constant.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
@Table(name = "invoice")
public class Invoice extends Common {

	@JsonProperty("reference_id")
	@Column(name = "referenceId", unique = true, nullable = false)
	@NotNull
	@NotBlank
	@Size(min = 4, max = 16, message = "{referenceId.size}")
	@Pattern(regexp = "[A-Za-z0-9]*", message = "{referenceId.format}")
	private String referenceId;
	


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountId", referencedColumnName = "id", nullable = false)
	@ToString.Exclude
	private Account account;
	
	@JsonProperty("amount")
	@Column(name = "amount", nullable = false)
	@NotNull
	private BigDecimal amount;
	
	@JsonProperty("due_date")
	@Column(name = "dueDate", nullable = false)
	@NotNull
	private Date dueDate;
	
	@JsonProperty("payment_type")
	@Column(name = "paymentType", nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private PaymentTypeEnum paymentType;
	
	@JsonProperty("status")
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private StatusEnum status;
	
	public Invoice(BigDecimal amount, LocalDate dueDate, PaymentTypeEnum paymentType, Account account) {
		this.amount = amount;
		this.dueDate = Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		this.paymentType = paymentType;
		this.account = account;
	}
	
	

}
