package com.myproject.backend.teacher.service;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.myproject.backend.teacher.repository.TeacherAccountRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TeacherAccountService {

    private final TeacherAccountRepository teacherAccountRepo;
    
    private final String myToken = "123";
    
    private final String trimData(String data) {
    	return data.trim();
    }

    public String createAccount(String username, String password, String token) {
    	
    	username = trimData(username);
    	password = trimData(password);
    	token = trimData(token);
    	
    	String[] s = {username, password};
    	boolean containWhiteSpaces = Arrays.stream(s).anyMatch(d -> d.contains(" "));
    	
    	if (containWhiteSpaces) return "Contain whitespaces";
    	
    	if (username.length() < 5) return "Username must be at least 5 characters long";
    	
    	if (password.length() < 5) return "Password must be at least 5 characters long";
    	
        if (!myToken.equals(token)) return "Invalid token";
        
        if (teacherAccountRepo.existsByUsername(username)) return "Username already exists";
        
        teacherAccountRepo.save(TeacherAccountEntity.builder()
        		.username(username)
        		.password(password)
        		.build());
        
        return "Account created successfully";
    }

    public boolean authenticate(String username, String password) {
        return teacherAccountRepo.findByUsername(username)
                .map(account -> account.getPassword().equals(password))
                .orElse(false);
    }

    public String loginAccount(String username, String password) {
        return teacherAccountRepo.findByUsername(username).map(account -> {
        	return account.getPassword().equals(password) ? "Log in success" : "Invalid credentials";
        }).orElse("Invalid credentials");
    }

    public TeacherAccountEntity getAccountByUsername(String username) {
        return teacherAccountRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public void saveChanges(TeacherAccountEntity acc) {
        teacherAccountRepo.save(acc);
    }

    public void saveSurnameAndFirstname(String surname, String firstname, TeacherAccountEntity acc) {
    	
    	teacherAccountRepo.findById(acc.getId()).ifPresentOrElse(account -> {
    		
    		String surnameC = trimData(surname), firstnameC = trimData(firstname);
    		
    		var surnameFormat = surnameC.substring(0, 1).toUpperCase() + surnameC.substring(1).toLowerCase();
    		var firstnameFormat = firstnameC.substring(0, 1).toUpperCase() + firstnameC.substring(1).toLowerCase();
    		
    		account.setSurname(surnameFormat);
    		account.setFirstname(firstnameFormat);
    		
    		saveChanges(account);
    		
    	}, () -> new Exception("Account not found by ID: " + acc.getId()));
    }
    
    public void deleteSectionByName(String sectionName, TeacherAccountEntity teacherAccount) {
    	TeacherAccountEntity t = getAccountByUsername(teacherAccount.getUsername());
    	t.getSections().removeIf(section -> section.getSectionName().equals(sectionName));
    	saveChanges(t);
    }
    
    public void updateSection(String newSectionName, String newCourseName, String sectionNameToUpdate, TeacherAccountEntity teacherAccountEntity) {

    	TeacherAccountEntity teacherAccount = getAccountByUsername(teacherAccountEntity.getUsername());
    	
    	boolean[] hasChanges = new boolean[1];
    	
    	teacherAccount.getSections().forEach(section -> {
    		
    		if (section.getSectionName().equals(sectionNameToUpdate)) {
    			
    			if (!section.getSectionName().equals(newSectionName)) {
    				section.setSectionName(newSectionName);
    				hasChanges[0] = true;
    			}
    			if (!section.getCourse().equals(newCourseName)) {
    				section.setCourse(newCourseName);
    				hasChanges[0] = true;
    			}
    			return;
    		}
    	});
    	
    	if (hasChanges[0]) saveChanges(teacherAccount);
    }
    
    
}
