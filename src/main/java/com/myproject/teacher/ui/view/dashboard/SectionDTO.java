package com.myproject.teacher.ui.view.dashboard;

public class SectionDTO {
	
	 private String sectionName;
     private String course;
     private String dateAdded;

     public SectionDTO(String sectionName, String course, String dateAdded) {
         this.sectionName = sectionName;
         this.course = course;
         this.dateAdded = dateAdded;
     }

     public String getSectionName() {
         return sectionName;
     }
     
     public String getCourse() {
     	return course;
     }

     public String getDateAdded() {
         return dateAdded;
     }
}
