package com.wellad.klink.business.model;

public class ProductCategoryBean {

	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public String getCategorysubid() {
		return categorysubid;
	}

	public void setCategorysubid(String categorysubid) {
		this.categorysubid = categorysubid;
	}

	public String getCategoryhasub() {
		return categoryhasub;
	}

	public void setCategoryhasub(String categoryhasub) {
		this.categoryhasub = categoryhasub;
	}

	private String categoryid;
	private String categoryname;
	private String categorysubid;
	private String categoryhasub;

}
