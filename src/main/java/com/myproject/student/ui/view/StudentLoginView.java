package com.myproject.student.ui.view;

import com.myproject.backend.student.service.StudentAccountService;
import com.myproject.student.ui.view.dashboard.StudentDashboardView;
import com.myproject.teacher.ui.view.dashboard.DashboardView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("student/login")
public class StudentLoginView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final StudentAccountService studentAccountService;
	
	private final ProgressBar loadingBar; // ProgressBar instance
	
	public StudentLoginView(StudentAccountService studentAccountService) {

		this.studentAccountService = studentAccountService;
		
		// Setup LoginI18n
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.getForm().setUsername("Student Number");

		// Create login overlay
		LoginOverlay loginOverlay = new LoginOverlay();
		loginOverlay.setI18n(i18n);

		loginOverlay.setTitle("Attendify");
		loginOverlay.setDescription(null);

		// --- Footer Design ---


		// Create footer layout
		VerticalLayout footerLayout = new VerticalLayout();
		footerLayout.setWidthFull();
		footerLayout.setPadding(false);
		footerLayout.setSpacing(false);
		footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

		// Sign up clickable link
		Anchor signUpLink = new Anchor("student/signup", "Don't have an account? Sign up");
		signUpLink.addClassNames(
				LumoUtility.TextAlignment.CENTER,
				LumoUtility.FontSize.SMALL,
				LumoUtility.Margin.NONE,
				LumoUtility.TextColor.PRIMARY
				);
		signUpLink.getStyle().set("text-decoration", "none"); // Optional: remove underline if you want


		loadingBar = new ProgressBar();
		loadingBar.setIndeterminate(true);
		loadingBar.setVisible(false); // hidden at start
		loadingBar.setWidthFull(); 

		
		loginOverlay.addLoginListener(evt -> {

			
			// Show loading
			loadingBar.setVisible(true);
			loginOverlay.setEnabled(false);
			
			String studentNumber = evt.getUsername();
			String password = evt.getPassword();

			// Simulate processing delay (optional)
			getUI().ifPresent(ui -> ui.access(() -> {

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // Half second pause (optional)
				boolean authenticated = studentAccountService.authenticate(studentNumber, password);

				if (authenticated) {

					UI.getCurrent().getSession().setAttribute("student_number", studentNumber);
					//UI.getCurrent().getSession().setAttribute("teacher_password", password);
					UI.getCurrent().navigate(StudentDashboardView.class, evt.getUsername());
					
				} else {
					loginOverlay.setError(true);
					loginOverlay.setEnabled(true);
					loadingBar.setVisible(false); // Hide loading if failed
				}
			}));

		});


		// Student toggle button
		Button toggleBtn = new Button("I am Student");
		toggleBtn.addClassNames(
				LumoUtility.Margin.Top.SMALL,
				LumoUtility.FontWeight.SEMIBOLD,
				LumoUtility.BorderRadius.MEDIUM,
				LumoUtility.Padding.SMALL,
				LumoUtility.TextColor.PRIMARY,
				LumoUtility.Background.PRIMARY
				);


		//toggleBtn.getStyle().set("background-color", "#4460EF");
		toggleBtn.getStyle().set("color", "white");



		toggleBtn.addClickListener(e -> {

			UI.getCurrent().navigate("teacher/login");

		});

		// Add elements to footer layout
		footerLayout.add(signUpLink, toggleBtn);
		loginOverlay.getFooter().add(footerLayout);

		// Add and show login overlay
		add(loginOverlay);
		loginOverlay.setOpened(true);
		loginOverlay.getElement().setAttribute("no-autofocus", "");
	}
}
