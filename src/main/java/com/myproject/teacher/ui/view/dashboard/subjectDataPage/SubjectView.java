package com.myproject.teacher.ui.view.dashboard.subjectDataPage;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.entity.TeacherAccount;
import com.myproject.backend.teacher.service.SectionService;
import com.myproject.backend.teacher.service.TeacherAccountService;
import com.myproject.teacher.ui.view.dashboard.DashboardHeader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("teacher/dashboard/subject")
public class SubjectView extends VerticalLayout implements HasUrlParameter<String> {

    private static final long serialVersionUID = 1L;

    private final TeacherAccountService tService;
    private final SectionService sectionService;

    private TeacherAccount acc;
    private String sessionedEmail;
    private String sectionName;

    private HorizontalLayout header;
    private Div bodyWrapper;
    private Div body;

    private Grid<Subject> subjectGrid;
    private List<Subject> subjects = new LinkedList<>();

    public SubjectView(TeacherAccountService tService, SectionService sectionService) {
        this.tService = tService;
        this.sectionService = sectionService;

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
    }

    @Override
    public void setParameter(BeforeEvent event, String sectionName) {
        this.sectionName = sectionName;

        sessionedEmail = (String) UI.getCurrent().getSession().getAttribute("teacher_email");
        if (sessionedEmail == null) {
            UI.getCurrent().navigate("teacher/login");
            return;
        }

        this.acc = tService.getAccountByEmail(sessionedEmail);
        buildUI();
    }

    private void buildUI() {
        header = new DashboardHeader(acc);
        bodyConfig();
        add(header, bodyWrapper);
    }

    private void bodyConfig() {
        body = new Div();
        body.getStyle()
                .set("border", "2px solid black")
                .set("border-radius", "8px")
                .set("padding", "20px")
                .set("width", "600px")
                .set("height", "600px")
                .set("margin-top", "10px")
                .set("overflow", "auto")
                .set("box-shadow", "0px 4px 10px rgba(0, 0, 0, 0.1), 4px 0px 10px rgba(0, 0, 0, 0.1), -4px 0px 10px rgba(0, 0, 0, 0.1), 0px -4px 10px rgba(0, 0, 0, 0.1)");

        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        
        // Section label
        Span labelText = new Span("Section:");
        Span sectionLabelText = new Span(sectionName);
        
        labelText.getStyle().set("font-size", "25px")
        .set("margin-left", "6px");
        
        sectionLabelText.getStyle().set("font-weight", "bold")
        .set("font-size", "25px");
        
        HorizontalLayout sectionLabel = new HorizontalLayout(labelText, sectionLabelText);
        sectionLabel.setAlignItems(Alignment.CENTER);
        
//        Div sectionLabel = new Div();
//        sectionLabel.setText("Section: " + sectionName);
//        sectionLabel.getStyle()
//                .set("flex-grow", "1")
//                .set("font-weight", "bold")
//                .set("font-size", "25px")
//                .set("width", "400px")
//                .set("margin-left", "6px");
                //.set("align-self", "center");

        HorizontalLayout topBar = new HorizontalLayout(sectionLabel, searchField);
        topBar.setWidthFull();
        topBar.setAlignItems(Alignment.CENTER);
        topBar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        
        subjectGrid = new Grid<>(Subject.class, false);
        subjectGrid.addColumn(Subject::getSubjectCode).setHeader("Subject Code");
        subjectGrid.addColumn(Subject::getSubjectDescription).setHeader("Description");
        subjectGrid.addColumn(Subject::getDateAdded).setHeader("Date Added");
        subjectGrid.setWidthFull();
        subjectGrid.setHeight("100%");
        subjectGrid.setEmptyStateText("Your list of subject for section " + sectionName + " will appear here.");
        subjectGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

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
            List<Subject> filtered = subjects.stream()
                    .filter(subj -> subj.getSubjectCode().toUpperCase().contains(searchTerm)
                            || subj.getSubjectDescription().toUpperCase().contains(searchTerm))
                    .collect(Collectors.toList());
            subjectGrid.setItems(filtered);
        });

        subjectGrid.addSelectionListener(selection -> {
            Optional<Subject> selected = selection.getFirstSelectedItem();
            selected.ifPresent(subject -> {
                System.out.println("Selected subject: " + subject.getSubjectDescription());
                UI.getCurrent().navigate("teacher/signup/"); // You might want to pass more data here
            });
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
        body.add(layout);

        Button addSubjectnBtn = new Button("Add Subject", e -> showAddSubjectDialog());
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
                .set("width", "600px")
                .set("height", "600px");

        bodyWrapper.add(body, addSubjectnBtn);
    }

    private void loadSubjectData() {
       
        subjects.clear();
        
        sectionService.getAllSubjectOfSection(acc).forEach(subject ->
            subjects.add(new Subject(subject.getSubjectCode(), subject.getSubjectDescription(), null))
        );
        
        subjectGrid.setItems(subjects);
    }

    private void showAddSubjectDialog() {
        SubjectDialog dialog = new SubjectDialog(this::addSubject, tService, acc);
        dialog.open();
    }

    private void addSubject(String subjectCode, String subjectDesc) {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Subject newSubject = new Subject(subjectCode, subjectDesc, formattedDateTime);
        subjects.add(newSubject);
        subjectGrid.setItems(subjects); // update grid
    }
}
