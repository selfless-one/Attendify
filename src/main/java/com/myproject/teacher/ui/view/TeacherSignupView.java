package com.myproject.teacher.ui.view;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.myproject.backend.teacher.service.TeacherAccountService;
import com.myproject.student.ui.view.StudentSignupView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Professor Sign Up")
@Route("professor/signup")
public class TeacherSignupView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final TeacherAccountService teacherAccService;

	private LoginI18n i18n;
	private LoginOverlay signupForm;

	private TextField tokenField = new TextField("Token");

	private VerticalLayout footerLayout;

	private void setupSignupForm() {

		i18n = LoginI18n.createDefault();
		i18n.getForm().setTitle("Sign up");
		i18n.getForm().setSubmit("Sign up");

		signupForm = new LoginOverlay();

		signupForm.setI18n(i18n);
		signupForm.addClassName("teacher-loginform");
		signupForm.setForgotPasswordButtonVisible(false);
		signupForm.setTitle("Attendify");
		signupForm.setDescription(null);

		signupForm.getCustomFormArea().add(tokenField);	
	}

	private void showErrorMessage(String title, String errorMsg) {
		signupForm.showErrorMessage(title, errorMsg);		
	}

	
	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	
	private void signupFormSignupEvent() {

		Dialog loadingDialog = new Dialog();
		ProgressBar loadingBar = new ProgressBar();

		loadingDialog.setModal(true); // Blocks interaction
		loadingDialog.setCloseOnEsc(false);
		loadingDialog.setCloseOnOutsideClick(false);
		loadingBar.setIndeterminate(true); // Animate
		loadingDialog.add(loadingBar);
		
		signupForm.addLoginListener(signup -> {

			var usernameIsEmpty = signup.getUsername().trim().isEmpty();
			var passwordIsEmpty = signup.getPassword().trim().isEmpty();
			var tokenIsEmpty = tokenField.getValue() == null || tokenField.getValue().trim().isEmpty();

			var allFieldsAreFilled = !usernameIsEmpty && !passwordIsEmpty && !tokenIsEmpty;

			if (allFieldsAreFilled) {

				loadingDialog.open();
				signupForm.setEnabled(false);
				
				UI ui = UI.getCurrent();
				
				scheduler.schedule(() -> {
					
					ui.access(() -> {
						
						String result = teacherAccService.createAccount(signup.getUsername(), signup.getPassword(), tokenField.getValue());
						
						switch (result) {
						case "Contain whitespaces" -> showErrorMessage(null, "Spaces are not allowed—please remove them.");
						case "Username must be at least 5 characters long" -> showErrorMessage(null, "Username must be at least 5 characters long");
						case "Password must be at least 5 characters long" ->  showErrorMessage(null, "Password must be at least 5 characters long");
						case "Invalid token" -> showErrorMessage(null, "Invalid token");
						case "Username already exists" -> showErrorMessage(null, "Username already exists");
						case "Account created successfully" -> {
							
							ConfirmDialog successDialog = new ConfirmDialog();
							successDialog.open();
							successDialog.setHeader("Account creation success");
							successDialog.setText("You can log in now");
							successDialog.setConfirmText("Ok");
							successDialog.addConfirmListener(e -> {
								successDialog.close();
								ui.navigate(TeacherLoginView.class);
							});
						}
						}

						loadingDialog.close();
						signupForm.setEnabled(true);
						
						System.out.println(result);
						
					});
				}, 2, TimeUnit.SECONDS);
			} else {
				
				showErrorMessage(null, "Please fill up all fields");

				if (tokenIsEmpty) {
					tokenField.setInvalid(true);
					tokenField.setErrorMessage("Token is required");
					signupForm.setOpened(true);
				}
			}

		});

	}

	private void setupFooterLayout() {

		footerLayout = new VerticalLayout();
		footerLayout.setWidthFull();
		footerLayout.setPadding(false);
		footerLayout.setSpacing(false);
		footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

		// login up clickable link
		Anchor loginLink = new Anchor("professor/login", "Already have an account? Log in");
		loginLink.addClassNames(
				LumoUtility.Margin.Top.MEDIUM,
				LumoUtility.TextAlignment.CENTER,
				LumoUtility.FontSize.SMALL,
				LumoUtility.Margin.NONE,
				LumoUtility.TextColor.PRIMARY
				);
		
		loginLink.getStyle().setFontSize("14px");
		loginLink.getStyle().setPaddingBottom("10px");
		loginLink.getStyle().setPaddingTop("10px");
		
		loginLink.getStyle().set("text-decoration", "none"); // Optional: remove underline if you want

		// Student toggle button
		Button toggleBtn = new Button("I am Professor");
		

		toggleBtn.getStyle().setHeight("30px");		
		
		toggleBtn.getStyle()
		.set("z-index", "1")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		toggleBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);	

		
		toggleBtn.addClassNames(
				LumoUtility.Margin.Top.MEDIUM,
				LumoUtility.Margin.Top.SMALL,
				LumoUtility.FontWeight.SEMIBOLD,
				LumoUtility.BorderRadius.MEDIUM,
				LumoUtility.Padding.SMALL,
				LumoUtility.Width.AUTO,
				LumoUtility.Height.SMALL,
				LumoUtility.FontSize.SMALL
				);
		
		toggleBtn.getStyle().set("background-color", "#4460EF");
		toggleBtn.getStyle().set("color", "WHITE");

		toggleBtn.addClickListener(e -> UI.getCurrent().navigate(StudentSignupView.class));

		footerLayout.add(loginLink, toggleBtn);
	}

	public TeacherSignupView(TeacherAccountService teacherAccService) {

		this.teacherAccService = teacherAccService;

		tokenField.setRequiredIndicatorVisible(true);

		setupSignupForm();
		signupFormSignupEvent();

		setupFooterLayout();

		signupForm.getFooter().add(footerLayout);

		add(signupForm);

		getStyle().setHeight("890px");

		signupForm.setOpened(true);
		signupForm.getElement().setAttribute("no-autofocus", "");
		//<theme-editor-local-classname>
		addClassName("teacher-signup-view-vertical-layout-1");
	}

}
