package com.myproject.backend.teacher.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.repository.SectionRepository;
import com.myproject.backend.teacher.repository.SubjectRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SubjectService {
	
	private final SubjectRepository subjectRepo;
	private final SectionRepository sectionRepo;
	
	public List<SubjectEntity> getAllSubjectBySectionID(Integer sectionID) {
		
		return sectionRepo.findById(sectionID).map(section -> {
			return subjectRepo.findBySection(section);
		}).orElse(null);
		
	}
	
	
}
