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
	public void launch(Activity a, int usType, String subcatname, String subcatid) {
		launch(a, usType, subcatname, subcatid, false);
	}
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public void launch(Activity a, int usType, String subcatname, String subcatid, boolean finish) {
		Intent intent = new Intent(a, AboutUsDetailActivity.class);
		intent.putExtra("usType", usType);
		intent.putExtra("subcatname", subcatname);
		intent.putExtra("subcatid", subcatid);
		a.startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
							//ArticleActivity.launch(AboutUsDetailActivity.this, usType, subcatname, subcatid, true);
							subCateBean2WebView();
						} else {
							Log.i("sub list value size ==== ", pCallbackValue.size() + "");
							if(usType == Config.US_BUSINESS){//单独处理
									SubCateBean sb = pCallbackValue.get(0);
									if(sb.getSubcatname().contains("Plan")){
										Config.BUSINESS_OPP_LIST.clear();
										Config.BUSINESS_OPP_LIST = pCallbackValue;
									}
									
									listView.setVisibility(View.INVISIBLE);
									launch(AboutUsDetailActivity.this, usType, sb.getSubcatname(), sb.getSubcatid(), true);

							}else if(usType == Config.US_DOWNLOAD){
								SubCateBean sb = pCallbackValue.get(0);
								if(sb.getSubcatname().contains("english riddance")){
									Config.BUSINESS_OPP_LIST.clear();
									Config.BUSINESS_OPP_LIST = pCallbackValue;
								}
								
								listView.setVisibility(View.INVISIBLE);
								launch(AboutUsDetailActivity.this, usType, sb.getSubcatname(), sb.getSubcatid(), true);
								
							}

							else{
								Config.BUSINESS_OPP_LIST.clear();
								listView.setVisibility(View.VISIBLE);
								subCateBeanAdapter.setList(pCallbackValue);
								listView.setAdapter(subCateBeanAdapter);
								//
							}
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
	private void subCateBean2WebView() {
		ActivityUtils.doAsync(AboutUsDetailActivity.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<ArrayList<ArticleBean>>() {

					@Override
					public ArrayList<ArticleBean> call()
							throws Exception {
						// TODO Auto-generated method stub
						return GeneralTools.getArticleList(subcatid);
					}

				}, new Callback<ArrayList<ArticleBean>>() {

					@Override
					public void onCallback(
							ArrayList<ArticleBean> pCallbackValue) {
						// TODO Auto-generated method stub
						if (pCallbackValue != null && pCallbackValue.size() == 1) {
							String htmlurl = getUrl(pCallbackValue.get(0).getArticleid());
							WebActivity.launch(AboutUsDetailActivity.this, usType, subcatname, htmlurl, true);
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
	 * 获得 Article Url
	 * @param id
	 * @return
	 */
	private String getUrl(String id) {
		return Config.LOADDETAILURL + id + "&lang=" + Config.APP_USER_LANGUAGE;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		SubCateBean bean = ((SubCateBeanAdapter) arg0.getAdapter()).getList().get(arg2);
		if (bean != null) {
			if (bean.getSubcatname().contains("#") && (usType == Config.US_CONTACT)) {
				String url = bean.getSubcatname().split("#")[1];
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(url);
				intent.setData(content_url);
				startActivity(intent);
			} else {
				launch(AboutUsDetailActivity.this, usType, bean.getSubcatname(), bean.getSubcatid(), true);
			}
		}
 }
	
}
