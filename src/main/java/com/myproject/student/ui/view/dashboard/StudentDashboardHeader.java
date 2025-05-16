package com.myproject.student.ui.view.dashboard;

import com.myproject.backend.student.entity.StudentAccountEntity;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class StudentDashboardHeader extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StudentDashboardHeader(StudentAccountEntity acc) {
		
		this.setJustifyContentMode(JustifyContentMode.BETWEEN);
		this.setWidth("400px");
		this.getStyle().set("margin-top", "20px");
		
		Span studentLabel = new Span(acc.getFirstname() + " " + acc.getSurname());
		studentLabel.getStyle()
		.set("font-size", "16px")
		.set("color", "#08cad1");

		Span sectionLabel = new Span(acc.getSectionName());
		//sectionLabel.getStyle().setPaddingLeft("10%");
		//sectionLabel.getStyle().setPaddingRight("31%");
		sectionLabel.getStyle().set("font-size", "16px")
		.set("color", "#08cad1").setMarginRight("2px");
		
		add(studentLabel, sectionLabel);
	}
}