package com.wellad.klink.business;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wellad.klink.business.model.AroundMeLocation;
import com.wellad.klink.business.model.AroundMeShop;
import com.wellad.klink.business.model.SearchResult;
import com.wellad.klink.business.model.SubCateBean;

import android.util.Log;


public class Config {
	
	public static String USER_REG = "http://117.53.153.156/klink/index.php?m=api&a=userreg"; //注册
	public static String USER_LOGIN = "http://117.53.153.156/klink/index.php?m=api&a=userlogin"; // 登录
	public static String SERVERUPR = "http://117.53.153.156/klink/"; 
	public static String SERVERMESSAGE = "http://117.53.153.156"; // 轮巡的server push 功能，相应代码移植到mobile版本中即可.
	public static String IMAGEURL = "http://117.53.153.156"; // 
	public static String JSON_GET_ROOT_CATEGORY = SERVERUPR + "index.php?m=api&a=get_child&catid=";// 产品根目录  －－－ JSON
	public static String JSON_GET_PRODUCT_LIST = SERVERUPR + "index.php?m=api&a=getproductlist&catid=";// 产品列表 “&catid＝”传参数

	//http://117.53.153.156/klink/index.php?m=api&a=getproductlist&catid=343//ͨ������ò�Ʒ�б��JSON
	public static String JSON_GET_ARTICLELIST = SERVERUPR + "index.php?m=article_api&a=getarticlelist&catid=";
	public static String JSON_GET_ABOUT_CATEGORY = SERVERUPR + "index.php?m=article_api&a=getarticlecate&catid=16";//���ABOUT USĿ¼
	public static String JSON_GET_ABOUT_CATEGORY_SUB = SERVERUPR + "index.php?m=article_api&a=getarticlecate&catid=";//���ABOUT US SUB Ŀ¼ 07 31
	public static String JSON_GET_DOWNLOAD_CATEGORY = SERVERUPR + "index.php?m=article_api&a=getarticlecate&catid=121";//���ABOUT USĿ¼

	
	public static String JSON_GET_ABOUT_BusinessOpportunity = SERVERUPR + "index.php?m=article_api&a=getarticlecate&catid=21";//���Business OPPORTUNITYĿ¼
	public static String JSON_GET_CONTACTUS = SERVERUPR + "index.php?m=article_api&a=getarticlecate&catid=35";//���Contact
	public static String JSON_GET_FAQ = SERVERUPR + "index.php?m=article_api&a=getarticlecate&catid=33";//���FAQ
	public static String JSON_GET_NEWSANDEVENT = SERVERUPR + "index.php?m=article_api&a=getarticlecate&catid=26";//
	public static String JSON_GET_SUBNEWSLIST = SERVERUPR + "index.php?m=article_api&a=getarticlecate&catid=";//���2�����࣬����

	
	public static String LOADDETAILURL = SERVERUPR + "index.php?m=article_api&a=details&id=";


	public static String ABOUT_US_MessagefromtheBoardOfDirectors = SERVERUPR + "index.php?m=article_api&a=details&id=9";
	public static String ABOUT_US_LatestIssue = SERVERUPR + "index.php?m=article_api&a=details&id=10";
	public static String ABOUT_US_KLINKACHIEVEMENT = SERVERUPR + "index.php?m=article_api&a=details&id=12";
	public static String ABOUT_US_WHYCHOOSEKLINK = SERVERUPR + "index.php?m=article_api&a=details&id=11";

	public static String GET_MESSAGE_KLINKUSER = SERVERUPR + "index.php?m=api&a=get_user_message_list&uid=";
	public static String UPDATE_MESSAGE = SERVERUPR + "index.php?m=api&a=update_message&uid=";
	public static String SEARCH_CONTENT = SERVERUPR + "index.php?m=api&a=search&keywords=";
	//http://117.53.153.156/klink/index.php?m=article_api&a=getarticlelist&catid=16 //ͨ�����ID�������Ŀ¼
	//http://janalove.com/klink/index.php?m=api&a=get_user_message_list&uid=25
	public static String APP_LANGUAGE = "";//language
	public static String APP_USER_LANGUAGE = "";
	public static boolean APP_REGUSER = false;
	
	public static String YOUTUBEURL = "";
	public static ArrayList<AroundMeLocation> items1 = new ArrayList<AroundMeLocation>();
	public static ArrayList<AroundMeShop> items2 = new ArrayList<AroundMeShop>();
	public static AroundMeShop aroundmeshop = null;
	public static SearchResult searchresultbean = null;
	public static String searchkeywords = null;
	
	public static String STARTX;
	public static String STARTY;
	public static String TARGETX;
	public static String TARGETY;
	
	public static String USERID = ""; //for test
	public static boolean VIDEO_FROM;
	public static String VIDEO_Name;
    public static String VIDEO_URL;
    public static String catid;
    
    
    public static List<SubCateBean> BUSINESS_OPP_LIST = new ArrayList<SubCateBean>();

	/*
	
				Johor
				SJH113 = 1.485903,103.886115
				SJH116 = 1.863859,102.939337
				SJH70 = 2.015822,103.065624
				SJH91 = 1.542263,103.63154
				
				Kedah
				SKD131 = 6.081163,100.36172
				
				Kelantan
				SKT104 = 6.164460, 102.281183
				SKT107 = 5.913929,102.244745
				
				Kuala Lumpur
				SWP60 = 3.128454,101.713638
				SWP99 = 3.206938,101.719172
				
				Labuan
				SLB93 = 5.279987,115.23972
				
				Malacca
				SML95 = 2.228027,102.225712
				
				Negeri Sembilan
				SNS121 = 2.693721,102.000264
				SNS129 = 2.801202,101.806997
				
				Pahang
				SPH86 = 3.828806,103.302018
				SPH124 = 3.797108,101.861672
				SPH135 = 3.770497,102.547633
				
				Penang
				SPN026 = 5.425979,100.318152
				SPN115 = 5.441743,100.386575
				SPN118 = 5.195161,100.492313
				SPN136 = 5.33185,100.293012
				
				Perak
				SPR03 = 4.615951,101.119583
				SPR92 = 4.005801,101.0218
				SPR94 = 4.208291,100.655332
				
				Sabah
				SSB109 = 4.242489,117.887447  
				SSB122 = 5.027132,118.298548
				SSB123 = 5.265303,116.328209
				SSB128 = 5.947768, 116.089589
				SSB133 = 5.84847,118.05555
				
				Sarawak
				SSR07 = 1.553082, 110.363955
				SSR15 = 4.413733,114.006108
				SSR134 = 2.291886,111.835101
				
				Selangor
				SSL102 = 2.992011,101.799761
				SSL105 = 3.210379,101.562327
				SSL110 = 2.84713,101.519346
				SSL111 = 3.135259,101.769887
				SSL117 = 3.320913,101.271178
				SSL120 = 3.319612,101.57695
				SSL74 = 3.010115,101.43979
				SSL125 = 3.086731,101.623411
				SSL126 = 3.031412,101.534952
				SSL132 = 2.982591,101.610904
				
				Terengganu
				STR112 = 5.166173,103.232167
				STR130 = 5.314613,103.119971
	
	 */
    
	public static final int US_ABOUT = 0x01;
	public static final int US_CONTACT = 0x02;
	public static final int US_NEWSEVENTS = 0x03;
	public static final int US_DOWNLOAD = 0x04;
	public static final int US_BUSINESS = 0x05;
	public static final int US_PRODUCT = 0x06;
	public static final int US_INBOX = 0x07;
	public static final int US_SEARCH = 0x08;
	public static final int US_AROUNDME = 0x09;
	public static final int US_DOWNLOAD_RECORD = 0x10;
	
	public static final int MENU_SEARCH = 0x01;
	public static final int MENU_INBOX = 0x02;
	public static final int MENU_FAVORITE = 0x04;
	
	
	public static boolean CoverStringNull(String str) {
		return str == null || str.length() == 0;
	}
	
	public static boolean isEmail(String str) {
		String email = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(email);
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
}
