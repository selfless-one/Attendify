package com.myproject.teacher.ui.view.dashboard;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.myproject.backend.teacher.service.TeacherAccountService;
import com.myproject.teacher.ui.view.TeacherLoginView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Route("professor/dashboard")
public class DashboardView extends VerticalLayout implements HasUrlParameter<String>  {

	private static final long serialVersionUID = 1L;

	private final TeacherAccountService teacherAccService;

	private TeacherAccountEntity teacherAccount;
	private final String professorUsernameSessioned = (String) UI.getCurrent().getSession().getAttribute("professorUsername");

	private HorizontalLayout header;
	private Div bodyWrapper;
	private Div body;

	
	//private Grid<SectionDTO> sectionsInGrid;
	private Set<SectionDTO> teacherSectionsHandled = new HashSet<>();

	@Override
	public void setParameter(BeforeEvent event, String username) {
	}
	
	public DashboardView(TeacherAccountService teacherAccService) {
		
		this.teacherAccService = teacherAccService;
		
		if (professorUsernameSessioned == null) {
			UI.getCurrent().navigate(TeacherLoginView.class);
			return;
		}
		
		this.teacherAccount = teacherAccService.getAccountByUsername(professorUsernameSessioned);
	
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.START);

		header = new DashboardHeader(teacherAccount, teacherAccService);
		bodyConfig();
		bodyWrapperConfig();

		add(header, bodyWrapper);
	}
     
    private TextField searchField;
    
    private void configureSearchField() {
    	
    	searchField = new TextField();
    	
    	searchField.setWidth("50%");
		searchField.setPlaceholder("Search");
		searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		searchField.setValueChangeMode(ValueChangeMode.EAGER);
    	
		searchField.addValueChangeListener(e -> {
			String searchTerm = searchField.getValue().trim().toUpperCase();
			List<SectionDTO> filteredSections = teacherSectionsHandled.stream()
					.filter(section -> section.getSectionName().toUpperCase().contains(searchTerm)
							|| section.getCourse().toUpperCase().contains(searchTerm))
					.collect(Collectors.toList());
			sectionsToDisplayInGrid.setItems(filteredSections);
		});
    }
    
    Grid<SectionDTO> sectionsToDisplayInGrid = new Grid<>(SectionDTO.class, false);
    Editor<SectionDTO> editor = sectionsToDisplayInGrid.getEditor();
    
    private void configureSectionsToDisplayInGrid() {
    	
    	sectionsToDisplayInGrid.setWidthFull();
		sectionsToDisplayInGrid.setHeight("100%");
		sectionsToDisplayInGrid.setEmptyStateText("Your list of sections will appear here.");
		sectionsToDisplayInGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		sectionsToDisplayInGrid.setSizeUndefined();
		sectionsToDisplayInGrid.setAllRowsVisible(true);

		sectionsToDisplayInGrid.setPartNameGenerator(section -> {
			return section.getSectionName().startsWith("OL") || section.getSectionName().startsWith("LF") ? "high-rating" : "low-rating";
		});

		// Add both high-rating and low-rating styles INLINE
		sectionsToDisplayInGrid.getElement().executeJs("const style = document.createElement('style');" + "style.innerHTML = `"
				+ "  vaadin-grid::part(high-rating) {" + "    background-color: var(--lumo-success-color-10pct);"
				+ "  }" + "  vaadin-grid::part(low-rating) {" + "    background-color: var(--lumo-error-color-10pct);"
				+ "  }" + "`;" + "document.head.appendChild(style);");

		// Inside bodyConfig() method
		sectionsToDisplayInGrid.getElement().getStyle().set("position", "relative");

		sectionsToDisplayInGrid.getElement().executeJs("this.shadowRoot.querySelector('style').textContent += "
				+ "'vaadin-grid-cell-content:hover {' + " + "'background-color: rgba(0, 123, 255, 0.2) !important; ' + "
				+ "'transition: background-color 0.3s ease;' + " + "'}';");

    	Span header1 = new Span("Section");
		Span header2 = new Span("Course");
		Span header3 = new Span("Date Added");
		Span header4 = new Span("Update Info");
		
		List.of(header1, header2, header3, header4).forEach(field -> {
			field.getStyle().setFontWeight("bold");
			field.getStyle().setFontSize("15px");
		});

    	Grid.Column<SectionDTO> sectionColumn = sectionsToDisplayInGrid.addColumn(SectionDTO::getSectionName)
    			.setHeader(header1)
    			.setAutoWidth(true)
    			.setTextAlign(ColumnTextAlign.CENTER);
        
        Grid.Column<SectionDTO> courseColumn = sectionsToDisplayInGrid.addColumn(SectionDTO::getCourse)
                .setHeader(header2)
                .setAutoWidth(true)
                .setTextAlign(ColumnTextAlign.CENTER);
        
        sectionsToDisplayInGrid.addColumn(SectionDTO::getDateAddedFormatted)
                .setHeader(header3)
                .setAutoWidth(true)
                .setTextAlign(ColumnTextAlign.CENTER);

        String[] sectionNameClicked = new String[1];
        String[] courseNameClicked = new String[1];
        
        Grid.Column<SectionDTO> editColumn = sectionsToDisplayInGrid.addComponentColumn(section -> {

        	Button editButton = new Button(LumoIcon.EDIT.create());
        	editButton.addThemeVariants(ButtonVariant.LUMO_WARNING);
        	
        	editButton.addClickListener(e -> {
        		if (editor.isOpen())
        			editor.cancel();
        			sectionNameClicked[0] = section.getSectionName();
        			courseNameClicked[0] = section.getCourse();
        		    sectionsToDisplayInGrid.getEditor().editItem(section);
        	});
        	
        	return editButton;

        }).setWidth("140px").setTextAlign(ColumnTextAlign.CENTER).setHeader(header4);
        
        Binder<SectionDTO> binder = new Binder<>(SectionDTO.class);
        editor.setBinder(binder);
        editor.setBuffered(true);
        
        TextField sectionField = new TextField();
        sectionField.setWidthFull();
        binder.forField(sectionField).asRequired("required").bind(SectionDTO::getSectionName, SectionDTO::setSectionName);
        
        TextField courseField = new TextField();
        courseField.setWidthFull();
        binder.forField(courseField).asRequired("required").bind(SectionDTO::getCourse, SectionDTO::setCourse);
        
        Button saveButton = new Button(VaadinIcon.CHECK.create());
        Button deleteButton = new Button(VaadinIcon.TRASH.create());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        
        saveButton.addClickListener(save -> {
        	
        	var sectionFieldValue = sectionField.getValue().trim();
        	var courseFieldValue = courseField.getValue().trim();
        	
        	if (!sectionFieldValue.isEmpty() && !courseFieldValue.isEmpty()) {
        		
        		var sectionContainWhiteSpace = sectionFieldValue.contains(" ");
        		var courseContainWhiteSpace = courseFieldValue.contains(" ");
        		
        		if (!sectionContainWhiteSpace && !courseContainWhiteSpace) {
        			
        			var sameSectionName = sectionFieldValue.equals(sectionNameClicked[0]);
        			var sameCourseName = courseFieldValue.equals(courseNameClicked[0]);
        			
        			if (sameSectionName && sameCourseName) {
        				editor.cancel(); // close editor
        				return;       				
        			} else {
        				
        				ConfirmDialog dialogConfirm = new ConfirmDialog();
        				dialogConfirm.setHeader("Confirm changes");
        				dialogConfirm.setConfirmText("Save");
        				dialogConfirm.setCancelable(true);
        				
        				if (!sameSectionName && !sameCourseName) {
        					
        					Span label1 = new Span();
        					
        					label1.add(new Text("Section: " + sectionNameClicked[0] + " "));
        					label1.add(VaadinIcon.ARROWS_LONG_RIGHT.create());
        					Span newSectionName = new Span(" " + sectionFieldValue);
        					newSectionName.getStyle().setColor("#71A7FF");
        					newSectionName.getStyle().setFontWeight("bold");
        					label1.add(newSectionName);
        					
        					Span label2 = new Span();
        					label2.add(new Text("Course: " + courseNameClicked[0] + " "));
        					label2.add(VaadinIcon.ARROWS_LONG_RIGHT.create());
        					Span newCourseName = new Span(" " + courseFieldValue);
        					newCourseName.getStyle().setColor("#71A7FF");
        					newCourseName.getStyle().setFontWeight("bold");
        					label2.add(newCourseName);
        					
        					VerticalLayout v = new VerticalLayout(label1, label2);
        					v.setSpacing(false);
        					dialogConfirm.setText(v);
        					
        					dialogConfirm.open();

        				} else if (!sameSectionName && sameCourseName) {
        					
        					Span label1 = new Span();

        					label1.add(new Text("Section: " + sectionNameClicked[0] + " "));
        					label1.add(VaadinIcon.ARROWS_LONG_RIGHT.create());
        					Span newSectionName = new Span(" " + sectionFieldValue);
        					newSectionName.getStyle().setColor("#71A7FF");
        					newSectionName.getStyle().setFontWeight("bold");
        					label1.add(newSectionName);
        					
        					Span label2 = new Span(new Text("Course: " + courseNameClicked[0] + " "));
        					
        					VerticalLayout v = new VerticalLayout(label1, label2);
        					v.setSpacing(false);
        					dialogConfirm.setText(v);
        					
        					dialogConfirm.open();

        				} else if (!sameCourseName && sameSectionName) {

        					Span label1 = new Span((new Text("Section: " + sectionNameClicked[0] + " ")));

        					Span label2 = new Span();
        					label2.add(new Text("Course: " + courseNameClicked[0] + " "));
        					label2.add(VaadinIcon.ARROWS_LONG_RIGHT.create());
        					Span newCourseName = new Span(" " + courseFieldValue);
        					newCourseName.getStyle().setColor("#71A7FF");
        					newCourseName.getStyle().setFontWeight("bold");
        					label2.add(newCourseName);
        					
        					VerticalLayout v = new VerticalLayout(label1, label2);
        					v.setSpacing(false);
        					dialogConfirm.setText(v);
        					
        					dialogConfirm.open();
        				}
        				
        				dialogConfirm.addConfirmListener(confirm -> {
        					
        					teacherAccService.updateSection(sectionFieldValue, 
            						courseFieldValue, 
            						sectionNameClicked[0], 
            						teacherAccount);
            				
            				syncTeacherSectionsHandled();
                			sectionsToDisplayInGrid.setItems(teacherSectionsHandled);
                			UI.getCurrent().getPage().reload();
        					
        				});
        			}
        			
        			
        			
        		} else {
        			ConfirmDialog dialog = new ConfirmDialog();
            		dialog.setText("Please remove any spaces from the input.");
            		dialog.setConfirmText("Ok");
            		dialog.open();
        		}
        		
        	} else {
        		
        		return;
        	}
        	
        });
        
        deleteButton.addClickListener(delete -> {
        	
        	ConfirmDialog dialog = new ConfirmDialog();
    		dialog.setText("Confirm deletion of section: " + sectionNameClicked[0] + "?");
    		dialog.setConfirmText("Confirm");
    		dialog.setCancelText("Cancel");
    		dialog.setCancelable(true);
    		
    		dialog.addConfirmListener(confirm -> {
    			
    			teacherAccService.deleteSectionByName(sectionNameClicked[0], teacherAccount);
    			syncTeacherSectionsHandled();
    			sectionsToDisplayInGrid.setItems(teacherSectionsHandled);
    			UI.getCurrent().getPage().reload();
    			editor.cancel(); // close editor
    		});
    		
    		dialog.open();
        	
        	
        	
        });
        
        
        saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        cancelButton.getStyle().setMarginRight("5px");
        cancelButton.getStyle().setMarginLeft("5px");
        
        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        actions.setSpacing(false);
        
        courseColumn.setEditorComponent(courseField);
        sectionColumn.setEditorComponent(sectionField);
        editColumn.setEditorComponent(actions);
        

    }

    private void getTeacherSectionsHandled() {
    	
    	teacherAccount.getSections().forEach(sec -> {
    		
    		LocalDateTime sectionTimeAdded = sec.getDateCreated();
    		String formattedDateTime = sectionTimeAdded.format(DateTimeFormatter.ofPattern("MM-dd-yy HH:mm:a"));
    		
    		teacherSectionsHandled.add(
    				new SectionDTO(sec.getId(), sec.getSectionName(), sec.getCourse(), formattedDateTime));
    	});
    }

    private void syncTeacherSectionsHandled() {

    	teacherSectionsHandled.clear();

    	teacherAccount = teacherAccService.getAccountByUsername(professorUsernameSessioned);

    	teacherAccount.getSections().forEach(sec -> {

    		LocalDateTime sectionTimeAdded = sec.getDateCreated();
    		String formattedDateTime = sectionTimeAdded.format(DateTimeFormatter.ofPattern("MM-dd-yy hh:mm:a"));

    		teacherSectionsHandled.add(
    				new SectionDTO(sec.getId(), sec.getSectionName(), sec.getCourse(), formattedDateTime));
    	});
    }

    
    private void displaySectionsInGrid() {
    	getTeacherSectionsHandled();
    	sectionsToDisplayInGrid.setItems(teacherSectionsHandled);
    }
    
	private void bodyConfig() {

		configureSearchField();
		configureSectionsToDisplayInGrid();
		displaySectionsInGrid();

		sectionsToDisplayInGrid.addSelectionListener(selection -> {
			Optional<SectionDTO> optionalSection = selection.getFirstSelectedItem();

			if (optionalSection.isPresent()) {
				SectionDTO selectedSection = optionalSection.get();
				String sectionName = selectedSection.getSectionName();

				System.out.printf("Selected section: %s%n", sectionName);

				try {
					Thread.sleep(2000);

					UI.getCurrent().getSession().setAttribute("idOfSelectedSection", selectedSection.getId());
					
					// path to subject list of selected section
					String path = "professor/username/" + professorUsernameSessioned + "/subjectlist/section/" + selectedSection.getSectionName();
					
					UI.getCurrent().navigate(path);
					
					
				} catch (InterruptedException e1) {

					e1.printStackTrace();
				}
			}
		});
		
		body = new Div();
		body.getStyle().set("border", "2px solid black").set("border-radius", "8px").set("padding", "20px")
				.set("width", "600px").set("height", "600px").set("margin-top", "10px").set("overflow", "auto")
				.set("box-shadow", "0px 4px 10px rgba(0, 0, 0, 0.1), " + "4px 0px 10px rgba(0, 0, 0, 0.1), "
						+ "-4px 0px 10px rgba(0, 0, 0, 0.1), " + "0px -4px 10px rgba(0, 0, 0, 0.1)");

		VerticalLayout layout = new VerticalLayout(searchField, sectionsToDisplayInGrid);
		layout.setPadding(false);
		//layout.setSpacing(false);
		layout.setSizeFull();
		
		body.add(layout);
	}

	private void bodyWrapperConfig() {
		
		Button addSectionBtn = new Button("Add section", e -> showAddSectionDialog());

		addSectionBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		addSectionBtn.getStyle()
				.set("position", "absolute")
				.set("bottom", "1px")
				.set("right", "20px")
				.set("z-index", "1")
				.set("border-radius", "10px")
				.set("padding", "10px 20px")
				.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
				.set("transition", "transform 0.2s ease-in-out");
		addSectionBtn.getElement().getThemeList().add("primary");
		addSectionBtn.getElement()
				.executeJs("this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });"
						+ "this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });");

		bodyWrapper = new Div();
		bodyWrapper.getStyle().set("position", "relative").set("width", "600px").set("height", "600px");
		
		bodyWrapper.add(body, addSectionBtn);
		
	}
	
	private void showAddSectionDialog() {
		SectionDialog dialog = new SectionDialog((sectionName, courseName) -> addSection(sectionName, courseName));
		dialog.open();
	}

	private void addSection(String sectionName, String courseName) {
		LocalDateTime now = LocalDateTime.now();
		
	//	sectionName = sectionName.toUpperCase();
	//	courseName = courseName.toUpperCase();
		
		teacherAccount = teacherAccService.getAccountByUsername(professorUsernameSessioned);
		
		var sectionNameAlreadyExists = teacherAccount.getSections().stream()
				.anyMatch(teacher -> teacher.getSectionName().equals(sectionName));
		
		if (sectionNameAlreadyExists) {
			
			ConfirmDialog alreadyExistsDialog = new ConfirmDialog();
			alreadyExistsDialog.setText("Section name already exists...");
			alreadyExistsDialog.setConfirmText("Ok");
			alreadyExistsDialog.open();
			
			return;
		}
		
		SectionEntity newSection = SectionEntity.builder()
        		.sectionName(sectionName)
        		.course(courseName)
        		.dateCreated(now)
        		.teacher(teacherAccount)
        		.build();
        

		teacherAccount.getSections().add(newSection);
        teacherAccService.saveChanges(teacherAccount);
        syncTeacherSectionsHandled();
		//teacherSectionsHandled.add(newSection);
		sectionsToDisplayInGrid.setItems(teacherSectionsHandled); // update grid
	}
}
