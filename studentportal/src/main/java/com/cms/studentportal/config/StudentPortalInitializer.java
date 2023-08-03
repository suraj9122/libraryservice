package com.cms.studentportal.config;

import java.math.BigDecimal;
import java.util.List;

import com.cms.studentportal.domain.Course;
import com.cms.studentportal.service.CourseServiceIfc;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StudentPortalInitializer {

	private CourseServiceIfc courseServiceIfc;

	StudentPortalInitializer(CourseServiceIfc courseServiceIfc) {
		this.courseServiceIfc = courseServiceIfc;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void appInit() {
		loadCourse();
	}

	private void loadCourse() {
		List<Course> courseList = courseServiceIfc.findAllCourse();
		if (courseList.isEmpty()) {

			Course cloudComputingCourse = new Course("COMP637", "Cloud Computing Development",
					"Cloud development simply means writing code in the cloud, or on a local machine directly connected to the cloud environment, to where it is transferred for testing. Cloud development requires only a browser or online interface that is connected to a cloud-based infrastructure.",
					new BigDecimal(350.00));
			this.courseServiceIfc.createCourse(cloudComputingCourse);

			Course databaseCourse = new Course("COMP712", "Database Systems",
					"Database Systems or DBMS is software that caters to the collection of electronic and digital records to extract useful information and store that information is known as Database Systems/ Database Management Systems or DBMS. The purpose of a standard database is to store and retrieve data.",
					new BigDecimal(230.30));
			this.courseServiceIfc.createCourse(databaseCourse);

			Course advanceSoftwareCourse = new Course("COMP725", "Advanced Software Engineering",
					"An advanced software engineer uses high-level programming skills and technical expertise to design, execute, and assess software programs. They focus on the organizational structure of the project and typically set the time frame for the program's completion.",
					new BigDecimal(350.00));
			this.courseServiceIfc.createCourse(advanceSoftwareCourse);

			Course researchCourse = new Course("COMP738", "Research Practice",
					"Practice-research is a methodology in which knowledge is gained via the doing of something, rather than reading about it (desk-research), or inquiring into what other people know about that thing (i.e., interviews, surveys, etc.) or other more traditional models of research such as case studies and ethnographies.",
					new BigDecimal(480.50));
			this.courseServiceIfc.createCourse(researchCourse);

			Course projectCourse = new Course("COMP753", "Project Management",
					"Project management is the process of leading the work of a team to achieve all project goals within the given constraints. This information is usually described in project documentation, created at the beginning of the development process. The primary constraints are scope, time, and budget.",
					new BigDecimal(360.00));
			this.courseServiceIfc.createCourse(projectCourse);

			Course serviceComputingCourse = new Course("COMP758", "Software Engineering for Service Computing",
					"Software engineering is the branch of computer science that deals with the design, development, testing, and maintenance of software applications. Software engineers apply engineering principles and knowledge of programming languages to build software solutions for end users.",
					new BigDecimal(615.00));
			this.courseServiceIfc.createCourse(serviceComputingCourse);

			Course dissertationCourse = new Course("COMP763", "Msc Dissertation",
					"A Masters Dissertation is a lengthy written study on a topic chosen by the student. It is undertaken with the guidance of a faculty supervisor, and involves an extended period of research and writing.",
					new BigDecimal(550.00));
			this.courseServiceIfc.createCourse(dissertationCourse);

			Course softwareCourse = new Course("COMP703", "Software And System",
					"System software is a program designed to run a computer's hardware and applications and manage its resources, such as its memory, processors, and devices. It also provides a platform for running application software, and system software is typically bundled with a computer's operating system.",
					new BigDecimal(200.18));
			this.courseServiceIfc.createCourse(softwareCourse);

			Course datawareHourseCourse = new Course("COMP713", "Data ware house",
					"A data warehouse is a type of data management system that is designed to enable and support business intelligence (BI) activities, especially analytics. Data warehouses are solely intended to perform queries and analysis and often contain large amounts of historical data.",
					new BigDecimal(700.12));
			this.courseServiceIfc.createCourse(datawareHourseCourse);
		}
	}

}
