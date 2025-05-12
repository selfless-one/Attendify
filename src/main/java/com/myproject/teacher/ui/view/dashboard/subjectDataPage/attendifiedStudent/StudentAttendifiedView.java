package com.myproject.teacher.ui.view.dashboard.subjectDataPage.attendifiedStudent;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.myproject.backend.teacher.entity.StudentAttentifiedEntity;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.service.SubjectService;
import com.myproject.teacher.ui.view.TeacherLoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("student/attendified/live/subject")
public class StudentAttendifiedView extends Div implements HasUrlParameter<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final SubjectService subjectService;
	private SubjectEntity subjectEntity;
	private String subjectCode;

	private Integer idOfSubjectEntity;
	
	private Grid<StudentAttentifiedEntity> studentsAttendifiedGrid;
	private List<StudentAttentifiedEntity> studentsAttendified = new LinkedList<>();
	
	private TextField searchField;
	private Button downloadBtn;
	
	@Override
	public void setParameter(BeforeEvent event, String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public StudentAttendifiedView(SubjectService subjectService) {
		
		this.subjectService = subjectService;
		
		idOfSubjectEntity = (Integer) UI.getCurrent().getSession().getAttribute("idOfSubjectEntity");
		
		if (idOfSubjectEntity == 0) {
			UI.getCurrent().close();
			UI.getCurrent().navigate(TeacherLoginView.class);
		}
		
		setupStudentsAttendifiedGrid();
		setupSearchField();
		setupDownloadButton();
		
		syncStudentsAttendified();
		
		
		Span sectionLabel = new Span("Section: " + subjectEntity.getSection().getSectionName());
		Span subjectLabel = new Span("Subject: " + subjectEntity.getSubjectCode());
		
		sectionLabel.getStyle().setFontSize("25px");
		subjectLabel.getStyle().setFontSize("25px");
		subjectLabel.getStyle().setPaddingLeft("10px");
		subjectLabel.getStyle().setPaddingRight("15px");
		
		
		HorizontalLayout header  = new HorizontalLayout(sectionLabel, subjectLabel, searchField);
		header.setWidthFull();

		VerticalLayout layout = new VerticalLayout(header, studentsAttendifiedGrid);

		add(layout);
		
		final ConfirmDialog closePageDialog = new ConfirmDialog("Attendance has been closed", 
				"You may close this tab", "ok", event -> event.getSource().close());
		
		boolean[] openClosePageDialogOnce = {false};

		UI.getCurrent().setPollInterval(1000);
		UI.getCurrent().addPollListener(pool -> {
			
			syncStudentsAttendified();

			if (subjectEntity.getStatus().equals("Closed") && !openClosePageDialogOnce[0]) {
				openClosePageDialogOnce[0] = true;
				closePageDialog.open();
				add(closePageDialog);
				
				UI.getCurrent().setPollInterval(-1);
			}
		});
		
	}

	private void syncStudentsAttendified() {
		
		studentsAttendified.clear();
		
		subjectEntity = subjectService.getById(idOfSubjectEntity).get();
		
		studentsAttendified = subjectEntity.getStudentAttentifiedEntity();
		
		studentsAttendifiedGrid.setItems(studentsAttendified);
	}

	private void setupStudentsAttendifiedGrid() {
		
		studentsAttendifiedGrid = new Grid<>();

		studentsAttendifiedGrid.setHeight("800px");

		studentsAttendifiedGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

		// row Header Label in grid
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
		
		
	}
	
	private void setupSearchField() {
		 searchField = new TextField();
		 
		 searchField.setWidth("370px");
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

	}
	
	private void setupDownloadButton() {
		downloadBtn = new Button("Download");
		
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
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });");
		
		downloadBtn.addClickListener(event -> {
			
			DownloadStudentAttendified exporter = new DownloadStudentAttendified(idOfSubjectEntity, subjectService);
			
			StreamResource excelResource = exporter.getExcelResource();
			
			Anchor downloadLink = new Anchor(excelResource, "");
		    downloadLink.getElement().setAttribute("download", true); // Important!
		    downloadLink.getStyle().set("display", "none"); // hide from view
		    add(downloadLink); // add to layout
		   
		    subjectService.hasBeendownloadedStudentData(subjectEntity.getId());
		    
		    downloadLink.getElement().executeJs("this.click();"); // Simulate automatic click
		    
		    final ConfirmDialog closePageDialog = new ConfirmDialog("Attendance has been downloaded", 
					"You may close this tab", "close", e -> UI.getCurrent().getPage().executeJs("window.close();"));
		    
		    closePageDialog.open();
		    
		});
		
	}
}

// StudentAttentifiedEntity[] studMock = {
//
//		StudentAttentifiedEntity.builder().studentNumber("UA202200305").surname("Razonable").firstname("Rowel").course("BSIT").email("razonabler31@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA202330433").surname("Chiong").firstname("Joriz").course("BSIT").email("chiong23@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA434343432").surname("Boncales").firstname("Brytch").course("BSIT").email("bonccales232@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA203223434").surname("De guzman").firstname("Errol").course("BSIT").email("deguzman@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA220038").surname("Laconsay").firstname("Reymart").course("BSIT").email("laconsay@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA2232424445").surname("Dalman").firstname("Flynn").course("BSIS").email("flynn@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA201132222").surname("Padua").firstname("Angel").course("BSIT").email("angelPadua23gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA2023113132").surname("Merca").firstname("Randy").course("BSIT").email("randymerca@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("U232233333").surname("Ramos").firstname("Kim").course("BSIT").email("ra@gmail.com").build(),
//
//		StudentAttentifiedEntity.builder().studentNumber("UA202200305").surname("Razonable").firstname("Rowel").course("BSIT").email("razonabler31@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA202330433").surname("Chiong").firstname("Joriz").course("BSIT").email("chiong23@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA434343432").surname("Boncales").firstname("Brytch").course("BSIT").email("bonccales232@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA203223434").surname("De guzman").firstname("Errol").course("BSIT").email("deguzman@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA220038").surname("Laconsay").firstname("Reymart").course("BSIT").email("laconsay@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA2232424445").surname("Dalman").firstname("Flynn").course("BSIS").email("flynn@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA201132222").surname("Padua").firstname("Angel").course("BSIT").email("angelPadua23gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA2023113132").surname("Merca").firstname("Randy").course("BSIT").email("randymerca@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("U232233333").surname("Ramos").firstname("Kim").course("BSIT").email("ra@gmail.com").build(),
//
//		StudentAttentifiedEntity.builder().studentNumber("UA202200305").surname("Razonable").firstname("Rowel").course("BSIT").email("razonabler31@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA202330433").surname("Chiong").firstname("Joriz").course("BSIT").email("chiong23@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA434343432").surname("Boncales").firstname("Brytch").course("BSIT").email("bonccales232@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA203223434").surname("De guzman").firstname("Errol").course("BSIT").email("deguzman@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA220038").surname("Laconsay").firstname("Reymart").course("BSIT").email("laconsay@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA2232424445").surname("Dalman").firstname("Flynn").course("BSIS").email("flynn@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA201132222").surname("Padua").firstname("Angel").course("BSIT").email("angelPadua23gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA2023113132").surname("Merca").firstname("Randy").course("BSIT").email("randymerca@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("U232233333").surname("Ramos").firstname("Kim").course("BSIT").email("ra@gmail.com").build(),
//
//		StudentAttentifiedEntity.builder().studentNumber("UA202200305").surname("Razonable").firstname("Rowel").course("BSIT").email("razonabler31@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA202330433").surname("Chiong").firstname("Joriz").course("BSIT").email("chiong23@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA434343432").surname("Boncales").firstname("Brytch").course("BSIT").email("bonccales232@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA203223434").surname("De guzman").firstname("Errol").course("BSIT").email("deguzman@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA220038").surname("Laconsay").firstname("Reymart").course("BSIT").email("laconsay@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA2232424445").surname("Dalman").firstname("Flynn").course("BSIS").email("flynn@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA201132222").surname("Padua").firstname("Angel").course("BSIT").email("angelPadua23gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("UA2023113132").surname("Merca").firstname("Randy").course("BSIT").email("randymerca@gmail.com").build(),
//		StudentAttentifiedEntity.builder().studentNumber("U232233333").surname("Ramos").firstname("Kim").course("BSIT").email("ra@gmail.com").build()
//};



