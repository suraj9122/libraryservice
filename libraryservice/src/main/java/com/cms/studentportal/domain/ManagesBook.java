package com.cms.studentportal.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "manges_book", uniqueConstraints = {
		@UniqueConstraint(name = "uq_StudentAndBookManage", columnNames = { "student_id", "isbn", "dateReturn" }) })
public class ManagesBook extends Common {

	@JsonProperty("student_id")
	@Column(name = "student_id", nullable = false)
	@NotNull
	private long studentId;

	@JsonProperty("date_borrow")
	@Column(name = "dateBorrow", nullable = false)
	@NotNull
	private LocalDateTime dateBorrow;
	
	@JsonProperty("date_return")
	@Column(name = "dateReturn")
	private LocalDateTime dateReturn;

	@JsonProperty("isbn")
	@OneToOne
	@JoinColumn(name = "isbn", referencedColumnName = "isbn", nullable = false)
	@ToString.Exclude
	@NotNull
	private Book book = new Book();

}
