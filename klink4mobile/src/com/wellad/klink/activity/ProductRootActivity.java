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
import com.wellad.klink.activity.adapter.ProductRootAdapter;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.GeneralTools;
import com.wellad.klink.business.model.ProductCategoryBean;

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
public class ProductRootActivity extends BaseActivity implements OnItemClickListener {
	private ListView listView;
	private ProductRootAdapter productRootAdapter = new ProductRootAdapter(ProductRootActivity.this);
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, ProductRootActivity.class);
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
		
		topBar.getTitleTextView().setText(getResources().getString(R.string.title_product));
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		initListView();
		initTopBarEvent(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	/**
	 * 执行搜索结果
	 * 
	 * @param pCallbackValue
	 * @return
	 */
	private boolean doSearchResult(ArrayList<ProductCategoryBean> pCallbackValue) {
		if (Config.searchresultbean != null) {
			for (ProductCategoryBean bean : pCallbackValue) {
				if (bean.getCategoryname().equals(Config.searchresultbean.getCategoryname())
						|| Config.searchresultbean.getCategoryname().equals(bean.getCategoryname())) {
					Config.searchresultbean = null;
					ProductDetailActivity.launch(ProductRootActivity.this, bean.getCategoryname(), bean.getCategoryid(), true);
					return true;
				}
			}
		}
		Config.searchresultbean = null;
		return false;
	}
	
	/**
	 * 初始化数据
	 */
	private void initListView() {
		ActivityUtils.doAsync(ProductRootActivity.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<ArrayList<ProductCategoryBean>>() {

					@Override
					public ArrayList<ProductCategoryBean> call()
							throws Exception {
						// TODO Auto-generated method stub
						return GeneralTools.getCategory(0);
					}

				}, new Callback<ArrayList<ProductCategoryBean>>() {

					@Override
					public void onCallback(
							ArrayList<ProductCategoryBean> pCallbackValue) {
						// TODO Auto-generated method stub
						if (!doSearchResult(pCallbackValue)) {
							productRootAdapter.setList(pCallbackValue);
							listView.setAdapter(productRootAdapter);
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
		ProductCategoryBean bean = ((ProductRootAdapter) arg0.getAdapter()).getList().get(arg2);
		if (bean != null) {
			ProductDetailActivity.launch(ProductRootActivity.this, bean.getCategoryname(), bean.getCategoryid());
		}
		
	}
	
}
