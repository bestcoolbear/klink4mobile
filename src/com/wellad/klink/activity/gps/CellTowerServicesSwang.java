package com.wellad.klink.activity.gps;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wellad.klink.activity.AroundMeShopActivity;
import com.wellad.klink.activity.WebActivity;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.model.AroundMeShop;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;  
import android.net.wifi.WifiConfiguration;  
import android.net.wifi.WifiInfo;  
import android.net.wifi.WifiManager;  
import android.net.wifi.WifiManager.WifiLock;  
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.wellad.klink.KLinkApplication;


public class CellTowerServicesSwang extends Service  {
	
	private Timer timer;
	private String strOutput ;
	private String lat;
	private String longitude;
	
	
	private List<LocationBean> locationlist;
	
	private double[] distance;
	private double[] radius;
	
	private Activity activity; 
	double latt;
	double logg;
	private ProgressDialog proDialog;
	private String startoption = "";
	StringBuffer sb = new StringBuffer();
	 
	int mcc = 0;
    int mnc = 0;
    int cid = 0;
    int lac = 0;
  	private String API_KEY="AIzaSyDHTvP4pI_YZqEZG-RQXsMiRwSbqd4VYjk";

    ComponentName cn;
	public void startLocationTimer(){
		
     //   Thread.setDefaultUncaughtExceptionHandler(this);    
		
		Log.i("start option",startoption);
		if(startoption.equals("ON")){
			Global.serviceoption = "ON";
			activity = ((KLinkApplication)getApplication()).getUiactivty();
		    if(activity != null){
		    	proDialog = ProgressDialog.show(activity, "Satellite positioning...","Please Wait....", true, true);
		    }
		}else if(startoption.equals("ALWAYSON")){
			Global.serviceoption = "ALWAYSON";
		}
		
		Log.i("start service option",Global.serviceoption);
		
		

		if(timer != null)
			timer = null;
		timer = new Timer();
		timer.schedule(task, 1000,15000); //every 30 seconds refrush when always on
	}
	
	 TimerTask task = new TimerTask(){   
	        public void run() {   
	        	
	        		Message message = new Message();       
	        		message.what = 1;       
	        		sendMsg(1);
	        }   
	 }; 

	
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	public void onCreate(){
		Log.i("gps service","onCreate");
		//startLocationTimer();
	}
	
	
	public void onDestroy() {
		Log.i("service destory","service destroy");
		task.cancel();
		super.onDestroy();
	}
	
	public void onStart(Intent intent, int startId) {
		Log.i("gps service","onStart");
		Bundle bunde = intent.getExtras(); 
		startoption = bunde.getString("STARTOPTION");
		Log.i("service param", startoption);
		startLocationTimer();
		super.onStart(intent, startId);
	}

	public class LocalBinder extends Binder {
		CellTowerServicesSwang getService() {
			return CellTowerServicesSwang.this;
		}
	}
	


	private final IBinder mBinder = new LocalBinder();
	
	private Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {//
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					break;
				case 1:
//				
//					if(proDialog != null){
//						proDialog.dismiss();
//					}

					downloadAndShowInternetFile();
					break;
				case 2:
					break;

				case -1:

					break;
					
				case 3:
					if(proDialog != null){
						proDialog.dismiss();
						
					}
					stopService(new Intent(Global.ACTION1));
//		        	Intent intent = new Intent();//
//			     	intent.setClass(CellTowerServicesSwang.this,com.smsbuy.qponsflash.activity.SorryActivity.class);
//			     	intent.putExtra("sorrytype", "x2");
//			     	 if(cn != null){
//						 intent.setAction(cn.getClassName());
//					 }	
//			     	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					startActivity(intent);
				
					break;
					
				case 4:
					break;
				case 5:
 					strOutput+=sb.toString();
						if(proDialog != null){
							proDialog.dismiss();
						}
						
					//	null{ "location": {  "lat": 31.2213186,  "lng": 121.37369899999997 }, "accuracy": 33.0}
						
						strOutput = strOutput.replaceAll("null", "");
						try{
							JSONObject jsonObj = new JSONObject(strOutput).getJSONObject("location"); 
 							String lat = jsonObj.getString("lat"); 
							String lng = jsonObj.getString("lng"); 
							
							
							Double latme = Double.parseDouble(lat);
							Double lngme = Double.parseDouble(lng);
							Log.i("lat ==",lat);
							Log.i("lng ==",lng);
							distance = new double[Config.items2.size()];
							for(int i = 0; i < Config.items2.size(); i ++)
							{
									AroundMeShop ashop = Config.items2.get(i);
									Double shoplat =  Double.parseDouble(ashop.getShopgpsx().trim());
									Double shoplng =  Double.parseDouble(ashop.getShopgpsy().trim());
									double value = Distance(lngme,latme,shoplng,shoplat);
									String tempresult = value + "";
							   		if(tempresult.length() > 10){
							   			tempresult = tempresult.substring(0,11);
							   		}
							   		double rvalue = Double.parseDouble(tempresult);
									distance[i] =  rvalue;
									ashop.setShopnearestid(distance[i]+"");//set nearest id
							}
							Double temp = 0.0;
							for(int i = 0; i < distance.length; i ++){ 
								for(int j = i +1 ; j < distance.length; j ++)
								{
									if(distance[i] > distance[j]){
										 temp = distance[i];
										 distance[i] = distance[j];
										 distance[j] = temp;
									}
								}
							}
							
//							for(int i = 0 ;i < distance.length; i ++){ //锟斤拷锟斤拷锟�//								Log.i("distance sorted:",distance[i] + "");
//							}
							
							for(int i = 0 ; i < Config.items2.size(); i ++){
								AroundMeShop ashop = Config.items2.get(i);
								if(ashop.getShopnearestid().equals(distance[0] + "")){
									Log.i("nearest shop is",ashop.getShoplocationame() + "," + ashop.getShopname());
									
									if(Config.aroundmeshop == null){
								    	   Config.aroundmeshop = new AroundMeShop(ashop.getShoplocationame(),ashop.getShopname(),ashop.getShopgpsx(),ashop.getShopgpsy(),ashop.getAreaname());
								     }
									
									Config.TARGETX = ashop.getShopgpsx();
									Config.TARGETY = ashop.getShopgpsy();
									
									 Config.STARTX = lng;
									 Config.STARTY = lat;
  									 stopService(new Intent(Global.ACTION1));

									 /*
									 Intent intent = new Intent();//
		    						 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									 intent.setClass(CellTowerServicesSwang.this, AroundMeShopActivity.class);
									 startActivity(intent);
									 */
										String url = "file:///android_asset/" + ashop.getShopname() + ".html";
										WebActivity.launch(activity, Config.US_AROUNDME, ashop.getShoplocationame(), url, false);


									 
									break;
								}
							}

						}catch(Exception e)
						{
							
						}
						
						//GeneralTools.showAlertDialog(strOutput, activity);
						stopService(new Intent(Global.ACTION1));

	                
	                	Log.i("strOutput",strOutput);

                        
                        
					break;
				case 8:
					if(proDialog != null){
						proDialog.dismiss();
						Toast.makeText(CellTowerServicesSwang.this, "The location can not be determined.",Toast.LENGTH_SHORT).show(); 
						stopService(new Intent(Global.ACTION1));
					}
					break;
				}
			}
			super.handleMessage(msg);
		}
	};
	
	
	public static double Distance(double n1, double e1, double n2, double e2)
    {
        double jl_jd = 102834.74258026089786013677476285;
        double jl_wd = 111712.69150641055729984301412873;
        double b = Math.abs((e1 - e2) * jl_jd);
        double a = Math.abs((n1 - n2) * jl_wd);
        return Math.sqrt((a * a + b * b));
    }

	

    private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler.sendMessage(msg);
	}
    
    
    
    public  StringBuffer getGoogleLat(int cid,int lac,int mcc,int mnc){
    	
    	StringBuffer sb = null;
  
    	try {
    	JSONObject holder = new JSONObject();
    	JSONArray array = new JSONArray();
    	JSONObject data = new JSONObject();
    	
    	holder.put("carrier", "T-Mobile");
    	holder.put("radioType", "gsm");
    	holder.put("homeMobileNetworkCode", mnc);
    	holder.put("homeMobileCountryCode", mcc);
    	
    	data.put("cellId", cid); // 
    	data.put("locationAreaCode", lac);//
    	data.put("mobileCountryCode", mcc);//
    	data.put("mobileNetworkCode", mnc);//
    	data.put("age", "0");//
    	data.put("signalStrength", "100");//

    	array.put(data);
    	holder.put("cellTowers", array);

    	int timeoutConnection = 100000;
    	int timeoutSocket = 100000;

    	
    	 HttpParams httpParameters = new BasicHttpParams();
    	 HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
         HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
         DefaultHttpClient client = new DefaultHttpClient(httpParameters);
         HttpPost post = new HttpPost("https://www.googleapis.com/geolocation/v1/geolocate?key=" + API_KEY);
         post.addHeader("Content-Type","application/json");
    	 StringEntity se = new StringEntity(holder.toString());
    	 Log.i("JSON STRING",holder.toString());
    	 post.setEntity(se);
    	 HttpResponse resp = client.execute(post);
    	 int responsecode = resp.getStatusLine().getStatusCode();

    	 if(responsecode == 200){
    	 HttpEntity entity = resp.getEntity();
         BufferedReader br = new BufferedReader(
    	 new InputStreamReader(entity.getContent()));
    		sb = new StringBuffer();
    		String result = br.readLine();
	    	while (result != null) {
	    		sb.append(result);
	    		result = br.readLine();
	    	}
    	  }else{
      		sendMsg(8);

    	  }
    	}
    	 catch(Exception e){
    		e.printStackTrace();
    		sendMsg(8);
    	}
    	
    	if(sb != null && sb.length() > 0){
    		sendMsg(4);
       	 //Log.i("JSON response",sb.toString());

    		return sb;
    	}else{
    		return null;
    	}
    }
    
    
  public  StringBuffer getWiFiLocation(List<ScanResult> wifiList){
    	StringBuffer sb = null;
    	try {
    	JSONObject holder = new JSONObject();
    	JSONArray array = new JSONArray();
    	for(int i = 0; i < wifiList.size(); i ++){
       	 	ScanResult sr = wifiList.get(i);
        	JSONObject data = new JSONObject();
    		data.put("macAddress", sr.BSSID); // 
    		data.put("signalStrength", sr.level);//
    		data.put("age", 0);//
    	
        	array.put(data);
    	}

    	holder.put("wifiAccessPoints", array);
    	int timeoutConnection = 100000;
    	int timeoutSocket = 100000;
    	 HttpParams httpParameters = new BasicHttpParams();
    	 HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
         HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
         DefaultHttpClient client = new DefaultHttpClient(httpParameters);
         HttpPost post = new HttpPost("https://www.googleapis.com/geolocation/v1/geolocate?key=" + API_KEY);
         post.addHeader("Content-Type","application/json");
    	 StringEntity se = new StringEntity(holder.toString());
    	 Log.i("JSON STRING",holder.toString());
    	 post.setEntity(se);
    	 HttpResponse resp = client.execute(post);
    	 int responsecode = resp.getStatusLine().getStatusCode();
    	 Log.i("response code",responsecode + "");
    	 if(responsecode == 200){
    	 HttpEntity entity = resp.getEntity();
         BufferedReader br = new BufferedReader(
    	 new InputStreamReader(entity.getContent()));
    		sb = new StringBuffer();
    		String result = br.readLine();
	    	while (result != null) {
	    		sb.append(result);
	    		result = br.readLine();
	    	}
    	  }else{
      		sendMsg(8);
    	  }
    	}
    	 catch(Exception e){
    		e.printStackTrace();
    		sendMsg(8);
    	}
    	if(sb != null && sb.length() > 0){
    		sendMsg(4);
    		return sb;
    	}else{
    		return null;
    	}
    }
    
    public void downloadAndShowInternetFile() {

    		TelephonyManager mTelephonyManager = (TelephonyManager) this.getSystemService(Service.TELEPHONY_SERVICE);
    		GsmCellLocation mGsmCellLocation = (GsmCellLocation) mTelephonyManager.getCellLocation();
    		if(mGsmCellLocation == null){
    			Log.i("mGsmCellLocation Null", "mGsmCellLocation");
    		}
    	
    	 	manager = (WifiManager) this .getSystemService(Context.WIFI_SERVICE); 
    	 	WifiInfo wifiInfo = manager.getConnectionInfo();
    	 	wifiInfo.getBSSID();
    	 	wifiInfo.getSSID();
    	 	Log.i("BSSID is ",wifiInfo.getBSSID());
    	 	Log.i("SSID is ",wifiInfo.getSSID());

    	 	int strength = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
    	 	Log.i("TAG", "WIFI MAC is:" + wifiInfo.getMacAddress()); 
    	 
    	 	receiverWifi = new WifiReceiver(); 
            registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); 
    	 	manager.startScan();
    	 	
    	
          }
    WifiReceiver receiverWifi;
    WifiManager manager;
    List<ScanResult> wifiList; 
    class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
        	if (sb != null){
        		sb = null;
        	}
        	sb = new StringBuffer();
            wifiList = manager.getScanResults();
            Log.i("get scan result size",wifiList.size() + "");
             unregisterReceiver(receiverWifi); 
             Thread t = new Thread(new Runnable(){
 				public void run() {
 					// TODO Auto-generated method stub
 					 sb = getWiFiLocation(wifiList);
 					 if(sb != null &&sb.length() > 0){
 			            	sendMsg(5);
 			            }else{
 			            	sendMsg(3);
 			            }
 				}
             });
             t.start();
        }
   } 
}
