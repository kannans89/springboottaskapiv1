package com.ksonline.training.dto;

public class TaskDto {

	private long id;
	private int categoryId;
	private String status;
	private String title;
	private String description;
	private String priority;
	private String categoryName;
	private int subTaskCount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public int getSubTaskCount() {
		return subTaskCount;
	}

	public void setSubTaskCount(int subTaskCount) {
		this.subTaskCount = subTaskCount;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}


}
