package com.myproject.teacher.ui.view.dashboard.subjectDataPage;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.repository.SubjectRepository;
import com.myproject.backend.teacher.service.SubjectService;
import com.myproject.teacher.ui.view.TeacherLoginView;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.timepicker.TimePicker.TimePickerI18n;
import com.vaadin.flow.dom.Style.AlignItems;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@CssImport("./styles/shared-styles.css")
@Route
public class OpenTheAttendanceDialog extends Dialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private VerticalLayout headerLayout() {

		H2 headline = new H2("Attendify");
		headline.getStyle().set("padding", "var(--lumo-space-m) 0")
		.set("padding-bottom", "5px")
		.set("user-select", "none")
		.set("color", "White");

		String subjectStatus = subjectServ.getById(IdOfSelectedSubject).get().getStatus();

		Span status = new Span("Status: ");
		Span statusVal = new Span(subjectStatus);

		// if closed
		if (subjectStatus.equals("Closed")) {

			statusVal.getStyle().set("padding", "var(--lumo-space-m) 0")
			.set("padding-bottom", "5px")
			.set("user-select", "none")
			.set("color", "#F0B4B4");


		} else {

			statusVal.getStyle().set("padding", "var(--lumo-space-m) 0")
			.set("padding-bottom", "5px")
			.set("user-select", "none")
			.set("color", "#82F5ED");

		}

		status.getStyle().set("padding", "var(--lumo-space-m) 0")
		.set("padding-bottom", "5px")
		.set("user-select", "none")
		.set("color", "White");

		statusVal.getStyle().set("margin-left", "5px");

		HorizontalLayout statusWrapper = new HorizontalLayout(status, statusVal);

		statusWrapper.setSpacing(false);
		statusWrapper.setPadding(false);
		statusWrapper.setMargin(false);

		HorizontalLayout headlineWrapper = new HorizontalLayout(headline,statusWrapper);

		headlineWrapper.setWidthFull();
		headlineWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		headlineWrapper.setAlignItems(FlexComponent.Alignment.CENTER);


		Span description = new Span("Subject: IAS1  |  " + "Section: LFAU133N004");
		description.getStyle().set("user-select", "none")
		.set("color", "White");

		VerticalLayout header = new VerticalLayout(headlineWrapper, description);
		header.addClassName("draggable");
		header.getStyle().set("cursor", "move");

		return header;

	}

	private Runnable r;
	private Integer IdOfSelectedSubject;
	private SubjectService subjectServ;

	public OpenTheAttendanceDialog(Runnable r, Integer IdOfSelectedSubject, SubjectService subjectServ) {

		this.r = r;
		this.IdOfSelectedSubject = IdOfSelectedSubject;
		this.subjectServ = subjectServ;

		this.subjectEnt = subjectServ.getById(IdOfSelectedSubject).get();

		setModal(true);
		setDraggable(true);

		getHeader().add(headerLayout());

		if (subjectEnt.getStatus().equals("Closed")) {
			add(contentLayout());
		} else {
			add(contentLayoutWhenStatusIsOpen());
		}

		getFooter().add(footerLayout());
	}

	private boolean[] buttonIsClosed = {true};
	private TimePicker timePicker = new TimePicker();

	private HorizontalLayout contentLayout() {

		Button openAttendifyBtn = new Button("Open");
		openAttendifyBtn.getStyle().setBackgroundColor("#877BAE");
		openAttendifyBtn.getStyle().setColor("White");


		//openAttendifyBtn.getStyle().set("flex-shrink", "0");

		openAttendifyBtn.addClickListener(evt -> {

			if (buttonIsClosed[0]) {
				openAttendifyBtn.getStyle().setBackgroundColor("#4460EF");
				openAttendifyBtn.getStyle().setColor("White");
				openAttendifyBtn.setText("Open");
				timePicker.setPlaceholder("until what time?");
				timePicker.setEnabled(true);
				timePicker.setInvalid(false);
				confirmBtn.setEnabled(true);
				buttonIsClosed[0] = false;
			} else {
				openAttendifyBtn.getStyle().setBackgroundColor("#877BAE");
				openAttendifyBtn.getStyle().setColor("Dark");
				openAttendifyBtn.setText("Open");
				timePicker.setEnabled(false);
				confirmBtn.setEnabled(false);	
				timePicker.setInvalid(false);
				timePicker.setValue(null);
				buttonIsClosed[0] = true;
			}
		});

		timePicker.setEnabled(false);
		timePicker.setPlaceholder("until what time?");
		timePicker.setClearButtonVisible(true);
		timePicker.setTooltipText("The time you set will apply only for today");

		timePicker.setRequiredIndicatorVisible(true);
		timePicker.setStep(Duration.ofMinutes(30));

		timePicker.setI18n(new TimePickerI18n());

		HorizontalLayout content = new HorizontalLayout(openAttendifyBtn, timePicker);
		content.getStyle().setAlignItems(AlignItems.CENTER);
		content.getStyle().setPaddingTop("10%").setPaddingBottom("1%");
		return content;


	}

	private SubjectEntity subjectEnt;

	private ScheduledExecutorService scheduler;
	private Span clock = new Span();

	private VerticalLayout contentLayoutWhenStatusIsOpen() {
		clock.getStyle()
		.set("color", "Black")
		.set("margin-left", "5px")
		.set("margin-right", "5px");


		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		UI ui = UI.getCurrent(); // capture UI before going async

		LocalTime attendanceEndTime = subjectEnt.getAttendanceEndTime();

		scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> {

			LocalTime now = LocalTime.now();
			Duration remaining = Duration.between(now, attendanceEndTime);


			//String timeCoundown = LocalTime.now().;

			if (!remaining.isNegative() && ui != null && ui.getSession() != null) {

				long hours = remaining.toHours();
				long minutes = remaining.toMinutes() % 60;
				long seconds = remaining.getSeconds() % 60;

				String countdownText = String.format("%02d:%02d:%02d", hours, minutes, seconds);

				ui.access(() -> clock.setText(countdownText)); // safely update UI
			} else {
				// Time's up
				ui.access(() -> clock.setText("00:00:00"));
				
				subjectEnt.setStatus("Closed");
				subjectServ.save(subjectEnt);
				System.out.println("Time is up! Reloading page...");
				ui.getPage().reload();
				
				scheduler.shutdown(); // Stop updating
			}
		}, 0, 1, TimeUnit.SECONDS); // update every 1 second


		Span clockLabel = new Span("Time left: ");
		Span closeOfTimeLabel = new Span("Ends at: " + subjectEnt.getAttendanceEndTime()); 


		HorizontalLayout content = new HorizontalLayout(clockLabel, clock, closeOfTimeLabel);
		//content.getStyle().setAlignItems(AlignItems.CENTER);
		content.getStyle().setPaddingTop("1%");
		content.setSpacing(false);

		RouterLink attendifiedList = new RouterLink("attendified list", TeacherLoginView.class);
		attendifiedList.getStyle().setMarginTop("5px");

		VerticalLayout content1 = new VerticalLayout(content, attendifiedList);
		content1.setSpacing(false);
		//content1.

		content1.getStyle().setPaddingTop("10%").setPaddingBottom("1%");


		return content1;
	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		super.onDetach(detachEvent);
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdownNow();
		}
	}



	Button confirmBtn = new Button("Confirm");
	Button cancelBtn = new Button("Cancel", evt -> this.close());

	private HorizontalLayout footerLayout() {
		confirmBtn.setEnabled(false);

		confirmBtn.addClickListener(evt -> {


			boolean timePickerIsEmpty = timePicker.isEmpty();
			boolean timePickerIsInvalid = timePicker.isInvalid();

			if (!timePickerIsEmpty && !timePickerIsInvalid) {

				System.out.println("adas");

				subjectServ.updateStatusAndAttedanceEndTimeAtById(IdOfSelectedSubject, "Open", timePicker.getValue());

				r.run();
				this.close();

			} else {
				timePicker.setInvalid(true);
			}


		});

		return new HorizontalLayout(confirmBtn, cancelBtn);


	}




}
