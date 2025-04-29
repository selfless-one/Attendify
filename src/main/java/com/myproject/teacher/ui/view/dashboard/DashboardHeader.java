package com.myproject.teacher.ui.view.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.UI;
import com.myproject.backend.teacher.entity.TeacherAccount;

public class DashboardHeader extends HorizontalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DashboardHeader(TeacherAccount acc) {
        setWidth("600px");
        setJustifyContentMode(JustifyContentMode.BETWEEN);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        getStyle().set("margin-top", "40px");

        Span teacherLabel = new Span("Teacher " + acc.getEmail());
        teacherLabel.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "18px");

        Button logoutBtn = new Button("Logout", e -> {
            UI.getCurrent().getSession().close();
            UI.getCurrent().navigate("teacher/login");
        });
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        add(teacherLabel, logoutBtn);
    }
}

