package com.myproject.teacher.ui.view.dashboard;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.myproject.backend.teacher.service.TeacherAccountService;
import com.myproject.teacher.ui.view.dashboard.subjectDataPage.SubjectView;
import com.myproject.teacher.ui.view.dashboard.subjectDataPage.ValidationMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("teacher/dashboard")
public class DashboardView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final TeacherAccountService teacherAccService;

	private final TeacherAccountEntity teacherAccount;

	private final String sessionedEmail;

	private HorizontalLayout header;
	private Div bodyWrapper;
	private Div body;

	//private Grid<SectionDTO> sectionsInGrid;
	private List<SectionDTO> teacherSectionsHandled = new LinkedList<>();

	public DashboardView(TeacherAccountService teacherAccService) {

		this.teacherAccService = teacherAccService;
		this.sessionedEmail = (String) UI.getCurrent().getSession().getAttribute("teacher_email");
		this.teacherAccount = teacherAccService.getAccountByEmail(sessionedEmail);

		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.START);

		header = new DashboardHeader(teacherAccount);

		bodyConfig();

		add(header, bodyWrapper);
	}

	
	Grid<SectionDTO> sectionsInGrid = new Grid<>(SectionDTO.class, false);
    Editor<SectionDTO> editor = sectionsInGrid.getEditor();
     
	private void bodyConfig() {
		body = new Div();
		body.getStyle().set("border", "2px solid black").set("border-radius", "8px").set("padding", "20px")
				.set("width", "600px").set("height", "600px").set("margin-top", "10px").set("overflow", "auto")
				.set("box-shadow", "0px 4px 10px rgba(0, 0, 0, 0.1), " + "4px 0px 10px rgba(0, 0, 0, 0.1), "
						+ "-4px 0px 10px rgba(0, 0, 0, 0.1), " + "0px -4px 10px rgba(0, 0, 0, 0.1)");

		TextField searchField = new TextField();
		searchField.setWidth("50%");
		searchField.setPlaceholder("Search");
		searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		searchField.setValueChangeMode(ValueChangeMode.EAGER);

		// --------------------------------
		ValidationMessage sectionValidationMessage = new ValidationMessage();
        ValidationMessage courseValidationMessage = new ValidationMessage();
		
        
        Grid.Column<SectionDTO> sectionColumn = sectionsInGrid.addColumn(SectionDTO::getSectionName)
        		.setHeader("Section")
                .setWidth("100px");
        
        Grid.Column<SectionDTO> courseColumn = sectionsInGrid.addColumn(SectionDTO::getCourse)
                .setHeader("Course")
                .setWidth("100px");
        
        sectionsInGrid.addColumn(SectionDTO::getDateAdded)
                .setHeader("Date added")
                .setWidth("100px");
        
        Grid.Column<SectionDTO> editColumn = sectionsInGrid.addComponentColumn(section -> {
        	
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                sectionsInGrid.getEditor().editItem(section);
            });
            return editButton;
            
        }).setWidth("150px").setFlexGrow(0);
        
        
        Binder<SectionDTO> binder = new Binder<>(SectionDTO.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField sectionField = new TextField();
        sectionField.setWidthFull();
        binder.forField(sectionField)
                .asRequired("Section name must not be empty")
                .withStatusLabel(sectionValidationMessage)
                .bind(SectionDTO::getSectionName, SectionDTO::setSectionName);
        sectionColumn.setEditorComponent(sectionField);
        
        TextField courseField = new TextField();
        courseField.setWidthFull();
        binder.forField(courseField).asRequired("Course name must not be empty")
                .withStatusLabel(courseValidationMessage)
                .bind(SectionDTO::getCourse, SectionDTO::setCourse);
        courseColumn.setEditorComponent(courseField);
        
        Button saveButton = new Button("Save", e -> editor.save());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton,
                cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);
        
        editor.addCancelListener(e -> {
            sectionValidationMessage.setText("");
            courseValidationMessage.setText("");
        });

        
		// ---------------------------------------
		
		//sectionsInGrid = new Grid<>(SectionDTO.class, false);
		//sectionsInGrid.addColumn(SectionDTO::getSectionName).setHeader("Section");
		//sectionsInGrid.addColumn(SectionDTO::getCourse).setHeader("Course");
		//sectionsInGrid.addColumn(SectionDTO::getDateAdded).setHeader("Date Added");
		sectionsInGrid.setWidthFull();
		sectionsInGrid.setHeight("100%");
		sectionsInGrid.setEmptyStateText("Your list of sections will appear here.");

		sectionsInGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

		sectionsInGrid.setPartNameGenerator(section -> {
			return section.getSectionName().startsWith("LF") ? "high-rating" : "low-rating";
		});

		// Add both high-rating and low-rating styles INLINE
		sectionsInGrid.getElement().executeJs("const style = document.createElement('style');" + "style.innerHTML = `"
				+ "  vaadin-grid::part(high-rating) {" + "    background-color: var(--lumo-success-color-10pct);"
				+ "  }" + "  vaadin-grid::part(low-rating) {" + "    background-color: var(--lumo-error-color-10pct);"
				+ "  }" + "`;" + "document.head.appendChild(style);");

		// Inside bodyConfig() method
		sectionsInGrid.getElement().getStyle().set("position", "relative");

		sectionsInGrid.getElement().executeJs("this.shadowRoot.querySelector('style').textContent += "
				+ "'vaadin-grid-cell-content:hover {' + " + "'background-color: rgba(0, 123, 255, 0.2) !important; ' + "
				+ "'transition: background-color 0.3s ease;' + " + "'}';");

		teacherAccount.getSections().forEach(sec -> {
			teacherSectionsHandled.add(new SectionDTO(sec.getId(), sec.getSectionName(), sec.getCourse(), sec.getDateCreated()));
		});
		
		sectionsInGrid.setItems(teacherSectionsHandled);

		searchField.addValueChangeListener(e -> {
			String searchTerm = searchField.getValue().trim().toUpperCase();
			List<SectionDTO> filteredSections = teacherSectionsHandled.stream()
					.filter(section -> section.getSectionName().toUpperCase().contains(searchTerm)
							|| section.getCourse().toUpperCase().contains(searchTerm))
					.collect(Collectors.toList());
			sectionsInGrid.setItems(filteredSections);
		});

		sectionsInGrid.addSelectionListener(selection -> {
			Optional<SectionDTO> optionalSection = selection.getFirstSelectedItem();

			if (optionalSection.isPresent()) {
				SectionDTO selectedSection = optionalSection.get();
				String sectionName = selectedSection.getSectionName();

				System.out.printf("Selected section: %s%n", sectionName);

				try {
					Thread.sleep(2000);

					UI.getCurrent().getSession().setAttribute("idOfSelectedSection", selectedSection.getId());
					
					UI.getCurrent().navigate(SubjectView.class, sectionName);
				} catch (InterruptedException e1) {

					e1.printStackTrace();
				}
			}
		});

		VerticalLayout layout = new VerticalLayout(searchField, sectionsInGrid);
		layout.setPadding(false);
		layout.setSpacing(false);
		layout.setSizeFull();

		body.add(layout);

		Button addSectionBtn = new Button("Add section", e -> showAddSectionDialog());

		addSectionBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		addSectionBtn.getStyle().set("position", "absolute").set("bottom", "1px").set("right", "20px")
				.set("z-index", "1").set("border-radius", "10px").set("padding", "10px 20px")
				.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)").set("transition", "transform 0.2s ease-in-out");
		addSectionBtn.getElement().getThemeList().add("primary");
		addSectionBtn.getElement()
				.executeJs("this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });"
						+ "this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });");

		bodyWrapper = new Div();
		bodyWrapper.getStyle().set("position", "relative").set("width", "600px").set("height", "600px");
		bodyWrapper.add(body, addSectionBtn);
	}

	private void showAddSectionDialog() {
		SectionDialog dialog = new SectionDialog((sectionName, courseName) -> addSection(sectionName, courseName),
				teacherAccService, teacherAccount);
		dialog.open();
	}

	private void addSection(String sectionName, String courseName) {
		LocalDateTime now = LocalDateTime.now();
		String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		SectionDTO newSection = new SectionDTO(sectionName, courseName, formattedDateTime);
		teacherSectionsHandled.add(newSection);
		sectionsInGrid.setItems(teacherSectionsHandled); // update grid
	}
}
