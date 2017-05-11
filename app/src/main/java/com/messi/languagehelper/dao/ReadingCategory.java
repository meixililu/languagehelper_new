package com.messi.languagehelper.dao;

public class ReadingCategory {

	private String name;
	private String category;

	public ReadingCategory(String name,String category){
		this.name = name;
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}