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

import com.wellad.klink.KLinkApplication;
import com.wellad.klink.R;
import com.wellad.klink.activity.adapter.AroundAdapter;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.model.AroundMeLocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author Wang Yiming
 */
public class AroundMeLocationActivity extends BaseActivity implements OnItemClickListener {
	private ListView listView;
	private AroundAdapter aroundAdapter = new AroundAdapter(AroundMeLocationActivity.this);
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, AroundMeLocationActivity.class);
		a.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_activity);
		topBar = (TopBar) this.findViewById(R.id.topBar);
		topBar.getLogoImageView().setVisibility(View.GONE);
		topBar.getBackImageButton().setVisibility(View.VISIBLE);
		
		topBar.getTitleTextView().setText(R.string.title_around);
		
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		initTopBarEvent(this);
		initListView4Around();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	/**
	 * 初始化数据
	 */
	private void initListView4Around() {
		ActivityUtils.doAsync(AroundMeLocationActivity.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<ArrayList<AroundMeLocation>>() {

					@Override
					public ArrayList<AroundMeLocation> call() {
						// TODO Auto-generated method stub
						KLinkApplication.getInstance().initGPS();
						return Config.items1;
					}

				}, new Callback<ArrayList<AroundMeLocation>>() {

					@Override
					public void onCallback(
							ArrayList<AroundMeLocation> pCallbackValue) {
						// TODO Auto-generated method stub
						aroundAdapter.setList(pCallbackValue);
						listView.setAdapter(aroundAdapter);
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
		AroundMeLocation bean = ((AroundAdapter) arg0.getAdapter()).getList().get(arg2);
		if (bean != null) {
			AroundMeShopActivity.launch(this, bean.getLocationame());
		}
		
	}
	
}
