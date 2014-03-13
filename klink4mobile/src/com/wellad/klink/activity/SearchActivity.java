/*
 * @(#)WelcomeActivity.java 2014年1月31日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity;

import org.andengine.util.ActivityUtils;
import org.andengine.util.call.Callable;
import org.andengine.util.call.Callback;

import com.wellad.klink.KLinkApplication;
import com.wellad.klink.R;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.GeneralTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Wang Yiming
 */
public class SearchActivity extends BaseActivity implements OnClickListener {
	private EditText searchEdittext;
	private TextView goTextView;
	
	private String content;
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, SearchActivity.class);
		a.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.search_activity);
		topBar = (TopBar) this.findViewById(R.id.topBar);
		topBar.getBackImageButton().setVisibility(View.VISIBLE);
		topBar.getLogoImageView().setVisibility(View.GONE);
		topBar.getMenuImageButton().setVisibility(View.GONE);
		topBar.getTitleTextView().setText(R.string.title_search);
		
		searchEdittext = (EditText) this.findViewById(R.id.searchedittext);
		goTextView = (TextView) this.findViewById(R.id.goTextView);
		goTextView.setOnClickListener(this);
		
		initTopBarEvent(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	/**
	 * 检测输入正确性
	 * 
	 * @return
	 */
	private boolean checking() {
		content = searchEdittext.getText().toString();
		if (Config.CoverStringNull(content)) {
			GeneralTools.showAlertDialog(getResources().getString(R.string.alert_nosearhkeywards), this);
			return false;
		}
		return true;
	}
	
	/**
	 * 注册
	 */
	private void doSearch() {
		ActivityUtils.doAsync(SearchActivity.this,
				R.string.ptitle_resource_id, R.string.alert_login_ing,
				new Callable<String>() {

					@Override
					public String call() {
						// TODO Auto-generated method stub
						if (checking()) {
							KLinkApplication.getInstance().setSrList(GeneralTools.getSearchResult(content));
							return String.valueOf(KLinkApplication.getInstance().getSrList().size());
						} else {
							return "checking out";
						}
					}

				}, new Callback<String>() {

					@Override
					public void onCallback(String pCallbackValue) {
						// TODO Auto-generated method stub
						if (!Config.CoverStringNull(pCallbackValue)) {
							if (pCallbackValue.contains("0")) {
								// success
								GeneralTools.showAlertDialog(getResources().getString(R.string.alert_nosearchresult), SearchActivity.this);
							} else {
								SearchResultActivity.launch(SearchActivity.this);
							}
						} else {
							GeneralTools.showAlertDialog(getResources().getString(R.string.alert_nosearhkeywards), SearchActivity.this);
						}
					}

				}, new Callback<Exception>() {

					@Override
					public void onCallback(Exception pCallbackValue) {
						// TODO Auto-generated method stub
						GeneralTools.showAlertDialog(getResources().getString(R.string.alert_nosearhkeywards), SearchActivity.this);
					}

				}, true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == goTextView.getId()) {
			doSearch();
		}
	}

}
