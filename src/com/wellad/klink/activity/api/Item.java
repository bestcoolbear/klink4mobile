/*
 * @(#)Item.java 2014年2月15日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity.api;

/**
 * 
 * @author Wang Yiming
 */
public class Item {
	private long key_id;
	private String text;
	private String url;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getKey_id() {
		return key_id;
	}

	public void setKey_id(long key_id) {
		this.key_id = key_id;
	}

}
