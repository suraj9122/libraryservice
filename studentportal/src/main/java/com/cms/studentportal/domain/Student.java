package com.cms.studentportal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_DEFAULT)
@Table(name = "student")
public class Student extends Common {

	@JsonProperty("fullname")
	@Column(name = "fullname", nullable = false)
	@NotNull
	@NotBlank
	private String fullname;

	@JsonProperty("mobile_number")
	@Column(name = "mobile_number", unique = true)
	@NotNull
	@NotBlank
	private String mobileNumber;

	@JsonProperty("home_address")
	@Column(name = "home_address")
	private String homeAddress;

	@JsonProperty("email_address")
	@Column(name = "email_address", unique = true, nullable = false)
	@NotNull
	@NotBlank
	@Email
	private String email;

	@JsonProperty("username")
	@Column(name = "username", unique = true, nullable = false)
	@NotNull()
	@NotBlank(message = "Username can not be empty")
	@Size(min = 4, max = 100, message = "${username.size}")
	@Pattern(regexp = "[A-Za-z0-9]*", message = "${username.format}")
	private String username;

	@JsonProperty("password")
	@Column(name = "password", nullable = false)
	@NotNull
	@NotBlank
	private String password;

	@JsonProperty("student_id")
	@Column(name = "student_id", unique = true, nullable = false)
	@NotNull
	private long studentId;

}
