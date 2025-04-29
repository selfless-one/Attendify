package com.myproject.backend.teacher.service;

import org.springframework.stereotype.Service;

import com.myproject.backend.teacher.entity.TeacherAccount;
import com.myproject.backend.teacher.repository.TeacherAccountRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TeacherAccountService {

    private final TeacherAccountRepository teacherAccountRepo;
    

    public String createAccount(String email, String password, String token) {
    	
        var myToken = "123";
        if (!myToken.equals(token)) return "Invalid token";
        
        return teacherAccountRepo.findByEmail(email).map(acc -> {
        	teacherAccountRepo.save(TeacherAccount.builder().email(email).password(password).build());
            return "Account created successfully";
        	
        }).orElse("Email already taken");
    }

    public boolean authenticate(String email, String password) {
        return teacherAccountRepo.findByEmail(email)
                .map(account -> account.getPassword().equals(password))
                .orElse(false);
    }

    public String loginAccount(String email, String password) {
        return teacherAccountRepo.findByEmail(email)
                .filter(account -> account.getPassword().equals(password))
                .map(account -> "Log in success")
                .orElse("Invalid credentials");
    }

    public TeacherAccount getAccountByEmail(String email) {
        return teacherAccountRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public void saveChanges(TeacherAccount acc) {
        teacherAccountRepo.save(acc);
    }

    
}
