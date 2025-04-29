package com.myproject.backend.teacher.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myproject.backend.teacher.entity.TeacherAccount;

@Repository
public interface TeacherAccountRepository extends JpaRepository<TeacherAccount, Integer> {
	
	Optional <TeacherAccount> findByEmail(String email);

}
