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
import com.wellad.klink.activity.adapter.PushMessageAdapter;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.model.PushMessage;
import com.wellad.klink.business.model.SavePushMessage;

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
public class InboxActivity extends BaseActivity implements OnItemClickListener {
	private ListView listView;
	private PushMessageAdapter pmAdapter;
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, InboxActivity.class);
		a.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.inbox_activity);
		topBar = (TopBar) this.findViewById(R.id.topBar);
		topBar.getLogoImageView().setVisibility(View.GONE);
		topBar.getBackImageButton().setVisibility(View.VISIBLE);
		topBar.getMenuImageButton().setVisibility(View.GONE);
		topBar.getTitleTextView().setText(R.string.title_inbox);
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
		ActivityUtils.doAsync(InboxActivity.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<ArrayList<PushMessage>>() {

					@Override
					public ArrayList<PushMessage> call() {
						// TODO Auto-generated method stub
						PerUtil pu = new PerUtil(InboxActivity.this);
						SavePushMessage getsp = pu.getObjectInfo();
						if (getsp != null) {
							return getsp.getMessagelist();
						}
						return null;
					}

				}, new Callback<ArrayList<PushMessage>>() {

					@Override
					public void onCallback(ArrayList<PushMessage> pCallbackValue) {
						// TODO Auto-generated method stub
						if (pmAdapter == null) {
							pmAdapter = new PushMessageAdapter(InboxActivity.this);
						}
						pmAdapter.setList(pCallbackValue);
						listView.setAdapter(pmAdapter);
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
		PushMessage bean = ((PushMessageAdapter) arg0.getAdapter()).getList().get(arg2);
		if (bean != null) {
			String url = Config.SERVERMESSAGE + bean.getContent();
			WebActivity.launch(this, Config.US_INBOX, bean.getTitle(), url, false);
		}
	}
	
}
