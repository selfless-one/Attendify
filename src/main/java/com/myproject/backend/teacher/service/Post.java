package com.myproject.backend.teacher.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.entity.TeacherAccount;
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
    
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
			
        TeacherAccount teacher1 = TeacherAccount.builder()
        		.email("chiong@gmail.com")
        		.password("chiong123")
        		.build();
        
        TeacherAccount teacher2 = TeacherAccount.builder()
        		.email("salen@gmail.com")
        		.password("salen123")
        		.build();

        // Save teachers
        teacherAccountRepo.save(teacher1);
        teacherAccountRepo.save(teacher2);

        
        SectionEntity sect1 = SectionEntity.builder().sectionName("LFAU133N001").course("BSIT").teacher(teacher1).build();
        SectionEntity sect2 = SectionEntity.builder().sectionName("LFAU133N003").course("BSIT").teacher(teacher1).build();
        SectionEntity sect3 = SectionEntity.builder().sectionName("LFAU133N002").course("BSIT").teacher(teacher1).build();
        
        SectionEntity sect5 = SectionEntity.builder().sectionName("LFAU133N004").course("BSIT").teacher(teacher2).build();
        SectionEntity sect6 = SectionEntity.builder().sectionName("LFAU133N004").course("BSIT").teacher(teacher2).build();
        SectionEntity sect7 = SectionEntity.builder().sectionName("LFAU133N006").course("BSIT").teacher(teacher2).build();
        SectionEntity sect8 = SectionEntity.builder().sectionName("LFAU133N006").course("BSIT").teacher(teacher2).build();
        
        // Save sections
        sectionRepository.save(sect1);
        sectionRepository.save(sect2);
        sectionRepository.save(sect3);
        sectionRepository.save(sect5);
        sectionRepository.save(sect6);
        sectionRepository.save(sect7);
        sectionRepository.save(sect8);
        
        SubjectEntity subjectOfSect1 = SubjectEntity.builder().subjectCode("CC05").subjectDescription("Com 5").section(sect1).build();
        SubjectEntity subjectOfSect2 = SubjectEntity.builder().subjectCode("PF1").subjectDescription("Event Driven").section(sect1).build();
        
        SubjectEntity subjectOfSect3 = SubjectEntity.builder().subjectCode("CCsdf05").subjectDescription("ss 5").section(sect3).build();
        SubjectEntity subjectOfSect4 = SubjectEntity.builder().subjectCode("PFsdf1").subjectDescription("ss Driven").section(sect3).build();
        SubjectEntity subjectOfSect5 = SubjectEntity.builder().subjectCode("CsdC05").subjectDescription("Cs 5").section(sect3).build();
        SubjectEntity subjectOfSect6 = SubjectEntity.builder().subjectCode("PdF1").subjectDescription("Est Driven").section(sect3).build();
        
        subjectRepository.save(subjectOfSect1);
        subjectRepository.save(subjectOfSect2);
        subjectRepository.save(subjectOfSect3);
        subjectRepository.save(subjectOfSect4);
        subjectRepository.save(subjectOfSect5);
        subjectRepository.save(subjectOfSect6);
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
