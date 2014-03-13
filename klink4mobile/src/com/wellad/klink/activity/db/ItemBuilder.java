/*
 * @(#)LBAccountBuilder.java 2012-3-13
 *
 * Copyright 2006-2012 Roy Wang, All Rights reserved.
 */
package com.wellad.klink.activity.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.wellad.klink.activity.api.Item;

/**
 * 
 * @author Roy
 */
public class ItemBuilder extends DatabaseBuilder<Item> {
	
	public ItemBuilder() {
		
	}

	@Override
	public Item build(Cursor c) {
		// TODO Auto-generated method stub
		int columnKeyId = c.getColumnIndex("key_id");
		int columnUrl = c.getColumnIndex("url");
		int columnText = c.getColumnIndex("text");

		long key_id = c.getLong(columnKeyId);
		String url = c.getString(columnUrl);
		String text = c.getString(columnText);
		
		Item item = new Item();
		item.setKey_id(key_id);
		item.setText(text);
		item.setUrl(url);
		return item;
	}

	@Override
	public ContentValues deconstruct(Item t) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put("text", t.getText());
		values.put("url", t.getUrl());
		return values;
	}

}
