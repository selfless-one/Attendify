package com.myproject.teacher.ui.view.dashboard;

public class Section {
	
	 private String sectionName;
     private String course;
     private String dateAdded;

     public Section(String sectionName, String course, String dateAdded) {
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
