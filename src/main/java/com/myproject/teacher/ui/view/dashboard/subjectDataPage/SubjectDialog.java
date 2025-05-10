package com.myproject.teacher.ui.view.dashboard.subjectDataPage;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import java.util.function.BiConsumer;

@CssImport("./styles/shared-styles.css")
public class SubjectDialog extends Dialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubjectDialog(BiConsumer<String, String> onSave) {
		
		this.setCloseOnOutsideClick(false);
		this.setModal(true);
		this.setDraggable(true);

		TextField subjectCodeField = new TextField("Subject code");
		TextField subjectDescField = new TextField("Subject description");

		subjectCodeField.setHelperText("e.g. CC05");
		subjectDescField.setHelperText("e.g. Application Development and Emerging Technologies (3/1)");
		
		subjectCodeField.setRequiredIndicatorVisible(true);
		subjectDescField.setRequiredIndicatorVisible(true);

		VerticalLayout layout = new VerticalLayout(subjectCodeField, subjectDescField);
		layout.setPadding(false);
		layout.setSpacing(false);
		layout.setAlignItems(FlexComponent.Alignment.STRETCH);
		layout.addClassName(LumoUtility.Padding.Top.MEDIUM);
		layout.getStyle().set("width", "300px").set("max-width", "100%");

		Button saveButton = new Button("Save", e -> {
			boolean subjectCodeIsEmpty = subjectCodeField.getValue().isBlank();
			boolean subjectDescIsEmpty = subjectDescField.getValue().isBlank();

			if (!subjectCodeIsEmpty && !subjectDescIsEmpty) {

				onSave.accept(subjectCodeField.getValue(), subjectDescField.getValue());
				
				close();
				
			} else {
				
				if (subjectCodeIsEmpty) {
					subjectCodeField.setInvalid(subjectCodeIsEmpty);
					subjectCodeField.setErrorMessage("required");
					
				}
				
				if (subjectDescIsEmpty) {
					subjectDescField.setInvalid(subjectDescIsEmpty);
					subjectDescField.setErrorMessage("required");
					
				}
				
			}
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
		
		saveButton.getStyle()
		//.set("position", "absolute")
		//.set("bottom", "1px")
		//.set("right", "20px")
		//.set("z-index", "1")
		.set("border-radius", "10px")
		.set("padding", "10px 20px")
		//.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");

		saveButton.getElement()
		.executeJs("this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });"
				+ "this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });");

		Button cancelButton = new Button("Cancel", e -> close());

		Span header = new Span("Add subject");
		header.addClassNames(LumoUtility.TextColor.PRIMARY_CONTRAST,
				LumoUtility.FontWeight.BOLD,
				LumoUtility.FontSize.XLARGE,
		LumoUtility.Padding.Top.MEDIUM);
		header.addClassName("draggable");
		header.getStyle().set("cursor", "move");
		
		getHeader().add(header);
		add(layout);
		getFooter().add(saveButton, cancelButton);
		
	}
}
