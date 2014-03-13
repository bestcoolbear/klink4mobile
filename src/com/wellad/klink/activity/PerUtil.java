package com.wellad.klink.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.wellad.klink.business.model.SavePushMessage;

import android.content.Context;
import android.content.SharedPreferences;

public class PerUtil {

	private Context ct;
	
	public PerUtil(Context ct){
		this.ct = ct;
	}
	
	public void savePushMessage(SavePushMessage message)
	{
		 SharedPreferences mSharedPreferences = ct.getSharedPreferences("base64", Context.MODE_PRIVATE);  
 	        try {  
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	            ObjectOutputStream oos = new ObjectOutputStream(baos);  
	            oos.writeObject(message);  
	            String personBase64 = new String(Base64.encode(baos.toByteArray()));  
	            SharedPreferences.Editor editor = mSharedPreferences.edit();  
	            editor.putString("person", personBase64);  
	            editor.commit();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	}

	 public SavePushMessage getObjectInfo() {  
	        try {  
	            SharedPreferences mSharedPreferences = ct.getSharedPreferences("base64", Context.MODE_PRIVATE);  
	            String personBase64 = mSharedPreferences.getString("person", "");  
	            byte[] base64Bytes = Base64.decode(personBase64.getBytes());  
	            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);  
	            ObjectInputStream ois = new ObjectInputStream(bais);  
	            SavePushMessage person = (SavePushMessage) ois.readObject(); 
	            return person;
	        } catch (Exception e) {  
	            e.printStackTrace();  
	            return null;
	        }  
	    }  
}
