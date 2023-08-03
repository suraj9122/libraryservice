package com.cms.studentportal.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
@Table(name = "book")
public class Book extends Common {

	@JsonProperty("isbn")
	@Column(name = "isbn", unique = true, nullable = false)
	@NotNull
	@NotBlank
	private String isbn;

	@JsonProperty("title")
	@Column(name = "title", nullable = false)
	@NotNull
	@NotBlank
	private String title;

	@JsonProperty("author")
	@Column(name = "author", length = 1000, nullable = false)
	@NotNull
	private String author;

	@JsonProperty("year")
	@Column(name = "year", nullable = false)
	@NotNull
	private int year;
	
	@JsonProperty("copies")
	@Column(name = "copies", nullable = false)
	@NotNull
	private int copies;

}
