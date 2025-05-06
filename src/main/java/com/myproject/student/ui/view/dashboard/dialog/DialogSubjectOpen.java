package com.myproject.student.ui.view.dashboard.dialog;

import com.myproject.backend.student.entity.StudentAccountEntity;
import com.myproject.backend.student.service.StudentAccountService;
import com.myproject.backend.teacher.entity.StudentAttentifiedEntity;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.service.SubjectService;
import com.myproject.student.ui.view.StudentLoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class DialogSubjectOpen extends Dialog {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final SubjectService subjectService;
	
	private StudentAccountEntity studentAccount;
	private SubjectEntity subjectEntity;
	
	private String studentUsernameSessioned = (String) UI.getCurrent().getSession().getAttribute("studentUsername");
	private Integer idOfSelectedSubject = (Integer) UI.getCurrent().getSession().getAttribute("idOfSelectedSubject");
	
	private Runnable r;
	
	private TextField studentNumberF  = new TextField("Student number");
	private TextField surnameF  = new TextField("Surname");
	private TextField firstnameF  = new TextField("Firstname");
	private TextField courseF  = new TextField("Course");
	private TextField emailF  = new TextField("Email");

	private Button submitBtn;
	private Button cancelBtn;
	
	private VerticalLayout headerLayout() {

		Button closeButton = new Button(VaadinIcon.CLOSE.create());

		closeButton.setAriaLabel("Close dialog");
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

		// Style to float in upper-right corner
		closeButton.getStyle()
		.set("color", "White")
		.set("position", "absolute")
		.set("top", "10px")
		.set("right", "10px")
		.set("z-index", "100")
		.set("padding", "0")
		.set("width", "24px")
		.set("height", "24px")
		.set("font-size", "12px");

		closeButton.addClickListener(evt -> this.close());

		H2 headline = new H2("Attendify");
		headline.getStyle().set("padding", "var(--lumo-space-m) 0")
		.set("padding-bottom", "5px")
		.set("user-select", "none")
		.set("color", "White");

		String subjectStatus = subjectService.getById(idOfSelectedSubject).get().getStatus();

		Span status = new Span("Status: ");
		Span statusVal = new Span(subjectStatus);

		statusVal.getStyle().set("padding", "var(--lumo-space-m) 0")
		.set("padding-bottom", "5px")
		.set("user-select", "none")
		.set("color", "#82F5ED");


		status.getStyle().set("padding", "var(--lumo-space-m) 0")
		.set("padding-bottom", "5px")
		.set("user-select", "none")
		.set("color", "White");

		statusVal.getStyle().set("margin-left", "5px");

		HorizontalLayout statusWrapper = new HorizontalLayout(status, statusVal);

		statusWrapper.setSpacing(false);
		statusWrapper.setPadding(false);
		statusWrapper.setMargin(false);

		HorizontalLayout headlineWrapper = new HorizontalLayout(closeButton, headline, statusWrapper);

		headlineWrapper.setWidthFull();
		headlineWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		headlineWrapper.setAlignItems(FlexComponent.Alignment.CENTER);


		String subjectToDisplay = subjectEntity.getSubjectCode();
		String sectionNameToDisplay = studentAccount.getSectionName();
		
		
		Span description = new Span(String.format("Subject: %s  |  Section: %s", subjectToDisplay, sectionNameToDisplay));
		description.getStyle().set("user-select", "none")
		.set("color", "White");

		VerticalLayout header = new VerticalLayout(headlineWrapper, description);
		header.addClassName("draggable");
		header.getStyle().set("cursor", "move");

		return header;

	}

	public DialogSubjectOpen(Runnable r, 
			SubjectEntity subjectEntity, 
			SubjectService subjectService,
			StudentAccountService studentAccountService) {

		this.r = r;
		this.subjectEntity = subjectEntity;
		this.subjectService = subjectService;
		
		if (studentUsernameSessioned == null || idOfSelectedSubject == 0) {
			UI.getCurrent().navigate(StudentLoginView.class);
			return;
			
		} else {
			studentAccount = studentAccountService.getAccountByUsername(studentUsernameSessioned);
		}
		
		setModal(true);
		setCloseOnOutsideClick(false);
		setDraggable(true);
		invokeDialog();
	}


	private void invokeDialog() {

		getHeader().removeAll();
		getFooter().removeAll();
		removeAll();

		getHeader().add(headerLayout());
		add(contentLayoutWhenStatusIsOpen());
		getFooter().add(footerLayoutWhenStatusIsOpen());
	}
	
	private VerticalLayout contentLayoutWhenStatusIsOpen() {
	
		studentNumberF.setRequired(true);
		surnameF.setRequired(true);
		firstnameF.setRequired(true);
		courseF.setRequired(true);
		emailF.setRequired(true);
		
		studentNumberF.setValue(studentAccount.getStudentNumber());
		surnameF.setValue(studentAccount.getSurname());
		firstnameF.setValue(studentAccount.getFirstname());
		//courseF.setValue(studentAccount.getCourse());
		//emailF.setValue(studentAccount.getEmail());
		
		VerticalLayout layout = new VerticalLayout(studentNumberF, surnameF, firstnameF, courseF, emailF);
		
		layout.setPadding(false);
		layout.setSpacing(false);
		layout.setAlignItems(FlexComponent.Alignment.STRETCH);
		layout.getStyle().set("width", "18rem").set("max-width", "100%");

	    return layout;
	}

	private HorizontalLayout footerLayoutWhenStatusIsOpen() {
		
		submitBtn = new Button("Submit");                       
		cancelBtn = new Button("Cancel", evt -> this.close());  
		                                               
		submitBtn.getStyle()
		.set("font-size", "14px")
		.set("background-color", "#21a05d")
		.set("border-radius", "10px")
		.set("padding", "10px 20px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		submitBtn.getElement().getThemeList().add("primary");
		submitBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);

		cancelBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancelBtn.getStyle()
		.set("font-size", "14px")
		.set("border-radius", "10px")
		.set("padding", "10px 20px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		cancelBtn.getElement().getThemeList().add("primary");
		cancelBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);

		submitBtn.addClickListener(evt -> {
			
			if (studentNumberF.getValue().isBlank()) {
				studentNumberF.setInvalid(true);
				studentNumberF.setErrorMessage("required");
			}
			
			if (surnameF.getValue().isBlank()) {
				surnameF.setInvalid(true);
				surnameF.setErrorMessage("required");
			}
			
			if (firstnameF.getValue().isBlank()) {
				firstnameF.setInvalid(true);
				firstnameF.setErrorMessage("required");
			}
			
			if (courseF.getValue().isBlank()) {
				courseF.setInvalid(true);
				courseF.setErrorMessage("required");
			}
			
			if (emailF.getValue().isBlank()) {
				emailF.setInvalid(true);
				emailF.setErrorMessage("required");
			}
			
			if (!studentNumberF.getValue().isBlank() &&
				!surnameF.getValue().isBlank() &&
				!firstnameF.getValue().isBlank() &&
				!courseF.getValue().isBlank() &&
				!emailF.getValue().isBlank()) {
				
				
				System.out.println("go lang");
				
				confirmSubmissionDialog();
				
			}
			
			
			
		});

		return new HorizontalLayout(submitBtn, cancelBtn);
	}

	private ConfirmDialog confirmSubmissionDialog() {

		ConfirmDialog dialog = new ConfirmDialog();

		dialog.setHeader("Confirm submission");
		dialog.setText("Are all details correct?");
		dialog.setCancelable(true);
		dialog.addCancelListener(event -> dialog.close());
		dialog.setConfirmText("Confirm");
		dialog.setConfirmButtonTheme("error primary");
		dialog.addConfirmListener(event -> {

			// double check if status is not closed
			if (subjectEntity.getStatus().equals("Open")) {


				StudentAttentifiedEntity attendifyToSave = StudentAttentifiedEntity.builder()
						.studentNumber(studentNumberF.getValue())
						.surname(surnameF.getValue())
						.firstname(firstnameF.getValue())
						.course(courseF.getValue())
						.email(emailF.getValue())
						.build();

				subjectEntity.getSAttentifiedEntity().add(attendifyToSave);
				subjectService.save(subjectEntity);

				// maybe void
				r.run();
				//	invokeDialog();
				dialog.close();
				this.close();

			} else {
				new DialogSubjectClose();
			}
	
			
		});
		
		dialog.open();
		return dialog;
	}

}
