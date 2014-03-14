/*
 * @(#)WelcomeActivity.java 2014年1月31日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andengine.util.ActivityUtils;
import org.andengine.util.call.Callable;
import org.andengine.util.call.Callback;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.wellad.klink.KLinkApplication;
import com.wellad.klink.R;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.GeneralTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Wang Yiming
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private EditText usernameEditText;
	private EditText passwordEditText;
	private TextView loginTextView;
	private TextView registerTextView;
	
	private String username;
	private String password;
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, LoginActivity.class);
		a.startActivity(intent);
		a.finish();
	}
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a, String username, String password) {
		Intent intent = new Intent(a, LoginActivity.class);
		intent.putExtra("username", username);
		intent.putExtra("password", password);
		a.startActivity(intent);
		a.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login_activity);
		topBar = (TopBar) this.findViewById(R.id.topBar);
		topBar.getBackImageButton().setVisibility(View.GONE);
		topBar.getLogoImageView().setVisibility(View.VISIBLE);
		topBar.getTitleTextView().setText(R.string.title_login);
		
		usernameEditText = (EditText) this.findViewById(R.id.lusernameedittext);
		passwordEditText = (EditText) this.findViewById(R.id.lpasswordedittext);
		loginTextView = (TextView) this.findViewById(R.id.loginTextView);
		registerTextView = (TextView) this.findViewById(R.id.registerTextView);
		
		loginTextView.setOnClickListener(this);
		registerTextView.setOnClickListener(this);
		
		// 系统存储的
		String saveUserName = KLinkApplication.getStringFromSharedPreferences(KLinkApplication.SHARED_PREFS_ACCOUNT_USERNAME);
		String savePassword = KLinkApplication.getStringFromSharedPreferences(KLinkApplication.SHARED_PREFS_ACCOUNT_PASSWORD);
		
		if (!Config.CoverStringNull(saveUserName)) {
			usernameEditText.setText(saveUserName);
		}
		
		if (!Config.CoverStringNull(savePassword)) {
			passwordEditText.setText(savePassword);
		}
		
		// 注册跳转的
		String extraUserName = getIntent().getStringExtra("username");
		String extraPassword = getIntent().getStringExtra("password");
		if (!Config.CoverStringNull(extraUserName)) {
			usernameEditText.setText(extraUserName);
		}
		
		if (!Config.CoverStringNull(extraPassword)) {
			passwordEditText.setText(extraPassword);
		}
		initTopBarEvent(this);
		
		//for test
		//loging();
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
		  username = usernameEditText.getText().toString();
		  password = passwordEditText.getText().toString();
		  
		  if (Config.CoverStringNull(username)) {
			  GeneralTools.showAlertDialog(getResources().getString(R.string.alert_username), this);
			  return false;
		  }
		  
		  if (Config.CoverStringNull(password)) {
			  GeneralTools.showAlertDialog(getResources().getString(R.string.alert_password), this);
			  return false;
		  }
		  
		  return true;
	}
	
	/**
	 * 注册
	 */
	private void loging() {
		ActivityUtils.doAsync(LoginActivity.this,
				R.string.ptitle_resource_id, R.string.alert_login_ing,
				new Callable<String>() {

					@Override
					public String call() {
						// TODO Auto-generated method stub
						if (checking()) {
							return validateLocalLogin();
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
								KLinkApplication.writeStringToSharedPreferences(KLinkApplication.SHARED_PREFS_ACCOUNT_USERNAME, username);
								KLinkApplication.writeStringToSharedPreferences(KLinkApplication.SHARED_PREFS_ACCOUNT_PASSWORD, password);
								// success
								String userid = pCallbackValue.split(",")[1];
								Config.USERID = userid;
								HomeActivity.launch(LoginActivity.this);
							} else if (pCallbackValue.equals("1")) {
								// reg							
								// failed
								GeneralTools.showAlertDialog(getResources().getString(R.string.alert_register_failed_6), LoginActivity.this);
							}
						} else {
							GeneralTools.showAlertDialog(getResources().getString(R.string.alert_register_failed_3), LoginActivity.this);
						}
					}

				}, new Callback<Exception>() {

					@Override
					public void onCallback(Exception pCallbackValue) {
						// TODO Auto-generated method stub
						GeneralTools.showAlertDialog(getResources().getString(R.string.alert_register_failed_3), LoginActivity.this);
					}

				}, true);
	}
	
	
	private String validateLocalLogin() {
		String strLoginResult = null;
		String requesturl = Config.USER_LOGIN + "&username=" + username.trim() + "&password=" + username.trim();
		Log.i("loginurl = ", requesturl);
		HttpGet httpRequest = new HttpGet(requesturl);
		HttpResponse httpResponse = null;
		try {
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strLoginResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			strLoginResult = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			strLoginResult = null;
		}

		return strLoginResult;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == loginTextView.getId()) {
			loging();
		} else if (v.getId() == registerTextView.getId()) {
			RegisterActivity.launch(LoginActivity.this);
		}
	}

}
