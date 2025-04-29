package com.myproject.backend.teacher.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.TeacherAccount;

@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, Integer> {

	List<SectionEntity> findByTeacher(TeacherAccount teacher);
	
}
