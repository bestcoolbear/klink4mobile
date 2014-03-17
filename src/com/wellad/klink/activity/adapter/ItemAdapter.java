/*
 * @(#)DescAdapter.java 2012-8-21
 * 
 * Copyright 2006-2012 Roy Wang, All Rights reserved.
 */
package com.wellad.klink.activity.adapter;

import com.wellad.klink.R;
import com.wellad.klink.activity.api.Item;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Roy Wang
 */
public class ItemAdapter extends ArrayListAdapter<Item> {

	public ItemAdapter(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		final Item item = this.mList.get(position);
		if (convertView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			convertView = inflater.inflate(R.layout.item_adapter, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.textView);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String flaglist = item.getText();
		
		flaglist = flaglist.replaceAll("_en", "");
		flaglist = flaglist.replaceAll("_cn", "");
		flaglist = flaglist.replaceAll("_mala", "");
		flaglist = flaglist.replaceAll(".html", "");
		holder.textView.setText(flaglist);
		
		return convertView;
	}
	
	/**
	 * 
	 * @author Roy
	 * 
	 */
	static class ViewHolder {
		TextView textView;
	}

}
