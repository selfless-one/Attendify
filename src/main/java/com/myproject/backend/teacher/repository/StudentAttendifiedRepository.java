package com.myproject.backend.teacher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myproject.backend.teacher.entity.StudentAttentifiedEntity;

@Repository
public interface StudentAttendifiedRepository extends JpaRepository<StudentAttentifiedEntity, Integer> {

	boolean existsByStudentNumber(String studentnumber);
	
}
