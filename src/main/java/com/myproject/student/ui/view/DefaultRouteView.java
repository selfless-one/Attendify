package com.myproject.student.ui.view;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

@Route("")
public class DefaultRouteView extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static boolean hasBeenReloaded;

	private static void reloadFirst() {
		// reload first because shows nothing at firsts the default route when visiting the site
		UI.getCurrent().getPage().executeJs("if (!window._reloadedOnce) { window._reloadedOnce = true; location.reload(); }");
		hasBeenReloaded = true;

	}
	public DefaultRouteView() {
		
		if (!hasBeenReloaded) {
			reloadFirst();
		}
		
		UI.getCurrent().navigate(StudentLoginView.class);
	}

}
