package com.myproject.teacher.ui.view.dashboard.subjectDataPage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Subject {
	
	@Setter(AccessLevel.NONE)
	private int id;
	private String subjectCode;
	private String subjectDescription;
	private String dateAdded;
	private String status;
	

}
