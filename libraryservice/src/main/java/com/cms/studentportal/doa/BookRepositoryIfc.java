package com.cms.studentportal.doa;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cms.studentportal.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepositoryIfc extends JpaRepository<Book, Long> {

	@Query("FROM Book WHERE title LIKE %:title%")
	@Modifying
	List<Book> searchByTitleKeyword(@NotNull @NotEmpty String title);

	Book findByIsbn(@NotNull @NotEmpty String isbn);
}
