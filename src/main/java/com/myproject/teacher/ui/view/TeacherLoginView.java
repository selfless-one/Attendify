package com.myproject.teacher.ui.view;

import com.myproject.backend.teacher.service.TeacherAccountService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("teacher/login")
public class TeacherLoginView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private final ProgressBar loadingBar; // ProgressBar instance

	public TeacherLoginView(TeacherAccountService tService_) {
		
		final TeacherAccountService tService;
		
		tService = tService_;

		// Setup LoginI18n
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.getForm().setUsername("Email");

		// Create login overlay
		LoginOverlay loginOverlay = new LoginOverlay();
		loginOverlay.setI18n(i18n);
		loginOverlay.setTitle("Attendify");
		loginOverlay.setDescription(null);
		
		
		// --- Create ProgressBar ---
		loadingBar = new ProgressBar();
		loadingBar.setIndeterminate(true);
		loadingBar.setVisible(false); // hidden at start
		loadingBar.setWidthFull(); 
		
		loginOverlay.addLoginListener(e -> {
			
			// Show loading
			loadingBar.setVisible(true);
			loginOverlay.setEnabled(false);
						
			String email = e.getUsername();
			String password = e.getPassword();
			
			
			// Simulate processing delay (optional)
			getUI().ifPresent(ui -> ui.access(() -> {
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // Half second pause (optional)
				boolean authenticated = tService.authenticate(email, password);
				
				if (authenticated) {
					
					UI.getCurrent().getSession().setAttribute("teacher_email", email);
					UI.getCurrent().getSession().setAttribute("teacher_password", password);
					
					UI.getCurrent().navigate("teacher/dashboard");
				} else {
					loginOverlay.setError(true);
					loginOverlay.setEnabled(true);
					loadingBar.setVisible(false); // Hide loading if failed
				}
			}));
					
			
//			if (tService.loginAccount(email, password).equals("Invalid credentials")) {
//				loginOverlay.setError(true);
//			} else {
//				UI.getCurrent().navigate("teacher/dashboard");
//			}
			
			
		});
		
		// --- Footer Design ---

		// Create footer layout
		VerticalLayout footerLayout = new VerticalLayout();
		footerLayout.setWidthFull();
		footerLayout.setPadding(false);
		footerLayout.setSpacing(false);
		footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

		// Sign up clickable link
		Anchor signUpLink = new Anchor("teacher/signup", "Don't have an account? Sign up");
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
		add(loadingBar, loginOverlay);
		loginOverlay.setOpened(true);
		loginOverlay.getElement().setAttribute("no-autofocus", "");
		//<theme-editor-local-classname>
		addClassName("teacher-login-view-vertical-layout-1");
getStyle().setHeight("100%");
	}
}
