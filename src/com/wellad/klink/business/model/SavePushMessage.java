package com.wellad.klink.business.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SavePushMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<PushMessage> getMessagelist() {
		return messagelist;
	}

	public void setMessagelist(ArrayList<PushMessage> messagelist) {
		this.messagelist = messagelist;
	}

	private ArrayList<PushMessage> messagelist;

}
