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
import com.wellad.klink.activity.adapter.ArticleBeanAdapter;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.GeneralTools;
import com.wellad.klink.business.model.ArticleBean;

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
public class ArticleActivity extends BaseActivity implements OnItemClickListener {
	private int usType;
	private String subcatname;
	private String subcatid;
	
	private ListView listView;
	private ArticleBeanAdapter articleBeanAdapter = new ArticleBeanAdapter(ArticleActivity.this);
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a, int usType, String subcatname, String subcatid, boolean finish) {
		Intent intent = new Intent(a, ArticleActivity.class);
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
		ActivityUtils.doAsync(ArticleActivity.this,
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
							WebActivity.launch(ArticleActivity.this, usType, subcatname, htmlurl, true);
						} else {
							articleBeanAdapter.setList(pCallbackValue);
							listView.setAdapter(articleBeanAdapter);
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
		ArticleBean bean = ((ArticleBeanAdapter) arg0.getAdapter()).getList().get(arg2);
		if (bean != null) {
			String htmlurl = getUrl(bean.getArticleid());
			WebActivity.launch(ArticleActivity.this, usType, bean.getArticletitle(), htmlurl, true);
		}
		
	}
	
}
