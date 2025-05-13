package com.myproject.teacher.ui.view.dashboard.subjectDataPage;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.UI;
import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.myproject.backend.teacher.service.TeacherAccountService;
import com.myproject.teacher.ui.view.dashboard.IdentityDialog;

public class SubjectHeader extends HorizontalLayout {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubjectHeader(TeacherAccountEntity acc, TeacherAccountService accService) {
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

        	ConfirmDialog confirmLogout = new ConfirmDialog();

        	confirmLogout.setCancelable(true);
        	confirmLogout.setConfirmText("Confirm");
        	confirmLogout.setText("Confirm Logout...");

        	confirmLogout.addConfirmListener(event -> {

        		UI.getCurrent().getSession().close();
        		UI.getCurrent().navigate("teacher/login");
        	});

        	confirmLogout.open();

        });
      //  logoutBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

       // logoutBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        logoutBtn.getStyle()
        .set("background-color", "#822020")
		//.set("position", "absolute")
		.set("bottom", "1px")
		//.set("right", "20px")
		//.set("z-index", "1")
		.set("border-radius", "10px")
		.set("padding", "10px 20px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
        logoutBtn.getElement().getThemeList().add("primary");
        logoutBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);	

        
        
        add(teacherLabel, logoutBtn);
       // addClassName(LumoUtility.Background.PRIMARY_10);
    }
}

