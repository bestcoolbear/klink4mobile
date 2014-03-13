package com.wellad.klink.business.model;

import java.io.Serializable;

/*
 * 
 * {"status":1,"error":"","data":[{"title":"KLINK TESTE","description":"KLINK TEST MESSAGE DESC","content":"KLINK TEST MESSAGE DESC CONTENT!
 ","msg_id":"22","uid":"39","user_name":"haixin"}]}
 */
public class PushMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	private String status;
	private String title;
	private String description;
	private String content;
	private String datetime;

}
