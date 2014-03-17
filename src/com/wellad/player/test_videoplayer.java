package com.wellad.player;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;


import com.wellad.klink.R;
import com.wellad.klink.business.Config;
import com.wellad.klink.util.FileUtil;

import android.app.Activity;  
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;  
import android.net.Uri;  
import android.os.AsyncTask;
import android.os.Bundle;  
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;  
import android.view.KeyEvent;
import android.view.SurfaceView;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.SeekBar;  
import android.widget.Toast;
  
public class test_videoplayer extends Activity {  
    private SurfaceView surfaceView;  
    private Button btnPause, btnPlayUrl, btnStop,downloadbutton;  
    private SeekBar skbProgress;  
    private Player player;  

    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);  
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);  
        surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);  
  
        btnPlayUrl = (Button) this.findViewById(R.id.btnPlayUrl);  
        btnPlayUrl.setOnClickListener(new ClickEvent());  
  
        btnPause = (Button) this.findViewById(R.id.btnPause);  
        btnPause.setOnClickListener(new ClickEvent());  
  
        btnStop = (Button) this.findViewById(R.id.btnStop);  
        btnStop.setOnClickListener(new ClickEvent());  
        
        downloadbutton = (Button)this.findViewById(R.id.videodownload);
        downloadbutton.setOnClickListener(new DownEvent());  

        if(Config.VIDEO_FROM == true){
        	downloadbutton.setVisibility(View.VISIBLE);
        }else{
        	downloadbutton.setVisibility(View.INVISIBLE);
        }
  
        skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);  
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());  
        player = new Player(surfaceView, skbProgress);  
  
    }  
    
    class DownEvent implements OnClickListener{
    	 public void onClick(View arg0) {  
    		 
    		 Config.VIDEO_Name = "Test";
    		 Config.VIDEO_URL = "rtsp://r8---sn-p5qlsu7e.c.youtube.com/CiILENy73wIaGQneB4yyodyDbBMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp";
    		 
    		 
    		 if(Config.VIDEO_Name != null && Config.VIDEO_URL != null)
    		 {
    			 if(player != null){
    	                player.stop();  
    			 }
    			 
 				FileUtil.creatDir("KLinkAPP/" + "Download");

   		  	  new YouTubePageStreamUriGetter().execute( Config.VIDEO_URL,Config.VIDEO_Name + "_" + Config.APP_USER_LANGUAGE + ".3gp");

    		 }
    	 }
    }
  
    class ClickEvent implements OnClickListener {  
  
        @Override  
        public void onClick(View arg0) {  
            if (arg0 == btnPause) {  
                player.pause();  
            } else if (arg0 == btnPlayUrl) {  
            	//07-29 17:54:53.653: I/Video url for playing=========>>>>>(3635): rtsp://r8---sn-p5qlsu7e.c.youtube.com/CiILENy73wIaGQneB4yyodyDbBMYDSANFEgGUgZ2aWRlb3MM/0/0/0/video.3gp
            	
                String url= Config.YOUTUBEURL;
                player.playUrl(url);  
            } else if (arg0 == btnStop) {  
                player.stop();  
            }  
  
        }  
    }  
  
    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {  
        int progress;  
  
        @Override  
        public void onProgressChanged(SeekBar seekBar, int progress,  
                boolean fromUser) {  
            this.progress = progress * player.mediaPlayer.getDuration()  
                    / seekBar.getMax();  
        }  
  
        @Override  
        public void onStartTrackingTouch(SeekBar seekBar) {  
  
        }  
  
        @Override  
        public void onStopTrackingTouch(SeekBar seekBar) {  
            // seekTo()�Ĳ����������ӰƬʱ������֣�������seekBar.getMax()��Ե�����  
            player.mediaPlayer.seekTo(progress);  
        }  
    }  
  
    
    //////////////////////////////////////////////////////////////////
    
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
	private void sendMsg(int flag)
	{
	    Message msg = new Message();
	    msg.what = flag;
	    registerHandler.sendMessage(msg);
	}
	Handler registerHandler = new Handler() {
		 
	    public void handleMessage(Message msg)
	    {//
	      if (!Thread.currentThread().isInterrupted())
	      {
	        switch (msg.what)
	        {
	          case 1:
	        	  	Toast.makeText(getApplicationContext(), "Download video successful!",
	            			     Toast.LENGTH_SHORT).show();
	        	  break;
	          case 2:
	        		Toast.makeText(getApplicationContext(), "Connection failed, please try again!",
           			     Toast.LENGTH_SHORT).show();
	        	  break;
	        }
	      }
	      super.handleMessage(msg);
	    }
   };
	public ArrayList<Video> getStreamingUrisFromYouTubePage(String ytUrl,String filename)
	        throws IOException {
	    if (ytUrl == null) {
	    	 sendMsg(2);
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
	        Log.i("Couldn't find any URLs and corresponding signatures","");
	        return null;
	    }

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

	    for (String format : typeMap.keySet()) {
	        Meta meta = typeMap.get(format);

	        if (foundArray.containsKey(format)) {
	            Video newVideo = new Video(meta.ext, meta.type,
	                    foundArray.get(format));
	            videos.add(newVideo);
	            Log.i("video ext ===",newVideo.ext);
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
	            		              File f = new File(storagePath + "/KLinkAPP/Download/",fileName);
	            		              Log.i("store path====",storagePath + "/KLinkAPP/Download/");

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
	            		          
	            		          sendMsg(1);
	            		    
	            		      	Log.i("download success","download success");
	            		     }catch (MalformedURLException mue) {
	            		            mue.printStackTrace();
	            		            
	            		            sendMsg(2);
	            		     } catch (IOException ioe) {
	            		            ioe.printStackTrace();
	            		       
	            		            sendMsg(2);
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
	            	}else{
	            		  sendMsg(2);
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
    progressDialog = ProgressDialog.show(test_videoplayer.this, "",
            "Downloading video,please wait...", true);
}

public boolean onKeyDown(int keyCode, KeyEvent event)  
{  
    if (keyCode == KeyEvent.KEYCODE_BACK )  
    {  
    	if(player != null){
    		player.stop();
    	}
    	
    	test_videoplayer.this.finish();

    }
    return false;  
}

@Override
protected String doInBackground(String... params) {
    String url = params[0];
    try {
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

            return retVidUrl;
        }
    } catch (Exception e) {
        Log.i("Couldn't get YouTube streaming URL", "");
        sendMsg(2);
    }
    Log.i("Couldn't get stream URI for " + url,"");
    return null;
}

@Override
protected void onPostExecute(String streamingUrl) {
    super.onPostExecute(streamingUrl);
  //  Log.i("streamingUrl",streamingUrl);
    progressDialog.dismiss();
    if (streamingUrl != null) {
                     /* Do what ever you want with streamUrl */
    }else{
    	sendMsg(2);
    }
}
}
    
    ///
}  