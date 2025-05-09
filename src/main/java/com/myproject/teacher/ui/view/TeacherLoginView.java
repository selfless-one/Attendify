package com.myproject.teacher.ui.view;

import com.myproject.backend.teacher.service.TeacherAccountService;
import com.myproject.teacher.ui.view.dashboard.DashboardView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("professor/login")
@CssImport("./styles/shared-styles.css")
public class TeacherLoginView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final TeacherAccountService teacherAccService;

	private final LoginOverlay loginForm = new LoginOverlay();

	private final VerticalLayout footerLayout = new VerticalLayout();

	public void setupLoginForm() {

		loginForm.setTitle("Attendify");
		loginForm.setDescription(null);
		loginForm.addClassName("teacher-loginform");
	}

	private void showErrorMessage(String title, String errorMsg) {
		loginForm.showErrorMessage(title, errorMsg);		
	}

	public void loginFormLoginEvent() {

		Dialog loadingDialog = new Dialog();
		ProgressBar progressBar = new ProgressBar();

		loadingDialog.setModal(true); // Blocks interaction
		loadingDialog.setCloseOnEsc(false);
		loadingDialog.setCloseOnOutsideClick(false);
		progressBar.setIndeterminate(true); // Animate
		loadingDialog.add(progressBar);

		loginForm.addLoginListener(login -> {

			if (login.getUsername().isBlank() || login.getPassword().isBlank()) {
				showErrorMessage(null, "Please fill up all fields");
			} else {

				String username = login.getUsername();
				String password = login.getPassword();

				// Show dialog first in UI thread
				getUI().ifPresent(ui -> {

					ui.access(() -> loadingDialog.open());

					// Then run the actual logic in a new thread
					new Thread(() -> {
						try {
							Thread.sleep(3000); // simulate delay
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						boolean authenticated = teacherAccService.authenticate(username, password);

						ui.access(() -> {

							loadingDialog.close();

							if (authenticated) {

								UI.getCurrent().getSession().setAttribute("professorUsername", username);
								UI.getCurrent().navigate(DashboardView.class, username);
								
							} else {
								showErrorMessage(
										"Incorrect username or password", 
										"Check that you have entered the correct username and password and try again.");
							}
						});
					}).start(); // important: start the thread!
				});
			}

		});
	}

	public void loginFormForgotPassEvent() {
		loginForm.addForgotPasswordListener(forgot -> {

			ConfirmDialog walapa = new ConfirmDialog();

			walapa.setText("wala pa to sir");
			walapa.setConfirmText("ok");
			walapa.addConfirmListener(e -> walapa.close());
			walapa.open();
		});
	}

	public void setupFooterLayout() {

		footerLayout.setWidthFull();
		footerLayout.setPadding(false);
		footerLayout.setSpacing(false);
		footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

		final Anchor signUpLink = new Anchor("professor/signup", "Don't have an account? Sign up");
		signUpLink.addClassNames(
				LumoUtility.TextAlignment.CENTER,
				LumoUtility.FontSize.SMALL,
				LumoUtility.Margin.NONE,
				LumoUtility.TextColor.PRIMARY);
		signUpLink.getStyle().set("text-decoration", "none"); // Optional: remove underline if you want

		// Student toggle button
		Button toggleBtn = new Button("I am Professor");
		toggleBtn.addClassNames(
				LumoUtility.Margin.Top.MEDIUM,
				LumoUtility.Margin.Top.SMALL,
				LumoUtility.FontWeight.SEMIBOLD,
				LumoUtility.BorderRadius.MEDIUM,
				LumoUtility.Padding.SMALL,
				LumoUtility.Width.AUTO,
				LumoUtility.Height.SMALL,
				LumoUtility.FontSize.SMALL);

		toggleBtn.getStyle().set("background-color", "#4460EF");
		toggleBtn.getStyle().set("color", "WHITE");

		toggleBtn.addClickListener(e -> {

			UI.getCurrent().navigate("student/login");

		});

		footerLayout.add(signUpLink, toggleBtn);
	}

	public TeacherLoginView(TeacherAccountService teacherAccService) {

		this.teacherAccService = teacherAccService;

		setupLoginForm();

		loginFormLoginEvent();
		loginFormForgotPassEvent();

		setupFooterLayout();

		loginForm.getFooter().add(footerLayout);

		// Add and show login overlay
		add(loginForm);
		loginForm.setOpened(true);
		loginForm.getElement().setAttribute("no-autofocus", "");
	}
}
