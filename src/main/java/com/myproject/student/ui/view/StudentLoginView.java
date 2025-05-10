package com.myproject.student.ui.view;

import com.myproject.backend.student.service.StudentAccountService;
import com.myproject.student.ui.view.dashboard.StudentDashboardView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("student/login")
public class StudentLoginView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private final StudentAccountService studentAccountService;

	private LoginOverlay loginForm = new LoginOverlay();

	private VerticalLayout footerLayout = new VerticalLayout();
	private Anchor signUpLink;
	private Button toggleBtn;

	public void configureLoginForm() {
		loginForm.setTitle("Attendify");
		loginForm.setDescription(null);
	}

	public void configureFooter() {

		loginForm.addForgotPasswordListener(forgot -> {
			
			ConfirmDialog walapa = new ConfirmDialog();
			
			walapa.setText("wala pa to");
			walapa.setConfirmText("ok");
			walapa.addConfirmListener(e -> walapa.close());
			walapa.open();
		});
		
		footerLayout.setWidthFull();
		footerLayout.setPadding(false);
		footerLayout.setSpacing(false);
		footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

		// Sign up clickable link
		signUpLink = new Anchor("student/signup", "Don't have an account? Sign up");
		signUpLink.addClassNames(
				LumoUtility.TextAlignment.CENTER,
				LumoUtility.FontSize.SMALL,
				LumoUtility.Margin.NONE,
				LumoUtility.TextColor.PRIMARY
				);
		
		signUpLink.getStyle().setFontSize("14px");
		signUpLink.getStyle().setPaddingBottom("10px");
		
		signUpLink.getStyle().set("text-decoration", "none"); // Optional: remove underline if you want

		toggleBtn = new Button("I am Student");
		
		toggleBtn.getStyle().setBackground("#267BFB");
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
				LumoUtility.FontWeight.SEMIBOLD,
				LumoUtility.BorderRadius.MEDIUM,
				LumoUtility.Padding.SMALL,
			//	LumoUtility.TextColor.PRIMARY,
			//	LumoUtility.Background.PRIMARY,
				LumoUtility.Width.AUTO,
				LumoUtility.Height.SMALL,
				LumoUtility.FontSize.SMALL
				);

		toggleBtn.getStyle().set("color", "white");

		toggleBtn.addClickListener(e -> UI.getCurrent().navigate("professor/login"));

		if (signUpLink != null && toggleBtn != null) {
			footerLayout.add(signUpLink, toggleBtn);
		}
	}


	// when all field are not filled
	private void showErrorMessage(String errortitle, String errorMsg) {
		loginForm.showErrorMessage(errortitle, errorMsg);
	}


	public void loginEvent() {

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
					
					ui.access(() -> {
						loadingDialog.open();
					});

					// Then run the actual logic in a new thread
					new Thread(() -> {
						try {
							Thread.sleep(3000); // simulate delay
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						boolean authenticated = studentAccountService.authenticate(username, password);

						ui.access(() -> {
							
							loadingDialog.close();

							if (authenticated) {
								
								UI.getCurrent().getSession().setAttribute("studentUsername", username);
								
								UI.getCurrent().navigate(StudentDashboardView.class, username);
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


	public StudentLoginView(StudentAccountService studentAccountService) {

		this.studentAccountService = studentAccountService;

		configureLoginForm();
		configureFooter();

		loginEvent();

		loginForm.getFooter().add(footerLayout);
		loginForm.setOpened(true);
		loginForm.getElement().setAttribute("no-autofocus", "");

		add(loginForm);
	}
}
