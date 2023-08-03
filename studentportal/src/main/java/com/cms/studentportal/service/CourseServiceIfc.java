package com.cms.studentportal.service;

import java.util.List;

import com.cms.studentportal.domain.Course;

public interface CourseServiceIfc {
	/**
	 * <h1>This method is used to create course</h1>
	 * 
	 * @param course This object hold all data which are need to create course
	 *               {@link Course}
	 *
	 * 
	 */
	void createCourse(Course course);

	/**
	 * <h1>This method is used to get all courses
	 * 
	 * @return List<Courses> This return all courses within a system
	 */
	List<Course> findAllCourse();

	/**
	 * <h1>Search Course using name</h1> This method get a course name/title and
	 * search all occurance of that keyword
	 * 
	 * @param name This is used to get name/title of course an
	 * @return List<Courses> This return all courses which satisfy name/title search
	 *         keyword
	 */
	List<Course> searchCourses(String name);

}
