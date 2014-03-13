package com.wellad.klink.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.wellad.klink.business.Config;

import android.os.Environment;
import android.util.Log;

public class FileUtil {

	public static String getSdRoot() {
		File sdDir = null;
		String sdroot = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}

		if (sdDir != null) {
			sdroot = sdDir.toString();
		}
		return sdroot;
	}

	public static boolean creatDir(String dirname) {
		File sdDir = null;
		String sdroot = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();//
		}
		if (sdDir != null) {
			sdroot = sdDir.toString();
		}

		File newdir = new File(sdroot + "/" + dirname);
		if (!newdir.exists()) {
			newdir.mkdir();
			return true;
		} else {
			return false;
		}
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(getSdRoot() + fileName);
		return file.exists();
	}

	public static void SavedToText(String filepath, String filename,
			String stringToWrite) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String foldername = getSdRoot() + filepath;
			// Log.i("file path",foldername);
			File targetFile = new File(foldername + filename);
			OutputStreamWriter osw;
			try {
				if (!targetFile.exists()) {
					targetFile.createNewFile();
					osw = new OutputStreamWriter(new FileOutputStream(
							targetFile), "utf-8");
					osw.write(stringToWrite);
					osw.close();
				} else {
					targetFile.delete();
					targetFile.createNewFile();
					osw = new OutputStreamWriter(new FileOutputStream(
							targetFile), "utf-8");
					osw.write(stringToWrite);
					osw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		FileOutputStream output = null;
		try {
			file = new File(getSdRoot() + "/" + path + fileName);
			output = new FileOutputStream(file);

			byte buffer[] = new byte[5 * 1024];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	//
	// Load Favorite Root
	public static List<String> loadFavoriteRoot() {
		List<String> resultarray = new ArrayList<String>();
		String sdroot = getSdRoot();
		String appfolder = sdroot + "/" + "KLinkAPP/";
		File file = new File(appfolder);
		if (file.exists()) {
			String[] foldernames = file.list();
			for (int i = 0; i < foldernames.length; i++) {
				File tmp = new File(appfolder + foldernames[i]);
				if (tmp.exists() && tmp.isDirectory()) {
					resultarray.add(tmp.getName());
				}
			}
		}
		return resultarray;
	}

	public static List<String> loadFavoriteSubRoot(String foldername) {
		List<String> resultarray = new ArrayList<String>();
		String sdroot = getSdRoot();
		String appfolder = sdroot + "/" + "KLinkAPP/" + foldername + "/";
		File file = new File(appfolder);
		if (file.exists()) {
			String[] foldernames = file.list();
			for (int i = 0; i < foldernames.length; i++) {
				File tmp = new File(appfolder + foldernames[i]);
				if (tmp.exists()
						&& !tmp.isDirectory()
						&& (foldernames[i].endsWith(".html") || foldernames[i]
								.endsWith(".3gp"))) {
					if (Config.APP_USER_LANGUAGE.equals("cn")) {
						if (foldernames[i].endsWith("_cn.html")
								|| foldernames[i].endsWith("_cn.3gp")) {
							resultarray.add(tmp.getName());
						}
					}
					if (Config.APP_USER_LANGUAGE.equals("en")) {
						if (foldernames[i].endsWith("_en.html")
								|| foldernames[i].endsWith("_en.3gp")) {
							resultarray.add(tmp.getName());
						}
					}
					if (Config.APP_USER_LANGUAGE.equals("mala")) {
						if (foldernames[i].endsWith("_mala.html")
								|| foldernames[i].endsWith("_mala.3gp")) {
							resultarray.add(tmp.getName());
						}
					}
				}
			}
		}
		return resultarray;
	}

}
