package com.cms.studentportal.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.cms.studentportal.constant.IntakeEnum;
import com.cms.studentportal.exception.CourseNotFoundException;
import com.cms.studentportal.exception.UserAlreadyEnrollIntoCourseException;
import com.cms.studentportal.thirdPartyApi.constant.PaymentType;
import com.cms.studentportal.thirdPartyApi.service.ThirdPartyAPIServiceIfc;
import org.springframework.stereotype.Service;

import com.cms.studentportal.doa.CourseRepositoryIfc;
import com.cms.studentportal.doa.EnrollRepositoryIfc;
import com.cms.studentportal.domain.Course;
import com.cms.studentportal.domain.Enroll;
import com.cms.studentportal.service.EnrollServiceIfc;
import com.cms.studentportal.utils.AuthenticateUtil;

import javax.transaction.Transactional;

@Service
public class EnrollServiceImpl implements EnrollServiceIfc {

	private EnrollRepositoryIfc enrollRepositoryIfc;

	private CourseRepositoryIfc courseRepositoryIfc;

	private ThirdPartyAPIServiceIfc thirdPartyAPIServiceIfc;

	EnrollServiceImpl(EnrollRepositoryIfc enrollRepositoryIfc, CourseRepositoryIfc courseRepositoryIfc,
			ThirdPartyAPIServiceIfc thirdPartyAPIServiceIfc) {
		this.enrollRepositoryIfc = enrollRepositoryIfc;
		this.courseRepositoryIfc = courseRepositoryIfc;
		this.thirdPartyAPIServiceIfc = thirdPartyAPIServiceIfc;
	}

	@Override
	public List<Enroll> getEnrolCourses() {
		long studentId = AuthenticateUtil.getStudentId();
		return enrollRepositoryIfc.findByStudentId(studentId);
	}

	@Override
	@Transactional
	public void enrolIntoCourse(String courseId) throws CourseNotFoundException, UserAlreadyEnrollIntoCourseException {
		Course course = courseRepositoryIfc.findByCourseId(courseId);
		if (course == null) {
			throw new CourseNotFoundException(courseId);
		}

		long studentId = AuthenticateUtil.getStudentId();

		// check student is already enroll into course
		Enroll alreadyEnroll = enrollRepositoryIfc.findByStudentIdAndCourse(studentId, course);
		List<Enroll> enrollListBeforeSaving = enrollRepositoryIfc.findByStudentId(studentId);
		if (alreadyEnroll != null) {
			throw new UserAlreadyEnrollIntoCourseException(course.getTitle());
		}

		Enroll enroll = new Enroll();
		enroll.setStudentId(studentId);
		enroll.setDate(LocalDateTime.now());
		enroll.setIntake(IntakeEnum.JAN);
		enroll.setCourse(course);
		enrollRepositoryIfc.save(enroll);

		//If this is your first enrolment, a student account is created at this point.
		if(enrollListBeforeSaving.isEmpty()){
			thirdPartyAPIServiceIfc.createFinanceAccount(studentId);
			thirdPartyAPIServiceIfc.createLibraryAccount(studentId);
		}

		thirdPartyAPIServiceIfc.createFinanceServiceInvoice(studentId, course.getFee(), PaymentType.TUITION_FEES);

	}

}
