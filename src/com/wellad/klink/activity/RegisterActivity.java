/*
 * @(#)WelcomeActivity.java 2014年1月31日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.andengine.util.ActivityUtils;
import org.andengine.util.call.Callable;
import org.andengine.util.call.Callback;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.wellad.klink.R;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.GeneralTools;
import com.wellad.klink.business.model.SubCateBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * 
 * @author Wang Yiming
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	private ImageButton submit;

	private EditText usernameedittext;
	private EditText passwordedittext;
	private EditText nameedittext;
	private EditText emaledittext;
	private EditText birthedittext;
	private EditText genderedittext;
	private EditText addressedittext;
	private EditText codeedittext;
	
	private String username;
	private String password;
	private String name;
	private String email;
	private String birth;
	private String gender;
	private String address;
	private String code;

	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, RegisterActivity.class);
		a.startActivity(intent);
		a.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.register_activity);
		topBar = (TopBar) this.findViewById(R.id.topBar);
		topBar.getLogoImageView().setVisibility(View.GONE);
		topBar.getTitleTextView().setText(R.string.title_register);
		
		submit = (ImageButton) this.findViewById(R.id.submit);
		submit.setOnClickListener(this);

		usernameedittext = (EditText) this.findViewById(R.id.usernameedittext);
		passwordedittext = (EditText) this.findViewById(R.id.passwordedittext);
		nameedittext = (EditText) this.findViewById(R.id.nameedittext);
		emaledittext = (EditText) this.findViewById(R.id.emaledittext);
		birthedittext = (EditText) this.findViewById(R.id.birthedittext);
		genderedittext = (EditText) this.findViewById(R.id.genderedittext);
		addressedittext = (EditText) this.findViewById(R.id.addressedittext);
		codeedittext = (EditText) this.findViewById(R.id.codeedittext);
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
		  username = usernameedittext.getText().toString();
		  password = passwordedittext.getText().toString();
		  name = nameedittext.getText().toString();
		  email = emaledittext.getText().toString();
		  birth = birthedittext.getText().toString();
		  gender = genderedittext.getText().toString();
		  address = addressedittext.getText().toString();
		  code = codeedittext.getText().toString();
		  
		  if (Config.CoverStringNull(username)) {
			  GeneralTools.showAlertDialog(getResources().getString(R.string.alert_username), this);
			  return false;
		  }
		  
		  if (Config.CoverStringNull(password)) {
			  GeneralTools.showAlertDialog(getResources().getString(R.string.alert_password), this);
			  return false;
		  }
		  
		  if (Config.CoverStringNull(name)) {
			  GeneralTools.showAlertDialog(getResources().getString(R.string.alert_name), this);
			  return false;
		  }
		  
		  if (Config.CoverStringNull(email)) {
			  GeneralTools.showAlertDialog(getResources().getString(R.string.alert_email), this);
			  return false;
		  }  else if (!Config.isEmail(email)){
			  GeneralTools.showAlertDialog(getResources().getString(R.string.alert_email_invalid), this);
			  return false;
		  }
		  
		  if (Config.CoverStringNull(birth)) {
			  GeneralTools.showAlertDialog(getResources().getString(R.string.alert_brith), this);
			  return false;
		  }
		  
		  if (Config.CoverStringNull(gender)) {
			  gender = getResources().getString(R.string.gender_male);
		  }
		  
		  if (Config.CoverStringNull(address)) {
			  GeneralTools.showAlertDialog(getResources().getString(R.string.alert_address), this);
			  return false;
		  }
		  
		  if (Config.CoverStringNull(code)) {
			  code = "00000000";
		  }
		  
		  return true;
	}
	
	/**
	 * 注册
	 */
	private void registering() {
		ActivityUtils.doAsync(RegisterActivity.this,
				R.string.ptitle_resource_id, R.string.alert_register_ing,
				new Callable<String>() {

					@Override
					public String call() {
						// TODO Auto-generated method stub
						if (checking()) {
							return validateLocalRegister();
						} else {
							return "checking out";
						}
					}

				}, new Callback<String>() {

					@Override
					public void onCallback(String pCallbackValue) {
						// TODO Auto-generated method stub
						if (!Config.CoverStringNull(pCallbackValue)) {
							if (pCallbackValue.equals("0")) {
								LoginActivity.launch(RegisterActivity.this, username.trim(), password.trim());
							} else if (pCallbackValue.equals("2")) {
								GeneralTools.showAlertDialog(getResources().getString(R.string.alert_register_failed_4), RegisterActivity.this);
							} else if (pCallbackValue.equals("1")) {
								GeneralTools.showAlertDialog(getResources().getString(R.string.alert_register_failed_3), RegisterActivity.this);
							}
						} else {
							GeneralTools.showAlertDialog(getResources().getString(R.string.alert_register_failed_3), RegisterActivity.this);
						}
					}

				}, new Callback<Exception>() {

					@Override
					public void onCallback(Exception pCallbackValue) {
						// TODO Auto-generated method stub
						
					}

				}, true);
	}
	
	private String validateLocalRegister() {
		String strRegisterResult = null;
		String requesturl = Config.USER_REG 
				+ "&username=" + username.trim()
				+ "&password=" + password.trim() 
				+ "&name=" + name.trim()
				+ "&email=" + email.trim() 
				+ "&birth=" + birth.trim()
				+ "&gender=" + gender.trim() 
				+ "&address=" + address.trim()
				+ "&memcode=" + code.trim();
		Log.i("regurl = ", requesturl);
		HttpGet httpRequest = new HttpGet(requesturl);
		try {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strRegisterResult = EntityUtils.toString(httpResponse
						.getEntity());
				Log.i("reg starResult", strRegisterResult);
				return strRegisterResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			strRegisterResult = null;
		}
		return strRegisterResult;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == submit.getId()) {
			registering();
		}
	}

}
