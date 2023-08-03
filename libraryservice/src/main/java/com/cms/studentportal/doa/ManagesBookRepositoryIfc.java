package com.cms.studentportal.doa;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cms.studentportal.domain.Book;
import com.cms.studentportal.domain.ManagesBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagesBookRepositoryIfc extends JpaRepository<ManagesBook, Long> {

	List<ManagesBook> findByStudentId(@NotNull @NotEmpty Long studentId);

	ManagesBook findByStudentIdAndBook(@NotNull @NotEmpty Long studentId,@NotNull Book book);
}
