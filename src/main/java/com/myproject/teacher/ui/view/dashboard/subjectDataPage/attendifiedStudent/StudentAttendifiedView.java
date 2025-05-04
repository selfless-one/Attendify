package com.myproject.teacher.ui.view.dashboard.subjectDataPage.attendifiedStudent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.myproject.backend.teacher.entity.StudentAttentifiedEntity;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;

@Route("student/attendified/live")
public class StudentAttendifiedView extends Div {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	StudentAttentifiedEntity[] studMock = {

			StudentAttentifiedEntity.builder().studentNumber("UA202200305").surname("Razonable").firstname("Rowel").course("BSIT").email("razonabler31@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA202330433").surname("Chiong").firstname("Joriz").course("BSIT").email("chiong23@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA434343432").surname("Boncales").firstname("Brytch").course("BSIT").email("bonccales232@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA203223434").surname("De guzman").firstname("Errol").course("BSIT").email("deguzman@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA220038").surname("Laconsay").firstname("Reymart").course("BSIT").email("laconsay@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA2232424445").surname("Dalman").firstname("Flynn").course("BSIS").email("flynn@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA201132222").surname("Padua").firstname("Angel").course("BSIT").email("angelPadua23gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA2023113132").surname("Merca").firstname("Randy").course("BSIT").email("randymerca@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("U232233333").surname("Ramos").firstname("Kim").course("BSIT").email("ra@gmail.com").build(),

			StudentAttentifiedEntity.builder().studentNumber("UA202200305").surname("Razonable").firstname("Rowel").course("BSIT").email("razonabler31@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA202330433").surname("Chiong").firstname("Joriz").course("BSIT").email("chiong23@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA434343432").surname("Boncales").firstname("Brytch").course("BSIT").email("bonccales232@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA203223434").surname("De guzman").firstname("Errol").course("BSIT").email("deguzman@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA220038").surname("Laconsay").firstname("Reymart").course("BSIT").email("laconsay@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA2232424445").surname("Dalman").firstname("Flynn").course("BSIS").email("flynn@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA201132222").surname("Padua").firstname("Angel").course("BSIT").email("angelPadua23gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA2023113132").surname("Merca").firstname("Randy").course("BSIT").email("randymerca@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("U232233333").surname("Ramos").firstname("Kim").course("BSIT").email("ra@gmail.com").build(),

			StudentAttentifiedEntity.builder().studentNumber("UA202200305").surname("Razonable").firstname("Rowel").course("BSIT").email("razonabler31@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA202330433").surname("Chiong").firstname("Joriz").course("BSIT").email("chiong23@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA434343432").surname("Boncales").firstname("Brytch").course("BSIT").email("bonccales232@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA203223434").surname("De guzman").firstname("Errol").course("BSIT").email("deguzman@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA220038").surname("Laconsay").firstname("Reymart").course("BSIT").email("laconsay@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA2232424445").surname("Dalman").firstname("Flynn").course("BSIS").email("flynn@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA201132222").surname("Padua").firstname("Angel").course("BSIT").email("angelPadua23gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA2023113132").surname("Merca").firstname("Randy").course("BSIT").email("randymerca@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("U232233333").surname("Ramos").firstname("Kim").course("BSIT").email("ra@gmail.com").build(),

			StudentAttentifiedEntity.builder().studentNumber("UA202200305").surname("Razonable").firstname("Rowel").course("BSIT").email("razonabler31@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA202330433").surname("Chiong").firstname("Joriz").course("BSIT").email("chiong23@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA434343432").surname("Boncales").firstname("Brytch").course("BSIT").email("bonccales232@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA203223434").surname("De guzman").firstname("Errol").course("BSIT").email("deguzman@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA220038").surname("Laconsay").firstname("Reymart").course("BSIT").email("laconsay@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA2232424445").surname("Dalman").firstname("Flynn").course("BSIS").email("flynn@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA201132222").surname("Padua").firstname("Angel").course("BSIT").email("angelPadua23gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("UA2023113132").surname("Merca").firstname("Randy").course("BSIT").email("randymerca@gmail.com").build(),
			StudentAttentifiedEntity.builder().studentNumber("U232233333").surname("Ramos").firstname("Kim").course("BSIT").email("ra@gmail.com").build()
	};


	private Grid<StudentAttentifiedEntity> studentsAttendifiedGrid;
	private List<StudentAttentifiedEntity> studentsAttendified = Arrays.asList(studMock);


	public StudentAttendifiedView() {

		studentsAttendifiedGrid = new Grid<>();

		studentsAttendifiedGrid.setHeight("800px");

		studentsAttendifiedGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

		Span field1 = new Span("Student number");
		Span field2 = new Span("Surname");
		Span field3 = new Span("First name");
		Span field4 = new Span("Course");
		Span field5 = new Span("Email");

		List.of(field1, field2, field3, field4, field5).forEach(header -> header.getStyle().setFontSize("20px"));


		studentsAttendifiedGrid.addColumn(StudentAttentifiedEntity::getStudentNumber).setHeader(field1).setAutoWidth(true);
		studentsAttendifiedGrid.addColumn(StudentAttentifiedEntity::getSurname).setHeader(field2).setAutoWidth(true);
		studentsAttendifiedGrid.addColumn(StudentAttentifiedEntity::getFirstname).setHeader(field3).setAutoWidth(true);
		studentsAttendifiedGrid.addColumn(StudentAttentifiedEntity::getCourse).setHeader(field4).setAutoWidth(true);
		studentsAttendifiedGrid.addColumn(StudentAttentifiedEntity::getEmail).setHeader(field5).setAutoWidth(true);

		TextField searchField = new TextField();
		searchField.setWidth("20%");
		searchField.setPlaceholder("Search");
		searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		searchField.setValueChangeMode(ValueChangeMode.EAGER);

		searchField.addValueChangeListener(e -> {
			String searchTerm = searchField.getValue().trim().toUpperCase();
			List<StudentAttentifiedEntity> filteredStudent = studentsAttendified.stream()
					.filter(
							student -> student.getStudentNumber().toUpperCase().contains(searchTerm)
							|| student.getSurname().toUpperCase().contains(searchTerm)
							|| student.getFirstname().toUpperCase().contains(searchTerm)
							|| student.getCourse().toUpperCase().contains(searchTerm)
							|| student.getEmail().toUpperCase().contains(searchTerm))
					.collect(Collectors.toList());
			studentsAttendifiedGrid.setItems(filteredStudent);
		});


		studentsAttendifiedGrid.setItems(studentsAttendified);

		Button downloadBtn = new Button("Download");

		downloadBtn.getStyle()
		.set("font-size", "14px")
		.set("background-color", "#21a05d")
		.set("border-radius", "10px")
		.set("padding", "10px 20px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		downloadBtn.getElement().getThemeList().add("primary");
		downloadBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);


		HorizontalLayout hor  = new HorizontalLayout(searchField, downloadBtn);

		hor.setWidthFull();

		VerticalLayout layout = new VerticalLayout(hor, studentsAttendifiedGrid);

		add(layout);
	}






}
