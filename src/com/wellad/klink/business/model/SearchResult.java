package com.wellad.klink.business.model;

public class SearchResult {

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public String getHtmlurl() {
		return htmlurl;
	}

	public void setHtmlurl(String htmlurl) {
		this.htmlurl = htmlurl;
	}

	public String getEngtitle() {
		return engtitle;
	}

	public void setEngtitle(String engtitle) {
		this.engtitle = engtitle;
	}

	public String getMalatitle() {
		return malatitle;
	}

	public void setMalatitle(String malatitle) {
		this.malatitle = malatitle;
	}

	public String getResulttype() {
		return resulttype;
	}

	public void setResulttype(String resulttype) {
		this.resulttype = resulttype;
	}

	private String title;//
	private String engtitle;//
	private String malatitle;//
	private String resulttype;//
	private String categoryname;//
	private String htmlurl;
}
