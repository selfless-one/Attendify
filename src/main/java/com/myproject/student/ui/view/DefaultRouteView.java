package com.myproject.student.ui.view;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class DefaultRouteView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DefaultRouteView() {
		UI.getCurrent().navigate(StudentLoginView.class);
	}

}
