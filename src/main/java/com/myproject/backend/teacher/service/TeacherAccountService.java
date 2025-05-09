package com.myproject.backend.teacher.service;

import org.springframework.stereotype.Service;

import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.myproject.backend.teacher.repository.TeacherAccountRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TeacherAccountService {

    private final TeacherAccountRepository teacherAccountRepo;
    
    private final String myToken = "123";

    public String createAccount(String username, String password, String token) {
    	
        if (!myToken.equals(token)) {
        	return "Invalid token";
        }
        
        if (teacherAccountRepo.existsByUsername(username)) {
        	return "Username already exists";
        }
        
        teacherAccountRepo.save(TeacherAccountEntity.builder().username(username).password(password).build());
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

    
}
