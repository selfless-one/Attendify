package com.myproject.teacher.ui.view.dashboard.subjectDataPage.attendifiedStudent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.myproject.backend.teacher.entity.StudentAttentifiedEntity;
import com.myproject.backend.teacher.entity.SubjectEntity;
import com.myproject.backend.teacher.service.SubjectService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.server.StreamResource;

public class DownloadStudentAttendified {
	
	
	private final SubjectService subjectService;

	private final SubjectEntity subjectEntity;
	
	private final List<StudentAttentifiedEntity> studentAttentifiedEntity;
	
	private final Grid<StudentAttentifiedEntity> grid = new Grid<>(StudentAttentifiedEntity.class);
	
	private final String filename;
	
	private final StreamResource excelResource;
	
	public DownloadStudentAttendified(Integer subjectID, SubjectService subjectService) {
		
		this.subjectService = subjectService;
		subjectEntity = subjectService.getById(subjectID).get();	
		this.studentAttentifiedEntity = subjectEntity.getStudentAttentifiedEntity();
		
		filename = subjectEntity.getSection().getSectionName() + ", " + subjectEntity.getSubjectCode();
		
		grid.setItems(studentAttentifiedEntity);
		
		this.excelResource = new StreamResource(filename + ".xlsx", () -> {
			
			try {
				return new ByteArrayInputStream(createExcel(studentAttentifiedEntity));
			} catch (Exception e) {
				e.printStackTrace();
                return null;
			}
			
		});
	}
	
	public StreamResource getExcelResource() {
		return excelResource;
	}
	
	private byte[] createExcel(List<StudentAttentifiedEntity> data) throws IOException {
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet(filename);
		
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue("Student Number");
		header.createCell(1).setCellValue("Surname");
		header.createCell(2).setCellValue("First Name");
		header.createCell(3).setCellValue("Course");
		header.createCell(4).setCellValue("Email");
		
		
		
		for (int i = 0; i < data.size(); i++) {
			Row row = sheet.createRow(i + 1);
			
			StudentAttentifiedEntity s = data.get(i);
			row.createCell(0).setCellValue(s.getStudentNumber());
			row.createCell(1).setCellValue(s.getSurname());
			row.createCell(2).setCellValue(s.getFirstname());
			row.createCell(3).setCellValue(s.getCourse());
			row.createCell(4).setCellValue(s.getEmail());
			
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		workbook.write(out);
		workbook.close();
		
		return out.toByteArray();
	}
	

}
