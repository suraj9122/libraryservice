package com.cms.studentportal.service.impl;

import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cms.studentportal.exception.AuthenticationException;
import com.cms.studentportal.thirdPartyApi.service.ThirdPartyAPIServiceIfc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.studentportal.doa.StudentRepositoryIfc;
import com.cms.studentportal.domain.Student;
import com.cms.studentportal.service.StudentServiceIfc;
import com.cms.studentportal.utils.AuthenticateUtil;
import com.cms.studentportal.utils.Util;

@Service
public class StudentServiceImpl implements StudentServiceIfc {

	private StudentRepositoryIfc studentRepositoryIfc;

	private PasswordEncoder passwordEncoder;

	private ThirdPartyAPIServiceIfc thirdPartyAPIServiceIfc;

	StudentServiceImpl(StudentRepositoryIfc studentRepositoryIfc, PasswordEncoder passwordEncoder,
			ThirdPartyAPIServiceIfc thirdPartyAPIServiceIfc) {
		this.studentRepositoryIfc = studentRepositoryIfc;
		this.passwordEncoder = passwordEncoder;
		this.thirdPartyAPIServiceIfc = thirdPartyAPIServiceIfc;
	}

	@Override
	public Student loginStudent(@NotNull @NotEmpty  String username,@NotNull @NotEmpty String password) throws AuthenticationException {
		Student dbStudentRecord = studentRepositoryIfc.findByUsername(username);
		if (dbStudentRecord != null) {
			if (passwordEncoder.matches(password, dbStudentRecord.getPassword())) {
				return dbStudentRecord;
			}
			throw new AuthenticationException(username);
		}

		throw new AuthenticationException();
	}

	@Override
	@Transactional
	public void createStudent(Student student) {
		long studentId = Util.generateStudentId();
		student.setStudentId(studentId);
		student.setPassword(passwordEncoder.encode(student.getPassword()));
		studentRepositoryIfc.save(student);

	}

	@Override
	public void updateStudent(Student student) {

		long studentId = AuthenticateUtil.getStudentId();

		Student dbStudentRecord = getStudentById(studentId);
		if (StringUtils.isBlank(student.getPassword())) {
			dbStudentRecord.setPassword(dbStudentRecord.getPassword());
		} else {
			dbStudentRecord.setPassword(passwordEncoder.encode(student.getPassword()));
		}

		dbStudentRecord.setMobileNumber(student.getMobileNumber());
		dbStudentRecord.setHomeAddress(student.getHomeAddress());
		dbStudentRecord.setFullname(student.getFullname());
		dbStudentRecord.setUsername(student.getUsername());
		dbStudentRecord.setEmail(student.getEmail());
		dbStudentRecord.setStudentId(studentId);

		studentRepositoryIfc.save(dbStudentRecord);
	}

	@Override
	public Student getStudentById(long studentId) {
		Student student = studentRepositoryIfc.findByStudentId(studentId);
		return student;
	}

	@Override
	public Student getStudentByIdWithoutPassword(long studentId) {
		Student student = getStudentById(studentId);
		student.setPassword(null);
		return student;
	}

	@Override
	public boolean isEligibleGraduation() {
		long studentId = AuthenticateUtil.getStudentId();
		return thirdPartyAPIServiceIfc.isEligibleGraduation(studentId);
	}

}
