package com.myproject.teacher.ui.view.dashboard.subjectDataPage;

import com.myproject.backend.teacher.entity.SectionEntity;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.entity.TeacherAccount;
import com.myproject.backend.teacher.service.SectionService;
import com.myproject.backend.teacher.service.TeacherAccountService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

import java.util.function.BiConsumer;

public class SubjectDialog extends Dialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubjectDialog(BiConsumer<String, String> onSave, SectionService sectionService, SectionEntity sectionEntity) {
		setModal(true);
		setDraggable(true);

		TextField subjectCodeField = new TextField("Subject code");
		TextField subjectDescField = new TextField("Subject description");

		subjectCodeField.setRequiredIndicatorVisible(true);
		subjectDescField.setRequiredIndicatorVisible(true);

		VerticalLayout layout = new VerticalLayout(subjectCodeField, subjectDescField);
		layout.setPadding(false);
		layout.setSpacing(false);
		layout.setAlignItems(FlexComponent.Alignment.STRETCH);
		layout.getStyle().set("width", "300px").set("max-width", "100%");

		Button saveButton = new Button("Save", e -> {
			boolean subjectCodeIsEmpty = subjectCodeField.isEmpty();
			boolean subjectDescIsEmpty = subjectDescField.isEmpty();

			subjectCodeField.setInvalid(subjectCodeIsEmpty);
			subjectDescField.setInvalid(subjectDescIsEmpty);

			if (!subjectCodeIsEmpty && !subjectDescIsEmpty) {

				onSave.accept(subjectCodeField.getValue(), subjectDescField.getValue());

				SubjectEntity subject = SubjectEntity.builder()

						.subjectCode(subjectCodeField.getValue())
						.subjectDescription(subjectDescField.getValue())
						.dateAdded(null)
						.status("Closed")
						.build();

				sectionEntity.getSubjects().add(subject);
				sectionService.saveChanges(sectionEntity);
				close();
			}
		});
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

		Button cancelButton = new Button("Cancel", e -> close());

		getFooter().add(saveButton, cancelButton);
		add(layout);
	}
}
