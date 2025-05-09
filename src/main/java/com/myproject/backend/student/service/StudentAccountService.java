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
	
	
	public boolean authenticate(String username, String password) {
		
		return sAccountRepository.findByUsername(username).map(acc -> {
			return acc.getPassword().equals(password) ? true : false;
		}).orElse(false);
		
	}
	
	public StudentAccountEntity getAccountByUsername(String username) {
		
		return sAccountRepository.findByUsername(username).orElseThrow();
		
	}
	
	public String createAccount(String username, 
			String password, 
			String surname, 
			String firstname, 
			String studentNum, 
			String sectionName) {
		
		if (!sectionService.sectionNameExists(sectionName)) {
			return "Section Not Exists";
		}
		
		if (sAccountRepository.existsByStudentNumber(studentNum)) {
			return "Student Number already exists";
		}
		
		if (sAccountRepository.existsByUsername(username)) {
			return "Username already exists";
		}
		
		sAccountRepository.save(StudentAccountEntity.builder()
				.username(username)
				.password(password)
				.surname(surname)
				.firstname(firstname)
				.studentNumber(studentNum)
				.sectionName(sectionName)
				.build());
		
		return "Account Created Success";
	}
	
	

}
