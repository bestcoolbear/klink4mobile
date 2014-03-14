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
import com.wellad.klink.business.model.ArticleBean;
import com.wellad.klink.business.model.SubCateBean;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author Wang Yiming
 */
public class AboutUsActivity extends BaseActivity implements OnItemClickListener {
	private int usType;
	private ListView listView;
	private SubCateBeanAdapter subCateBeanAdapter = new SubCateBeanAdapter(AboutUsActivity.this);
	private int currentlist;
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a, int usType) {
		Intent intent = new Intent(a, AboutUsActivity.class);
		intent.putExtra("usType", usType);
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
		initTopBarEvent(this);
		
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		// 分别不同类型
		usType = getIntent().getIntExtra("usType", Config.US_ABOUT);
		switch (usType) {
		case Config.US_ABOUT:
			currentlist = 0;
			topBar.getTitleTextView().setText(getResources().getString(R.string.title_about));
			initListView4About();
			break;
		case Config.US_CONTACT:
			currentlist = 1;
			topBar.getTitleTextView().setText(getResources().getString(R.string.title_contact));
			initListView4Contact();
			break;
		case Config.US_NEWSEVENTS:
			currentlist = 2;
			topBar.getTitleTextView().setText(getResources().getString(R.string.title_news));
			initListView4NewsEvents();
			break;
		case Config.US_BUSINESS:
			currentlist = 3;
			topBar.getTitleTextView().setText(getResources().getString(R.string.title_business));
			initListView4Business();
			break;
		case Config.US_DOWNLOAD:
			currentlist = 4;
			topBar.getTitleTextView().setText(R.string.title_download);
			initListView4Download();
			break;	
		}
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
	private boolean doSearchResult(ArrayList<SubCateBean> pCallbackValue) {
		if (Config.searchresultbean != null) {
			String categoryName = Config.searchresultbean.getCategoryname().split("\\|")[1];
			for (SubCateBean bean : pCallbackValue) {
				if (bean.getSubcatname().equals(categoryName)) {
					Config.searchresultbean = null;
					String title = null;
					if (Config.APP_USER_LANGUAGE.equals("cn")) {
						title = Config.searchresultbean.getTitle();
					} else if (Config.APP_USER_LANGUAGE.equals("en")) {
						title = Config.searchresultbean.getEngtitle();
					} else if (Config.APP_USER_LANGUAGE.equals("mala")) {
						title = Config.searchresultbean.getMalatitle();
					}
					WebActivity.launch(AboutUsActivity.this, usType, title, Config.searchresultbean.getHtmlurl(), true);
					Config.searchresultbean = null;
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
	private void initListView4About() {
		ActivityUtils.doAsync(AboutUsActivity.this,
				R.string.ptitle_resource_id, R.string.pmessage_resource_id,
				new Callable<ArrayList<SubCateBean>>() {

					@Override
					public ArrayList<SubCateBean> call()  
							throws Exception {
						// TODO Auto-generated method stub
						return GeneralTools.getAboutUsSubCateList();
					}

				}, new Callback<ArrayList<SubCateBean>>() {

					@Override
					public void onCallback(
							ArrayList<SubCateBean> pCallbackValue) {
						// TODO Auto-generated method stub
						if (!doSearchResult(pCallbackValue)) {
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
	
	/**
	 * 初始化数据
	 */
	private void initListView4Contact() {
		ActivityUtils.doAsync(AboutUsActivity.this,
				R.string.ptitle_resource_id, R.string.pmessage_resource_id,
				new Callable<ArrayList<SubCateBean>>() {

					@Override
					public ArrayList<SubCateBean> call() {
						// TODO Auto-generated method stub
						return GeneralTools.getContactUS();
					}

				}, new Callback<ArrayList<SubCateBean>>() {

					@Override
					public void onCallback(ArrayList<SubCateBean> pCallbackValue) {
						// TODO Auto-generated method stub
						if (!doSearchResult(pCallbackValue)) {
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
	
	/**
	 * 初始化数据
	 */
	private void initListView4NewsEvents() {
		ActivityUtils.doAsync(AboutUsActivity.this,
				R.string.ptitle_resource_id, R.string.pmessage_resource_id,
				new Callable<ArrayList<SubCateBean>>() {

					@Override
					public ArrayList<SubCateBean> call() {
						// TODO Auto-generated method stub
						return GeneralTools.getNewsAndEventFirstList();
					}

				}, new Callback<ArrayList<SubCateBean>>() {

					@Override
					public void onCallback(ArrayList<SubCateBean> pCallbackValue) {
						// TODO Auto-generated method stub
						if (!doSearchResult(pCallbackValue)) {
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
	
	/**
	 * 初始化数据
	 */
	private void initListView4Download() {
		ActivityUtils.doAsync(AboutUsActivity.this,
				R.string.ptitle_resource_id, R.string.pmessage_resource_id,
				new Callable<ArrayList<SubCateBean>>() {

					@Override
					public ArrayList<SubCateBean> call() {
						// TODO Auto-generated method stub
						return GeneralTools.getDownloadSubCateList();
					}

				}, new Callback<ArrayList<SubCateBean>>() {

					@Override
					public void onCallback(ArrayList<SubCateBean> pCallbackValue) {
						// TODO Auto-generated method stub
						if (!doSearchResult(pCallbackValue)) {
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
	
	/**
	 * 初始化数据
	 */
	private void initListView4Business() {
		ActivityUtils.doAsync(AboutUsActivity.this,
				R.string.ptitle_resource_id, R.string.pmessage_resource_id,
				new Callable<ArrayList<SubCateBean>>() {

					@Override
					public ArrayList<SubCateBean> call() {
						// TODO Auto-generated method stub
						return GeneralTools.getBusinessOpportunity();
					}

				}, new Callback<ArrayList<SubCateBean>>() {

					@Override
					public void onCallback(ArrayList<SubCateBean> pCallbackValue) {
						// TODO Auto-generated method stub
						if (!doSearchResult(pCallbackValue)) {
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
		Log.i("current list ==",currentlist+"");
		
		SubCateBean bean = ((SubCateBeanAdapter) arg0.getAdapter()).getList().get(arg2);
	//	AboutUsDetailActivity.launch(AboutUsActivity.this, usType, bean.getSubcatname(), bean.getSubcatid());
		
		Intent intent = new Intent(AboutUsActivity.this, AboutUsDetailActivity.class);
		intent.putExtra("usType", usType);
		intent.putExtra("subcatname", bean.getSubcatname());
		intent.putExtra("subcatid", bean.getSubcatid());
		AboutUsActivity.this.startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	//	if (AboutUsActivity.this) {
			AboutUsActivity.this.finish();
	//	}
	}
	
}
