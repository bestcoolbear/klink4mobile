/*
 * @(#)DescAdapter.java 2012-8-21
 * 
 * Copyright 2006-2012 Roy Wang, All Rights reserved.
 */
package com.wellad.klink.activity.adapter;

import com.wellad.klink.R;
import com.wellad.klink.activity.api.Item;
import com.wellad.klink.business.model.ProductCategoryBean;
import com.wellad.klink.business.model.SubCateBean;

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
public class SubCateBeanAdapter extends ArrayListAdapter<SubCateBean> {

	public SubCateBeanAdapter(Activity context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		final SubCateBean bean = this.mList.get(position);
		if (convertView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			convertView = inflater.inflate(R.layout.item_adapter, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.textView);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(bean.getSubcatname().contains("#")){
			holder.textView.setText(bean.getSubcatname().split("#")[0]);
		}else{
			holder.textView.setText(bean.getSubcatname());
		}
		
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
