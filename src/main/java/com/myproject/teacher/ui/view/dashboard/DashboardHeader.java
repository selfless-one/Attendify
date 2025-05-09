package com.myproject.teacher.ui.view.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.UI;
import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.myproject.backend.teacher.service.TeacherAccountService;

public class DashboardHeader extends HorizontalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DashboardHeader(TeacherAccountEntity acc, TeacherAccountService accService) {
        setWidth("930px");
        setJustifyContentMode(JustifyContentMode.BETWEEN);
        setDefaultVerticalComponentAlignment(Alignment.CENTER);
        getStyle().set("margin-top", "40px");
        getStyle().set("margin-left", "40px");
        
        
        var identityNotDefined = acc.getSurname() == null && acc.getFirstname() == null;
        
        String professorFullname;
        
        if (identityNotDefined) {
        	professorFullname = "Unknown";
        	
        	new IdentityDialog(acc, accService);
        	
        } else {
        	professorFullname = String.format("%s, %s", acc.getSurname(), acc.getFirstname());
        }
        
        Span teacherLabel = new Span("Professor: " + professorFullname);
        teacherLabel.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "18px");

        Button logoutBtn = new Button("Logout", e -> {
            UI.getCurrent().getSession().close();
            UI.getCurrent().navigate("teacher/login");
        });
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        add(teacherLabel, logoutBtn);
       // addClassName(LumoUtility.Background.PRIMARY_10);
    }
}

