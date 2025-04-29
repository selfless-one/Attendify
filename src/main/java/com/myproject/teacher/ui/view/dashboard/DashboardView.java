package com.myproject.teacher.ui.view.dashboard;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.TeacherAccount;
import com.myproject.backend.teacher.service.TeacherAccountService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("teacher/dashboard")
public class DashboardView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final TeacherAccountService tService;
    
    private final TeacherAccount acc;
    
    private final String sessionedEmail;

    private HorizontalLayout header;
    private Div bodyWrapper;
    private Div body;
    
    private Grid<Section> sectionGrid;
    private List<Section> sections = new LinkedList<>();

    public DashboardView(TeacherAccountService tService) {
    	
        this.tService = tService;
        this.sessionedEmail = (String) UI.getCurrent().getSession().getAttribute("teacher_email");
        this.acc = tService.getAccount(sessionedEmail);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

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
                .set("box-shadow",
                        "0px 4px 10px rgba(0, 0, 0, 0.1), " +
                                "4px 0px 10px rgba(0, 0, 0, 0.1), " +
                                "-4px 0px 10px rgba(0, 0, 0, 0.1), " +
                                "0px -4px 10px rgba(0, 0, 0, 0.1)");

        TextField searchField = new TextField();
        searchField.setWidth("50%");
        searchField.setPlaceholder("Search");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        sectionGrid = new Grid<>(Section.class, false);
        sectionGrid.addColumn(Section::getSectionName).setHeader("Section");
        sectionGrid.addColumn(Section::getCourse).setHeader("Course");
        sectionGrid.addColumn(Section::getDateAdded).setHeader("Date Added");
        sectionGrid.setWidthFull();
        sectionGrid.setHeight("100%");
        sectionGrid.setEmptyStateText("Your list of sections will appear here.");

        sectionGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        
        sectionGrid.setPartNameGenerator(section -> {
            return section.getSectionName().startsWith("LF") ? "high-rating" : "low-rating";
        });

     // Add both high-rating and low-rating styles INLINE
        sectionGrid.getElement().executeJs(
            "const style = document.createElement('style');" +
            "style.innerHTML = `" +
            "  vaadin-grid::part(high-rating) {" +
            "    background-color: var(--lumo-success-color-10pct);" +
            "  }" +
            "  vaadin-grid::part(low-rating) {" +
            "    background-color: var(--lumo-error-color-10pct);" +
            "  }" +
            "`;" +
            "document.head.appendChild(style);"
        );

        
        
        List<SectionEntity> savedSections = tService.getAllSectionOfTeacher(sessionedEmail);
        savedSections.forEach(section -> sections.add(new Section(section.getSectionName(), section.getCourse(), null)));
        sectionGrid.setItems(sections);

        searchField.addValueChangeListener(e -> {
            String searchTerm = searchField.getValue().trim().toUpperCase();
            List<Section> filteredSections = sections.stream()
                    .filter(section -> section.getSectionName().toUpperCase().contains(searchTerm)
                            || section.getCourse().toUpperCase().contains(searchTerm))
                    .collect(Collectors.toList());
            sectionGrid.setItems(filteredSections);
        });
        
        

        sectionGrid.addSelectionListener(selection -> {
            Optional<Section> optionalSection = selection.getFirstSelectedItem();
            if (optionalSection.isPresent()) {
                Section selectedSection = optionalSection.get();
                String sectionName = selectedSection.getSectionName();

                System.out.printf("Selected section: %s%n", sectionName);

                
                try {
					Thread.sleep(2000);
					
					UI.getCurrent().navigate("teacher/signup/");
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
                
            }
        });

        
//        sectionGrid.addItemClickListener(event -> {
//            Section clickedSection = event.getItem();
//            UI.getCurrent().navigate("teacher/signup/" + clickedSection.getSectionName());
//        });
        
     // Inside bodyConfig() method
        sectionGrid.getElement().getStyle().set("position", "relative");

        sectionGrid.getElement().executeJs(
            "this.shadowRoot.querySelector('style').textContent += " +
            "'vaadin-grid-cell-content:hover {' + " +
            "'background-color: rgba(0, 123, 255, 0.2) !important; ' + " +
            "'transition: background-color 0.3s ease;' + " +
            "'}';"
        );
        VerticalLayout layout = new VerticalLayout(searchField, sectionGrid);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setSizeFull();

        body.add(layout);

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
        addSectionBtn.getElement().executeJs(
                "this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
                        "this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
        );

        bodyWrapper = new Div();
        bodyWrapper.getStyle()
                .set("position", "relative")
                .set("width", "600px")
                .set("height", "600px");
        bodyWrapper.add(body, addSectionBtn);
    }


    private void showAddSectionDialog() {
        SectionDialog dialog = new SectionDialog((sectionName, courseName) -> addSection(sectionName, courseName), tService, acc);
        dialog.open();
    }

    private void addSection(String sectionName, String courseName) {
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Section newSection = new Section(sectionName, courseName, formattedDateTime);
        sections.add(newSection);
        sectionGrid.setItems(sections); // update grid
    }
}
