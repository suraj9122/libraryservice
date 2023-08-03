package com.cms.studentportal.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.cms.studentportal.constant.IntakeEnum;
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
@Table(name = "enroll", uniqueConstraints = {
		@UniqueConstraint(name = "uq_StudentAndCourseEnrollment", columnNames = { "student_id", "course_id" }) })
public class Enroll extends Common {

	@JsonProperty("student_id")
	@Column(name = "student_id", nullable = false)
	@NotNull
	private long studentId;

	@JsonProperty("intake")
	@Column(name = "intake", nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private IntakeEnum intake;

	@JsonProperty("enroll_date")
	@Column(name = "date", nullable = false)
	@NotNull
	private LocalDateTime date;

	@JsonProperty("course")
	@OneToOne
	@JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
	@ToString.Exclude
	@NotNull
	private Course course = new Course();

}
