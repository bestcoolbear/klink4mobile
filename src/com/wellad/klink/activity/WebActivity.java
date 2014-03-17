/*
 * @(#)WelcomeActivity.java 2014年1月31日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.andengine.util.ActivityUtils;
import org.andengine.util.call.Callable;
import org.andengine.util.call.Callback;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.wellad.klink.R;
import com.wellad.klink.activity.api.Item;
import com.wellad.klink.activity.db.ExpandDatabaseImpl;
import com.wellad.klink.activity.ui.widget.TopBar;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.GeneralTools;
import com.wellad.klink.business.model.ArticleBean;
import com.wellad.klink.util.FileUtil;
import com.wellad.klink.util.HttpDownloader;
import com.wellad.player.test_videoplayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * 
 * @author Wang Yiming
 */
public class WebActivity extends BaseActivity {
	private int usType;
	private String url;
	private String text;
	
	private ProgressDialog proDialog;
	boolean loadFinished;

	Vector<String> imagevector = new Vector<String>();
	Vector<String> videovector = new Vector<String>();
	Vector<String> urlvector = new Vector<String>();
	String videourl = "http://www.youtube.com/embed";
	String htmlurl = "";
	String htmlstring = "";
	  String productname = "";

	ImageButton leftimage;
	ImageButton rightimage;
	int currentsubindex = 0;

	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a, int usType, String title, String url, boolean finish) {
		Intent intent = new Intent(a, WebActivity.class);
		intent.putExtra("usType", usType);
		intent.putExtra("title", title);
		intent.putExtra("url", url);
		a.startActivity(intent);

		if (finish) {
			a.finish();
		}
	}
	
	 class getHtmlHandler implements Runnable {
		  public void run() {
			  htmlstring = "";
			  htmlstring = GeneralTools.getPageHtmlString(htmlurl);
				if(htmlstring != null && htmlstring.length() > 0)
				{
					sendMsg(10);
				}else{
					sendMsg(11);
				}
			}
	  }
	
	private void add2FavoriteAndSave2Sdcard(String subcatename)
	{
		if(loadFinished == true)
		{
			Log.i("add to favorite","add to favorite");
			String sdroot = FileUtil.getSdRoot();
			Log.i("sd root",sdroot);
			if(sdroot != null && sdroot.length() > 0)
			{
				FileUtil.creatDir("KLinkAPP/" + subcatename);
				if(htmlurl != null && htmlurl.length() > 0)
				{
					proDialog = ProgressDialog.show(WebActivity.this, "Saving...","Please Wait....", true, true);
					Thread getHtmlThread = new Thread(new getHtmlHandler());
					getHtmlThread.start();
				}
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.web_activity);
		// CONTENT
		usType = getIntent().getIntExtra("usType", Config.US_ABOUT);
		url = getIntent().getStringExtra("url");
		text = getIntent().getStringExtra("title");
		
		// TOP BAR
		topBar = (TopBar) this.findViewById(R.id.topBar);
		topBar.getLogoImageView().setVisibility(View.GONE);
		topBar.getBackImageButton().setVisibility(View.VISIBLE);
		topBar.getTitleTextView().setText(text);
		productname = text + "_" + Config.APP_USER_LANGUAGE;

		initTopBarEvent(this);
		topBar.getFavoriteButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 String folder = "";
	   		       	switch (usType) {
	        		case Config.US_ABOUT: // about us 入口
	        			folder = "About Us";
	        			break;
	        		case Config.US_CONTACT: // contact us 入口
	        			folder = "Contact";

	        			break; 
	        		case Config.US_NEWSEVENTS: // news and evnets 入口
	        			folder = "News";

	        			break; 
	        		case Config.US_BUSINESS: // business 入口
	        			folder = "Business";

	        			break; 
	        		case Config.US_DOWNLOAD_RECORD: // 从下载记录中 入口

	        			break; 
	        		case Config.US_AROUNDME: // around me
	        			break;
	        		case Config.US_DOWNLOAD: // download 入口
	        			folder = "Download";

	        		case Config.US_PRODUCT: // product 入口
	        			folder = "Product";

	        			break;
	        		case Config.US_INBOX: // push message 入口
	        			break;
	        		}
				add2FavoriteAndSave2Sdcard(folder);
				//add2Favorite();
			}
			
		});
		
		leftimage = (ImageButton) this.findViewById(R.id.imageButtonLeft);
		rightimage = (ImageButton) this.findViewById(R.id.imageButtonRight);

		leftimage.setVisibility(View.INVISIBLE);
		rightimage.setVisibility(View.INVISIBLE);
		
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		Log.i("screen width = ", width + "");
		Log.i("screen height = ", height + "");


		// WEBVIEW
		webView = (WebView) this.findViewById(R.id.webView);
		 WebSettings webSettings = webView.getSettings();
		webView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
		webView.getSettings().setJavaScriptEnabled(true); // 设置支持Javascript
		webView.getSettings().setDomStorageEnabled(true);
		if(width > 480 && height > 800){
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(true);
		}
		webView.requestFocus();// 触摸焦点起作用
		//webView.setInitialScale(110);//
		webView.loadUrl(url);
		htmlurl = url;
		DisplayMetrics metrics = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(metrics);
		  int mDensity = metrics.densityDpi;
		  if (mDensity == 240) {
		   webSettings.setDefaultZoom(ZoomDensity.FAR);
		  } else if (mDensity == 160) {
		     webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
		  } else if(mDensity == 120) {
		   webSettings.setDefaultZoom(ZoomDensity.CLOSE);
		  }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
		   webSettings.setDefaultZoom(ZoomDensity.FAR);
		  }else if (mDensity == DisplayMetrics.DENSITY_TV){
		   webSettings.setDefaultZoom(ZoomDensity.FAR);
		  }
		
		proDialog = ProgressDialog.show(this, getResources().getString(R.string.webview_load_title), getResources().getString(R.string.webview_load_wait));
		
		loadFinished = false;
		Log.i("url webview load ===",url);

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				loadFinished = true;
				if (proDialog != null) {
					proDialog.dismiss();
				}
			}

			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				loadFinished = true;
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Log.i("usType ==", usType+"");
				switch (usType) {
				case Config.US_ABOUT: // about us 入口
					break;
				case Config.US_AROUNDME: // around me
				case Config.US_CONTACT: // contact us 入口
					if(url.contains("Phone") || url.contains("电话") || url.contains("Tel") || url.contains("tel") || url.contains("phone")){
            			int index = url.lastIndexOf(":");
            			if(index > 0){
            				String phonenumber = url.substring(index);
            				Log.i(phonenumber,phonenumber);
            				
            				Intent phoneIntent = new Intent("android.intent.action.CALL",
            				Uri.parse("tel:" + phonenumber));
            				startActivity(phoneIntent);
            			}
            		}
					if(url.contains("maps.google.com") || url.contains("youtube.com") || url.contains("facebook.com") || url.contains("k-link.com")){
        			 	Intent intent= new Intent();       
        			    intent.setAction("android.intent.action.VIEW");   
        			    Uri content_url = Uri.parse(url);  
        			    intent.setData(content_url); 
        			    startActivity(intent);
					}
					return true;
				case Config.US_NEWSEVENTS: // news and evnets 入口
					break; 
				case Config.US_BUSINESS: // business 入口
					
					break;
				case Config.US_DOWNLOAD_RECORD: // 从下载记录中 入口
					break; 
				case Config.US_DOWNLOAD: // download 入口
					if(url.contains("youtube.com") || url.contains("youtu")){
	            		Log.i("play video",url);
	            		if(url != null && url.length() > 0)
	            		{
		                    Config.VIDEO_FROM = true;
		                	String[] splistes = url.split("#");
                			String myurl = splistes[0];
                			Config.VIDEO_URL = splistes[0];
                			Config.VIDEO_Name =  splistes[1];
                			Log.i("vidoe name",Config.VIDEO_Name);
                			int index = myurl.lastIndexOf("/");
                			String videoid = myurl.substring(index);
                			videourl = videourl + videoid;
                			Log.i("video url",videourl);

                	        YourAsyncTask  mTask = new YourAsyncTask();
                	        mTask.execute();
	            	        return true;
	            		}
					}
					break;
				case Config.US_PRODUCT: // product 入口
					
					if(url.contains("youtube.com") || url.contains("youtu")){
	            		Log.i("play video",url);
	            		if(url != null && url.length() > 0)
	            		{
	      	               Config.VIDEO_FROM = false;
	            			int index = url.lastIndexOf("/");
	            			String videoid = url.substring(index);
	            			videourl = videourl + videoid;
	            			Log.i("video url",videourl);
	            	        YourAsyncTask  mTask = new YourAsyncTask();
	            	        mTask.execute();
	            	        return true;
	            		}
	        		}else{
	        			view.loadUrl(url);
	        			return true;
	        		}
					break;
				case Config.US_INBOX: // push message 入口
					break;
				}
				
			
				return super.shouldOverrideUrlLoading(view, url);
			}
			
		}) ;
		
		
		
		
		// TODO 老顾，几个入口的地方都标示出来了
				switch (usType) {
				case Config.US_ABOUT: // about us 入口
					break;
				case Config.US_CONTACT: // contact us 入口
					break; 
				case Config.US_NEWSEVENTS: // news and evnets 入口
					break; 
				case Config.US_BUSINESS: // business 入口
					if(text.contains("Plan") ){
						leftimage.setVisibility(View.VISIBLE);
						rightimage.setVisibility(View.VISIBLE);
						currentsubindex = 0;
					}
					break;
				case Config.US_DOWNLOAD: // download 入口
					if(text.contains("english riddance")){
						leftimage.setVisibility(View.VISIBLE);
						rightimage.setVisibility(View.VISIBLE);
						currentsubindex = 0;
					}
					break; 
				case Config.US_DOWNLOAD_RECORD: // 从下载记录中 入口
					filterDoUrl();
					break; 
				case Config.US_AROUNDME: // around me
					break;
				case Config.US_PRODUCT: // product 入口
					break;
				case Config.US_INBOX: // push message 入口
					break;
				}
		
		
	}
	
	
	private void filterDoUrl()
	{
		webView.setWebChromeClient(new WebChromeClient() {
			
		});
		
		webView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                	if(proDialog != null){
                		proDialog.dismiss();
                	}
                        // TODO Auto-generated method stub
                        proDialog.dismiss();
                        loadFinished = true;
                        super.onPageFinished(view, url);

                }
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // TODO Auto-generated method stub
                        //锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟?锟斤拷锟斤拷锟解部锟斤拷转
                	if(proDialog != null){
                		proDialog.dismiss();
                	}
                		Log.i("content url",url);
                		if(url.contains("youtube.com") || url.contains("youtu")){
                			//锟斤拷锟叫诧拷锟斤拷锟斤拷
                    		Log.i("play video",url);
                    		if(url != null && url.length() > 0)
                    		{

                    			int index = url.lastIndexOf("/");
                    			String videoid = url.substring(index);
                    			videourl = "file://" + FileUtil.getSdRoot() + "/KLinkAPP/" + Config.catid  + videoid + ".3gp";
                    			Log.i("video local",videourl);
                     			
                    			 Config.YOUTUBEURL = videourl;
                         		
                    			 Intent intent = new Intent(Intent.ACTION_VIEW);
                    		     String type = "video/mp4";
                    		     Uri uri = Uri.parse(Config.YOUTUBEURL);
                    		     intent.setDataAndType(uri, type);
                    		     startActivity(intent);   
                    		}
                		}else{
                			
                			
                			view.loadUrl(url);
                		}
                        return true;
                }
                 
		});
	}
	
	private String getUrl(String id) {
		return Config.LOADDETAILURL + id + "&lang=" + Config.APP_USER_LANGUAGE;
	}
	
	public void leftClick(View view) {
		if(usType == Config.US_BUSINESS || usType == Config.US_DOWNLOAD){
			if(Config.BUSINESS_OPP_LIST != null && Config.BUSINESS_OPP_LIST.size() > 0){
				if(currentsubindex == 0){
					  Toast.makeText(getApplicationContext(), "No more content,at first page!",
							     Toast.LENGTH_SHORT).show();
				  }else{
					   currentsubindex = currentsubindex -1;
						ActivityUtils.doAsync(WebActivity.this,
			    				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
			    				new Callable<ArrayList<ArticleBean>>() {

			    					@Override
			    					public ArrayList<ArticleBean> call()
			    							throws Exception {
			    						// TODO Auto-generated method stub
			    						return GeneralTools.getArticleList(Config.BUSINESS_OPP_LIST.get(currentsubindex).getSubcatid());
			    					}

			    				}, new Callback<ArrayList<ArticleBean>>() {

			    					@Override
			    					public void onCallback(
			    							ArrayList<ArticleBean> pCallbackValue) {
			    						// TODO Auto-generated method stub
			    						if (pCallbackValue != null && pCallbackValue.size() == 1) {
			    							String htmlurl = getUrl(pCallbackValue.get(0).getArticleid());
			    							//WebActivity.launch(ArticleActivity.this, usType, subcatname, htmlurl, true);
			    							topBar.getTitleTextView().setText(pCallbackValue.get(0).getArticletitle());
			    							webView.loadUrl(htmlurl);
			    						
			    				    		Log.i("url ===",htmlurl);
			    						}
			    					}

			    				}, new Callback<Exception>() {
			    					@Override
			    					public void onCallback(Exception pCallbackValue) {
			    						// TODO Auto-generated method stub
			    					}

			    				}, true);
				  }
			}
		}
	}
	
	public void rightClick(View view) {
		if(usType == Config.US_BUSINESS || usType == Config.US_DOWNLOAD){
			if(Config.BUSINESS_OPP_LIST != null && Config.BUSINESS_OPP_LIST.size() > 0){
				if(currentsubindex < Config.BUSINESS_OPP_LIST.size() - 1){
					
					currentsubindex = currentsubindex + 1;
					ActivityUtils.doAsync(WebActivity.this,
		    				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
		    				new Callable<ArrayList<ArticleBean>>() {

		    					@Override
		    					public ArrayList<ArticleBean> call()
		    							throws Exception {
		    						// TODO Auto-generated method stub
		    						return GeneralTools.getArticleList(Config.BUSINESS_OPP_LIST.get(currentsubindex).getSubcatid());
		    					}

		    				}, new Callback<ArrayList<ArticleBean>>() {

		    					@Override
		    					public void onCallback(
		    							ArrayList<ArticleBean> pCallbackValue) {
		    						// TODO Auto-generated method stub
		    						if (pCallbackValue != null && pCallbackValue.size() == 1) {
		    							String htmlurl = getUrl(pCallbackValue.get(0).getArticleid());
		    							//WebActivity.launch(ArticleActivity.this, usType, subcatname, htmlurl, true);
		    							topBar.getTitleTextView().setText(pCallbackValue.get(0).getArticletitle());
		    							webView.loadUrl(htmlurl);
		    				    		Log.i("url ===",htmlurl);
		    						}
		    					}

		    				}, new Callback<Exception>() {
		    					@Override
		    					public void onCallback(Exception pCallbackValue) {
		    						// TODO Auto-generated method stub
		    					}

		    				}, true);
					
				  }else{
					  Toast.makeText(getApplicationContext(), "No more content,at last page!",
							     Toast.LENGTH_SHORT).show();
				  }
			}
		}
	}
	
	/**
	 * 初始化数据
	 */
	private void add2Favorite() {
		ActivityUtils.doAsync(WebActivity.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<Boolean>() {

					@Override
					public Boolean call() {
						// TODO Auto-generated method stub
						Item item = new Item();
						item.setText(text);
						item.setUrl(url);
						ExpandDatabaseImpl edi = new ExpandDatabaseImpl(WebActivity.this);
						edi.updateItemTicket(item);
						edi.finalize();
						return true;
					}

				}, new Callback<Boolean>() {

					@Override
					public void onCallback(Boolean pCallbackValue) {
						// TODO Auto-generated method stub
						topBar.closeMenu();
					}

				}, new Callback<Exception>() {

					@Override
					public void onCallback(Exception pCallbackValue) {
						// TODO Auto-generated method stub
						
					}

				}, true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
			webView.goBack();// goBack()表示返回webView的上一页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	Handler myHandler = new Handler() {
	    public void handleMessage(Message msg)
	    {//
	      if (!Thread.currentThread().isInterrupted())
	      {
	        switch (msg.what)
	        {
	          case 1:
	        	  //play video
	        	  Intent intent  = new Intent();
	              intent.setClass(WebActivity.this, test_videoplayer.class);
	              startActivity(intent);
	            break;
	            
	          case 10:
		        	 
		        	
		        	//  FileUtil.SavedToText("/KLinkAPP/Product/",productname + ".html",htmlstring);
		        	  imagevector.clear();//清空
		        	  videovector.clear();//清空
		        	  
		        	  try {  
		        		  org.jsoup.nodes.Document doc = Jsoup.parse(htmlstring);
		        		  Elements links = doc.getElementsByTag("img");
		        		  Elements youlinks = doc.getElementsByTag("a");
		        		  
		        		  for (org.jsoup.nodes.Element link : links) {
		        		    String linkHref = link.attr("src");
		        		    if(linkHref != null && linkHref.length() > 0){
		        		    	imagevector.add(linkHref);
		        		    }
		        		  }
		        		  
		        		  for (org.jsoup.nodes.Element link : youlinks) {
			        		    String linkHref = link.attr("href");
			        		    if(linkHref != null && linkHref.length() > 0){
			        		      if(linkHref.contains("youtu.be") || linkHref.contains("youtube")){
			        		    		videovector.add(linkHref);
			        		    		Log.i("link herf VIDEO=====s",linkHref);
			        		      }
			        		    }
			        	  }
		        		  
		              } catch (Exception e) {  
		                  e.printStackTrace();  
		              }  
		              //下载图片
		        	  if(imagevector.size() > 0){
			        	  for(int i = 0; i < imagevector.size(); i ++)
			        	  {
			        		   final String imageurl = Config.IMAGEURL + imagevector.elementAt(i);
			        		   final int size = imagevector.size();
			        		   int index = imageurl.lastIndexOf("/");
			        		   if(index > 0)
			        		   {
			        			   final String filename = imageurl.substring(index+1);
			        			   final int count = i;
				        		   Thread t = new Thread(
				        				   new Runnable(){
											@Override
											public void run() {
												 String folder = "";
									   		       	switch (usType) {
									        		case Config.US_ABOUT: // about us 入口
									        			folder = "About Us";
									        			break;
									        		case Config.US_CONTACT: // contact us 入口
									        			folder = "Contact";

									        			break; 
									        		case Config.US_NEWSEVENTS: // news and evnets 入口
									        			folder = "News";

									        			break; 
									        		case Config.US_BUSINESS: // business 入口
									        			folder = "Business";

									        			break; 
									        		case Config.US_DOWNLOAD_RECORD: // 从下载记录中 入口

									        			break; 
									        		case Config.US_AROUNDME: // around me
									        			break;
									        		case Config.US_DOWNLOAD: // download 入口
									        			folder = "Download";

									        		case Config.US_PRODUCT: // product 入口
									        			folder = "Product";

									        			break;
									        		case Config.US_INBOX: // push message 入口
									        			break;
									        		}
								        		 if(HttpDownloader.downImageFile(imageurl,"KLinkAPP/" + folder + "/",filename) == 0 && (count == size-1)){
								        			  sendMsg(12);
								        		 }
											}
				        				}
				        		    );
				        		   t.start();
				        		   htmlstring = htmlstring.replaceAll(imagevector.elementAt(i),filename);
			        		   }
			        	  	}
		        	  }
		        	  else{
		        			  sendMsg(12);
		        	  }
		        	  break;
		          case 11:
		        	  GeneralTools.showAlertDialog("Connection Failed,Please check your device's internet connection", WebActivity.this);
		        	  break;
		          case 12:
		        	  if (proDialog != null) {
		    				proDialog.dismiss();
		    		  }
		        	  
		         	  if(videovector.size() > 0){//下载视频
		        		  for(int i = 0; i < videovector.size(); i ++)
			        	  {
		        			  String youtubeurl = videovector.elementAt(i);
		        			  Log.i("youtube url",youtubeurl);
		        			  int index = youtubeurl.lastIndexOf("/");
		        			  String filename = youtubeurl.substring(index+1) + ".3gp";
		        		  	  new YouTubePageStreamUriGetter().execute(youtubeurl,filename);
			        	  }
		         	  }
		         	  
		         	 String folder = "";
		   		       	switch (usType) {
		        		case Config.US_ABOUT: // about us 入口
		        			folder = "About Us";
		        			break;
		        		case Config.US_CONTACT: // contact us 入口
		        			folder = "Contact";

		        			break; 
		        		case Config.US_NEWSEVENTS: // news and evnets 入口
		        			folder = "News";

		        			break; 
		        		case Config.US_BUSINESS: // business 入口
		        			folder = "Business";

		        			break; 
		        		case Config.US_DOWNLOAD_RECORD: // 从下载记录中 入口

		        			break; 
		        		case Config.US_AROUNDME: // around me
		        			break;
		        		case Config.US_DOWNLOAD: // download 入口
		        			folder = "Download";

		        		case Config.US_PRODUCT: // product 入口
		        			folder = "Product";

		        			break;
		        		case Config.US_INBOX: // push message 入口
		        			break;
		        		}
		         	  
		        	  FileUtil.SavedToText("/KLinkAPP/" + folder + "/",productname + ".html",htmlstring);
		        	 
		        	//  GeneralTools.jumpToFav(ProductActivity.this,ProductActivity.this);
		        	  break;
	        }
	      }
	      super.handleMessage(msg);
	    }
};

private void sendMsg(int flag)
{
	    Message msg = new Message();
	    msg.what = flag;
	    myHandler.sendMessage(msg);
}
	
	//get youtube video rtsp link
		private class YourAsyncTask extends AsyncTask<Void, Void, Void>
	    {
	        ProgressDialog progressDialog;

	        @Override
	        protected void onPreExecute()
	        {
	            super.onPreExecute();
			   		  proDialog = ProgressDialog.show(WebActivity.this, "Connecting...","Please Wait....", true, true);
			  }

	        @Override
	        protected Void doInBackground(Void... params)
	        {
	            try
	            {
	                 String videoUrl = getUrlVideoRTSP(videourl);
	                Log.i("Video url for playing=========>>>>>", videoUrl);
	                if(videoUrl != null && videoUrl.startsWith("rtsp")){
	                	if(proDialog != null){
	                		proDialog.dismiss();
	                	}
	                	
	                	//start video play..
	                	
	                	
	                	switch (usType) {
	            		case Config.US_ABOUT: // about us 入口
	            			break;
	            		case Config.US_CONTACT: // contact us 入口
	            			break; 
	            		case Config.US_NEWSEVENTS: // news and evnets 入口
	            			break; 
	            		case Config.US_BUSINESS: // business 入口
	            			
	            			break; 
	            		case Config.US_DOWNLOAD_RECORD: // 从下载记录中 入口
	            			break; 
	            		case Config.US_AROUNDME: // around me
	            			break;
	            		case Config.US_DOWNLOAD: // download 入口
	            		case Config.US_PRODUCT: // product 入口
	            			Log.i("start play video", "start play video..");
	            			Config.YOUTUBEURL = videoUrl;
	            			sendMsg(1);
	            			break;
	            		case Config.US_INBOX: // push message 入口
	            			break;
	            		}
	                 

	                }
	            }
	            catch (Exception e)
	            {
	                Log.i("Login Soap Calling in Exception", e.toString());
	            }
	            return null;
	        }

	        @Override
	        protected void onPostExecute(Void result)
	        {
	            super.onPostExecute(result);
	            if(progressDialog != null){
	            	progressDialog.dismiss();
	            }
	            

	            /*
	            videoView.setVideoURI(Uri.parse("rtsp://v4.cache1.c.youtube.com/CiILENy73wIaGQk4RDShYkdS1BMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp"));
	            videoView.setMediaController(new MediaController(AlertDetail.this));
	            videoView.requestFocus();
	            videoView.start();
	            */            
	        
	         }

	    }
	
	
	public static String getUrlVideoRTSP(String urlYoutube)
    {
        try
        {
            String gdy = "http://gdata.youtube.com/feeds/api/videos/";
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String id = extractYoutubeId(urlYoutube);
            URL url = new URL(gdy + id);
            Log.i("URL === ", gdy + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Document doc = documentBuilder.parse(connection.getInputStream());
            Element el = doc.getDocumentElement();
            NodeList list = el.getElementsByTagName("media:content");///media:content
            Log.i("list length =", list.getLength() + "");
            String cursor = urlYoutube;
            for (int i = 0; i < list.getLength(); i++)
            {
                Node node = list.item(i);
                if (node != null)
                {
                    NamedNodeMap nodeMap = node.getAttributes();
                    HashMap<String, String> maps = new HashMap<String, String>();
                    for (int j = 0; j < nodeMap.getLength(); j++)
                    {
                        Attr att = (Attr) nodeMap.item(j);
                        maps.put(att.getName(), att.getValue());
                    }
                    if (maps.containsKey("yt:format"))
                    {
                        String f = maps.get("yt:format");
                        if (maps.containsKey("url"))
                        {
                            cursor = maps.get("url");
                        }
                        if (f.equals("6"))
                            return cursor;
                    }
                }
            }
            return cursor;
        }
        catch (Exception ex)
        {
            Log.e("Get Url Video RTSP Exception======>>", ex.toString());
        }
        return urlYoutube;

    }

  protected static String extractYoutubeId(String url) throws MalformedURLException
    {
        String id = null;
        try
        {
            String query = new URL(url).getQuery();
            if (query != null)
            {
                String[] param = query.split("&");
                for (String row : param)
                {
                    String[] param1 = row.split("=");
                    if (param1[0].equals("v"))
                    {
                        id = param1[1];
                    }
                }
            }
            else
            {
                if (url.contains("embed"))
                {
                    id = url.substring(url.lastIndexOf("/") + 1);
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("Exception", ex.toString());
        }
        return id;
    }
  
	
	class Meta {
	    public String num;
	    public String type;
	    public String ext;

	    Meta(String num, String ext, String type) {
	        this.num = num;
	        this.ext = ext;
	        this.type = type;
	    }
	}

	class Video {
	    public String ext = "";
	    public String type = "";
	    public String url = "";

	    Video(String ext, String type, String url) {
	        this.ext = ext;
	        this.type = type;
	        this.url = url;
	    }
	}
	
	
	public void downloadVideo(String ytUrl,String filename)
	{

   	 //Log.i("YouTube Video streaming details: ext:" + newVideo.ext
           //     + ", type:" + newVideo.type + ", url:" + newVideo.url,"AAAAA" + newVideo.url);
   	Log.i("3gp download url",ytUrl);
   	if(ytUrl != null && ytUrl.length() > 0){
   		URL u = null;
   		InputStream is = null;  
   		     try {
   		          u = new URL(ytUrl);
   		          is = u.openStream(); 
   		          HttpURLConnection huc = (HttpURLConnection)u.openConnection();//to know the size of video
   		          if(huc != null){
   		              String fileName = filename;
   		              String storagePath = Environment.getExternalStorageDirectory().toString();
   		              String folder = "";
		   		       	switch (usType) {
		        		case Config.US_ABOUT: // about us 入口
		        			folder = "About Us";
		        			break;
		        		case Config.US_CONTACT: // contact us 入口
		        			folder = "Contact";

		        			break; 
		        		case Config.US_NEWSEVENTS: // news and evnets 入口
		        			folder = "News";

		        			break; 
		        		case Config.US_BUSINESS: // business 入口
		        			folder = "Business";

		        			break; 
		        		case Config.US_DOWNLOAD_RECORD: // 从下载记录中 入口

		        			break; 
		        		case Config.US_AROUNDME: // around me
		        			break;
		        		case Config.US_DOWNLOAD: // download 入口
		        			folder = "Download";

		        		case Config.US_PRODUCT: // product 入口
		        			folder = "Product";

		        			break;
		        		case Config.US_INBOX: // push message 入口
		        			break;
		        		}
   		              File f = new File(storagePath + "/KLinkAPP/" + folder + "/" ,fileName);
   		              Log.i("store path====",storagePath + "/KLinkAPP/Product/");

   		              FileOutputStream fos = new FileOutputStream(f);
   		              byte[] buffer = new byte[1024];
   		              int len1 = 0;
   		              if(is != null){
   		                 while ((len1 = is.read(buffer)) > 0) {
   		                       fos.write(buffer,0, len1);   
   		                 }
   		              }
   		              if(fos != null){
   		                 fos.close();
   		              }
   		          }                     
   		     }catch (MalformedURLException mue) {
   		            mue.printStackTrace();
   		     } catch (IOException ioe) {
   		            ioe.printStackTrace();
   		    } finally {
   		               try {                
   		                 if(is != null){
   		                   is.close();
   		                 }
   		               }catch (IOException ioe) {
   		                     // just going to ignore this one
   		               }
   		    }
   	}
 }


	public ArrayList<Video> getStreamingUrisFromYouTubePage(String ytUrl,String filename)
	        throws IOException {
	//	Log.i("download video ==","getStreamingUrisFromYouTubePage");
		Log.i("ytUrl",ytUrl);
		Log.i("filename",filename);

	    if (ytUrl == null) {
	        return null;
	    }

	    // Remove any query params in query string after the watch?v=<vid> in
	    // e.g.
	    // http://www.youtube.com/watch?v=0RUPACpf8Vs&feature=youtube_gdata_player
	    int andIdx = ytUrl.indexOf('&');
	    if (andIdx >= 0) {
	        ytUrl = ytUrl.substring(0, andIdx);
	    }

	    // Get the HTML response
	    String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0.1)";
	    HttpClient client = new DefaultHttpClient();
	    client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
	            userAgent);
	    HttpGet request = new HttpGet(ytUrl);
	    HttpResponse response = client.execute(request);
	    String html = "";
	    InputStream in = response.getEntity().getContent();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	    StringBuilder str = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	        str.append(line.replace("\\u0026", "&"));
	    }
	    in.close();
	    html = str.toString();

	    Log.i("000000000000000000",html);

	    // Parse the HTML response and extract the streaming URIs
	    if (html.contains("verify-age-thumb")) {
	        Log.i("YouTube is asking for age verification. We can't handle that sorry.","");
	        return null;
	    }

	    if (html.contains("das_captcha")) {
	    	Log.i("Captcha found, please try with different IP address.","");
	        return null;
	    }

	    Pattern p = Pattern.compile("stream_map\": \"(.*?)?\"");
	    // Pattern p = Pattern.compile("/stream_map=(.[^&]*?)\"/");
	    Matcher m = p.matcher(html);
	    List<String> matches = new ArrayList<String>();
	    while (m.find()) {
	        matches.add(m.group());
	    }

	    if (matches.size() != 1) {
	        Log.i("Found zero or too many stream maps.","");
		    Log.i("-----------------","----------------------------");

	        return null;
	    }

	    String urls[] = matches.get(0).split(",");
	    HashMap<String, String> foundArray = new HashMap<String, String>();
	    for (String ppUrl : urls) {
	        String url = URLDecoder.decode(ppUrl, "UTF-8");

	        Pattern p1 = Pattern.compile("itag=([0-9]+?)[&]");
	        Matcher m1 = p1.matcher(url);
	        String itag = null;
	        if (m1.find()) {
	            itag = m1.group(1);
	        }

	        Pattern p2 = Pattern.compile("sig=(.*?)[&]");
	        Matcher m2 = p2.matcher(url);
	        String sig = null;
	        if (m2.find()) {
	            sig = m2.group(1);
	        }

	        Pattern p3 = Pattern.compile("url=(.*?)[&]");
	        Matcher m3 = p3.matcher(ppUrl);
	        String um = null;
	        if (m3.find()) {
	            um = m3.group(1);
	        }

	        if (itag != null && sig != null && um != null) {
	            foundArray.put(itag, URLDecoder.decode(um, "UTF-8") + "&"
	                    + "signature=" + sig);
	        }
	    }

	    if (foundArray.size() == 0) {
		    Log.i("333333333333333333333","----------------------------");
	        return null;
	    }
	    Log.i("11111111111111111111","1111111111111111111111111");
	    HashMap<String, Meta> typeMap = new HashMap<String, Meta>();
	    typeMap.put("13", new Meta("13", "3GP", "Low Quality - 176x144"));
	    typeMap.put("17", new Meta("17", "3GP", "Medium Quality - 176x144"));
	    typeMap.put("36", new Meta("36", "3GP", "High Quality - 320x240"));
	    typeMap.put("5", new Meta("5", "FLV", "Low Quality - 400x226"));
	    typeMap.put("6", new Meta("6", "FLV", "Medium Quality - 640x360"));
	    typeMap.put("34", new Meta("34", "FLV", "Medium Quality - 640x360"));
	    typeMap.put("35", new Meta("35", "FLV", "High Quality - 854x480"));
	    typeMap.put("43", new Meta("43", "WEBM", "Low Quality - 640x360"));
	    typeMap.put("44", new Meta("44", "WEBM", "Medium Quality - 854x480"));
	    typeMap.put("45", new Meta("45", "WEBM", "High Quality - 1280x720"));
	    typeMap.put("18", new Meta("18", "MP4", "Medium Quality - 480x360"));
	    typeMap.put("22", new Meta("22", "MP4", "High Quality - 1280x720"));
	    typeMap.put("37", new Meta("37", "MP4", "High Quality - 1920x1080"));
	    typeMap.put("33", new Meta("38", "MP4", "High Quality - 4096x230"));

	    ArrayList<Video> videos = new ArrayList<Video>();
	    Log.i("22222222222222222222222222","1111111111111111111111111");

	    for (String format : typeMap.keySet()) {
	        Meta meta = typeMap.get(format);
	        
	        Log.i("format ===",format);
	        if (foundArray.containsKey(format)) {
	            Video newVideo = new Video(meta.ext, meta.type,
	                    foundArray.get(format));
	            videos.add(newVideo);
	            
	    		Log.i("download video ==","start download video..");
	    		Log.i("newVideo ==",newVideo.ext);

	            if(newVideo.ext.equals("3GP")){
	            	 //Log.i("YouTube Video streaming details: ext:" + newVideo.ext
	 	               //     + ", type:" + newVideo.type + ", url:" + newVideo.url,"AAAAA" + newVideo.url);
	            	Log.i("3gp download url",newVideo.url);
	            	if(newVideo.url != null && newVideo.url.length() > 0){
	            		URL u = null;
	            		InputStream is = null;  
	            		     try {
	            		          u = new URL(newVideo.url);
	            		          is = u.openStream(); 
	            		          HttpURLConnection huc = (HttpURLConnection)u.openConnection();//to know the size of video
	            		          if(huc != null){
	            		              String fileName = filename;
	            		              String storagePath = Environment.getExternalStorageDirectory().toString();
	            		              String folder = "";
	          		   		       	switch (usType) {
	          		        		case Config.US_ABOUT: // about us 入口
	          		        			folder = "About Us";
	          		        			break;
	          		        		case Config.US_CONTACT: // contact us 入口
	          		        			folder = "Contact";

	          		        			break; 
	          		        		case Config.US_NEWSEVENTS: // news and evnets 入口
	          		        			folder = "News";

	          		        			break; 
	          		        		case Config.US_BUSINESS: // business 入口
	          		        			folder = "Business";

	          		        			break; 
	          		        		case Config.US_DOWNLOAD_RECORD: // 从下载记录中 入口

	          		        			break; 
	          		        		case Config.US_AROUNDME: // around me
	          		        			break;
	          		        		case Config.US_DOWNLOAD: // download 入口
	          		        			folder = "Download";

	          		        		case Config.US_PRODUCT: // product 入口
	          		        			folder = "Product";

	          		        			break;
	          		        		case Config.US_INBOX: // push message 入口
	          		        			break;
	          		        		}
	            		              File f = new File(storagePath + "/KLinkAPP/" + folder + "/",fileName);
	            		              Log.i("store path====",storagePath + "/KLinkAPP/Product/");

	            		              FileOutputStream fos = new FileOutputStream(f);
	            		              byte[] buffer = new byte[1024];
	            		              int len1 = 0;
	            		              if(is != null){
	            		                 while ((len1 = is.read(buffer)) > 0) {
	            		                       fos.write(buffer,0, len1);   
	            		                 }
	            		              }
	            		              if(fos != null){
	            		                 fos.close();
	            		              }
	            		          }                     
	            		     }catch (MalformedURLException mue) {
	            		            mue.printStackTrace();
	            		     } catch (IOException ioe) {
	            		            ioe.printStackTrace();
	            		    } finally {
	            		               try {                
	            		                 if(is != null){
	            		                   is.close();
	            		                 }
	            		               }catch (IOException ioe) {
	            		                     // just going to ignore this one
	            		               }
	            		    }
	            		
	            		break;
	            	}
	            }
	        }
	    }

	    return videos;
	}

	private class YouTubePageStreamUriGetter extends
	        AsyncTask<String, String, String> {
	    ProgressDialog progressDialog;

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        progressDialog = ProgressDialog.show(WebActivity.this, "",
	                "Downloading video,please wait...", true);
	    }

	    @Override
	    protected String doInBackground(String... params) {
	        String url = params[0];
	        try {
	        	
	        //	downloadVideo("http://v4.cache1.c.youtube.com/CiILENy73wIaGQk4RDShYkdS1BMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp","test.3gp");
	        
	        	ArrayList<Video> videos = getStreamingUrisFromYouTubePage(url,params[1]);
	            if (videos != null && !videos.isEmpty()) {
	                String retVidUrl = null;
	                for (Video video : videos) {
	                    if (video.ext.toLowerCase().contains("mp4")
	                            && video.type.toLowerCase().contains("medium")) {
	                        retVidUrl = video.url;
	                        break;
	                    }
	                }
	                if (retVidUrl == null) {
	                    for (Video video : videos) {
	                        if (video.ext.toLowerCase().contains("3gp")
	                                && video.type.toLowerCase().contains(
	                                        "medium")) {
	                            retVidUrl = video.url;
	                            break;

	                        }
	                    }
	                }
	                if (retVidUrl == null) {

	                    for (Video video : videos) {
	                        if (video.ext.toLowerCase().contains("mp4")
	                                && video.type.toLowerCase().contains("low")) {
	                            retVidUrl = video.url;
	                            break;

	                        }
	                    }
	                }
	                if (retVidUrl == null) {
	                    for (Video video : videos) {
	                        if (video.ext.toLowerCase().contains("3gp")
	                                && video.type.toLowerCase().contains("low")) {
	                            retVidUrl = video.url;
	                            break;
	                        }
	                    }
	                }
	                Log.i("retVidUrl ===",retVidUrl);
	                return retVidUrl;
	            }
	            
	            
	        } catch (Exception e) {
	            Log.i("Couldn't get YouTube streaming URL", "");
	        }
	        Log.i("Couldn't get stream URI for " + url,"");
	        return null;
	    }

	    @Override
	    protected void onPostExecute(String streamingUrl) {
	        super.onPostExecute(streamingUrl);
	        progressDialog.dismiss();
	        if (streamingUrl != null) {
	                         /* Do what ever you want with streamUrl */
	        }
	    }
	}
}
