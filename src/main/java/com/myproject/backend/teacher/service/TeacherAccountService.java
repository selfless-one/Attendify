package com.myproject.backend.teacher.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.TeacherAccount;
import com.myproject.backend.teacher.repository.SectionRepository;
import com.myproject.backend.teacher.repository.TeacherAccountRepository;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TeacherAccountService {
	
	private final TeacherAccountRepository tAccountRepository;
	private final SectionRepository sRepository;	
	
	public String createAccount(String email, String password, String token) {
		
		var myToken = "123";
		
		if (!myToken.endsWith(token)) {
			return "Invalid token";
		}
		
		var emailTaken = tAccountRepository.existsByEmail(email);
		
		if (emailTaken) {
			return "Email already taken";
		}
		
		
		tAccountRepository.save(TeacherAccount.builder().email(email).password(password).build());
		return "Account created successfully";
	}
	
	
	public boolean authenticate(String email, String password) {
		
		return tAccountRepository.findByEmail(email).map(a -> {
			
			var passwordMatched = a.getPassword().equals(password);
			
			if (passwordMatched) return true;
			else return false;
			
		}).orElse(false);
		
	}
	public String loginAccount(String email, String password) {
		
		Optional<TeacherAccount> account = tAccountRepository.findByEmail(email);
		
		if (account.isEmpty()) {
			return "Invalid credentials";
		}
		
		var accPassword = account.get().getPassword();
		
		if (password.equals(accPassword)) {
			
			return "Log in success";
		}
		
		return "Invalid credentials";
	}
	
	
	public TeacherAccount getAccount(String email) {
		
		Optional<TeacherAccount> acc = tAccountRepository.findByEmail(email);
		
		return acc.get();
		
	}
	
	public List<SectionEntity> getAllSectionOfTeacher(String email) {
		
		return tAccountRepository.findByEmail(email).get().getSections();

	}
	
	
	public void saveChanges(TeacherAccount acc) {
		
		tAccountRepository.save(acc);
		
	}
	
	@PostConstruct void initialData() {
		
		TeacherAccount teacher1 = TeacherAccount.builder().email("chiong@gmail.com").password("chiong123").build();
	    TeacherAccount teacher2 = TeacherAccount.builder().email("rowel@gmail.com").password("rowel123").build();
		
		tAccountRepository.save(teacher1);
		tAccountRepository.save(teacher2);
		
		
		sRepository.save(SectionEntity.builder().sectionName("LFAU133N001").course("BSIT").teacher(teacher1).build());
		sRepository.save(SectionEntity.builder().sectionName("LFAU133N002").course("BSCS").teacher(teacher1).build());
		sRepository.save(SectionEntity.builder().sectionName("LFAU133N004").course("BSED").teacher(teacher1).build());
		
		sRepository.save(SectionEntity.builder().sectionName("LFAU133N001").course("BSIT").teacher(teacher2).build());
		sRepository.save(SectionEntity.builder().sectionName("LFAU133N002").course("BSCS").teacher(teacher2).build());
		sRepository.save(SectionEntity.builder().sectionName("LFAU133N004").course("BSED").teacher(teacher2).build());
		sRepository.save(SectionEntity.builder().sectionName("LFAU133N001").course("BSIT").teacher(teacher2).build());
		sRepository.save(SectionEntity.builder().sectionName("LFAU133N002").course("BSCS").teacher(teacher2).build());
		sRepository.save(SectionEntity.builder().sectionName("LFAU133N004").course("BSED").teacher(teacher2).build());
		
		
		
			
	}
	
	

}
