package com.wellad.klink.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.wellad.klink.R;
import com.wellad.klink.activity.InboxActivity;
import com.wellad.klink.activity.PerUtil;
import com.wellad.klink.business.Config;
import com.wellad.klink.business.GeneralTools;
import com.wellad.klink.business.model.PushMessage;
import com.wellad.klink.business.model.SavePushMessage;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Roy
 *
 */
public class MessageServicesSwang extends Service {

	private Notification mNotification;
	private NotificationManager mManager;
	private String messagetitle;
	private String messagedesc;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		initNotifiManager();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		new PollingThread().start();
	}

	private void initNotifiManager() {
		mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_launcher;
		mNotification = new Notification();
		mNotification.icon = icon;
		mNotification.tickerText = "New Message";
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
	}

	private void showNotification() {
		mNotification.when = System.currentTimeMillis();
		// Navigator to the new activity when click the notification title
		Intent i = new Intent(this, InboxActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		mNotification.setLatestEventInfo(this, messagetitle, messagedesc, pendingIntent);
		mManager.notify(0, mNotification);
	}

	/**
	 * thread 妯℃嫙鍚慡erver杞鐨勫紓姝ョ嚎绋�
	 */
	int count = 0;

	class PollingThread extends Thread {
		@Override
		public void run() {
			// System.out.println("Polling...");
			count++;
			if (count >= 1000 * 10) {
				count = 0;
			}
			
			if (count % 10 == 0) {
				Thread longinthread = new Thread(new getMessageHandler());
				longinthread.start();
			}

		}
	}

	class getMessageHandler implements Runnable {
		public void run() {
			ArrayList<PushMessage> message = GeneralTools.getPushMessage(Config.USERID);
			if (message != null && message.size() > 0) {
				// save
				PerUtil pu = new PerUtil(MessageServicesSwang.this);
				SavePushMessage getsp = pu.getObjectInfo();
				if (getsp == null) { // no message
					SavePushMessage sp = new SavePushMessage();
					sp.setMessagelist(message);
					pu.savePushMessage(sp);
				} else {// already message need append new
					ArrayList<PushMessage> oldlist = getsp.getMessagelist();
					ArrayList<PushMessage> newlist = new ArrayList<PushMessage>();
					// remove same message
					for (int i = 0; i < oldlist.size(); i++) {
						PushMessage oldmessage = oldlist.get(i);
						for (int j = 0; j < message.size(); j++) {
							if ((oldmessage.getTitle().equals(message.get(j)
									.getTitle()))
									&& (oldmessage.getDescription()
											.equals(message.get(j)
													.getDescription()))
									&& (oldmessage.getContent().equals(message
											.get(j).getContent()))) {
								message.remove(j);
							}
						}
					}
					for (int i = 0; i < oldlist.size(); i++) {
						newlist.add(oldlist.get(i));
					}
					for (int i = 0; i < message.size(); i++) {
						newlist.add(message.get(i));
					}

					// set date and time

					getsp.setMessagelist(newlist);
					pu.savePushMessage(getsp);
				}
				// 25-08-2013
				messagetitle = "KLink-International";
				messagedesc = "Please click here to check your message";
				sendMsg(1);
			}
		}
	}

	class updateMessageHandler implements Runnable {
		public void run() {
			GeneralTools.setReadMessage(Config.USERID);
			sendMsg(2);
		}
	}

	Handler registerHandler = new Handler() {

		public void handleMessage(Message msg) {//
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {

				case 1:
					Thread longinthread = new Thread(new updateMessageHandler());
					longinthread.start();
					showNotification();
					break;
				case 2:
					messagetitle = "";
					messagedesc = "";
					break;

				}
			}
			super.handleMessage(msg);
		}
	};

	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		registerHandler.sendMessage(msg);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("Service:onDestroy");
	}

}
