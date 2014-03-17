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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * @author Wang Yiming
 */
public class ListsubFavorite extends BaseActivity implements OnItemClickListener {
	private ListView listView;
	private ItemAdapter itemAdapter;
	private List<String> items1 = new ArrayList<String>();
	int index;
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, ListsubFavorite.class);
		a.startActivity(intent);
	}
	
	public boolean onContextItemSelected(MenuItem item){
	     if(item.getItemId() == 0){
	    	 Log.i("menu item 1","menu item 1");
			String td = items1.get(index);
			String firsturl = td;
      		String localfile = FileUtil.getSdRoot() + "/" + "KLinkAPP/" + Config.catid + "/" + firsturl;
      		File delfile = new File(localfile);
      		if(delfile.exists())
      		{
      			delfile.delete();
      		}
      		
    		items1 = FileUtil.loadFavoriteSubRoot(Config.catid);

    		
    		if (itemAdapter == null) {
    			itemAdapter = new ItemAdapter(ListsubFavorite.this);
    		}
    		ArrayList<Item> adapterlist = new ArrayList<Item>();
    		for(int i = 0; i < items1.size(); i ++){
    			String str = items1.get(i);
    			Item it = new Item();
    			it.setText(str);
    			adapterlist.add(it);
    		}
    		itemAdapter.setList(adapterlist);
    		if(adapterlist.size() == 0)
    		{
    			//delete father
    			/*
          		String localfile2 = FileUtil.getSdRoot() + "/" + "KLinkAPP/" + Config.catid + "/";
          		File delfile2 = new File(localfile2);
          		if(delfile2.exists())
          		{
          			delfile2.delete();
          		}
          		*/
    		}
    		itemAdapter.notifyDataSetChanged();
	     }
	     
	     if(item.getItemId() == 1){
	    	 
	     }
		 return super.onContextItemSelected(item);
	 }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_activity);
		topBar = (TopBar) this.findViewById(R.id.topBar);
		topBar.getLogoImageView().setVisibility(View.GONE);
		topBar.getBackImageButton().setVisibility(View.VISIBLE);
		topBar.getTitleTextView().setText(R.string.title_favorite);
		topBar.getMenuImageButton().setVisibility(View.GONE);
		initTopBarEvent(this);
		
		listView = (ListView) this.findViewById(R.id.listView);
		listView.setOnItemClickListener(this);
		
		items1 = FileUtil.loadFavoriteSubRoot(Config.catid);

		
		if (itemAdapter == null) {
			itemAdapter = new ItemAdapter(ListsubFavorite.this);
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
		
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					// TODO Auto-generated method stub
					 // TODO Auto-generated method stub
					final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
					index = info.position;
				    String menutitel = "Menu";
				    String menutext = "Delete";
				  //  String menutext2 = "Rename";
				    
				    if(Config.APP_USER_LANGUAGE.equals("cn")){
		    	    	 menutitel = "菜单";
		    	    	 menutext = "删除";
			    	 }
		    	     if(Config.APP_USER_LANGUAGE.equals("en")){
		    	    	 menutitel = "Menu";
		    	    	 menutext = "Delete";

			    	 }
		    	     if(Config.APP_USER_LANGUAGE.equals("mala")){
		    	    	 menutitel = "menu";
		    	    	 menutext = "Padam";
			    	 }
					 menu.setHeaderTitle(menutitel);
					 menu.add(0,0,0,menutext);
				}
			 });
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
		ActivityUtils.doAsync(ListsubFavorite.this,
				R.string.ptitle_resource_id, R.string.ptitle_resource_id,
				new Callable<ArrayList<Item>>() {

					@Override
					public ArrayList<Item> call()
							throws Exception {
						// TODO Auto-generated method stub
						ExpandDatabaseImpl edi = new ExpandDatabaseImpl(ListsubFavorite.this);
						ArrayList<Item> job = edi.getAllItem();
						edi.finalize();
						return job;
					}

				}, new Callback<ArrayList<Item>>() {

					@Override
					public void onCallback(ArrayList<Item> pCallbackValue) {
						// TODO Auto-generated method stub
						if (itemAdapter == null) {
							itemAdapter = new ItemAdapter(ListsubFavorite.this);
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
    		String localfile = FileUtil.getSdRoot() + "/" + "KLinkAPP/" + Config.catid + "/" + item.getText();

    		if(localfile.endsWith(".3gp")){
     		 Config.YOUTUBEURL = localfile;
   			 Intent intent = new Intent(Intent.ACTION_VIEW);
   		     String type = "video/mp4";
   		     Uri uri = Uri.parse(Config.YOUTUBEURL);
   		     intent.setDataAndType(uri, type);
   		     startActivity(intent);   
   			 return;
    		}
    		
    		if(localfile.startsWith("file://")){
    			//Favoritewebview.loadUrl(localfile);
    		}else{
    			localfile = "file://" + localfile;
    			//Favoritewebview.loadUrl(localfile);
    		}

    		
    		WebActivity.launch(this, Config.US_DOWNLOAD_RECORD, item.getText(), localfile, false);
		}
	}
	
}
