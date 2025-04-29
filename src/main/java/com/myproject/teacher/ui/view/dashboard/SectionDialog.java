package com.myproject.teacher.ui.view.dashboard;

import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.TeacherAccount;
import com.myproject.backend.teacher.service.TeacherAccountService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import java.util.function.BiConsumer;

public class SectionDialog extends Dialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SectionDialog(BiConsumer<String, String> onSave, TeacherAccountService tService, TeacherAccount acc) {
        setModal(true);
        setDraggable(true);
        
        TextField sectionField = new TextField("Section name");
        TextField courseField = new TextField("Course");

        sectionField.setRequiredIndicatorVisible(true);
        courseField.setRequiredIndicatorVisible(true);

        VerticalLayout layout = new VerticalLayout(sectionField, courseField);
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        layout.getStyle().set("width", "300px").set("max-width", "100%");

        Button saveButton = new Button("Save", e -> {
            boolean sectionIsEmpty = sectionField.isEmpty();
            boolean courseIsEmpty = courseField.isEmpty();
            sectionField.setInvalid(sectionIsEmpty);
            courseField.setInvalid(courseIsEmpty);

            if (!sectionIsEmpty && !courseIsEmpty) {
            	
                onSave.accept(sectionField.getValue(), courseField.getValue());
                
                SectionEntity sec = SectionEntity.builder()
                		.sectionName(sectionField.getValue())
                		.course(courseField.getValue())
                		.build();
                
                
                acc.getSections().add(sec);
                tService.saveChanges(acc);
                
                close();
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        Button cancelButton = new Button("Cancel", e -> close());

        getFooter().add(saveButton, cancelButton);
        add(layout);
    }
}
