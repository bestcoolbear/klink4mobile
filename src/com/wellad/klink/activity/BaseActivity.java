/*
 * @(#)BaseActivity.java 2014年3月3日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity;

import com.wellad.klink.activity.ui.widget.TopBar;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

/**
 * 
 * @author Wang Yiming
 */
public class BaseActivity extends Activity {
	protected TopBar topBar;
	WebView webView;

	protected void initTopBarEvent(final Activity activity) {
		topBar.getFavoriteButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ListActivity.launch(activity);
			}
		});
		
		topBar.getInboxButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InboxActivity.launch(activity);
			}
			
		});
		
		topBar.getSearchButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SearchActivity.launch(activity);
			}
			
		});
		
		topBar.getBackImageButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(webView != null){
					webView.getSettings().setBuiltInZoomControls(false);
					webView.destroy();
				}
				activity.finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(webView != null){
				webView.getSettings().setBuiltInZoomControls(false);
				webView.destroy();
			//	webView = null;
			}
			if (topBar != null) {
				if (topBar.isDisplayMenu()) {
					topBar.closeMenu();
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}
