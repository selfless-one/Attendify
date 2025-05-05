package com.myproject.backend.student.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myproject.backend.student.entity.StudentAccountEntity;
import com.myproject.backend.student.repository.StudentAccountRepository;
import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.service.SectionService;

@Service
public class StudentAccountService {
	
	private final StudentAccountRepository sAccountRepository;
	private final SectionService sectionService;

	public StudentAccountService(StudentAccountRepository sAccountRepository, SectionService sectionService) {
		this.sAccountRepository = sAccountRepository;
		this.sectionService = sectionService;
	}


	public List<SectionEntity> getSectionBySectionName(String sectionName) {
		return sectionService.getSectionByName(sectionName);
	}
	
	
	public boolean authenticate(String studentNumber, String password) {
		
		return sAccountRepository.findByStudentNumber(studentNumber).map(acc -> {
			return acc.getPassword().equals(password) ? true : false;
		}).orElse(false);
		
	}
	
	public StudentAccountEntity getAccountByStudentNumber(String studentNumber) {
		
		return sAccountRepository.findByStudentNumber(studentNumber).orElseThrow();
		
	}
	
	public String createAccount(String sectionName, String studentNumber) {
		
		if (!sectionService.sectionNameExists(sectionName)) {
			return "Section Not Exists";
		}
		
		if (sAccountRepository.existsByStudentNumber(studentNumber)) {
			return "Student Number already exists";
		}
		
		sAccountRepository.save(StudentAccountEntity.builder()
				.studentNumber(studentNumber)
				.sectionName(sectionName)
				.build());
		
		return "Account Created Success";
	}

	
	

}
