package com.cms.studentportal.service;

import com.cms.studentportal.exception.AuthenticationException;
import com.cms.studentportal.domain.Student;

public interface StudentServiceIfc {

	/**
	 * <h1>This method will create student while registering</h1>
	 * 
	 * @param student all basic student info enter by user {@link Student}
	 */
	void createStudent(Student student);

	/**
	 * <h1>This method will update login user info</h1>
	 * 
	 * @apiNote This will update password if provided otherwise used old one
	 * @apiNote This will not update student Id rather than get from login user
	 *          session
	 * @param student all basic student info which are changed except studentId,
	 *                password{optional}
	 */
	void updateStudent(Student student);

	/**
	 * <h1>This will authenticate user</h1>
	 * 
	 * @param username username enter by user
	 * @param password plain password enter by user
	 * @return Student all student info based on credential
	 */
	Student loginStudent(String username, String password) throws AuthenticationException;

	/**
	 * <h1>Eligible graduation</h1>
	 * 
	 * @apiNote This will call third party finance api to check for eligibility for
	 *          graduation
	 */
	boolean isEligibleGraduation();

	/**
	 * <h1>This will get Student without encrypted password</h1>
	 * 
	 * @param studentId student id of user not system Id
	 * @return Student student record based on student Id
	 */
	Student getStudentByIdWithoutPassword(long studentId);

	/**
	 * <h1>This will get Student with encrypted password</h1>
	 * 
	 * @param studentId student id of user not system Id
	 * @return Student student record based on student Id
	 */
	Student getStudentById(long studentId);
}
