/*
 * @(#)WelcomeActivity.java 2014年1月31日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity;

import java.util.ArrayList;

import com.wellad.klink.R;
import com.wellad.klink.activity.adapter.TagAdapter;
import com.wellad.klink.activity.api.Tag;
import com.wellad.klink.activity.gps.Global;
import com.wellad.klink.activity.gps.ServiceManage;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.service.MessageServicesSwang;
import com.wellad.klink.service.PollingUtils;
import com.wellad.klink.util.FileUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.GridView;
import com.wellad.klink.KLinkApplication;

/**
 * 
 * @author Wang Yiming
 */
public class HomeActivity extends BaseActivity implements OnItemClickListener {
	
	/**
	 * 事件枚举 
	 * 
	 * @author Roy
	 *
	 */
	public enum ContentIDs {
		about_us(1), 
		products(2), 
		business_opportunity(3), 
		news_events(4), 
		download(5), 
		contact_us(6), 
		stokist_login(7), 
		around_me(8), 
		language(9);

		private int value;

		ContentIDs(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static ContentIDs getContentIDs(int value) {
			for (ContentIDs id : ContentIDs.values()) {
				if (id.getValue() == value) {
					return id;
				}
			}
			return about_us;
		}
	}
	
	/** 
	 * 主菜单图标 
	 */
	public static final int[] HOME_RESOURCEIDS = {
		R.drawable.about_us, 
		R.drawable.products,
		R.drawable.business_opportunity, 
		R.drawable.news_events, 
		R.drawable.download, 
		R.drawable.contact_us, 
		R.drawable.stokist_login,
		R.drawable.around_me, 
		R.drawable.language };
	
	/** 
	 * 主菜单图标 
	 */
	public static final int[] HOME_GALLERYIDS = {
		R.drawable.slideshow01, 
		R.drawable.slideshow02,
		R.drawable.slideshow03, 
		R.drawable.slideshow04 };

	private Gallery gallery;
	private GridView homeGridView;
	private TagAdapter galleryTagAdapter;
	private TagAdapter homeTagAdapter;
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, HomeActivity.class);
		a.startActivity(intent);
		a.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.home_activity);
		topBar = (TopBar) this.findViewById(R.id.topBar);
		topBar.getBackImageButton().setVisibility(View.GONE);
		topBar.getLogoImageView().setVisibility(View.VISIBLE);
		topBar.getTitleTextView().setText(R.string.title_home);
		
		gallery = (Gallery) this.findViewById(R.id.gallery);
		homeGridView = (GridView) this.findViewById(R.id.homeGridView);
		gallery.setOnItemClickListener(this);
		homeGridView.setOnItemClickListener(this);
		initTopBarEvent(this);
		// init sdcard
		FileUtil.creatDir("KLinkAPP");
		// start service
		PollingUtils.stopPollingService(this, MessageServicesSwang.class, Global.ACTION2);
        PollingUtils.startPollingService(this, 5, MessageServicesSwang.class, Global.ACTION2); 
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		((KLinkApplication) getApplication()).setUiactivty(this);
		super.onResume();
		initHomeGallery();
		initHomeGridView();
	}
	
	private void initHomeGallery() {
		// TODO Auto-generated method stub
		ArrayList<Tag> array = new ArrayList<Tag>();
		Tag tag = null;
		
		tag = new Tag();
		tag.setResourceId(HOME_GALLERYIDS[0]);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_GALLERYIDS[1]);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_GALLERYIDS[2]);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_GALLERYIDS[3]);
		array.add(tag);
		
		galleryTagAdapter = new TagAdapter(HomeActivity.this);
		galleryTagAdapter.setList(array);
		gallery.setAdapter(galleryTagAdapter);
	}

	/**
	 * 初始化菜单
	 */
	private void initHomeGridView() {
		ArrayList<Tag> array = new ArrayList<Tag>();
		Tag tag = null;
		
		tag = new Tag();
		tag.setResourceId(HOME_RESOURCEIDS[0]);
		tag.setId(ContentIDs.about_us);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_RESOURCEIDS[1]);
		tag.setId(ContentIDs.products);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_RESOURCEIDS[2]);
		tag.setId(ContentIDs.business_opportunity);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_RESOURCEIDS[3]);
		tag.setId(ContentIDs.news_events);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_RESOURCEIDS[4]);
		tag.setId(ContentIDs.download);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_RESOURCEIDS[5]);
		tag.setId(ContentIDs.contact_us);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_RESOURCEIDS[6]);
		tag.setId(ContentIDs.stokist_login);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_RESOURCEIDS[7]);
		tag.setId(ContentIDs.around_me);
		array.add(tag);
		
		tag = new Tag();
		tag.setResourceId(HOME_RESOURCEIDS[8]);
		tag.setId(ContentIDs.language);
		array.add(tag);
		
		homeTagAdapter = new TagAdapter(HomeActivity.this);
		homeTagAdapter.setList(array);
		homeGridView.setAdapter(homeTagAdapter);
	}
	
	

	/**
	 * stokist login
	 */
	public void stokistLogin() {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse("https://www.k-linkonline.com/member/index.jsp");
		intent.setData(content_url);
		startActivity(intent);
	}
	private String getTopActivity()
	{
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		return cn.getClassName();
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg0.getId() == homeGridView.getId()) {
			Tag tag = ((TagAdapter) arg0.getAdapter()).getList().get(arg2);
			if (tag != null) {
				switch (tag.getId()) {
				case about_us:
					AboutUsActivity.launch(HomeActivity.this, Config.US_ABOUT);
					break;
				case products:
					ProductRootActivity.launch(HomeActivity.this);
					break;
				case business_opportunity:
					AboutUsActivity.launch(HomeActivity.this, Config.US_BUSINESS);
					break;
				case news_events:
					AboutUsActivity.launch(HomeActivity.this, Config.US_NEWSEVENTS);
					break;
				case download:
					AboutUsActivity.launch(HomeActivity.this, Config.US_DOWNLOAD);
					break;
				case contact_us:
					AboutUsActivity.launch(HomeActivity.this, Config.US_CONTACT);
					break;
				case stokist_login:
					stokistLogin();
					break;
				case around_me:
				//	AroundMeLocationActivity.launch(HomeActivity.this);
					if(getTopActivity().contains("AroundMeShopActivity") || getTopActivity().contains("AroundMeShopActivity"))
					{
						return;
					}
					ServiceManage.startService("ON", HomeActivity.this);
					break;
				case language:
					WelcomeActivity.launch(HomeActivity.this);
					break;
				default:
					break;
				}
				//ListActivity.launch(HomeActivity.this);
			}
		} else if (arg0.getId() == gallery.getId()) {
			Tag tag = ((TagAdapter) arg0.getAdapter()).getList().get(arg2);
			if (tag != null) {
				
			}
		}
		
	}
	
}
