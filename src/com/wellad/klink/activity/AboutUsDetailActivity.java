/*
 * @(#)WelcomeActivity.java 2014年1月31日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity;

import java.util.ArrayList;

import org.andengine.util.ActivityUtils;
import org.andengine.util.call.Callable;
import org.andengine.util.call.Callback;

import com.wellad.klink.R;
import com.wellad.klink.activity.adapter.SubCateBeanAdapter;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.GeneralTools;
import com.wellad.klink.business.model.SubCateBean;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author Wang Yiming
 */
public class AboutUsDetailActivity extends BaseActivity implements OnItemClickListener {
	private int usType;
	private String subcatname;
	private String subcatid;
	
	private ListView listView;
	private SubCateBeanAdapter subCateBeanAdapter = new SubCateBeanAdapter(AboutUsDetailActivity.this);
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a, int usType, String subcatname, String subcatid) {
		launch(a, usType, subcatname, subcatid, false);
	}
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a, int usType, String subcatname, String subcatid, boolean finish) {
		Intent intent = new Intent(a, AboutUsDetailActivity.class);
		intent.putExtra("usType", usType);
		intent.putExtra("subcatname", subcatname);
		intent.putExtra("subcatid", subcatid);
		a.startActivity(intent);
		if (finish) {
			a.finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_activity);
		topBar = (TopBar) this.findViewById(R.id.topBar);
		topBar.getLogoImageView().setVisibility(View.GONE);
		topBar.getBackImageButton().setVisibility(View.VISIBLE);
		
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		usType = getIntent().getIntExtra("usType", Config.US_ABOUT);
		subcatname = getIntent().getStringExtra("subcatname");
		subcatid = getIntent().getStringExtra("subcatid");
		
		topBar.getTitleTextView().setText(subcatname);
		initListView();
		
		initTopBarEvent(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	/**
	 * 初始化数据
	 */
	private void initListView() {
		ActivityUtils.doAsync(AboutUsDetailActivity.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<ArrayList<SubCateBean>>() {

					@Override
					public ArrayList<SubCateBean> call()
							throws Exception {
						// TODO Auto-generated method stub
						return GeneralTools.getAboutUsSubCateList(subcatid);
					}

				}, new Callback<ArrayList<SubCateBean>>() {

					@Override
					public void onCallback(
							ArrayList<SubCateBean> pCallbackValue) {
						// TODO Auto-generated method stub
						if (pCallbackValue == null || pCallbackValue.size() == 0) {
							ArticleActivity.launch(AboutUsDetailActivity.this, usType, subcatname, subcatid, true);
						} else {
							subCateBeanAdapter.setList(pCallbackValue);
							listView.setAdapter(subCateBeanAdapter);
						}
						
					}

				}, new Callback<Exception>() {

					@Override
					public void onCallback(Exception pCallbackValue) {
						// TODO Auto-generated method stub
						
					}

				}, true);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		SubCateBean bean = ((SubCateBeanAdapter) arg0.getAdapter()).getList().get(arg2);
		if (bean != null) {
				if(bean.getSubcatname().contains("#") && (usType == Config.US_CONTACT)){
					String url = bean.getSubcatname().split("#")[1];
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri content_url = Uri.parse(url);
					intent.setData(content_url);
					startActivity(intent);
				}else{
					AboutUsDetailActivity.launch(AboutUsDetailActivity.this, usType, bean.getSubcatname(), bean.getSubcatid());
				}
		}
 }
	
}
