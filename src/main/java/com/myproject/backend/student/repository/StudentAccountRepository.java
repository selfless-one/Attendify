package com.myproject.backend.student.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myproject.backend.student.entity.StudentAccountEntity;

@Repository
public interface StudentAccountRepository extends JpaRepository<StudentAccountEntity, Integer> {
	
	Optional<StudentAccountEntity> findByEmail(String email);
//	Optional<StudentAccountEntity> findByStudentNumber(String studentNumber);
	Optional<StudentAccountEntity> findByUsername(String surname);

	boolean existsByStudentNumber(String studentNumber);
	boolean existsByUsername(String username);
	
}
