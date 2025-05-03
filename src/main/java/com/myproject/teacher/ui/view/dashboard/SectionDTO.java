package com.myproject.teacher.ui.view.dashboard;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SectionDTO {

	public SectionDTO(String sectionName, String course, String dateAdded) {
		super();
		this.sectionName = sectionName;
		this.course = course;
		this.dateAdded = dateAdded;
	}

	@Setter(AccessLevel.NONE)
	private int id;
	private String sectionName;
	private String course;
	private String dateAdded;

}
