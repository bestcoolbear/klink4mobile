/*
 * @(#)DescAdapter.java 2012-8-21
 * 
 * Copyright 2006-2012 Roy Wang, All Rights reserved.
 */
package com.wellad.klink.activity.adapter;

import com.wellad.klink.R;
import com.wellad.klink.activity.api.Item;
import com.wellad.klink.business.model.ProductBean;

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
public class ProductDetailAdapter extends ArrayListAdapter<ProductBean> {

	public ProductDetailAdapter(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		final ProductBean bean = this.mList.get(position);
		if (convertView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			convertView = inflater.inflate(R.layout.item_adapter, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.textView);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.textView.setText(bean.getProductname());
		
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
