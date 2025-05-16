package com.myproject.student.ui.view.dashboard;import java.util.LinkedList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.myproject.backend.student.entity.StudentAccountEntity;
import com.myproject.backend.student.service.StudentAccountService;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.service.SectionService;
import com.myproject.backend.teacher.service.StudentAttendifiedService;
import com.myproject.backend.teacher.service.SubjectService;
import com.myproject.student.ui.view.StudentLoginView;
import com.myproject.student.ui.view.dashboard.dialog.DialogSubjectClose;
import com.myproject.student.ui.view.dashboard.dialog.DialogSubjectOpen;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Student Dashboard")
@Route("student/dashboard/username")
public class StudentDashboardView extends VerticalLayout implements HasUrlParameter<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HorizontalLayout header;
	private Div body;

	private final StudentAccountService studentAccountService;
	private SubjectService subjectService;
	private SectionService sectionService;

	
	private final StudentAttendifiedService studentAttendifiedService;

	private StudentAccountEntity studentAccount;
	
	private String studentSection;
	
	private final String studentUsernameSessioned = (String) UI.getCurrent().getSession().getAttribute("studentUsername");
	
	@Override
	public void setParameter(BeforeEvent event, String studentUsernameParam) {

		if (studentUsernameSessioned == null) {
			UI.getCurrent().navigate("student/login");
			return;
		}
		
		studentAccount = studentAccountService.getAccountByUsername(studentUsernameSessioned);
		
		this.studentSection = studentAccount.getSectionName();
		
		verifyIfSectionExists();
		
		buildUI();
	}

	
	private Grid<SubjectEntity> subjectGrid;
	private List<SubjectEntity> subjects;

	public StudentDashboardView(StudentAccountService studentAccountService, 
			SubjectService subjectService, 
			SectionService sectionService,
			StudentAttendifiedService studentAttendifiedService) {

		this.studentAttendifiedService = studentAttendifiedService;
		this.studentAccountService = studentAccountService;
		this.sectionService = sectionService;
		this.subjectService = subjectService;
	}
	
	
	private Notification createSubjectCodeInfo(String subjectDesc, String professorName) {
		
        Notification notification = new Notification();
        notification.setPosition(Position.MIDDLE);

        Icon icon1 = VaadinIcon.USER.create();
        icon1.setColor("var(--lumo-success-color)");
        Div profLabel = new Div(new Text("Professor"));
        profLabel.getStyle().set("font-weight", "600")
                .setColor("var(--lumo-success-text-color)");
        Span profName = new Span(professorName);
        profName.getStyle().set("font-size", "var(--lumo-font-size-s)")
                .set("font-weight", "600");
        Div profInfo = new Div(profLabel, new Div(professorName));
        profInfo.getStyle().set("font-size", "var(--lumo-font-size-s)")
                .setColor("var(--lumo-secondary-text-color)");
        
        
        Icon icon2 = VaadinIcon.SWORD.create();
        icon2.setColor("var(--lumo-success-color)");
        Div sectionLabel = new Div(new Text("Subject"));
        sectionLabel.getStyle().set("font-weight", "600")
                .setColor("var(--lumo-success-text-color)");
        Span sectionName = new Span(subjectDesc);
        sectionName.getStyle().set("font-size", "var(--lumo-font-size-s)")
                .set("font-weight", "600");
        Div sectionInfo = new Div(sectionLabel, new Div(sectionName));
        sectionInfo.getStyle().set("font-size", "var(--lumo-font-size-s)")
                .setColor("var(--lumo-secondary-text-color)");
        

        var closeBtn = new Button(new Icon("lumo", "cross"));
        closeBtn.getStyle().setBackgroundColor("transparent");
        
        closeBtn.addClickListener(e -> {	
        	this.setEnabled(true);
        	notification.setEnabled(true);
        	notification.close();
        });
        
        var p = new HorizontalLayout(icon1, profInfo);
        p.setAlignItems(FlexComponent.Alignment.CENTER);
        
        var proflayout = new HorizontalLayout(p, closeBtn);
       // proflayout.setAlignItems(FlexComponent.Alignment.CENTER);
        proflayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        proflayout.setWidthFull();
        
        var s = new HorizontalLayout(icon2, sectionInfo);
        s.setAlignItems(FlexComponent.Alignment.CENTER);
        
        var sectionlayout = new HorizontalLayout(s);
      //  proflayout.setAlignItems(FlexComponent.Alignment.CENTER);
        
        var layout = new VerticalLayout(proflayout, sectionlayout);
        layout.setPadding(false);
        notification.add(layout);

        return notification;
    }

	private void bodyConfig() {
		body = new Div();

		body.setWidthFull();
		
		body.getStyle()
		.set("border", "2px solid black")
		.set("border-radius", "8px")
		.set("padding", "20px")
		.set("width", "360px")
		.set("height", "420px")
		//.set("margin-top", "10px")
		.set("overflow", "auto")
		.set("box-shadow", "0px 4px 10px rgba(0, 0, 0, 0.1), 4px 0px 10px rgba(0, 0, 0, 0.1), -4px 0px 10px rgba(0, 0, 0, 0.1), 0px -4px 10px rgba(0, 0, 0, 0.1)");

		TextField searchField = new TextField();
		searchField.setWidth("64%");
		searchField.setPlaceholder("Search");
		searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		searchField.setValueChangeMode(ValueChangeMode.EAGER);

		Button logoutBtn = new Button("Log out", e -> {

			ConfirmDialog confirmLogout = new ConfirmDialog();

			confirmLogout.setText("Confirm Logout...");
			confirmLogout.setConfirmText("confirm");
			confirmLogout.setCancelable(true);

			confirmLogout.addConfirmListener(evt -> {
				confirmLogout.close();

				UI.getCurrent().getSession().close();
				UI.getCurrent().navigate(StudentLoginView.class);
			});

			confirmLogout.open();

		});

		logoutBtn.getStyle()
		.setColor("white")
		.set("background-color", "#822020")
		.set("border-radius", "5px")
		.setHeight("35px")
		.setFontSize("13px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		logoutBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);


//		// Section label
//		Span labelText = new Span("Section:");
//		Span sectionLabelText = new Span(studentSection);
//
//		labelText.getStyle().set("font-size", "25px")
//		.set("margin-left", "6px");
//
//		sectionLabelText.getStyle().set("font-weight", "bold")
//		.set("font-size", "25px");

		//HorizontalLayout sectionLabel = new HorizontalLayout(labelText, sectionLabelText);
		//sectionLabel.setAlignItems(Alignment.CENTER);

		HorizontalLayout topBar = new HorizontalLayout(searchField, logoutBtn);
		topBar.setWidthFull();
		topBar.setAlignItems(Alignment.CENTER);
		topBar.setJustifyContentMode(JustifyContentMode.BETWEEN);
		topBar.getStyle().setPaddingBottom("15px");
		
		subjectGrid = new Grid<>(SubjectEntity.class, false);
		subjectGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		//subjectGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		subjectGrid.setAllRowsVisible(true);
		subjectGrid.setSizeUndefined();
		
		Span field1 = new Span("Subject Code");
		Span field2 = new Span("Description");
		Span field3 = new Span("Professor");
		Span field4 = new Span("Status");

		List.of(field1, field2, field3, field4).forEach(field -> {
			field.getStyle().setFontWeight("bold");
			field.getStyle().setFontSize("17px");
		});
		
		subjectGrid.addComponentColumn(source -> {
			
			Span subCode = new Span(source.getSubjectCode());

			Button notificationBtn = new Button(VaadinIcon.ELLIPSIS_DOTS_H.create());
			notificationBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
			notificationBtn.getStyle().setBackgroundColor("transparent");
			
			notificationBtn.addClickListener(event -> {
				
				this.setEnabled(false);
				notificationBtn.setEnabled(false);
				
				var profnameFormat = source.getSection().getTeacher().getFirstname() + " " + source.getSection().getTeacher().getSurname();
				var subjectDesc = source.getSubjectDescription();
				
				Notification notification = createSubjectCodeInfo(subjectDesc, profnameFormat);
				notification.open();
				
//				Notification notification = new Notification();
//				//notification.setm
//				
//				Paragraph text1 = new Paragraph("Subject: " + source.getSubjectDescription());
//				Paragraph text2 = new Paragraph("Prof: " + source.getSection().getTeacher().getFirstname() + " " + source.getSection().getTeacher().getSurname());
//				Div text = new Div(text1, text2);
//				text.getStyle().setWidth("300px");
//				
//				Button closeBtn = new Button(new Icon("lumo", "cross"));
//				closeBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
//				closeBtn.setAriaLabel("Close");
//				closeBtn.addClickListener(event_ -> {
//					notification.close();
//					notificationBtn.setEnabled(true);
//					this.setEnabled(true);
				});
//				
//				HorizontalLayout notificationLayout = new HorizontalLayout(text, closeBtn);
//				notificationLayout.setAlignSelf(Alignment.CENTER);
//				notification.add(notificationLayout);
//				notification.open();
//				notification.setPosition(Notification.Position.MIDDLE);
//				
//			});
//			
			HorizontalLayout componentColumn = new HorizontalLayout(subCode, notificationBtn);
			componentColumn.setSizeFull();
			componentColumn.setAlignItems(FlexComponent.Alignment.CENTER);
			componentColumn.expand(subCode);
//			
			return componentColumn;
			
		}).setHeader(field1).setAutoWidth(true);
		
		
		
		//subjectGrid.addColumn(SubjectEntity::getSubjectCode).setHeader(field1).setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);
	//	subjectGrid.addColumn(SubjectEntity::getSubjectDescription).setHeader(field2).setAutoWidth(true);
		//subjectGrid.addColumn(s -> s.getSection().getTeacher().getFirstname() + " " + s.getSection().getTeacher().getSurname()).setHeader(field3).setAutoWidth(true);
		subjectGrid.addColumn(createStatusComponentRenderer()).setHeader(field4).setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);
		

		subjectGrid.addSelectionListener(selection -> {
			
			verifyIfSectionExists();
			
			Optional<SubjectEntity> selectedSub = selection.getFirstSelectedItem();

			selectedSub.ifPresent(subject -> {

				System.out.println("Selected subject: " + subject.getSubjectDescription());

			//	try {
					//Thread.sleep(2000);

					if (subject.getStatus().equals("Open"))  {
						
						verifyIfSectionExists();
						
						UI.getCurrent().getSession().setAttribute("idOfSelectedSubject", subject.getId());
						
						showOpenAttendifyDialog(subject);
						
					} else if (subject.getStatus().equals("Closed"))  {
						new DialogSubjectClose();
					}

//				} catch (InterruptedException e1) {
//
//					e1.printStackTrace();
//				}


			});
		});


		subjectGrid.setWidthFull();
		subjectGrid.setHeight("100%");
		subjectGrid.setEmptyStateText("You'll see your subjects for " + studentSection + " listed here.");
		//subjectGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		subjectGrid.setPartNameGenerator(subject -> !subject.getSubjectCode().isEmpty() ? "high-rating" : "low-rating");

		// Add custom grid styling
		subjectGrid.getElement().executeJs(
				"const style = document.createElement('style');" +
						"style.innerHTML = `" +
						"  vaadin-grid::part(high-rating) { background-color: var(--lumo-success-color-10pct); }" +
						"  vaadin-grid::part(low-rating) { background-color: var(--lumo-error-color-10pct); }" +
						"`;" +
						"document.head.appendChild(style);"
				);

		// Load subject data
		loadSubjectData();

		// Search filtering
		searchField.addValueChangeListener(e -> {
			String searchTerm = e.getValue().trim().toUpperCase();
			List<SubjectEntity> filtered = subjects.stream()
					.filter(subj -> subj.getSubjectCode().toUpperCase().contains(searchTerm)
							|| subj.getSubjectDescription().toUpperCase().contains(searchTerm)
							|| subj.getSection().getTeacher().getFirstname().toUpperCase().contains(searchTerm)
							|| subj.getSection().getTeacher().getSurname().toUpperCase().contains(searchTerm)
							|| subj.getStatus().toUpperCase().contains(searchTerm))
					.collect(Collectors.toList());
			subjectGrid.setItems(filtered);
		});


		subjectGrid.getElement().getStyle().set("position", "relative");
		subjectGrid.getElement().executeJs(
				"this.shadowRoot.querySelector('style').textContent += " +
						"'vaadin-grid-cell-content:hover {" +
						"background-color: rgba(0, 123, 255, 0.2) !important;" +
						"transition: background-color 0.3s ease;" +
						"}';"
				);


		VerticalLayout layout = new VerticalLayout(topBar, subjectGrid);
		layout.setPadding(false);
		layout.setSpacing(false);
		layout.setSizeFull();
		
		layout.setSizeUndefined();
		body.add(layout);

	}
	
	 
    // ---------------------------------- grid status field util
    private final SerializableBiConsumer<Span, SubjectEntity> statusComponentUpdater = (
            span, subject) -> {
//        boolean isOpen = "Open".equals(subject.getStatus());
//        String theme = String.format("badge %s",
//                isOpen ? "success" : "error");
//        span.getElement().setAttribute("theme", theme);
        span.setText(subject.getStatus());
        
    	
		if ("Open".equals(subject.getStatus())) {
			
			span.getStyle().setBackgroundColor("#05b888");
			span.getStyle().setPaddingRight("16px");
			span.getStyle().setPadding("5px");
		}
		
		if ("Closed".equals(subject.getStatus())) {
			
			span.getStyle().setBackgroundColor("#ee4654");
			span.getStyle().setPadding("5px");

		}
		
		span.getStyle().setColor("White");
		span.getStyle().setBorderRadius("3px");
		span.getStyle().setFontSize("14px");
		
		span.setText(subject.getStatus());
    };
    
    private ComponentRenderer<Span, SubjectEntity> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, statusComponentUpdater);
    }

    
 // --------------------------------------------
    private void loadSubjectData() {
    	
    	if (subjects != null) subjects.clear();
        
        subjects = new LinkedList<>();
        
        sectionService.getSectionByName(studentAccount.getSectionName()).forEach(section -> {
        	
        	section.getSubjects().forEach(subject -> {
        		subjects.add(subject);
//        		System.out.printf("\n\n subjectCode: %s Section: %s Prof: %s \n\n", subject.getSubjectCode(),
//        				subject.getSection().getSectionName(),
//        				subject.getSection().getTeacher().getEmail());
        	});
        });
        
        subjectGrid.setItems(subjects);
    }
    
    private void showOpenAttendifyDialog(SubjectEntity subj) {
		DialogSubjectOpen dialog = new DialogSubjectOpen(this::updateSubjectStatus, subj, subjectService, studentAccountService, studentAttendifiedService);
		dialog.open();
    }

    private void updateSubjectStatus() {

    	if (subjects != null) subjects.clear();
        

    	sectionService.getSectionByName(studentAccount.getSectionName()).forEach(section -> {

    		section.getSubjects().forEach(subject -> {
    			subjects.add(subject);
    		});
    	});

    	subjectGrid.setItems(subjects);
    }

    private void buildUI() {
        header = new StudentDashboardHeader(studentAccount);
        bodyConfig();
        
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        
        add(header, body);
    }
    
    
    
    
    private void verifyIfSectionExists() {
    	
    	// if not exists anymore
    	if (!sectionService.sectionNameExists(studentSection)) {
    		
    		Dialog dialogForSectionNotExists = new Dialog();
    		dialogForSectionNotExists.setCloseOnOutsideClick(false);
    		dialogForSectionNotExists.setCloseOnEsc(false);
    		
    		Span headerLabel = new Span("Section " + studentSection + " no longer exists");
    		headerLabel.getStyle().setColor("white");
    		headerLabel.getStyle().setPaddingBottom("10px");
    		headerLabel.getStyle().setPaddingLeft("8px");
    		headerLabel.getStyle().setPaddingTop("15px");
    		headerLabel.getStyle().setFontWeight("bold");
    		
    		Span label = new Span("It may have been modified or removed");
    		Span label2 = new Span("by your Professor.");
    		label.getStyle().setColor("#ee4654");
    		label2.getStyle().setColor("#ee4654");
    		
    		VerticalLayout wrapper1 = new VerticalLayout(label, label2);
    		wrapper1.setSpacing(false);
    		wrapper1.setPadding(false);
    		wrapper1.getStyle().setPaddingTop("18px");
    		wrapper1.getStyle().setPaddingBottom("20px");
    		
    		Span label3 = new Span("Provide new section name");
    		TextField newSectionNameField = new TextField();
    		newSectionNameField.setRequired(true);
    		
    		VerticalLayout wrapper2 = new VerticalLayout(label3, newSectionNameField);
    		wrapper2.setSpacing(false);
    		wrapper2.setPadding(false);
    		
    		
    		VerticalLayout content = new VerticalLayout(wrapper1, wrapper2);
    		content.setPadding(true);
    		content.setSpacing(false);
    		
    		Button saveBtn = new Button("Save");
    		saveBtn.getStyle()
    		.set("color", "white")
    		.set("font-size", "14px")
    		.set("background-color", "#4460EF")
    		.set("border-radius", "10px")
    		.set("padding", "10px 20px")
    		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
    		.set("transition", "transform 0.2s ease-in-out");
    		saveBtn.getElement().executeJs(
    				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
    						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
    				);
    		
    		saveBtn.addClickListener(save -> {
    			
    			var newSectionValue = newSectionNameField.getValue().toUpperCase().trim();
    			
    			if (newSectionValue.isEmpty()) {
    				newSectionNameField.setInvalid(true);
    				newSectionNameField.setErrorMessage("required");
    				return;
    			} else if (newSectionValue.contains(" ")) {
    				newSectionNameField.setInvalid(true);
    				newSectionNameField.setErrorMessage("whitespace is not allowed");
    				return;
    			} else {
    				
    				Dialog loadingDialog = new Dialog();
					ProgressBar progressBar = new ProgressBar();
					loadingDialog.setModal(true); // Blocks interaction
					loadingDialog.setCloseOnEsc(false);
					loadingDialog.setCloseOnOutsideClick(false);
					progressBar.setIndeterminate(true); // Animate
					loadingDialog.add(progressBar);
    				
    				if (sectionService.sectionNameExists(newSectionValue)) {
    					
    					getUI().ifPresent(ui -> {
    						ui.access(() -> loadingDialog.open());
    						new Thread(() -> {
        						try {
    								Thread.sleep(3000);
    							} catch (InterruptedException e) {
    								e.printStackTrace();
    							}
        						ui.access(() -> {
        							loadingDialog.close();
        							
        							studentAccountService.updateSectionName(newSectionValue, studentAccount);
        							ui.getPage().reload();
        							
        						});
        					}).start();
    					});
    					
    				} else {
    					
    					getUI().ifPresent(ui -> {
    						ui.access(() -> loadingDialog.open());
    						new Thread(() -> {
        						try {
    								Thread.sleep(3000);
    							} catch (InterruptedException e) {
    								e.printStackTrace();
    							}
        						ui.access(() -> {
        							loadingDialog.close();
        							newSectionNameField.setInvalid(true);
        	        				newSectionNameField.setErrorMessage("section " + newSectionValue + " not found");
        						});
        					}).start();
    					});
    				}
    				
    				
    			}
    			
    			
    		});

    		
    		dialogForSectionNotExists.getHeader().add(headerLabel);
    		dialogForSectionNotExists.add(content);
    		dialogForSectionNotExists.getFooter().add(saveBtn);
    		
    		dialogForSectionNotExists.open();
    		
    	}
    	
    	
    }




}
