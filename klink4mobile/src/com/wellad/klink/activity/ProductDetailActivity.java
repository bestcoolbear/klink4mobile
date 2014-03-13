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
import com.wellad.klink.activity.adapter.ProductDetailAdapter;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.GeneralTools;
import com.wellad.klink.business.model.ProductBean;

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
public class ProductDetailActivity extends BaseActivity implements OnItemClickListener {
	private String categoryTitle;
	private String categoryid;
	
	private ListView listView;
	private ProductDetailAdapter productDetailAdapter = new ProductDetailAdapter(ProductDetailActivity.this);
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a, String categoryTitle, String categoryid) {
		launch(a, categoryTitle, categoryid, false);
	}
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a, String categoryTitle, String categoryid, boolean finish) {
		Intent intent = new Intent(a, ProductDetailActivity.class);
		intent.putExtra("categoryTitle", categoryTitle);
		intent.putExtra("categoryid", categoryid);
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
		
		categoryTitle = getIntent().getStringExtra("categoryTitle");
		categoryid = getIntent().getStringExtra("categoryid");
		
		topBar.getTitleTextView().setText(categoryTitle);
		initListView(categoryid);
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
	private void initListView(final String categoryid) {
		final int catid = Integer.parseInt(categoryid);
		
		ActivityUtils.doAsync(ProductDetailActivity.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<ArrayList<ProductBean>>() {

					@Override
					public ArrayList<ProductBean> call()
							throws Exception {
						// TODO Auto-generated method stub
						return GeneralTools.getProductList(catid);
					}

				}, new Callback<ArrayList<ProductBean>>() {

					@Override
					public void onCallback(
							ArrayList<ProductBean> pCallbackValue) {
						// TODO Auto-generated method stub
						productDetailAdapter.setList(pCallbackValue);
						listView.setAdapter(productDetailAdapter);
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
		ProductBean bean = ((ProductDetailAdapter) arg0.getAdapter()).getList().get(arg2);
		if (bean != null) {
			String firsturl = bean.getProducthtml();
    		firsturl = Config.SERVERUPR + firsturl + "&lang=" + Config.APP_USER_LANGUAGE;
    		String htmlurl = firsturl;
			WebActivity.launch(ProductDetailActivity.this, Config.US_PRODUCT, bean.getProductname(), htmlurl, false);
		}
		
	}
	
}
