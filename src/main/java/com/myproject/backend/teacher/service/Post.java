package com.myproject.backend.teacher.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.myproject.backend.student.entity.StudentAccountEntity;
import com.myproject.backend.student.repository.StudentAccountRepository;
import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.myproject.backend.teacher.repository.SectionRepository;
import com.myproject.backend.teacher.repository.SubjectRepository;
import com.myproject.backend.teacher.repository.TeacherAccountRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class Post implements CommandLineRunner {

	
	final TeacherAccountRepository teacherAccountRepo;
	final SectionRepository sectionRepository;
    final SubjectRepository subjectRepository;
    final StudentAccountRepository studentAccountRepository;
    
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		
//		StudentAccountEntity student1 = StudentAccountEntity.builder()
//				.studentNumber("UA202200305")
//				//.email("razonabler31@gmail.com")
//				.password("rowel123")
//				.firstname("Rowel")
//				.surname("Razonable")
//				//.course("BSIT")
//				.sectionName("LFAU133N001")
//				.username("rowel123")
//				.build();
//			
//		studentAccountRepository.save(student1);
//		
//		
//        TeacherAccountEntity teacher1 = TeacherAccountEntity.builder()
//        		//.email("chiong@gmail.com")
//        		.username("chiong123")
//        		.firstname("Joriz")
//        		.surname("Chiong")
//        		.password("chiong123")
//        		.build();
//        
//        TeacherAccountEntity teacher2 = TeacherAccountEntity.builder()
//        		//.email("salen@gmail.com")
//        		.password("salen123")
//        		.username("salen123")
//        		.firstname("Jerico")
//        		.surname("Salen")
//        		.build();
//
//        // Save teachers
//        teacherAccountRepo.save(teacher1);
//        teacherAccountRepo.save(teacher2);
//
//        
//        SectionEntity sect1 = SectionEntity.builder().sectionName("LFAU133N001").course("BSIT").teacher(teacher1).dateCreated(LocalDateTime.now()).build();
//        SectionEntity sect2 = SectionEntity.builder().sectionName("LFAU133N003").course("BSIT").teacher(teacher1).dateCreated(LocalDateTime.now()).build();
//        SectionEntity sect3 = SectionEntity.builder().sectionName("LFAU133N002").course("BSIT").teacher(teacher1).dateCreated(LocalDateTime.now()).build();
//        
//        SectionEntity sect5 = SectionEntity.builder().sectionName("LFAU133N001").course("BSIT").teacher(teacher2).dateCreated(LocalDateTime.now()).build();
//        SectionEntity sect6 = SectionEntity.builder().sectionName("LFAU133N004").course("BSIT").teacher(teacher2).dateCreated(LocalDateTime.now()).build();
//        SectionEntity sect7 = SectionEntity.builder().sectionName("LFAU133N001").course("BSIT").teacher(teacher2).dateCreated(LocalDateTime.now()).build();
//        SectionEntity sect8 = SectionEntity.builder().sectionName("LFAU133N0022").course("BSIT").teacher(teacher2).dateCreated(LocalDateTime.now()).build();
//        
//        // Save sections
//        sectionRepository.save(sect1);
//        sectionRepository.save(sect2);
//        sectionRepository.save(sect3);
//        sectionRepository.save(sect5);
//        sectionRepository.save(sect6);
//        sectionRepository.save(sect7);
//        sectionRepository.save(sect8);
//        
//        SubjectEntity subjectOfSect1 = SubjectEntity.builder().subjectCode("CC05").subjectDescription("Com 5").section(sect1).status("Open").dateCreated(LocalDateTime.now()).attendanceEndTime(LocalTime.of(0, 0)).build();
//        SubjectEntity subjectOfSect2 = SubjectEntity.builder().subjectCode("PF1").subjectDescription("Event Driven programming architetura tanggggggggg")
//        		.section(sect1).status("Closed").dateCreated(LocalDateTime.now()).attendanceEndTime(LocalTime.of(16, 0)).build();
//        
//        SubjectEntity subjectOfSect3 = SubjectEntity.builder().subjectCode("CCsdf05").subjectDescription("ss 5").section(sect7)
//        		.status("Closed").dateCreated(LocalDateTime.now()).attendanceEndTime(LocalTime.of(16, 0)).build();
//        SubjectEntity subjectOfSect4 = SubjectEntity.builder().subjectCode("PFsdf1").subjectDescription("ss Drivenz")
//        		.section(sect3).status("Closed").dateCreated(LocalDateTime.now()).attendanceEndTime(LocalTime.of(16, 0)).build();
//        SubjectEntity subjectOfSect5 = SubjectEntity.builder().subjectCode("CsdC05").subjectDescription("Cs 5")
//        		.section(sect5).status("Closed").dateCreated(LocalDateTime.now()).attendanceEndTime(LocalTime.of(16, 0)).build();
//        SubjectEntity subjectOfSect6 = SubjectEntity.builder().subjectCode("PdF1").subjectDescription("Est Driven").section(sect3).status("Closed").dateCreated(LocalDateTime.now()).build();
//        
//        subjectRepository.save(subjectOfSect1);
//        subjectRepository.save(subjectOfSect2);
//        subjectRepository.save(subjectOfSect3);
//        subjectRepository.save(subjectOfSect4);
//        subjectRepository.save(subjectOfSect5);
//        subjectRepository.save(subjectOfSect6);
//        
//        SubjectEntity subjectNiSalen1 = SubjectEntity.builder().subjectCode("s").subjectDescription("Esast Driven").section(sect5).build();
//        SubjectEntity subjectNiSalen2 = SubjectEntity.builder().subjectCode("ss").subjectDescription("Easdst Driven").section(sect5).build();
//        SubjectEntity subjectNiSalen3 = SubjectEntity.builder().subjectCode("sss").subjectDescription("Essdasdt Driven").section(sect5).build();
//        
//        subjectRepository.save(subjectNiSalen1);
//        subjectRepository.save(subjectNiSalen2);
//        subjectRepository.save(subjectNiSalen3);
//		
	}

}
