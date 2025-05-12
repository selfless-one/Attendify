package com.myproject.backend.teacher.service;

import org.springframework.stereotype.Service;

import com.myproject.backend.teacher.repository.StudentAttendifiedRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentAttendifiedService {

	private final StudentAttendifiedRepository studentAttendifiedRepository;
	
	public boolean studentNumberAlreadyExists(String studentnumber) {
		return studentAttendifiedRepository.existsByStudentNumber(studentnumber);
	}	
}
