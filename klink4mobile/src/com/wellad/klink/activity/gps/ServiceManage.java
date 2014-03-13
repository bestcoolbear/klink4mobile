package com.wellad.klink.activity.gps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ServiceManage {
	
	
	public static void startService(String values, Context ct) {
		Log.i("start service","start service");
		
		stopBServices(ct);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("STARTOPTION", values);
		intent.putExtras(bundle);
		intent.setAction(Global.ACTION1);
		ct.startService(intent);

	}

	public static void stopBServices(Context ct) {
		ct.stopService(new Intent(Global.ACTION1));
	}
	public static void stopMessageServices(Context ct)
	{
		ct.stopService(new Intent(Global.ACTION2));
	}
	
	public static void startMessageServices(Context ct)
	{
		stopMessageServices(ct);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		intent.putExtras(bundle);
		intent.setAction(Global.ACTION2);
		ct.startService(intent);
	}
	
}
