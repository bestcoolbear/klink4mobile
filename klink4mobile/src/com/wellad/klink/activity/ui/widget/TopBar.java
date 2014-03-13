/*
 * @(#)TopBar.java 2014年1月31日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity.ui.widget;

import com.wellad.klink.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * 
 * @author Wang Yiming
 */
public class TopBar extends RelativeLayout implements OnClickListener {
	private ImageButton backImageButton;
	private ImageView logoImageView;
	private ImageButton menuImageButton;
	private TextView titleTextView;
	
	private RelativeLayout bgRelativeLayout;
	private RelativeLayout menuRelativeLayout;
	
	private ImageButton searchButton;
	private ImageButton inboxButton;
	private ImageButton favoriteButton;

	public TopBar(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public TopBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public TopBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.navigation_include, this);

		backImageButton = (ImageButton) this.findViewById(R.id.backImageButton);
		logoImageView = (ImageView) this.findViewById(R.id.logoImageView);
		menuImageButton = (ImageButton) this.findViewById(R.id.menuImageButton);
		titleTextView = (TextView) this.findViewById(R.id.titleTextView);
		
		bgRelativeLayout = (RelativeLayout) this.findViewById(R.id.bgRelativeLayout);
		menuRelativeLayout = (RelativeLayout) this.findViewById(R.id.menuRelativeLayout);
		 
		searchButton = (ImageButton) this.findViewById(R.id.searchButton);
		inboxButton = (ImageButton) this.findViewById(R.id.inboxButton);
		favoriteButton = (ImageButton) this.findViewById(R.id.favoriteButton);
		
		bgRelativeLayout.setOnClickListener(this);
		menuImageButton.setOnClickListener(this);
		favoriteButton.setOnClickListener(this);
	}

	public ImageButton getBackImageButton() {
		return backImageButton;
	}

	public ImageView getLogoImageView() {
		return logoImageView;
	}

	public ImageButton getMenuImageButton() {
		return menuImageButton;
	}

	public TextView getTitleTextView() {
		return titleTextView;
	}
	
	public RelativeLayout getBgRelativeLayout() {
		return bgRelativeLayout;
	}

	public RelativeLayout getMenuRelativeLayout() {
		return menuRelativeLayout;
	}

	public ImageButton getSearchButton() {
		return searchButton;
	}

	public ImageButton getInboxButton() {
		return inboxButton;
	}

	public ImageButton getFavoriteButton() {
		return favoriteButton;
	}

	public void displayMenu() {
		if (bgRelativeLayout.getVisibility() == View.GONE) {
			bgRelativeLayout.setVisibility(View.VISIBLE);
			menuRelativeLayout.setVisibility(View.VISIBLE);
		}
	}
	
	public boolean isDisplayMenu() {
		return bgRelativeLayout.getVisibility() == View.VISIBLE;
	}
	
	public void closeMenu() {
		bgRelativeLayout.setVisibility(View.GONE);
		menuRelativeLayout.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (bgRelativeLayout.getId() == v.getId()) {
			closeMenu();
		} else if (menuImageButton.getId() == v.getId()) {
			displayMenu();
		}
	}

}
