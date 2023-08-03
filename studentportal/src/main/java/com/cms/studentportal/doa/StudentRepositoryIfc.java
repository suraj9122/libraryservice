package com.cms.studentportal.doa;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cms.studentportal.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepositoryIfc extends JpaRepository<Student, Long> {

	Student findByStudentId(@NotNull @NotEmpty long studentId);

	Student findByUsername(@NotNull @NotEmpty String username);

}
