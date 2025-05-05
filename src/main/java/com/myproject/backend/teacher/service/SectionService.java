package com.myproject.backend.teacher.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.myproject.backend.teacher.repository.SectionRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SectionService {
	
	private final SectionRepository sectionRepo;
	
	public List<SectionEntity> getAllSectionByTeacher(TeacherAccountEntity teacherAccount) {
		
		return sectionRepo.findByTeacher(teacherAccount);
	}
	
	public List<SubjectEntity> getAllSubjectOfSection(TeacherAccountEntity teacherAccount) {
		
	    List<SectionEntity> sections = getAllSectionByTeacher(teacherAccount);
	    List<SubjectEntity> subjects = new LinkedList<>();
	    
	    for (SectionEntity section : sections) {
	        subjects.addAll(section.getSubjects());
	    }
	    
	    return subjects;
	}
	
	
	public Optional<SectionEntity> getAccountById(Integer id) {
		return sectionRepo.findById(id);
	}
	
	public void saveChanges(SectionEntity sectionEntity) {
		sectionRepo.save(sectionEntity);
	}

	
	public List<SectionEntity> getSectionByName(String sectionName) {
		return sectionRepo.findBySectionName(sectionName);
	}
	
	public boolean sectionNameExists(String sectionName) {
		return sectionRepo.existsBySectionName(sectionName);
	}
	
}
