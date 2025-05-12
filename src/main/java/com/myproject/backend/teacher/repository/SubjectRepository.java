package com.myproject.backend.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.SubjectEntity;
import java.util.List;


@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, Integer> {
		
	List<SubjectEntity> findBySection(SectionEntity section);
	List<SubjectEntity> findByStatus(String status);
	
}
