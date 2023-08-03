package com.cms.studentportal.doa;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cms.studentportal.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepositoryIfc extends JpaRepository<Course, Long> {

	@Query("FROM Course WHERE title LIKE %:title%")
	@Modifying
	List<Course> searchByTitleKeyword(@NotNull @NotEmpty String title);

	Course findByCourseId(@NotNull @NotEmpty String courseId);
}
