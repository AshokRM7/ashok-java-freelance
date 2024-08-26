package com.divproject.divAutomation;

public class student {
	
	private String id;
	private String name;
	private String course;
	
	
	public student(String id, String name, String course) {
		this.id = id;
		this.name = name;
		this.course = course;
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getCourse() {
		return course;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	@Override
	public String toString() {
		return "student [id=" + id + ", name=" + name + ", course=" + course + "]";
	}
	

	
}
