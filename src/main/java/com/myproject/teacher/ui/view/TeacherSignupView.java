package com.myproject.teacher.ui.view;

import com.myproject.backend.teacher.service.TeacherAccountService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("teacher/signup")
public class TeacherSignupView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	public TeacherSignupView(TeacherAccountService tService_) {

		final TeacherAccountService tService;
		
		tService = tService_;
		// Token field
		TextField tokenField = new TextField("Token");
		tokenField.setRequiredIndicatorVisible(true);
		
		// Setup LoginI18n
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.getForm().setUsername("Email");
		i18n.getForm().setTitle("Sign up");
		

		// Create login overlay
		LoginOverlay loginOverlay = new LoginOverlay();
		loginOverlay.setI18n(i18n);
		loginOverlay.getCustomFormArea().add(tokenField);

		loginOverlay.setTitle("Attendify");
		loginOverlay.setDescription(null);

		loginOverlay.addLoginListener(signup -> {
			
			tokenField.setInvalid(true);
			tokenField.setErrorMessage("Token is required");
			if (tokenField.getValue() == null || tokenField.getValue().trim().isEmpty()) {
				
				tokenField.setInvalid(true);
				tokenField.setErrorMessage("Token is required");
				
				loginOverlay.setOpened(true);
			
			} else {
			    tokenField.setInvalid(false);
			     
			    var email = signup.getUsername();
			    var password = signup.getPassword();
			    var token = tokenField.getValue();
			    
			    var result = tService.createAccount(email, password, token);
			    
			    if (result.equals("Account created successfully")) {
			    	
			    	UI.getCurrent().navigate("teacher/login");
			    	
			    }
			    
			    
			}
				
			
			
		});



		// --- Footer Design ---

		// Create footer layout
		VerticalLayout footerLayout = new VerticalLayout();
		footerLayout.setWidthFull();
		footerLayout.setPadding(false);
		footerLayout.setSpacing(false);
		footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

		// Sign up clickable link
		Anchor signUpLink = new Anchor("teacher/login", "Already have an account? Log in");
		signUpLink.addClassNames(
		    LumoUtility.TextAlignment.CENTER,
		    LumoUtility.FontSize.SMALL,
		    LumoUtility.Margin.NONE,
		    LumoUtility.TextColor.PRIMARY
		);
		signUpLink.getStyle().set("text-decoration", "none"); // Optional: remove underline if you want

		// Student toggle button
		Button toggleBtn = new Button("I am Teacher");
		toggleBtn.addClassNames(
			LumoUtility.Margin.Top.SMALL,
			LumoUtility.FontWeight.SEMIBOLD,
			LumoUtility.BorderRadius.MEDIUM,
			LumoUtility.Padding.SMALL,
			LumoUtility.TextColor.PRIMARY
		);
		

		toggleBtn.addClickListener(e -> {
			
			UI.getCurrent().navigate("student/login");
			
		});

		// Add elements to footer layout
		footerLayout.add(signUpLink, toggleBtn);
		loginOverlay.getFooter().add(footerLayout);

		// Add and show login overlay
		add(loginOverlay);
// Change the HEADER background to green
getStyle().setHeight("890px");
// Change the HEADER background to green
		loginOverlay.setOpened(true);
		loginOverlay.getElement().setAttribute("no-autofocus", "");
		//<theme-editor-local-classname>
		addClassName("teacher-signup-view-vertical-layout-1");
		
		
	}

}
