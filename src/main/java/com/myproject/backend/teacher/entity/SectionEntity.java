package com.myproject.backend.teacher.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "section_list")
@Entity
public class SectionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Integer id;
	
	private String sectionName;
	private String course;
	private LocalDateTime dateCreated;
	
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private TeacherAccountEntity teacher;
	
	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<SubjectEntity> subjects;


	
}
