/*
 * @(#)Tag.java 2014年2月24日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity.api;

import com.wellad.klink.activity.HomeActivity.ContentIDs;

/**
 * 
 * @author Wang Yiming
 */
public class Tag {
	private int resourceId;
	private ContentIDs id;

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public ContentIDs getId() {
		return id;
	}

	public void setId(ContentIDs id) {
		this.id = id;
	}

}
