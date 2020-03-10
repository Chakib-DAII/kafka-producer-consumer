package com.kafkaexample.kafkaproducerconsumer.models;

public class AnotherSimpleModel {

	private String title;
	private String description;
	
	public AnotherSimpleModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AnotherSimpleModel(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "title = " + title + ", description = " + description + "\n";
	}
	
	
}
