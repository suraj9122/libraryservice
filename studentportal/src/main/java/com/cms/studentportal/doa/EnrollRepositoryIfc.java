package com.cms.studentportal.doa;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cms.studentportal.domain.Course;
import com.cms.studentportal.domain.Enroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollRepositoryIfc extends JpaRepository<Enroll, Long> {

	List<Enroll> findByStudentId(@NotNull @NotEmpty Long studentId);

	Enroll findByStudentIdAndCourse(@NotNull @NotEmpty Long studentId,@NotNull Course course);
}
