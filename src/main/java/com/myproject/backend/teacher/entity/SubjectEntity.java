package com.myproject.backend.teacher.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
@Builder
@Getter
@Setter
@Entity
@Table(name = "subject_list")
public class SubjectEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE)
	private Integer id;
	
	private String subjectCode;
	private String subjectDescription;
	
	private LocalDateTime dateCreated;
	
	private LocalTime attendanceEndTime;
	private String status;
	
	@Builder.Default
	private boolean hasBeenDownloadedStudentAttendified = true;
	
	@ManyToOne()
	@JoinColumn(name = "section_id")
	private SectionEntity section;
	
	@OneToMany(mappedBy = "subjectCode", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<StudentAttentifiedEntity> studentAttentifiedEntity;

}
