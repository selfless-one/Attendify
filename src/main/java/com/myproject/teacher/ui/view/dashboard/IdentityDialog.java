package com.myproject.teacher.ui.view.dashboard;

import com.myproject.backend.teacher.entity.TeacherAccountEntity;
import com.myproject.backend.teacher.service.TeacherAccountService;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route
public class IdentityDialog extends Dialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Span headerLabel;
	private Button buttonTip;
	private Popover tip;
	private HorizontalLayout headerLayout;

	private void setupHeader() {

		headerLabel = new Span("Enter your name");
		headerLabel.addClassNames(
				LumoUtility.FontSize.LARGE,
				LumoUtility.FontWeight.BOLD,
				LumoUtility.Padding.Top.MEDIUM);
		headerLabel.getStyle().setColor("white");

		headerLabel.getStyle().setFontSize("18px");
		headerLabel.getStyle().setPaddingBottom("3px");
		headerLabel.getStyle().setPaddingLeft("8px");
		
		buttonTip = new Button(new Icon(VaadinIcon.INFO_CIRCLE));
		buttonTip.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ICON);
		buttonTip.getStyle().setColor("white");
		buttonTip.addClassNames(LumoUtility.Padding.Top.MEDIUM);

		tip = new Popover();
		tip.setTarget(buttonTip);

		final Span info1 = new Span("This helps students recognize their professor");
		final Span info2 = new Span("in the section they’ve registered for.");

		tip.add(info1,new HtmlComponent("br"), info2);

		headerLayout = new HorizontalLayout(headerLabel, buttonTip);
		headerLayout.getStyle().set("cursor", "move");
		headerLayout.getStyle().setPaddingTop("15px");
		headerLayout.addClassName("draggable");
	}

	TextField surname;
	TextField firstname;
	private VerticalLayout contentLayout;


	private void setupContent() {

		surname = new TextField("Surname");
		firstname = new TextField("Firstname");
		
		surname.setRequired(true);
		firstname.setRequired(true);

		surname.setWidthFull();
		firstname.setWidthFull();

		contentLayout = new VerticalLayout(surname, firstname);
		
		contentLayout.setSpacing(false);
		contentLayout.setPadding(false);
		contentLayout.addClassNames(LumoUtility.Padding.Top.MEDIUM);
	}

	private Button submitBtn;

	private void setupFooter() {

		submitBtn = new Button("Submit");

		submitBtn.getStyle()
		.set("color", "white")
		.set("font-size", "14px")
		.set("background-color", "#4460EF")
		.set("border-radius", "10px")
		.set("padding", "10px 20px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		//	logoutBtn.getElement().getThemeList().add("error");
		submitBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);

		//logoutBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

		submitBtn.addClickListener(evt -> {

			var surnameVal = surname.getValue().trim();
			var firstnameVal =  firstname.getValue().trim(); 

			if (!surnameVal.isBlank() && !firstnameVal.isBlank()) {
				
//					if (surnameVal.contains(" ")) {
//						surname.setInvalid(true);
//						surname.setErrorMessage("whitespace is not allowed");
//						return;
//					}
//					
//					if (firstnameVal.contains(" ")) {
//						firstname.setInvalid(true);
//						firstname.setErrorMessage("whitespace is not allowed");
//						return;
//					}

					Span header = new Span("Confirm your details to continue");
					header.addClassNames(LumoUtility.FontWeight.BOLD, 
							LumoUtility.Margin.Bottom.SMALL, 
							LumoUtility.TextColor.PRIMARY_CONTRAST);
					
					
					String surnameC = surname.getValue().trim(), firstnameC = firstname.getValue().trim();
		    		
		    		var surnameFormat = surnameC.substring(0, 1).toUpperCase() + surnameC.substring(1);
		    		var firstnameFormat = firstnameC.substring(0, 1).toUpperCase() + firstnameC.substring(1);
					
		    		Span surnameData = new Span("Surname: " + surnameFormat);
					Span firstnameData = new Span("Firstname: " + firstnameFormat);
					
					VerticalLayout details = new VerticalLayout(surnameData, firstnameData);
					details.setSpacing(false);
					details.setPadding(false);
					details.getStyle().setPaddingTop("20px");
					
					Button confirmBtn = new Button("Confirm");
					Button cancelBtn = new Button("Cancel");
					
					Dialog dialogConfirm = new Dialog();
					
					dialogConfirm.setTop("342px");
					dialogConfirm.setWidth("280px");
					dialogConfirm.setCloseOnOutsideClick(false);
					dialogConfirm.getHeader().add(header);
					dialogConfirm.add(details);
					dialogConfirm.getFooter().add(cancelBtn, confirmBtn);
					
					dialogConfirm.open();
					
					cancelBtn.addClickListener(e -> dialogConfirm.close());
					
					confirmBtn.addClickListener(e -> {
						
						accService.saveSurnameAndFirstname(surname.getValue(), firstname.getValue(), acc);
						
						dialogConfirm.close();
						this.close();
						
						UI.getCurrent().getPage().reload();
					});


			} else {

				if (surname.getValue().isBlank()) {
					surname.setInvalid(true);
					surname.setErrorMessage("required");
				}
				
				if (firstname.getValue().isBlank()) {
					firstname.setInvalid(true);
					firstname.setErrorMessage("required");
				}
			}
		});

	}
	
	private final TeacherAccountEntity acc;
	private final TeacherAccountService accService;

	public IdentityDialog(TeacherAccountEntity acc, TeacherAccountService accService) {
		
		this.acc = acc;
		this.accService = accService;
		
		setupHeader();
		setupContent();
		setupFooter();

		getHeader().add(headerLayout);
		add(contentLayout);
		getFooter().add(submitBtn);

		// dialog width
		
		
		this.setCloseOnOutsideClick(false);
		this.setDraggable(true);
		this.setWidth("280px");
		this.open();
	}


}
