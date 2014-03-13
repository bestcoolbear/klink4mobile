package com.wellad.klink.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class HttpDownloader {
	
	
	private static URL url=null;
	private static Bitmap bitmap;  
	 
    public static int downfile(String urlStr,String path,String fileName)  
    {  
        if(FileUtil.isFileExist(path+fileName))  
            {return 1;}  
        else{  
          
        try {  
            InputStream input=null;  
            input = getInputStream(urlStr);  
            File resultFile=FileUtil.write2SDFromInput(path, fileName, input);  
            if(resultFile==null)  
            {  
                return -1;  
            }  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
          
        }  
        return 0;  
    }  
    
    public static int downImageFile(String urlStr,String path,String fileName){
    	   try {
			byte[] data = getImage(urlStr);
			if(data!=null){       
				if(bitmap != null){
					bitmap = null;
				}
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap   
                saveFile(bitmap,path, fileName);   
                return 0;
             }else{
            	 return -1;
             }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}         
   	   return -1;
    }
    
    public static byte[] getImage(String path) throws Exception{      
        URL url = new URL(path);      
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();      
        conn.setConnectTimeout(5 * 1000);      
        conn.setRequestMethod("GET");      
        InputStream inStream = conn.getInputStream();      
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){      
            return readStream(inStream);      
        }      
        return null;      
    }       
    
    public static byte[] readStream(InputStream inStream) throws Exception{      
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();      
        byte[] buffer = new byte[1024];      
        int len = 0;      
        while( (len=inStream.read(buffer)) != -1){      
            outStream.write(buffer, 0, len);      
        }      
        outStream.close();      
        inStream.close();      
        return outStream.toByteArray();      
    }   
    
    
    public static void saveFile(Bitmap bm, String path,String fileName) throws IOException {   

        File myCaptureFile = new File(getSdRoot() + "/" + path + fileName);   
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));   
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);   
        bos.flush();   
        bos.close();   
    }    
    
    public static String getSdRoot(){
		 File sdDir = null; 
		 String sdroot = null;
		 boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		 if(sdCardExist)   
		 {                               
			 sdDir = Environment.getExternalStorageDirectory();
		 }   
		 
		 if(sdDir != null){
			 sdroot = sdDir.toString();
		 }
		 return sdroot;
	}
    
  //���ڵõ�һ��InputStream�����������ļ�����ǰ����Ĳ��������Խ����������װ����һ������  
       public static InputStream getInputStream(String urlStr) throws IOException  
       {       
           InputStream is=null;  
            try {  
                url=new URL(urlStr);  
                HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();  
                is=urlConn.getInputStream();  
                  
            } catch (MalformedURLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
            return is;  
       }  
}
