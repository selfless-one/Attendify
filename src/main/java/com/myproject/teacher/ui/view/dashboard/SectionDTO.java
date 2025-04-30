package com.myproject.teacher.ui.view.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SectionDTO {

	public SectionDTO(String sectionName, String course, String dateAdded) {
		super();
		this.sectionName = sectionName;
		this.course = course;
		this.dateAdded = dateAdded;
	}

	private int id;
	private String sectionName;
	private String course;
	private String dateAdded;

}
