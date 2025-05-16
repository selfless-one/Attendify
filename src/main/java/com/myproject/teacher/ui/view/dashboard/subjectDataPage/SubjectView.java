package com.myproject.teacher.ui.view.dashboard.subjectDataPage;

import com.vaadin.flow.router.Location;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.myproject.backend.teacher.service.SectionService;
import com.myproject.backend.teacher.service.SubjectService;
import com.myproject.backend.teacher.service.TeacherAccountService;
import com.myproject.teacher.ui.view.TeacherLoginView;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("professor/username/:username/subjectlist/section/:section")
public class SubjectView extends VerticalLayout implements BeforeEnterObserver, HasDynamicTitle {

	private static final long serialVersionUID = 1L;

	private final TeacherAccountService teacherAccService;
	private final SectionService sectionService;
	private final SubjectService subjectService;

	private TeacherAccountEntity teacherAccount;

	private String professorUsernameSessioned = (String) UI.getCurrent().getSession().getAttribute("professorUsername");
	private Integer idOfSelectedSection = (Integer) UI.getCurrent().getSession().getAttribute("idOfSelectedSection");
	private SectionEntity selectedSection;

	private String sectionNamePath;


	//------------------------------
	private HorizontalLayout header;

	private Div body;
	private HorizontalLayout topbarInBody;
	private VerticalLayout bodyContentLayout;

	private Div bodyWrapper;
	private Button addSubjectnBtn;

	private Grid<Subject> subjectsToDisplayInGrid;
	private List<Subject> subjects = new LinkedList<>();

	public SubjectView(TeacherAccountService teacherAccService, SectionService sectionService, SubjectService subjectService) {
		this.teacherAccService = teacherAccService;
		this.sectionService = sectionService;
		this.subjectService = subjectService;
		
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		//setJustifyContentMode(JustifyContentMode.s);
	}
	
	@Override
	public String getPageTitle() {
		return sectionNamePath + " subjects";
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		
		if (professorUsernameSessioned == null || idOfSelectedSection == 0) {
			event.forwardTo(TeacherLoginView.class);
			return;
		}

		Location location = event.getLocation();

		List<String> segments = location.getSegments();

		// Example: /professor/username/rowel/subjectlist/section/BSIT-2A
		// segments: ["professor", "username", "rowel", "subjectlist", "section", "BSIT-2A"]

		// String usernamePath = segments.size() > 2 ? segments.get(2) : "unknown";
		String sectionNamePath = segments.size() > 5 ? segments.get(5) : "unknown";

		this.sectionNamePath = sectionNamePath;

		this.teacherAccount = teacherAccService.getAccountByUsername(professorUsernameSessioned);
		this.selectedSection = sectionService.getAccountById(idOfSelectedSection).orElseThrow();

		buildUI();	
	}

	private void setupHeader() {
		header = new SubjectHeader(teacherAccount, teacherAccService);
	}

	private void setupTopbarInBody() {

		Span sectionLabel = new Span("Section:");
		Span sectionLabelValue = new Span(sectionNamePath);

		sectionLabel.getStyle().set("font-size", "25px").set("margin-left", "6px");
		sectionLabelValue.getStyle().set("font-weight", "bold").set("font-size", "25px").setColor("#7289da");

		HorizontalLayout sectionLayout = new HorizontalLayout(sectionLabel, sectionLabelValue);
		sectionLayout.setAlignItems(Alignment.CENTER);

		TextField searchField = new TextField();

		searchField.setWidth("33%");
		searchField.setPlaceholder("Search");
		searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
		searchField.setValueChangeMode(ValueChangeMode.EAGER);

		// Search filtering
		searchField.addValueChangeListener(e -> {
			String searchTerm = e.getValue().trim().toUpperCase();
			List<Subject> filtered = subjects.stream()
					.filter(subj -> subj.getSubjectCode().toUpperCase().contains(searchTerm)
							|| subj.getSubjectDescription().toUpperCase().contains(searchTerm))
					.collect(Collectors.toList());
			subjectsToDisplayInGrid.setItems(filtered);
		});


		// section layout and search field
		topbarInBody = new HorizontalLayout(sectionLayout, searchField);
		topbarInBody.setWidthFull();
		topbarInBody.setAlignItems(Alignment.CENTER);
		topbarInBody.setJustifyContentMode(JustifyContentMode.BETWEEN);

	}

	private void setupSubjectsToDisplayInGrid() {

		subjectsToDisplayInGrid = new Grid<>(Subject.class, false);

		subjectsToDisplayInGrid.setWidthFull();
		subjectsToDisplayInGrid.setHeight("100%");
		subjectsToDisplayInGrid.setEmptyStateText("Your list of subject for section " + sectionNamePath + " will appear here.");
		subjectsToDisplayInGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
		subjectsToDisplayInGrid.setAllRowsVisible(true);
		subjectsToDisplayInGrid.setSizeUndefined();
		subjectsToDisplayInGrid.setPartNameGenerator(subject -> !subject.getSubjectCode().isEmpty() ? "high-rating" : "low-rating");

		// Add custom grid styling
		subjectsToDisplayInGrid.getElement().executeJs(
				"const style = document.createElement('style');" +
						"style.innerHTML = `" +
						"  vaadin-grid::part(high-rating) { background-color: var(--lumo-success-color-10pct); }" +
						"  vaadin-grid::part(low-rating) { background-color: var(--lumo-error-color-10pct); }" +
						"`;" +
						"document.head.appendChild(style);"
				);

		subjectsToDisplayInGrid.getElement().getStyle().set("position", "relative");
		subjectsToDisplayInGrid.getElement().executeJs(
				"this.shadowRoot.querySelector('style').textContent += " +
						"'vaadin-grid-cell-content:hover {" +
						"background-color: rgba(0, 123, 255, 0.2) !important;" +
						"transition: background-color 0.3s ease;" +
						"}';"
				);

		Span field1 = new Span("Subject Code");
		Span field2 = new Span("Description");
		Span field3 = new Span("Date Added");
		Span field4 = new Span("Status");
		

		List.of(field1, field2, field3, field4).forEach(field -> {
			field.getStyle().setFontWeight("bold");
			field.getStyle().setFontSize("15px");
		});
		
		
		DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MM-dd-yy hh:mm:a");
			
		subjectsToDisplayInGrid.addColumn(Subject::getSubjectCode).setHeader(field1).setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);
		subjectsToDisplayInGrid.addColumn(Subject::getSubjectDescription).setHeader(field2).setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);
		subjectsToDisplayInGrid.addColumn(source -> source.getDateCreated().formatted(dateTimeFormat)).setHeader(field3).setAutoWidth(true)
		.setTextAlign(ColumnTextAlign.CENTER);
		subjectsToDisplayInGrid.addColumn(createStatusComponentRenderer()).setHeader(field4).setTextAlign(ColumnTextAlign.CENTER);


		subjectsToDisplayInGrid.addSelectionListener(selection -> {
			Optional<Subject> selectedSub = selection.getFirstSelectedItem();

			selectedSub.ifPresent(subject -> {

				System.out.println("Selected subject: " + subject.getSubjectDescription());

//				try {
				//	Thread.sleep(2000);

					showOpenAttendifyDialog(subject.getId());

//				} catch (InterruptedException e1) {
//
//					e1.printStackTrace();
//				}


			});
		});

		getSubjectsData();


	}

	private void setupBodyContentLayout() {

		bodyContentLayout = new VerticalLayout(topbarInBody, subjectsToDisplayInGrid);
		bodyContentLayout.setPadding(false);
		bodyContentLayout.setSizeFull();
	}

	private void setupBody() {

		setupTopbarInBody();
		setupSubjectsToDisplayInGrid();

		setupBodyContentLayout();

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

		body.add(bodyContentLayout);
	}

	private void setupBodyWrapper() {

		addSubjectnBtn = new Button("Add Subject", evt -> showAddSubjectDialog());
		addSubjectnBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		addSubjectnBtn.getStyle()
		.set("position", "absolute")
		.set("bottom", "1px")
		.set("right", "20px")
		.set("z-index", "1")
		.set("border-radius", "10px")
		.set("padding", "10px 20px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		addSubjectnBtn.getElement().getThemeList().add("primary");
		addSubjectnBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);	

		bodyWrapper = new Div();
		bodyWrapper.getStyle()
		.set("position", "relative")
		.set("width", "900px")
		.set("height", "600px");

		bodyWrapper.add(body, addSubjectnBtn);
	}

	private void buildUI() {

		setupHeader();

		setupBody();
		setupBodyWrapper();

		add(header, bodyWrapper);
	}

	// ---------------------------------- grid status field util
	private final SerializableBiConsumer<Span, Subject> statusComponentUpdater = (
			span, subject) -> {
			//	boolean isOpen = "Open".equals(subject.getStatus());
//				String theme = String.format("badge %s",
//						isOpen ? "success" : "error");
//				span.getElement().setAttribute("theme", theme);
//				
				if ("Open".equals(subject.getStatus())) {
					
					span.getStyle().setBackgroundColor("#05b888");
					span.getStyle().setPaddingRight("13px");
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

	private ComponentRenderer<Span, Subject> createStatusComponentRenderer() {
		return new ComponentRenderer<>(Span::new, statusComponentUpdater);
	}
	// --------------------------------------------


	private void getSubjectsData() {

		subjectService.getAllSubjectBySectionID(idOfSelectedSection).forEach(subs -> {

			LocalDateTime sectionTimeAdded = subs.getDateCreated();
			String formattedDateTime = sectionTimeAdded.format(DateTimeFormatter.ofPattern("MM-dd-yy HH:mm:a"));

			subjects.add(new Subject(subs.getId(), subs.getSubjectCode(), subs.getSubjectDescription(), formattedDateTime, subs.getStatus()));
		});

		subjectsToDisplayInGrid.setItems(subjects);
	}

	private void showOpenAttendifyDialog(Integer selectedSubjectId) {
		OpenTheAttendanceDialog dialog = new OpenTheAttendanceDialog(this::syncSubjectsData, selectedSubjectId, subjectService);
		dialog.open();
	}

//	private void updateSubjectStatus() {
//
//		subjects.clear();
//
//		subjectService.getAllSubjectBySectionID(idOfSelectedSection).forEach(subs -> {
//			subjects.add(new Subject(subs.getId(), subs.getSubjectCode(), subs.getSubjectDescription(), null, subs.getStatus()));
//		});
//
//		subjectsToDisplayInGrid.setItems(subjects);
//	}


	private void showAddSubjectDialog() {
		SubjectDialog dialog = new SubjectDialog((subjectCode, subjectDesc) -> addSubject(subjectCode, subjectDesc));
		dialog.open();
	}

	private void syncSubjectsData() {
		
		subjects.clear();

		subjectService.getAllSubjectBySectionID(idOfSelectedSection).forEach(subs -> {

			LocalDateTime sectionTimeAdded = subs.getDateCreated();
			String formattedDateTime = sectionTimeAdded.format(DateTimeFormatter.ofPattern("MM-dd-yy HH:mm:a"));

			subjects.add(new Subject(subs.getId(), subs.getSubjectCode(), subs.getSubjectDescription(), formattedDateTime, subs.getStatus()));
		});
		
		subjectsToDisplayInGrid.setItems(subjects);
	}

	private void addSubject(String subjectCode, String subjectDesc) {
		
		ZoneId philippinesZoneId = ZoneId.of("Asia/Manila");
		LocalDateTime now = LocalDateTime.now(philippinesZoneId);
		
//		subjectCode = subjectCode.toUpperCase().trim();
//		subjectDesc = subjectDesc.trim();

		selectedSection = sectionService.getAccountById(idOfSelectedSection).orElseThrow();

		var sectionNameAlreadyExists = selectedSection.getSubjects().stream()
				.anyMatch(section -> section.getSubjectCode().equals(subjectCode));

		if (sectionNameAlreadyExists) {

			ConfirmDialog alreadyExistsDialog = new ConfirmDialog();
			alreadyExistsDialog.setText("Subject name already exists...");
			alreadyExistsDialog.setConfirmText("Ok");
			alreadyExistsDialog.open();

			return;
		}
		
		SubjectEntity newSub = SubjectEntity.builder()
		.subjectCode(subjectCode)
		.subjectDescription(subjectDesc)
		.dateCreated(now)
		.status("Closed")
		.attendanceEndTime(null)
		.section(selectedSection)
		.build();
		
		selectedSection.getSubjects().add(newSub);
		sectionService.saveChanges(selectedSection);
		
		syncSubjectsData();
		
	}

}
