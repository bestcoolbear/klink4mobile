/*
 * @(#)WelcomeActivity.java 2014年1月31日
 * 
 * Copyright 2006-2014 Wang Yiming, All Rights reserved.
 */
package com.wellad.klink.activity;

import java.util.ArrayList;
import java.util.Locale;

import com.wellad.klink.R;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.model.AroundMeLocation;
import com.wellad.klink.business.model.AroundMeShop;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * 
 * @author Wang Yiming
 */
public class WelcomeActivity extends BaseActivity implements OnClickListener {
	
	private static final String[] locales = { "en", "ms", "cn" };
	
	private ImageButton english;
	private ImageButton malay;
	private ImageButton chinese;
	
	/**
	 * Launch
	 * 
	 * @param a
	 */
	public static void launch(Activity a) {
		Intent intent = new Intent(a, WelcomeActivity.class);
		a.startActivity(intent);
		a.finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initGPS();
		this.setContentView(R.layout.welcome_activity);
		english = (ImageButton) this.findViewById(R.id.englisth);
		malay = (ImageButton) this.findViewById(R.id.malay);
		chinese = (ImageButton) this.findViewById(R.id.chinese);
		
		english.setOnClickListener(this);
		malay.setOnClickListener(this);
		chinese.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.englisth) {
			setLocale(locales[0]);
			Config.APP_LANGUAGE = "en";
			Config.APP_USER_LANGUAGE = "en";
		} else if (v.getId() == R.id.malay) {
			setLocale(locales[1]);
			Config.APP_LANGUAGE = "mala";
			Config.APP_USER_LANGUAGE = "mala";
		} else if (v.getId() == R.id.chinese) {
			setLocale(locales[2]);
			Config.APP_LANGUAGE = "cn";
			Config.APP_USER_LANGUAGE = "cn";
		}
		LoginActivity.launch(WelcomeActivity.this);
	}
	
	/**
	 * 选择语言
	 * 
	 * @param languageToLoad
	 */
	private void setLocale(String languageToLoad) {
	    Configuration config = getResources().getConfiguration();
	    DisplayMetrics metrics = getResources().getDisplayMetrics();
		if (languageToLoad.equals(locales[0])) {
			config.locale = Locale.ENGLISH;
		} else if (languageToLoad.equals(locales[1])) {
			config.locale = new Locale("ms_MY");
		} else if (languageToLoad.equals(locales[2])) {
			config.locale = Locale.SIMPLIFIED_CHINESE;
		}
	    
	    Log.d("setLocale", config.locale.getCountry());
	    getResources().updateConfiguration(config, metrics);
	}
	
	private void initGPS()
	{
		if(Config.items1 != null){
			AroundMeLocation al1 = new AroundMeLocation();
			AroundMeLocation al2 = new AroundMeLocation();
			AroundMeLocation al3 = new AroundMeLocation();
			AroundMeLocation al4 = new AroundMeLocation();
			AroundMeLocation al5 = new AroundMeLocation();
			AroundMeLocation al6 = new AroundMeLocation();
			AroundMeLocation al7 = new AroundMeLocation();
			AroundMeLocation al8 = new AroundMeLocation();
			AroundMeLocation al9 = new AroundMeLocation();
			AroundMeLocation al10 = new AroundMeLocation();
			AroundMeLocation al11 = new AroundMeLocation();
			AroundMeLocation al12 = new AroundMeLocation();
			AroundMeLocation al13 = new AroundMeLocation();
			AroundMeLocation al14 = new AroundMeLocation();
			//
			AroundMeLocation al15 = new AroundMeLocation();
			AroundMeLocation al16 = new AroundMeLocation();
			AroundMeLocation al17 = new AroundMeLocation();
			AroundMeLocation al18 = new AroundMeLocation();
			AroundMeLocation al19 = new AroundMeLocation();
			AroundMeLocation al20 = new AroundMeLocation();
			AroundMeLocation al21 = new AroundMeLocation();
			AroundMeLocation al22 = new AroundMeLocation();

			AroundMeLocation al23 = new AroundMeLocation();
			AroundMeLocation al24 = new AroundMeLocation();
			AroundMeLocation al25 = new AroundMeLocation();
			AroundMeLocation al26 = new AroundMeLocation();
			AroundMeLocation al27 = new AroundMeLocation();
			AroundMeLocation al28 = new AroundMeLocation();
			AroundMeLocation al29 = new AroundMeLocation();
			AroundMeLocation al30 = new AroundMeLocation();
			AroundMeLocation al31 = new AroundMeLocation();
			
			//
			AroundMeLocation al32 = new AroundMeLocation();
			AroundMeLocation al33 = new AroundMeLocation();
			AroundMeLocation al34 = new AroundMeLocation();
			AroundMeLocation al35 = new AroundMeLocation();
			AroundMeLocation al36 = new AroundMeLocation();
			AroundMeLocation al37 = new AroundMeLocation();
			AroundMeLocation al38 = new AroundMeLocation();
			AroundMeLocation al39 = new AroundMeLocation();
			AroundMeLocation al40 = new AroundMeLocation();
			AroundMeLocation al41 = new AroundMeLocation();
			AroundMeLocation al42 = new AroundMeLocation();


		//	AroundMeLocation al15 = new AroundMeLocation();//for test

			al1.setLocationame("MASAI");
			al2.setLocationame("BATU PAHAT");
			al3.setLocationame("YONG PENG");
			al4.setLocationame("SKUDAI");
			al5.setLocationame("ALOR SETAR");
			al6.setLocationame("KOTA BHARU");
			al7.setLocationame("Fraser Business Park");
			al8.setLocationame("Setapak");
			al9.setLocationame("LABUAN");
			al10.setLocationame("Bandar Malacca");
			al11.setLocationame("Senawang");
			al12.setLocationame("Nilai");
			al13.setLocationame("KUANTAN");
			al14.setLocationame("RAUB");
		//	al15.setLocationame("ShangHai");
			al15.setLocationame("JENGKA");
			al16.setLocationame("PENANG");
			al17.setLocationame("BUTTERWORTH");
			al18.setLocationame("NIBONG TEBAL");
			al19.setLocationame("BAYAN LEPAS");
			al20.setLocationame("IPOH");
			al21.setLocationame("TELUK INTAN");
			al22.setLocationame("Sitiawan");

			//
			al23.setLocationame("TAWAU");
			al24.setLocationame("LAHAD DATU");
			al25.setLocationame("KENINGAU");
			al26.setLocationame("KOTA KINABALU");
			al27.setLocationame("SANDAKAN");
			al28.setLocationame("KUCHING");
			al29.setLocationame("MIRI");
			al30.setLocationame("SIBU");
			al31.setLocationame("Kajang");

			al32.setLocationame("Sungai Buloh");
			al33.setLocationame("Banting");
			al34.setLocationame("Ampang");
			al35.setLocationame("Kuala Selangor");
			al36.setLocationame("Rawang");
			al37.setLocationame("Klang");
			al38.setLocationame("Petaling Jaya");
			al39.setLocationame("Shah Alam");
			al40.setLocationame("Puchong");
			al41.setLocationame("Marang");
			al42.setLocationame("Kuala Terengganu");

			
			Config.items1.add(al1);
			Config.items1.add(al2);
			Config.items1.add(al3);
			Config.items1.add(al4);
			Config.items1.add(al5);
			Config.items1.add(al6);
			Config.items1.add(al7);
			Config.items1.add(al8);
			Config.items1.add(al9);
			Config.items1.add(al10);
			Config.items1.add(al11);
			Config.items1.add(al12);
			Config.items1.add(al13);
			Config.items1.add(al14);
		//	Config.items1.add(al15);
			
			
			Config.items1.add(al15);
			Config.items1.add(al16);
			Config.items1.add(al17);
			Config.items1.add(al18);
			Config.items1.add(al19);
			Config.items1.add(al20);
			Config.items1.add(al21);
			Config.items1.add(al22);
			Config.items1.add(al23);
			Config.items1.add(al24);
			Config.items1.add(al25);
			Config.items1.add(al26);
			Config.items1.add(al27);
			Config.items1.add(al28);
			Config.items1.add(al29);
			Config.items1.add(al30);
			Config.items1.add(al31);
			Config.items1.add(al32);
			Config.items1.add(al33);
			Config.items1.add(al34);
			Config.items1.add(al35);
			Config.items1.add(al36);
			Config.items1.add(al37);
			Config.items1.add(al38);
			Config.items1.add(al39);
			Config.items1.add(al40);
			Config.items1.add(al41);
			Config.items1.add(al42);


		}
		
		if(Config.items2 != null){
			Config.items2.clear();
			Config.items2 = null;
		}
		
		
		Config.items2 = new ArrayList<AroundMeShop>();
		 
		if(Config.items2 != null)
		{
			
		//	AroundMeShop sh0 = new AroundMeShop("ShangHai","SH001","121.223933","31.1326347");

			AroundMeShop sh1 = new AroundMeShop("MASAI","SJH113","1.485903","103.886115","Yap Siow Yen");
			AroundMeShop sh2 = new AroundMeShop("BATU PAHAT","SJH116","1.863859","102.939337","Mohd Zakaria Bin Salim");
			AroundMeShop sh3 = new AroundMeShop("YONG PENG","SJH70","2.015822","103.065624","Spring Sunflower Trading");
			AroundMeShop sh4 = new AroundMeShop("SKUDAI","SJH91","1.542263","103.63154","Valiant Link Sdn. Bhd");

			//
			AroundMeShop sh5 = new AroundMeShop("ALOR SETAR","SKD131","6.081163","100.36172","Khong Woei Seong");
			//
			AroundMeShop sh6 = new AroundMeShop("KOTA BHARU","SKT104","6.164460", "102.281183","DR. Mohd Razali Bin Aziz");
			AroundMeShop sh7 = new AroundMeShop("KOTA BHARU","SKT107","5.913929","102.244745","Mohamad Bin Ibrahim");
			//
			AroundMeShop sh8 = new AroundMeShop("Fraser Business Park","SWP60","3.128454","101.713638","K-System Global Sdn Bhd");
			AroundMeShop sh9 = new AroundMeShop("Setapak","SWP99","3.206938","101.719172","Raihan Binti Haji Wan Ibrahim");
			//
			AroundMeShop sh10 = new AroundMeShop("LABUAN","SLB93","5.279987","115.23972","Chieng Hock Ting KEDAI UNION TRADING (DUTY FREE SHOP)");
			//
			AroundMeShop sh11 = new AroundMeShop("Bandar Malacca","SML95","2.228027","102.225712","Rose Pua");
			//
			AroundMeShop sh12 = new AroundMeShop("Senawang","SNS121","2.693721","102.000264","Mohamad Ma'Arof Bin Ariffin");
			AroundMeShop sh13 = new AroundMeShop("Nilai","SNS129","2.801202","101.806997","Erik A/L Michael");
			AroundMeShop sh133 = new AroundMeShop("KUANTAN","SNS133","3.828806","103.302018","Datin Sri Hjh Juriah Binti Hassan");
			AroundMeShop sh134 = new AroundMeShop("RAUB","SNS134","3.797108","101.861672","V Sithambaram A/L Velusamy");
			AroundMeShop sh135 = new AroundMeShop("JENGKA","SNS135","3.770497","102.547633","Zainal Abidin Bin Zakaria");

			//
//			AroundMeShop sh14 = new AroundMeShop("Pahang","SPH86","3.828806","103.302018","");
//			AroundMeShop sh15 = new AroundMeShop("Pahang","SPH124","3.797108","101.861672","");
//			AroundMeShop sh16 = new AroundMeShop("Pahang","SPH135","3.770497","102.547633","");
			//
			AroundMeShop sh17 = new AroundMeShop("PENANG","SPN026","5.425979","100.318152","Lighthouse Lighting & Interior Sdn.Bhd");
			AroundMeShop sh18 = new AroundMeShop("BUTTERWORTH","SPN115","5.441743","100.386575","Wan Giap Poh");
			AroundMeShop sh19 = new AroundMeShop("NIBONG TEBAL","SPN118","5.195161","100.492313","Tamil Selvi A/P Arumugam");
			AroundMeShop sh20 = new AroundMeShop("BAYAN LEPAS","SPN136","5.33185","100.293012","Abd Hamid Bin Abd Aziz");
			//
			AroundMeShop sh21 = new AroundMeShop("IPOH","SPR03","4.615951","101.119583","Eric Wong Wai Keong");
			AroundMeShop sh22 = new AroundMeShop("TELUK INTAN","SPR92","4.005801","101.0218","Ooi Hock Tai");
			AroundMeShop sh23 = new AroundMeShop("Sitiawan","SPR94","4.208291","100.655332","Sri Harti");
			//
			AroundMeShop sh24 = new AroundMeShop("TAWAU","SSB109","4.242489","117.887447","Andi Nurerfarina Famizah Bt ADjafar");
			AroundMeShop sh25 = new AroundMeShop("LAHAD DATU","SSB122","5.027132","118.298548","Bahri Bin Silang");
			AroundMeShop sh26 = new AroundMeShop("KENINGAU","SSB123","5.265303","116.328209","Dalley Binti Jaimi");
			AroundMeShop sh27 = new AroundMeShop("KOTA KINABALU","SSB128","5.947768", "116.089589","Naimah Binti Ilyas");
			AroundMeShop sh28 = new AroundMeShop("SANDAKAN","SSB133","5.84847","118.05555","Usman Bin Ruslan");
			//
			AroundMeShop sh29 = new AroundMeShop("KUCHING","SSR07","1.553082", "110.363955","K-Link Kuching Stockist");
			AroundMeShop sh30 = new AroundMeShop("MIRI","SSR15","4.413733","114.006108","Kino-Link Sdn Bhd");
			AroundMeShop sh31 = new AroundMeShop("SIBU","SSR134","2.291886","111.835101","Law Ngieh Hui");
			//
			AroundMeShop sh32 = new AroundMeShop("Kajang","SSL102","2.992011","101.799761","Nazaruddin Bin Zakaria");
			AroundMeShop sh33 = new AroundMeShop("Sungai Buloh","SSL105","3.210379","101.562327","Mejer Lias Bin Mohd");
			AroundMeShop sh34 = new AroundMeShop("Banting","SSL110","2.84713","101.519346","Ibrahim Bin Osman");
			AroundMeShop sh35 = new AroundMeShop("Ampang","SSL111","3.135259","101.769887","Yap Swee Meng");
			AroundMeShop sh36 = new AroundMeShop("Kuala Selangor","SSL117","3.320913","101.271178","Joshua Yap Wei Lieh");
			AroundMeShop sh37 = new AroundMeShop("Rawang","SSL120","3.319612","101.57695","Asmah Binti Atan");
			AroundMeShop sh38 = new AroundMeShop("Klang","SSL74","3.010115","101.43979","Irene Chen Yoke Moi");
			AroundMeShop sh39 = new AroundMeShop("Petaling Jaya","SSL125","3.086731","101.623411","Chuah Chin Koon");
			AroundMeShop sh40 = new AroundMeShop("Shah Alam","SSL126","3.031412","101.534952","Peter Rayan A/L Gnanapragasam");
			AroundMeShop sh41 = new AroundMeShop("Puchong","SSL132","2.982591","101.610904","Siti Sara Binti Mat Jusoh");
			
			//
			AroundMeShop sh42 = new AroundMeShop("Marang","STR112","5.166173","103.232167","Zulkefli Bin Mohamed");
			AroundMeShop sh43 = new AroundMeShop("Kuala Terengganu","STR130","5.314613","103.119971","Rohani Binti Harun");
			
 
			Config.items2.add(sh1);
			Config.items2.add(sh2);
			Config.items2.add(sh3);
			Config.items2.add(sh4);
			Config.items2.add(sh5);
			Config.items2.add(sh6);
			Config.items2.add(sh7);
			Config.items2.add(sh8);
			Config.items2.add(sh9);
			Config.items2.add(sh10);
			Config.items2.add(sh11);
			Config.items2.add(sh12);
			Config.items2.add(sh13);
			Config.items2.add(sh133);
			Config.items2.add(sh134);
			Config.items2.add(sh135);
			Config.items2.add(sh17);
			Config.items2.add(sh18);
			Config.items2.add(sh19);
			Config.items2.add(sh20);
			Config.items2.add(sh21);
			Config.items2.add(sh22);
			Config.items2.add(sh23);
			Config.items2.add(sh24);
			Config.items2.add(sh25);
			Config.items2.add(sh26);
			Config.items2.add(sh27);
			Config.items2.add(sh28);
			Config.items2.add(sh29);
			Config.items2.add(sh30);
			Config.items2.add(sh31);
			Config.items2.add(sh32);
			Config.items2.add(sh33);
			Config.items2.add(sh34);
			Config.items2.add(sh35);
			Config.items2.add(sh36);
			Config.items2.add(sh37);
			Config.items2.add(sh38);
			Config.items2.add(sh39);
			Config.items2.add(sh40);
			Config.items2.add(sh41);
			Config.items2.add(sh42);
			Config.items2.add(sh43);
			//Config.items2.add(sh0);

		}
		
	}

}
