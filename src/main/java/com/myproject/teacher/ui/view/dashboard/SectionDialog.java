package com.myproject.teacher.ui.view.dashboard;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.function.BiConsumer;

@CssImport("./styles/shared-styles.css")
public class SectionDialog extends Dialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Span headerLabel;
	
	private TextField sectionField;
	private TextField courseField;
	private VerticalLayout content;
	
	
	private Button saveButton;
	private Button cancelButton;
	private HorizontalLayout footer;
	
	private void setupHeader() {

		headerLabel = new Span("Add section");

		headerLabel.getStyle().setFontSize("18px");
		headerLabel.getStyle().setFontWeight("bold");
		headerLabel.getStyle().setPaddingBottom("3px");
		headerLabel.getStyle().setPaddingLeft("8px");
		
		headerLabel.addClassName("draggable");
		headerLabel.getStyle()
		.set("cursor", "move")
		.set("padding", "var(--lumo-space-m) 0");
		
		headerLabel.addClassNames(LumoUtility.FontWeight.BOLD,
				LumoUtility.TextColor.PRIMARY_CONTRAST,
				LumoUtility.FontSize.XLARGE);
	}
	
	private void setupContent() {
		
		sectionField = new TextField("Section name");
		courseField = new TextField("Course");
		
        sectionField.setRequiredIndicatorVisible(true);
        courseField.setRequiredIndicatorVisible(true);
        
        
        sectionField.setWidthFull();
        courseField.setWidthFull();
        
        sectionField.setHelperText("e.g. LFAU333N004");
        courseField.setHelperText("e.g. BSIT");
        
		content = new VerticalLayout(sectionField, courseField);
		
		content.setPadding(false);
		content.setSpacing(false);
		content.getStyle().setPaddingTop("15px");
		
//		layout.setPadding(false);
//		layout.setSpacing(false);
//		layout.setAlignItems(FlexComponent.Alignment.STRETCH);
//		layout.getStyle().set("width", "300px").set("max-width", "100%");

	}

	private void setupFooter(BiConsumer<String, String> onSave) {

		saveButton = new Button("Save");
		cancelButton = new Button("Cancel", e -> this.close());

		footer = new HorizontalLayout(saveButton, cancelButton);
		
		saveButton.addClickListener(e -> {
			
			var sectionValue = sectionField.getValue().trim();
			var courseValue = courseField.getValue().trim();
			
			boolean sectionIsEmpty = sectionValue.isBlank();
			boolean courseIsEmpty = courseValue.isBlank();

			if (!sectionIsEmpty && !courseIsEmpty) {
				
				if (sectionValue.contains(" ")) {
					sectionField.setInvalid(true);
					sectionField.setErrorMessage("whitespace is not allowed");
					return;
				}
				
				if (courseValue.contains(" ")) {
					courseField.setInvalid(true);
					courseField.setErrorMessage("whitespace is not allowed");
					return;
				}

				onSave.accept(sectionValue.toUpperCase(), courseValue.toUpperCase());
				close();
				
			} else {
				
				if (sectionIsEmpty) {
					sectionField.setInvalid(sectionIsEmpty);
					sectionField.setErrorMessage("required");
				}
				
				if (courseIsEmpty) {
					courseField.setInvalid(courseIsEmpty);
					courseField.setErrorMessage("required");
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

	}


	public SectionDialog(BiConsumer<String, String> onSave) {

		setupHeader();
        setupContent();
        setupFooter(onSave);
        
        getHeader().add(headerLabel);
        add(content);
        getFooter().add(footer);
        
        setCloseOnOutsideClick(false);
        setModal(true);
        setDraggable(true);
        setWidth("300px");
       
    }
}
