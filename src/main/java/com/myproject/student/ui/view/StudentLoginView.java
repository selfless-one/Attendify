package com.myproject.student.ui.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("student/login")
public class StudentLoginView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	public StudentLoginView() {

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

		
		// Student toggle button
		Button toggleBtn = new Button("I am Student");
		toggleBtn.addClassNames(
			LumoUtility.Margin.Top.SMALL,
			LumoUtility.FontWeight.SEMIBOLD,
			LumoUtility.BorderRadius.MEDIUM,
			LumoUtility.Padding.SMALL,
			LumoUtility.TextColor.PRIMARY
		);
		
		
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
