package com.myproject.student.ui.view.dashboard;

import com.myproject.backend.student.entity.StudentAccountEntity;
import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;

public class StudentDashboardHeader extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StudentDashboardHeader(StudentAccountEntity acc) {
		setWidth("935px");
		setJustifyContentMode(JustifyContentMode.BETWEEN);
		setDefaultVerticalComponentAlignment(Alignment.CENTER);
		getStyle().set("margin-top", "40px");

		Span studentLabel = new Span("Student: " + acc.getFirstname() + " " + acc.getSurname());
		studentLabel.getStyle()
		//.set("font-weight", "bold")
		.set("font-size", "18px");

		Span sectionLabel = new Span("Section: " + acc.getSectionName());
		sectionLabel.getStyle().setPaddingLeft("10%");
		sectionLabel.getStyle().setPaddingRight("31%");
		sectionLabel.getStyle().set("font-size", "18px")
		.set("margin-left", "6px");

//		sectionValueLabel.getStyle().set("font-weight", "bold")
//		.set("font-size", "25px");

		Button logoutBtn = new Button("Log out", e -> {
			UI.getCurrent().getSession().close();
			UI.getCurrent().navigate("teacher/login");
		});
		
		logoutBtn.getStyle()
		.set("color", "white")
 		.set("font-size", "14px")
		.set("background-color", "#4460EF")
		.set("border-radius", "10px")
		.set("padding", "10px 20px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
	//	logoutBtn.getElement().getThemeList().add("error");
		logoutBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);
		
		//logoutBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

		add(studentLabel, sectionLabel, logoutBtn);
	}
}