package com.myproject.teacher.ui.view.dashboard.subjectDataPage;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.service.SubjectService;
import com.myproject.teacher.ui.view.TeacherLoginView;
import com.myproject.teacher.ui.view.dashboard.subjectDataPage.attendifiedStudent.DownloadStudentAttendified;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.timepicker.TimePicker.TimePickerI18n;
import com.vaadin.flow.dom.Style.AlignItems;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;

@CssImport("./styles/shared-styles.css")
public class OpenTheAttendanceDialog extends Dialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Runnable r;
	private Integer IdOfSelectedSubject;
	private SubjectService subjectService;                                                                                
	
	private SubjectEntity subjectEntity;

	public OpenTheAttendanceDialog(Runnable r, Integer IdOfSelectedSubject, SubjectService subjectService) {

		this.r = r;
		this.IdOfSelectedSubject = IdOfSelectedSubject;
		this.subjectService = subjectService;

		this.subjectEntity = subjectService.getById(IdOfSelectedSubject).get();
                                                                                             
		setCloseOnOutsideClick(false);
		setModal                                                                                                                                                                                                                                                             (true);
		setDraggable(true);
		
		invokeDialog();
	}

	private void invokeDialog() {

		getHeader().removeAll();
		getFooter().removeAll();
		removeAll();
		
		getHeader().add(headerLayout());
		
		if (subjectEntity.getStatus().equals("Closed")) {
			
			if (subjectEntity.isHasBeenDownloadedStudentAttendified()) {
				
				add(contentLayoutWhenStatusIsClose());
				getFooter().add(footerLayoutWhenStatusIsClose());
				
			} else {
				
				add(contentLayoutWhenStatusIsOpenAndDownloadable());
				getFooter().add(footerLayoutWhenStatusIsClosedAndDownloadable());
			}
			
		} else {
			add(contentLayoutWhenStatusIsOpen());
			getFooter().add(footerLayoutWhenStatusIsOpen());
		}
	}

	private boolean[] buttonIsClosed = {true};
	private TimePicker timePicker = new TimePicker();
	
	private ScheduledExecutorService scheduler;
	private Span clock = new Span();
	
	private Button confirmBtn = new Button("Confirm");
	
	private VerticalLayout headerLayout() {

		Button closeButton = new Button(VaadinIcon.CLOSE.create());

		closeButton.setAriaLabel("Close dialog");
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

		// Style to float in upper-right corner
		closeButton.getStyle()
		.set("color", "White")
		.set("position", "absolute")
		.set("top", "10px")
		.set("right", "10px")
		.set("z-index", "100")
		.set("padding", "0")
		.set("width", "24px")
		.set("height", "24px")
		.set("font-size", "12px");

		closeButton.addClickListener(evt -> this.close());

		H2 headline = new H2("Attendify");
		headline.getStyle().set("padding", "var(--lumo-space-m) 0")
		.set("padding-bottom", "5px")
		.set("user-select", "none")
		.set("color", "White");

		String subjectStatus = subjectService.getById(IdOfSelectedSubject).get().getStatus();

		Span status = new Span("Status: ");
		Span statusVal = new Span(subjectStatus);
		
		status.getStyle().set("padding", "var(--lumo-space-m) 0")
		.set("padding-bottom", "5px")
		.set("user-select", "none")
		.set("color", "White");

		statusVal.getStyle().set("margin-left", "5px");

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

		HorizontalLayout statusWrapper = new HorizontalLayout(status, statusVal);

		statusWrapper.setSpacing(false);
		statusWrapper.setPadding(false);
		statusWrapper.setMargin(false);

		HorizontalLayout headlineWrapper = new HorizontalLayout(closeButton, headline,statusWrapper);

		headlineWrapper.setWidthFull();
		headlineWrapper.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		headlineWrapper.setAlignItems(FlexComponent.Alignment.CENTER);
		
		Span description = new Span(String.format("Subject: %s  |  Section: %s", subjectEntity.getSubjectCode(), subjectEntity.getSection().getSectionName()));
		description.getStyle().set("user-select", "none")
		.set("color", "White");

		VerticalLayout header = new VerticalLayout(headlineWrapper, description);
		header.addClassName("draggable");
		header.getStyle().set("cursor", "move");

		return header;

	}

	private HorizontalLayout contentLayoutWhenStatusIsClose() {

		Button openAttendifyBtn = new Button("Open");
		openAttendifyBtn.getStyle().setBackgroundColor("#877BAE");
		openAttendifyBtn.getStyle().setColor("White");

		timePicker.setEnabled(false);
		//openAttendifyBtn.getStyle().set("flex-shrink", "0");

		openAttendifyBtn.addClickListener(evt -> {

			if (buttonIsClosed[0]) {
				openAttendifyBtn.getStyle().setBackgroundColor("#4460EF");
				openAttendifyBtn.getStyle().setColor("White");
				openAttendifyBtn.setText("Open");
				timePicker.setPlaceholder("until what time?");
				timePicker.setTooltipText("The time you set will apply only for today");
				timePicker.setRequiredIndicatorVisible(true);
				timePicker.setStep(Duration.ofMinutes(30));
				timePicker.setI18n(new TimePickerI18n());
				timePicker.setClearButtonVisible(true);
				timePicker.setEnabled(true);
				timePicker.setInvalid(false);
				
				confirmBtn.setEnabled(true);
				
				buttonIsClosed[0] = false;
			} else {
				openAttendifyBtn.getStyle().setBackgroundColor("#877BAE");
				openAttendifyBtn.getStyle().setColor("Dark");
				openAttendifyBtn.setText("Open");
				timePicker.setEnabled(false);
				timePicker.setInvalid(false);
				timePicker.setValue(null);
				
				confirmBtn.setEnabled(false);				
				
				buttonIsClosed[0] = true;
			}
		});

		//timePicker.setEnabled(false);
		//timePicker.setPlaceholder("until what time?");
		
		HorizontalLayout content = new HorizontalLayout(openAttendifyBtn, timePicker);
		content.getStyle().setAlignItems(AlignItems.CENTER);
		content.getStyle().setPaddingTop("10%").setPaddingBottom("1%");
		return content;
	}

	private VerticalLayout contentLayoutWhenStatusIsOpen() {
		
		clock.getStyle()
		.set("color", "#ee4654")
		.set("font-weight", "bold")
		.set("margin-left", "5px")		
		.set("margin-right", "5px");

		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		UI ui = UI.getCurrent(); // capture UI before going async

		LocalTime attendanceEndTime = subjectEntity.getAttendanceEndTime();

		scheduler = Executors.newSingleThreadScheduledExecutor();
		
		scheduler.scheduleAtFixedRate(() -> {

			LocalTime now = LocalTime.now();
			Duration remaining = Duration.between(now, attendanceEndTime);

			if (!remaining.isNegative() && ui != null && ui.getSession() != null) {

				long hours = remaining.toHours();
				long minutes = remaining.toMinutes() % 60;
				long seconds = remaining.getSeconds() % 60;

				String countdownText = String.format("%02d:%02d:%02d", hours, minutes, seconds);

				ui.access(() -> clock.setText(countdownText)); // safely update UI
				
			} else {
				
				// Time's up
				ui.access(() -> clock.setText("00:00:00"));

				
				//subjectEntity.setStatus("Closed");
				//subjectEntity.setHasBeenDownloadedStudentAttendified(false);
				//subjectService.save(subjectEntity);
				System.out.println("Time is up! Reloading page...");
				ui.access(() -> {
					ui.getPage().reload();
//					invokeDialog();
//					r.run();
					
				});
				
				scheduler.shutdown(); // Stop updating
			}
		}, 0, 1, TimeUnit.SECONDS); // update every 1 second

		
		Span attendanceEndTimeToDisplay = new Span(subjectEntity.getAttendanceEndTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
		
		attendanceEndTimeToDisplay.getStyle()
		.set("color", "#05b888")
		.set("font-weight", "bold")
		.set("margin-left", "5px")
		.set("margin-right", "5px");
		
		Span clockLabel = new Span("Time left: ");
		Span closeOfTimeLabel = new Span("Ends at: "); 


		HorizontalLayout content = new HorizontalLayout(clockLabel, clock, closeOfTimeLabel, attendanceEndTimeToDisplay);
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

	private VerticalLayout contentLayoutWhenStatusIsOpenAndDownloadable() {
		
		Span label = new Span("Pending download of student attendance.");
		
		label.getStyle().setColor("#05b888");
		label.getStyle().setPaddingTop("15px");
		label.getStyle().setFontSize("20spx");
		
		VerticalLayout v = new VerticalLayout(label);
		v.setWidthFull();
		return v;
	}

	private HorizontalLayout footerLayoutWhenStatusIsClose() {

		confirmBtn.setEnabled(false);

		confirmBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		confirmBtn.getStyle()
		.set("border-radius", "10px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		confirmBtn.getElement().getThemeList().add("primary");
		confirmBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);	


		boolean[] timePickedIsValidToday = {false};

		timePicker.addValueChangeListener(event -> {

			LocalTime currentTime = LocalTime.now();
			LocalTime selectedTime = event.getValue();

			if (selectedTime != null && selectedTime.isAfter(currentTime)) {
				timePickedIsValidToday[0] = true;
				timePicker.getElement().getThemeList().add("valid-time");
				timePicker.getElement().getThemeList().remove("invalid-time");
			} else {
				timePickedIsValidToday[0] = false;
				timePicker.getElement().getThemeList().add("invalid-time");
				timePicker.getElement().getThemeList().remove("valid-time");
			}
		});


		confirmBtn.addClickListener(evt -> {

			if (timePickedIsValidToday[0] ) {

				subjectService.updateStatusAndAttedanceEndTimeById(IdOfSelectedSubject, "Open", timePicker.getValue());

				//  DateTimeFormatter formatted = DateTimeFormatter.ofPattern("HH:mm:ss a");
				UI.getCurrent().getPage().reload();

				r.run();
				this.close();

			} else {
				timePicker.setInvalid(true);
			}
		});

		final Button cancelBtn = new Button("Cancel", evt -> this.close());
		
		return new HorizontalLayout(confirmBtn, cancelBtn);
	}

	private HorizontalLayout footerLayoutWhenStatusIsOpen() {

		final Button viewBtn = new Button("View");
		final Button closeAndResetBtn = new Button("Reset and Close");
		final Button downloadBtn = new Button("Download");

		downloadBtn.getStyle()
		.set("font-size", "14px")
		.set("background-color", "#21a05d")
		.set("border-radius", "10px")
		.set("padding", "10px 10px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		downloadBtn.getElement().getThemeList().add("primary");
		downloadBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
				"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });");

		viewBtn.getStyle()
		.set("font-size", "14px")
		.set("background-color", "#21a05d")
		.set("border-radius", "10px")
		.set("padding", "10px 10px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		viewBtn.getElement().getThemeList().add("primary");
		viewBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);

		closeAndResetBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
		closeAndResetBtn.getStyle()
		.set("font-size", "14px")
		.set("border-radius", "10px")
		.set("padding", "10px 10px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		closeAndResetBtn.getElement().getThemeList().add("primary");
		closeAndResetBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);

		downloadBtn.addClickListener(event -> {

			DownloadStudentAttendified exporter = new DownloadStudentAttendified(IdOfSelectedSubject, subjectService);

			StreamResource excelResource = exporter.getExcelResource();

			Anchor downloadLink = new Anchor(excelResource, "");
			downloadLink.getElement().setAttribute("download", true); // Important!
			downloadLink.getStyle().set("display", "none"); // hide from view
			add(downloadLink); // add to layout

			downloadLink.getElement().executeJs("this.click();"); // Simulate automatic click
		});

		viewBtn.addClickListener(evt -> {

			UI.getCurrent().getSession().setAttribute("idOfSubjectEntity", subjectEntity.getId());
			
			String pathToLiveAttendance = String.format("window.open('student/attendified/live/subject/%s', '_blank')", subjectEntity.getSubjectCode());

			UI.getCurrent().getPage().executeJs(pathToLiveAttendance);


		});

		closeAndResetBtn.addClickListener(evt -> {
			confirmCloseAndResetDialog();
		});

		return new HorizontalLayout(viewBtn, downloadBtn, closeAndResetBtn);
	}

	private HorizontalLayout footerLayoutWhenStatusIsClosedAndDownloadable() {

		final Button viewBtn = new Button("View");
		final Button downloadBtn = new Button("Download");
		
		viewBtn.getStyle()
		.set("font-size", "14px")
		.set("background-color", "#21a05d")
		.set("border-radius", "10px")
		.set("padding", "10px 20px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		viewBtn.getElement().getThemeList().add("primary");
		viewBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);
		
		downloadBtn.getStyle()
		.set("font-size", "14px")
		.set("background-color", "#21a05d")
		.set("border-radius", "10px")
		.set("padding", "10px 20px")
		.set("box-shadow", "0 2px 8px rgba(0,0,0,0.2)")
		.set("transition", "transform 0.2s ease-in-out");
		downloadBtn.getElement().getThemeList().add("primary");
		downloadBtn.getElement().executeJs(
				"this.addEventListener('mouseover', function() { this.style.transform='scale(1.05)'; });" +
						"this.addEventListener('mouseout', function() { this.style.transform='scale(1.0)'; });"
				);

		viewBtn.addClickListener(view -> {

			UI.getCurrent().getSession().setAttribute("subjectEntity", subjectEntity);

			
			String pathToLiveAttendance = String.format("window.open('student/attendified/live/subject/%s', '_blank')", subjectEntity.getSubjectCode());
			
			UI.getCurrent().getPage().executeJs(pathToLiveAttendance);

		});

		downloadBtn.addClickListener(download -> {

			DownloadStudentAttendified exporter = new DownloadStudentAttendified(IdOfSelectedSubject, subjectService);
			StreamResource excelResource = exporter.getExcelResource();
			
			Anchor downloadLink = new Anchor(excelResource, "");
		    downloadLink.getElement().setAttribute("download", true); // Important!
		    downloadLink.getStyle().set("display", "none"); // hide from view
		    add(downloadLink); // add to layout
		   
		    downloadLink.getElement().executeJs("this.click();"); // Simulate automatic click
			
			subjectService.hasBeendownloadedStudentData(subjectEntity.getId());
			UI.getCurrent().getPage().reload();

		});

		return new HorizontalLayout(viewBtn, downloadBtn);
	}

	private void confirmCloseAndResetDialog() {

		ConfirmDialog dialog = new ConfirmDialog();

		dialog.setHeader("Reset & Close");
		dialog.setText("Are you sure you want to close and reset "
				+ "the ongoing attendance for subject CC05, section LFAU133A004?");

		dialog.setCancelable(true);
		dialog.addCancelListener(event -> dialog.close());

		dialog.setConfirmText("Confirm");
		dialog.setConfirmButtonTheme("error primary");
		dialog.addConfirmListener(event -> {

			subjectEntity.setStatus("Closed");
			
			subjectEntity.getStudentAttentifiedEntity().clear();
			subjectService.save(subjectEntity);
			
			subjectEntity = subjectService.getById(IdOfSelectedSubject).get();

			r.run();
			invokeDialog();
			dialog.close();
			

		});

		dialog.open();

	}

	@Override
	protected void onDetach(DetachEvent detachEvent) {
		super.onDetach(detachEvent);
		if (scheduler != null && !scheduler.isShutdown()) {
			scheduler.shutdownNow();
		}
	}



}
