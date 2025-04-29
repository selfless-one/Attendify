package com.myproject.backend.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myproject.backend.teacher.entity.SectionEntity;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, Integer> {
	
	

}
