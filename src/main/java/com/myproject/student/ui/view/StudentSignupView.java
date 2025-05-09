package com.myproject.student.ui.view;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.myproject.backend.student.service.StudentAccountService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Anchor;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("student/signup")
public class StudentSignupView extends VerticalLayout {

	private static final long serialVersionUID = 1L;
	
	private final StudentAccountService studentAccountService;

	
	private TextField surnameF = new TextField("Surname");
	private TextField firstnameF = new TextField("Firstname");
	HorizontalLayout layout1 = new HorizontalLayout(surnameF, firstnameF);

	private TextField studentNumF = new TextField("Student Number");
	private TextField sectionF = new TextField("Section");
	HorizontalLayout layout2 = new HorizontalLayout(studentNumF, sectionF);

	private LoginI18n i18n = LoginI18n.createDefault();
	private LoginOverlay loginOverlay = new LoginOverlay();

	private ProgressBar loadingBar;

	private void customizeProperties() {

		surnameF.setRequired(true);
		surnameF.setMaxWidth("150px");

		firstnameF.setRequired(true);
		firstnameF.setMaxWidth("150px");

		studentNumF.setRequired(true);
		studentNumF.setMaxWidth("150px");

		sectionF.setRequired(true);
		sectionF.setMaxWidth("150px");

		i18n.getForm().setTitle("Sign up");
		i18n.getForm().setSubmit("Sign up");

		loginOverlay.setI18n(i18n);
		loginOverlay.setTitle("Attendify");
		loginOverlay.setDescription(null);
		loginOverlay.setForgotPasswordButtonVisible(false);

	}

	public StudentSignupView(StudentAccountService studentAccountService) {
		
		this.studentAccountService = studentAccountService;
		
		customizeProperties();

		loadingBar = new ProgressBar();
		loadingBar.getStyle().setPaddingTop("5px");
		//loadingBar.setIndeterminate(true);
		loadingBar.setVisible(true); // hidden at start
		loadingBar.setWidthFull(); 


		loginOverlay.getCustomFormArea().add(loadingBar, layout1, layout2);

		signUpEvent();

		// --- Footer Design ---

		// Create footer layout
		VerticalLayout footerLayout = new VerticalLayout();
		footerLayout.setWidthFull();
		footerLayout.setPadding(false);
		footerLayout.setSpacing(false);
		footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

		// Sign up clickable link
		Anchor signUpLink = new Anchor("student/login", "Already have an account? Log in");
		signUpLink.addClassNames(
				LumoUtility.Margin.Top.MEDIUM,
				LumoUtility.TextAlignment.CENTER,
				LumoUtility.FontSize.SMALL,
				LumoUtility.Margin.NONE,
				LumoUtility.TextColor.PRIMARY
				);
		signUpLink.getStyle().set("text-decoration", "none"); // Optional: remove underline if you want

		// Student toggle button
		Button toggleBtn = new Button("I am Student");
		toggleBtn.addClassNames(
				LumoUtility.Margin.Top.MEDIUM,
				LumoUtility.Margin.Top.SMALL,
				LumoUtility.FontWeight.SEMIBOLD,
				LumoUtility.BorderRadius.MEDIUM,
				LumoUtility.Padding.SMALL,
				LumoUtility.TextColor.PRIMARY,
				LumoUtility.Background.PRIMARY,
				LumoUtility.Width.AUTO,
				LumoUtility.Height.SMALL,
				LumoUtility.FontSize.SMALL
				);

		toggleBtn.getStyle().set("color", "white");
		
		toggleBtn.addClickListener(e -> {

			UI.getCurrent().navigate("professor/signup");

		});

		// Add elements to footer layout
		footerLayout.add(signUpLink, toggleBtn);
		loginOverlay.getFooter().add(footerLayout);

		//loginOverlay.getCustomFormArea().add(loadingBar);

		// Add and show login overlay
		add(loginOverlay);
		loginOverlay.setOpened(true);
		loginOverlay.getElement().setAttribute("no-autofocus", "");
	}


	private void signUpEvent() {

		loginOverlay.addLoginListener(signup -> {

			String username = signup.getUsername();
			String password = signup.getPassword();
			String surname = surnameF.getValue();
			String firstname = firstnameF.getValue();
			String studentNum = studentNumF.getValue();
			String section = sectionF.getValue();

			final boolean[] hasBlankField = new boolean[1];

			List<TextField> fields = List.of(surnameF, firstnameF, studentNumF, sectionF);

			fields.stream()
			.filter(field -> field.getValue().isBlank())
			.forEach(blankField -> {
				hasBlankField[0] = true;
				blankField.setInvalid(true);

				UI ui = UI.getCurrent(); // capture UI once
				scheduler.schedule(() -> {
					ui.access(() -> blankField.setInvalid(false));
				}, 2, TimeUnit.SECONDS);
			});

			var allFieldsAreFilled = !hasBlankField[0] && !signup.getUsername().isBlank() && !signup.getPassword().isBlank();

			if (allFieldsAreFilled) {
				
				String result = studentAccountService.createAccount(username, 
						password, 
						surname, 
						firstname, 
						studentNum, 
						section);

				loadingBar.setIndeterminate(true);
				loginOverlay.setEnabled(false);
				
				UI ui = UI.getCurrent();
				
				scheduler.schedule(() -> {
					
					ui.access(() -> {
						
						switch (result) {
						case "Section Not Exists" -> showErrorMessage("Section Not Exists");
						case "Student Number already exists" -> showErrorMessage("Student Number already exists");
						case "Username already exists" -> showErrorMessage("Username already exists");
						case "Account Created Success" -> {
							
							ConfirmDialog successDialog = new ConfirmDialog();
							successDialog.open();
							successDialog.setHeader("Account creation success");
							successDialog.setText("You can log in now");
							successDialog.setConfirmText("Ok");
							successDialog.addConfirmListener(e -> {
								successDialog.close();
								ui.navigate(StudentLoginView.class);
							});
						}
						}

						loadingBar.setIndeterminate(false);
						loginOverlay.setEnabled(true);
						
						System.out.println(result);
					});
				}, 2, TimeUnit.SECONDS);
			} else {
				showErrorMessage("please fill up all fields");
			}

		});

	}


	// Create scheduled executor for delay
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	// when all field are not filled
	private void showErrorMessage(String errorMsg) {

		UI ui = UI.getCurrent(); // capture UI once
		loginOverlay.showErrorMessage(null, errorMsg);

		scheduler.schedule(() -> {
			ui.access(() -> {
				loginOverlay.setError(false);
			});
		}, 3, TimeUnit.SECONDS);
		
	}

//	private void loadProgressBar() {
//		loadingBar.setIndeterminate(true);
//		loadingBar.setVisible(true);
//		loginOverlay.setEnabled(false);
//
//		scheduler.schedule(() -> {
//			UI ui = UI.getCurrent();
//			ui.access(() -> {
//				loadingBar.setVisible(false);
//				loginOverlay.setEnabled(true);
//			});
//		}, 2, TimeUnit.SECONDS);
//	}
//


}
