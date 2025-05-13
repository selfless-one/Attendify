package com.myproject.backend.teacher.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.repository.SubjectRepository;

@Component
public class AttendanceStatusScheduler {

	private final SubjectRepository subjectRepository;
	private final SubjectService subjectService;
	
	public AttendanceStatusScheduler(SubjectRepository subjectRepository, SubjectService subjectService) {
		this.subjectRepository = subjectRepository;
		this.subjectService = subjectService;
	}
	
	@Scheduled(fixedRate = 30000)
	public void checkAndCloseSubjects() {
		
		List<SubjectEntity> openSubjects = subjectRepository.findByStatus("Open");
		
		if (openSubjects.isEmpty()) return;
		
		for (SubjectEntity sub : openSubjects) {
			
			if (LocalTime.now().isAfter(sub.getAttendanceEndTime())) {
				
				sub.setStatus("Closed");
				sub.setHasBeenDownloadedStudentAttendified(false);
				subjectService.save(sub);
				
				System.out.println("Closed subject: " + sub.getId());
			}
			
			
		}
		
		
		
	}
	
	
}
