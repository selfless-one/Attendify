package com.myproject.backend.student.service;

import java.util.Arrays;
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
	
	private String trimData(String data) {
		return data.trim();
	}
	
//	private String capitalize(String data) {
//		
//		var d = trimData(data).toLowerCase();
//		return d.substring(0, 1) + d.substring(1);
//	}
	
	private String toUpperCase(String data) {
		return trimData(data).toUpperCase();
	}
	
	private boolean containWhitespace(String... data) {
		
		boolean result = Arrays.stream(data).anyMatch(d -> trimData(d).contains(" "));
		return result;
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
		
		username = trimData(username);
		surname = trimData(surname);
		firstname = trimData(firstname);
		
		studentNum = toUpperCase(studentNum);
		sectionName = toUpperCase(sectionName);
		
		if (containWhitespace(username, password, surname, firstname, studentNum, sectionName)) return "Contain whitespaces";
		
		if (username.length() < 5) return "Username must be at least 5 characters";
			
		if (password.length() < 5) return "Password must be at least 5 characters";
		
		if (!sectionService.sectionNameExists(sectionName)) return "Section Not Exists";
			
		if (sAccountRepository.existsByStudentNumber(studentNum)) return "Student Number already exists";
			
		if (sAccountRepository.existsByUsername(username)) return "Username already exists";
		
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
	
	
	public void updateSectionName(String newSectionName, StudentAccountEntity studentAcc) {
		StudentAccountEntity acc = getAccountByUsername(studentAcc.getUsername());
		acc.setSectionName(newSectionName);
		sAccountRepository.save(acc);
	}
}
