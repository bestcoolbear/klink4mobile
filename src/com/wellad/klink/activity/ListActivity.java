/*
 * @(#)WelcomeActivity.java 2014年1月31日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.andengine.util.ActivityUtils;
import org.andengine.util.call.Callable;
import org.andengine.util.call.Callback;
import com.wellad.klink.R;
import com.wellad.klink.activity.adapter.ItemAdapter;
import com.wellad.klink.activity.api.Item;
import com.wellad.klink.activity.db.ExpandDatabaseImpl;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.util.FileUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author Wang Yiming
 */
public class ListActivity extends BaseActivity implements OnItemClickListener {
	private ListView listView;
	private ItemAdapter itemAdapter;
	private List<String> items1 = new ArrayList<String>();

	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, ListActivity.class);
		a.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_activity);
		inradioAD = -1;
		topBar = (TopBar) this.findViewById(R.id.topBar);
		topBar.getLogoImageView().setVisibility(View.GONE);
		topBar.getBackImageButton().setVisibility(View.VISIBLE);
		topBar.getTitleTextView().setText(R.string.title_favorite);
		topBar.getMenuImageButton().setVisibility(View.GONE);
		initTopBarEvent(this);
		
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		items1 = FileUtil.loadFavoriteRoot();

		
		if (itemAdapter == null) {
			itemAdapter = new ItemAdapter(ListActivity.this);
		}
		ArrayList<Item> adapterlist = new ArrayList<Item>();
		for(int i = 0; i < items1.size(); i ++){
			String str = items1.get(i);
			Item it = new Item();
			it.setText(str);
			adapterlist.add(it);
		}
		itemAdapter.setList(adapterlist);

		listView.setAdapter(itemAdapter);
	//	initListView();
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
		ActivityUtils.doAsync(ListActivity.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<ArrayList<Item>>() {

					@Override
					public ArrayList<Item> call()
							throws Exception {
						// TODO Auto-generated method stub
						ExpandDatabaseImpl edi = new ExpandDatabaseImpl(ListActivity.this);
						ArrayList<Item> job = edi.getAllItem();
						edi.finalize();
						return job;
					}

				}, new Callback<ArrayList<Item>>() {

					@Override
					public void onCallback(ArrayList<Item> pCallbackValue) {
						// TODO Auto-generated method stub
						if (itemAdapter == null) {
							itemAdapter = new ItemAdapter(ListActivity.this);
						}
						itemAdapter.setList(pCallbackValue);
						listView.setAdapter(itemAdapter);
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
		Item item = ((ItemAdapter) arg0.getAdapter()).getList().get(arg2);
		if (item != null) {
		//	WebActivity.launch(this, Config.US_DOWNLOAD_RECORD, item.getText(), item.getUrl(), false);
			
			Config.catid =	item.getText();
			Intent intent  = new Intent();
			intent.setClass(ListActivity.this, ListsubFavorite.class);
			startActivity(intent);
		}
	}
	
}
