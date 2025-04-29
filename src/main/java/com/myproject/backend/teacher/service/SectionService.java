package com.myproject.backend.teacher.service;

import org.springframework.stereotype.Service;

import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.TeacherAccount;
import com.myproject.backend.teacher.repository.SectionRepository;

import jakarta.annotation.PostConstruct;

@Service
public class SectionService {

	private final SectionRepository sRepository;

	public SectionService(SectionRepository sRepository) {
		this.sRepository = sRepository;
	}


	@PostConstruct void initialData() {

		
	}










}
