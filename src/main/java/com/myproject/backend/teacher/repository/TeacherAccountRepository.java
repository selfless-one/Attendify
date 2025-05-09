package com.myproject.backend.teacher.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myproject.backend.teacher.entity.TeacherAccountEntity;

@Repository
public interface TeacherAccountRepository extends JpaRepository<TeacherAccountEntity, Integer> {
	
	//Optional<TeacherAccountEntity> findByEmail(String email);
    Optional<TeacherAccountEntity> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
}
