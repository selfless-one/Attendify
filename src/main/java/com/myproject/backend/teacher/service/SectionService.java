package com.myproject.backend.teacher.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.entity.TeacherAccount;
import com.myproject.backend.teacher.repository.SectionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SectionService {
	
	private final SectionRepository sectionRepo;
	
	public List<SectionEntity> getAllSectionByTeacher(TeacherAccount teacherAccount) {
		
		return sectionRepo.findByTeacher(teacherAccount);
	}
	
	public List<SubjectEntity> getAllSubjectOfSection(TeacherAccount teacherAccount) {
		
	    List<SectionEntity> sections = getAllSectionByTeacher(teacherAccount);
	    List<SubjectEntity> subjects = new LinkedList<>();
	    
	    for (SectionEntity section : sections) {
	        subjects.addAll(section.getSubjects());
	    }
	    
	    return subjects;
	}

	
}
