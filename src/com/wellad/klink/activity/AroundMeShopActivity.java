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
import com.wellad.klink.activity.adapter.AroundSubAdapter;
import com.wellad.klink.activity.adapter.ProductDetailAdapter;
import com.wellad.klink.activity.adapter.SubCateBeanAdapter;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.GeneralTools;
import com.wellad.klink.business.model.AroundMeShop;
import com.wellad.klink.business.model.ProductBean;
import com.wellad.klink.business.model.SubCateBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author Wang Yiming
 */
public class AroundMeShopActivity extends BaseActivity implements OnItemClickListener {
	private String locationame;
	
	private ListView listView;
	private AroundSubAdapter aroundSubAdapter = new AroundSubAdapter(AroundMeShopActivity.this);
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a, String locationame) {
		Intent intent = new Intent(a, AroundMeShopActivity.class);
		intent.putExtra("locationame", locationame);
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
		
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		locationame = getIntent().getStringExtra("locationame");
		
		topBar.getTitleTextView().setText(locationame);
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
		ActivityUtils.doAsync(AroundMeShopActivity.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<ArrayList<AroundMeShop>>() {

					@Override
					public ArrayList<AroundMeShop> call()
							throws Exception {
						// TODO Auto-generated method stub
						ArrayList<AroundMeShop> array = new ArrayList<AroundMeShop>();
						for (int i = 0; i < Config.items2.size(); i++) {
							if (Config.items2.get(i).getShoplocationame().equals(locationame)) {
								array.add(Config.items2.get(i));
							}
						}
						return array;
					}

				}, new Callback<ArrayList<AroundMeShop>>() {

					@Override
					public void onCallback(
							ArrayList<AroundMeShop> pCallbackValue) {
						// TODO Auto-generated method stub
						aroundSubAdapter.setList(pCallbackValue);
						listView.setAdapter(aroundSubAdapter);
						
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
		AroundMeShop bean = ((AroundSubAdapter) arg0.getAdapter()).getList().get(arg2);
		if (bean != null) {
			String url = "file:///android_asset/" + bean.getShopname() + ".html";
			WebActivity.launch(this, Config.US_AROUNDME, locationame, url, false);
		}
		
	}
	
}
