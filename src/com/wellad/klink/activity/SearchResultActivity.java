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
import com.wellad.klink.activity.adapter.SearchResultAdapter;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.model.SearchResult;

import android.app.Activity;
import android.content.Intent;
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
public class SearchResultActivity extends BaseActivity implements
		OnItemClickListener {
	private ListView listView;
	private SearchResultAdapter srAdapter;

	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, SearchResultActivity.class);
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
		topBar.getTitleTextView().setText(R.string.title_search);
		topBar.getMenuImageButton().setVisibility(View.GONE);
		initTopBarEvent(this);
		
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		initListView();
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
		ActivityUtils.doAsync(SearchResultActivity.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<ArrayList<SearchResult>>() {

					@Override
					public ArrayList<SearchResult> call() throws Exception {
						// TODO Auto-generated method stub
						return KLinkApplication.getInstance().getSrList();
					}

				}, new Callback<ArrayList<SearchResult>>() {

					@Override
					public void onCallback(
							ArrayList<SearchResult> pCallbackValue) {
						// TODO Auto-generated method stub
						if (srAdapter == null) {
							srAdapter = new SearchResultAdapter(SearchResultActivity.this);
						}
						srAdapter.setList(pCallbackValue);
						listView.setAdapter(srAdapter);
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
		SearchResult sr = ((SearchResultAdapter) arg0.getAdapter()).getList().get(arg2);
		if (sr != null) {
			if (sr.getResulttype().equals("goods")) { // product
				Log.i("product category", sr.getCategoryname());
				Log.i("product name", sr.getEngtitle());
				Config.searchresultbean = sr;
				ProductRootActivity.launch(this);
			}
			if (sr.getResulttype().equals("article")) {//
				String rootcategoryname = sr.getCategoryname();
				if (rootcategoryname != null && rootcategoryname.length() > 0) {
					Log.i("article category", rootcategoryname);
					Log.i("article name", sr.getEngtitle());

					String[] splitarray = rootcategoryname.split("\\|");
					String rootname = splitarray[0];
					Log.i("rootname", rootname);
					if (rootname.equals("About Us")) {
						Log.i("About Us", "About Us");
						Config.searchresultbean = sr;
						AboutUsActivity.launch(this, Config.US_ABOUT);
					}

					if (rootname.equals("Business Opportunity")) {
						Log.i("Business Opportunity", "Business Opportunity");
						Config.searchresultbean = sr;
						AboutUsActivity.launch(this, Config.US_BUSINESS);
					}

					if (rootname.equals("News&Events")) {
						Log.i("News&Events", "News&Events");
						Config.searchresultbean = sr;
						AboutUsActivity.launch(this, Config.US_NEWSEVENTS);
					}

					if (rootname.equals("DownLoad")) {
						Log.i("Download", "Download");
						Config.searchresultbean = sr;
						AboutUsActivity.launch(this, Config.US_DOWNLOAD);
					}

					if (rootname.equals("FAQ")) {
						Log.i("FAQ", "FAQ");
						Config.searchresultbean = sr;
						AboutUsActivity.launch(this, Config.US_CONTACT);
					}

					if (rootname.equals("Contact Us")) {
						Log.i("Contact Us", "Contact Us");
						Config.searchresultbean = sr;
						AboutUsActivity.launch(this, Config.US_CONTACT);
					}
				}
			}
		}
	}

}
