package com.myproject.student.ui.view.dashboard.dialog;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;

@CssImport("./styles/shared-styles.css")
public class DialogSubjectClose extends ConfirmDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DialogSubjectClose() {
		
	//	setVisible(true);
		open();
		
		setHeader("Attendance is closed");
		setText("Attendance is currently closed. Wait for your professor to open it.");

		setConfirmText("OK");  
        
		addConfirmListener(evt -> this.close());
	}

}
