package com.myproject.backend.teacher.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.repository.SectionRepository;
import com.myproject.backend.teacher.repository.SubjectRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SubjectService {
	
	private final SubjectRepository subjectRepo;
	private final SectionRepository sectionRepo;
	
	public List<SubjectEntity> getAllSubjectBySectionID(Integer sectionID) {
		
		return sectionRepo.findById(sectionID).map(section -> {
			return subjectRepo.findBySection(section);
		}).orElse(null);
		
	}
	
	public Optional<SubjectEntity> getById(Integer id) {
		return subjectRepo.findById(id);
	}
	
	
	public void updateSubject(SubjectEntity sub) {
		subjectRepo.save(sub);
	}
	
	public void updateStatusAndAttedanceEndTimeById(Integer subjectId, String newStatus, LocalTime attendanceEndtime) {
		
		subjectRepo.findById(subjectId).ifPresentOrElse(subject -> {
			subject.setStatus(newStatus);
			subject.setAttendanceEndTime(attendanceEndtime);
			updateSubject(subject);
		}, () -> new Exception("Subject not found. ID: " + subjectId));	
	}
	
	
	public void save(SubjectEntity sub) {
		subjectRepo.save(sub);
	}
	
	public void hasBeendownloadedStudentData(Integer subjectEntityID) {
		
		subjectRepo.findById(subjectEntityID).ifPresentOrElse(subject -> {
			
			subject.setHasBeenDownloadedStudentAttendified(true);
			subject.getStudentAttentifiedEntity().clear();
			save(subject);
		}, () -> new Exception("Subject not found. ID: " + subjectEntityID));
		
		
	}
}
