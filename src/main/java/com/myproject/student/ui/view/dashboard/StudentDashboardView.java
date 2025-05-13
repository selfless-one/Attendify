package com.myproject.student.ui.view.dashboard;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.myproject.backend.student.entity.StudentAccountEntity;
import com.myproject.backend.student.service.StudentAccountService;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.service.SectionService;
import com.myproject.backend.teacher.service.StudentAttendifiedService;
import com.myproject.backend.teacher.service.SubjectService;
import com.myproject.student.ui.view.dashboard.dialog.DialogSubjectClose;
import com.myproject.student.ui.view.dashboard.dialog.DialogSubjectOpen;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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

	private void bodyConfig() {
		body = new Div();

		body.getStyle()
		.set("border", "2px solid black")
		.set("border-radius", "8px")
		.set("padding", "20px")
		.set("width", "900px")
		.set("height", "600px")
		.set("margin-top", "10px")
		.set("overflow", "auto")
		.set("box-shadow", "0px 4px 10px rgba(0, 0, 0, 0.1), 4px 0px 10px rgba(0, 0, 0, 0.1), -4px 0px 10px rgba(0, 0, 0, 0.1), 0px -4px 10px rgba(0, 0, 0, 0.1)");

		TextField searchField = new TextField();
		searchField.setWidth("35%");
		searchField.setPlaceholder("Search");
		searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		searchField.setValueChangeMode(ValueChangeMode.EAGER);

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

		HorizontalLayout topBar = new HorizontalLayout(searchField);
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
		
		subjectGrid.addColumn(SubjectEntity::getSubjectCode).setHeader(field1).setAutoWidth(true);
		subjectGrid.addColumn(SubjectEntity::getSubjectDescription).setHeader(field2).setAutoWidth(true);
		subjectGrid.addColumn(s -> s.getSection().getTeacher().getFirstname() + " " + s.getSection().getTeacher().getSurname()).setHeader(field3).setAutoWidth(true);
		subjectGrid.addColumn(createStatusComponentRenderer()).setHeader(field4).setAutoWidth(true);
		

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
