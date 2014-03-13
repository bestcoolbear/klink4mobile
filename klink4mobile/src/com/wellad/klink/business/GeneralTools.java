package com.wellad.klink.business;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wellad.klink.business.model.ArticleBean;
import com.wellad.klink.business.model.ProductBean;
import com.wellad.klink.business.model.ProductCategoryBean;
import com.wellad.klink.business.model.PushMessage;
import com.wellad.klink.business.model.SearchResult;
import com.wellad.klink.business.model.SubCateBean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.ActionMode;

public class GeneralTools {
	
	public static void showAlertDialog(final String message, final Activity activity) {
		if (message != null && message.length() > 0) {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(activity)
					.setTitle("Alert")
					.setMessage(message)
					.setIcon(null)
					.setPositiveButton("Close",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							}).show();
				}
				
			});
			
		}
	}

	public static void setReadMessage(String userid) {
		if (userid != null && userid.length() > 0) {
			String requesturl = Config.UPDATE_MESSAGE + userid;
			HttpURLConnection connection = null;
			InputStream l_urlStream = null;
			String strResult = "";

			try {
				Log.i("request url", requesturl);
				URL url = new URL(requesturl);
				connection = (HttpURLConnection) url.openConnection();
				String sCurrentLine;
				String sTotalString;
				sTotalString = "";
				l_urlStream = connection.getInputStream();
				BufferedReader l_reader = new BufferedReader(
						new InputStreamReader(l_urlStream, "utf-8"));
				while ((sCurrentLine = l_reader.readLine()) != null) {
					sTotalString += sCurrentLine;
				}
				strResult = sTotalString;
				Log.i("Message update", strResult);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
				if (l_urlStream != null) {
					try {
						l_urlStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// 搜索
	public static ArrayList<SearchResult> getSearchResult(String keywords) {
		// http://janalove.com/klink/index.php?m=api&a=search&keywrods=m
		ArrayList<SearchResult> resultlist = new ArrayList<SearchResult>();
		if (keywords != null && keywords.length() > 0) {
			String requesturl = Config.SEARCH_CONTENT + keywords + "&lang="
					+ Config.APP_USER_LANGUAGE;
			HttpURLConnection connection = null;
			InputStream l_urlStream = null;
			String strResult = "";

			try {
				Log.i("request url", requesturl);
				URL url = new URL(requesturl);
				connection = (HttpURLConnection) url.openConnection();
				String sCurrentLine;
				String sTotalString;
				sTotalString = "";
				l_urlStream = connection.getInputStream();
				BufferedReader l_reader = new BufferedReader(
						new InputStreamReader(l_urlStream, "utf-8"));
				while ((sCurrentLine = l_reader.readLine()) != null) {
					sTotalString += sCurrentLine;
				}
				strResult = sTotalString;

				Log.i("Search Result", strResult);

				if (strResult != null && strResult.length() > 0) {
					if (strResult.contains("\"status\":1")) {

						JSONObject dataobject = new JSONObject(strResult)
								.getJSONObject("data");
						JSONArray jsonObjs = dataobject.getJSONArray("goods");
						JSONArray jsonObjsarticle = dataobject
								.getJSONArray("article");

						// Log.i("result size",jsonObjs.length() + "");
						// goods
						for (int i = 0; i < jsonObjs.length(); i++) {
							JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
							SearchResult result = new SearchResult();
							String eng_title = jsonObj.getString("eng_title");
							String mala_title = jsonObj.getString("mala_title");
							String title = jsonObj.getString("title");
							String catename = jsonObj.getString("cate_name");
							String resulttype = jsonObj
									.getString("result_type");
							result.setCategoryname(catename);
							result.setEngtitle(eng_title);
							result.setMalatitle(mala_title);
							result.setTitle(title);
							result.setResulttype(resulttype);
							resultlist.add(result);
						}

						// article

						for (int i = 0; i < jsonObjsarticle.length(); i++) {
							JSONObject jsonObj = ((JSONObject) jsonObjsarticle
									.opt(i));
							SearchResult result = new SearchResult();
							String eng_title = jsonObj.getString("eng_title");
							String mala_title = jsonObj.getString("mala_title");
							String title = jsonObj.getString("title");
							String catename = jsonObj.getString("cate_name");
							String resulttype = jsonObj
									.getString("result_type");
							result.setCategoryname(catename);
							result.setEngtitle(eng_title);
							result.setMalatitle(mala_title);
							result.setTitle(title);
							result.setResulttype(resulttype);
							resultlist.add(result);
						}

						return resultlist;
					} else {
						return null;
					}

				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
				if (l_urlStream != null) {
					try {
						l_urlStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return resultlist;
		} else {
			return null;
		}
	}

	public static ArrayList<PushMessage> getPushMessage(String userid) {
		ArrayList<PushMessage> resultlist = new ArrayList<PushMessage>();
		if (userid != null && userid.length() > 0) {
			String requesturl = Config.GET_MESSAGE_KLINKUSER + userid;
			HttpURLConnection connection = null;
			InputStream l_urlStream = null;
			String strResult = "";

			try {
				Log.i("request url", requesturl);
				URL url = new URL(requesturl);
				connection = (HttpURLConnection) url.openConnection();
				String sCurrentLine;
				String sTotalString;
				sTotalString = "";
				l_urlStream = connection.getInputStream();
				BufferedReader l_reader = new BufferedReader(
						new InputStreamReader(l_urlStream, "utf-8"));
				while ((sCurrentLine = l_reader.readLine()) != null) {
					sTotalString += sCurrentLine;
				}
				strResult = sTotalString;

				Log.i("Message", strResult);

				if (strResult != null && strResult.length() > 0) {
					if (strResult.contains("\"status\":1")) {
						JSONArray jsonObjs = new JSONObject(strResult)
								.getJSONArray("data");
						for (int i = 0; i < jsonObjs.length(); i++) {
							PushMessage result = new PushMessage();
							JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
							String title = jsonObj.getString("title");
							String desc = jsonObj.getString("description");
							String content = jsonObj.getString("detail_url");

							Date date = new Date();
							SimpleDateFormat formatter = new SimpleDateFormat(
									"dd-MM-yyyy HH:mm:ss");
							String datetime = formatter.format(date);

							// Log.i("title",title);
							// Log.i("desc",desc);
							// Log.i("content",content);
							if (!title.equals("null") && !desc.equals("null")
									&& !content.equals("null")) {
								result.setTitle(title);
								result.setDescription(desc);
								result.setContent(content);
								result.setDatetime(datetime);
								resultlist.add(result);

							}
						}
						return resultlist;
					} else {
						return null;
					}

				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
				if (l_urlStream != null) {
					try {
						l_urlStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return resultlist;
		} else {
			return null;
		}
	}

	public static ArrayList<ArticleBean> getArticleList(String catid) {
		if (catid != null && catid.length() > 0) {
			String requesturl = Config.JSON_GET_ARTICLELIST + catid + "&lang="
					+ Config.APP_USER_LANGUAGE;
			ArrayList<ArticleBean> result = new ArrayList<ArticleBean>();
			HttpURLConnection connection = null;
			InputStream l_urlStream = null;
			String strResult = "";

			try {
				Log.i("request url", requesturl);
				URL url = new URL(requesturl);
				connection = (HttpURLConnection) url.openConnection();
				String sCurrentLine;
				String sTotalString;
				sTotalString = "";
				l_urlStream = connection.getInputStream();
				BufferedReader l_reader = new BufferedReader(
						new InputStreamReader(l_urlStream, "utf-8"));
				while ((sCurrentLine = l_reader.readLine()) != null) {
					sTotalString += sCurrentLine;
				}
				strResult = sTotalString;

				// Log.i("getArticleList",strResult);

				if (strResult != null && strResult.length() > 0) {
					JSONArray jsonObjs = new JSONObject(strResult)
							.getJSONArray("articlelist");
					for (int i = 0; i < jsonObjs.length(); i++) {
						JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));

						String id = jsonObj.getString("id");
						String title = jsonObj.getString("title");

						Log.i("id", id);
						Log.i("title", title);
						ArticleBean ab = new ArticleBean();
						ab.setArticleid(id);
						ab.setArticletitle(title);
						result.add(ab);

					}

					return result;
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
				if (l_urlStream != null) {
					try {
						l_urlStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return result;
		} else {
			return null;
		}
	}

	public static ArrayList<SubCateBean> getBusinessOpportunity()// getBusinessOpportunity
	{
		String requesturl = Config.JSON_GET_ABOUT_BusinessOpportunity
				+ "&lang=" + Config.APP_USER_LANGUAGE;
		ArrayList<SubCateBean> result = new ArrayList<SubCateBean>();
		Log.i("getCategory request = ", requesturl);
		String strResult = "";
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;
		try {
			URL url = new URL(requesturl);
			connection = (HttpURLConnection) url.openConnection();
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "utf-8"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			strResult = sTotalString;
			Log.i("strResult=", strResult);
			if (strResult.equals("{\"productlist\":null}")) {

				return null;
			}

			JSONObject jsonObjs = new JSONObject(strResult)
					.getJSONObject("category");
			String cateid = jsonObjs.getString("cateid");
			String catename = jsonObjs.getString("catename");

			Log.i("cateid", cateid);
			Log.i("catename", catename);

			String subarray = jsonObjs.getString("subcatinfo");
			subarray = subarray.replaceAll("\"17\":", "");
			subarray = subarray.replaceAll("\"18\":", "");
			subarray = subarray.replaceAll("\"19\":", "");
			subarray = subarray.replaceAll("\"20\":", "");
			// Log.i("subarray",subarray);
			// subarray = subarray.substring(1, subarray.length() - 1);
			// subarray = "[" + subarray + "]";
			Log.i("subarray", subarray);

			JSONArray myarray = new JSONArray(subarray);
			// Log.i("myarray count",myarray.length() + "");

			for (int i = 0; i < myarray.length(); i++) {
				JSONObject jsonObj = ((JSONObject) myarray.opt(i));

				String catid = jsonObj.getString("subcatid");
				String cname = jsonObj.getString("subcatname");
				SubCateBean pb = new SubCateBean();
				pb.setSubcatname(cname);
				pb.setSubcatid(catid);
				result.add(pb);

				// String name = jsonObj.getString("subcatname");
				// String id = jsonObj.getString("subcatid");
				//
				// if((name != null && name.length() > 0) && (id != null &&
				// id.length() > 0)){
				// SubCateBean pb = new SubCateBean();
				// pb.setSubcatname(name);
				// pb.setSubcatid(id);
				// result.add(pb);
				// }
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (l_urlStream != null) {
				try {
					l_urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	// 锟斤拷锟�boutUS2锟斤拷锟斤拷止录 07 31
	public static ArrayList<SubCateBean> getAboutUsSubCateList(String scatid) {
		String requesturl = Config.JSON_GET_ABOUT_CATEGORY_SUB + scatid
				+ "&lang=" + Config.APP_USER_LANGUAGE;
		ArrayList<SubCateBean> result = new ArrayList<SubCateBean>();
		Log.i("getCategory request = ", requesturl);
		String strResult = "";
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;
		try {
			URL url = new URL(requesturl);
			connection = (HttpURLConnection) url.openConnection();
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "utf-8"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			strResult = sTotalString;
			Log.i("strResult=", strResult);
			if (strResult.equals("{\"productlist\":null}")) {

				return null;
			}

			JSONObject jsonObjs = new JSONObject(strResult)
					.getJSONObject("category");
			String cateid = jsonObjs.getString("cateid");
			String catename = jsonObjs.getString("catename");

			Log.i("cateid", cateid);
			Log.i("catename", catename);

			String subarray = jsonObjs.getString("subcatinfo");
			subarray = subarray.replaceAll("\"17\":", "");
			subarray = subarray.replaceAll("\"18\":", "");
			subarray = subarray.replaceAll("\"19\":", "");
			subarray = subarray.replaceAll("\"20\":", "");
			// Log.i("subarray",subarray);
			// subarray = subarray.substring(1, subarray.length() - 1);
			// subarray = "[" + subarray + "]";
			// Log.i("subarray",subarray);
			if (subarray != null && subarray.length() > 0
					&& !subarray.equals("null")) {
				JSONArray myarray = new JSONArray(subarray);
				for (int i = 0; i < myarray.length(); i++) {
					JSONObject jsonObj = ((JSONObject) myarray.opt(i));

					String catid = jsonObj.getString("subcatid");
					String cname = jsonObj.getString("subcatname");
					SubCateBean pb = new SubCateBean();
					pb.setSubcatname(cname);
					pb.setSubcatid(catid);
					result.add(pb);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (l_urlStream != null) {
				try {
					l_urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static ArrayList<SubCateBean> getDownloadSubCateList()// 锟斤拷锟斤拷
	{
		String requesturl = Config.JSON_GET_DOWNLOAD_CATEGORY + "&lang="
				+ Config.APP_USER_LANGUAGE;
		ArrayList<SubCateBean> result = new ArrayList<SubCateBean>();
		Log.i("getCategory request = ", requesturl);
		String strResult = "";
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;
		try {
			URL url = new URL(requesturl);
			connection = (HttpURLConnection) url.openConnection();
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "utf-8"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			strResult = sTotalString;
			Log.i("strResult=", strResult);
			if (strResult.equals("{\"productlist\":null}")) {

				return null;
			}

			JSONObject jsonObjs = new JSONObject(strResult)
					.getJSONObject("category");
			String cateid = jsonObjs.getString("cateid");
			String catename = jsonObjs.getString("catename");

			Log.i("cateid", cateid);
			Log.i("catename", catename);

			String subarray = jsonObjs.getString("subcatinfo");
			subarray = subarray.replaceAll("\"17\":", "");
			subarray = subarray.replaceAll("\"18\":", "");
			subarray = subarray.replaceAll("\"19\":", "");
			subarray = subarray.replaceAll("\"20\":", "");
			// Log.i("subarray",subarray);
			// subarray = subarray.substring(1, subarray.length() - 1);
			// subarray = "[" + subarray + "]";
			Log.i("subarray", subarray);
			if (subarray != null && !subarray.equals("null")) {
				JSONArray myarray = new JSONArray(subarray);
				for (int i = 0; i < myarray.length(); i++) {
					JSONObject jsonObj = ((JSONObject) myarray.opt(i));
					String catid = jsonObj.getString("subcatid");
					String cname = jsonObj.getString("subcatname");
					SubCateBean pb = new SubCateBean();
					pb.setSubcatname(cname);
					pb.setSubcatid(catid);
					result.add(pb);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (l_urlStream != null) {
				try {
					l_urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static ArrayList<SubCateBean> getAboutUsSubCateList()// 锟斤拷锟斤拷锟斤拷锟斤拷
	{
		String requesturl = Config.JSON_GET_ABOUT_CATEGORY + "&lang="
				+ Config.APP_USER_LANGUAGE;
		ArrayList<SubCateBean> result = new ArrayList<SubCateBean>();
		Log.i("getCategory request = ", requesturl);
		String strResult = "";
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;
		try {
			URL url = new URL(requesturl);
			connection = (HttpURLConnection) url.openConnection();
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "utf-8"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			strResult = sTotalString;
			Log.i("strResult=", strResult);
			if (strResult.equals("{\"productlist\":null}")) {

				return null;
			}

			JSONObject jsonObjs = new JSONObject(strResult)
					.getJSONObject("category");
			String cateid = jsonObjs.getString("cateid");
			String catename = jsonObjs.getString("catename");

			Log.i("cateid", cateid);
			Log.i("catename", catename);

			String subarray = jsonObjs.getString("subcatinfo");
			subarray = subarray.replaceAll("\"17\":", "");
			subarray = subarray.replaceAll("\"18\":", "");
			subarray = subarray.replaceAll("\"19\":", "");
			subarray = subarray.replaceAll("\"20\":", "");
			// Log.i("subarray",subarray);
			// subarray = subarray.substring(1, subarray.length() - 1);
			// subarray = "[" + subarray + "]";
			Log.i("subarray", subarray);
			if (subarray != null && !subarray.equals("null")) {
				JSONArray myarray = new JSONArray(subarray);
				for (int i = 0; i < myarray.length(); i++) {
					JSONObject jsonObj = ((JSONObject) myarray.opt(i));

					String catid = jsonObj.getString("subcatid");
					String cname = jsonObj.getString("subcatname");
					SubCateBean pb = new SubCateBean();
					pb.setSubcatname(cname);
					pb.setSubcatid(catid);
					result.add(pb);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (l_urlStream != null) {
				try {
					l_urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static ArrayList<SubCateBean> getNewsAndEventSecondList(String id)// newsandevent
																		// second
																		// folder
	{
		String requesturl = Config.JSON_GET_SUBNEWSLIST + id + "&lang="
				+ Config.APP_USER_LANGUAGE;
		ArrayList<SubCateBean> result = new ArrayList<SubCateBean>();
		Log.i("getCategory request = ", requesturl);
		String strResult = "";
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;
		try {
			URL url = new URL(requesturl);
			connection = (HttpURLConnection) url.openConnection();
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "utf-8"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			strResult = sTotalString;
			Log.i("strResult=", strResult);
			if (strResult.equals("{\"productlist\":null}")) {

				return null;
			}

			JSONObject jsonObjs = new JSONObject(strResult)
					.getJSONObject("category");
			String cateid = jsonObjs.getString("cateid");
			String catename = jsonObjs.getString("catename");

			Log.i("cateid", cateid);
			Log.i("catename", catename);

			String subarray = jsonObjs.getString("subcatinfo");
			subarray = subarray.replaceAll("\"17\":", "");
			subarray = subarray.replaceAll("\"18\":", "");
			subarray = subarray.replaceAll("\"19\":", "");
			subarray = subarray.replaceAll("\"20\":", "");
			// Log.i("subarray",subarray);
			// subarray = subarray.substring(1, subarray.length() - 1);
			// subarray = "[" + subarray + "]";
			// Log.i("subarray",subarray);

			JSONArray myarray = new JSONArray(subarray);
			// Log.i("myarray count",myarray.length() + "");

			for (int i = 0; i < myarray.length(); i++) {
				JSONObject jsonObj = ((JSONObject) myarray.opt(i));

				String catid = jsonObj.getString("subcatid");
				String cname = jsonObj.getString("subcatname");
				SubCateBean pb = new SubCateBean();
				pb.setSubcatname(cname);
				pb.setSubcatid(catid);
				result.add(pb);

				// String name = jsonObj.getString("subcatname");
				// String id = jsonObj.getString("subcatid");
				//
				// if((name != null && name.length() > 0) && (id != null &&
				// id.length() > 0)){
				// SubCateBean pb = new SubCateBean();
				// pb.setSubcatname(name);
				// pb.setSubcatid(id);
				// result.add(pb);
				// }
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (l_urlStream != null) {
				try {
					l_urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static ArrayList<SubCateBean> getNewsAndEventFirstList()// newsandevent
																// first folder
	{
		String requesturl = Config.JSON_GET_NEWSANDEVENT + "&lang="
				+ Config.APP_USER_LANGUAGE;
		ArrayList<SubCateBean> result = new ArrayList<SubCateBean>();
		Log.i("getCategory request = ", requesturl);
		String strResult = "";
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;

		try {
			URL url = new URL(requesturl);
			connection = (HttpURLConnection) url.openConnection();
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "utf-8"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			strResult = sTotalString;
			Log.i("strResult=", strResult);
			if (strResult.equals("{\"productlist\":null}")) {

				return null;
			}

			JSONObject jsonObjs = new JSONObject(strResult)
					.getJSONObject("category");
			String cateid = jsonObjs.getString("cateid");
			String catename = jsonObjs.getString("catename");

			Log.i("cateid", cateid);
			Log.i("catename", catename);

			String subarray = jsonObjs.getString("subcatinfo");
			subarray = subarray.replaceAll("\"17\":", "");
			subarray = subarray.replaceAll("\"18\":", "");
			subarray = subarray.replaceAll("\"19\":", "");
			subarray = subarray.replaceAll("\"20\":", "");
			// Log.i("subarray",subarray);
			// subarray = subarray.substring(1, subarray.length() - 1);
			// subarray = "[" + subarray + "]";
			Log.i("subarray", subarray);

			JSONArray myarray = new JSONArray(subarray);
			// Log.i("myarray count",myarray.length() + "");

			for (int i = 0; i < myarray.length(); i++) {
				JSONObject jsonObj = ((JSONObject) myarray.opt(i));

				String catid = jsonObj.getString("subcatid");
				String cname = jsonObj.getString("subcatname");
				SubCateBean pb = new SubCateBean();
				pb.setSubcatname(cname);
				pb.setSubcatid(catid);
				result.add(pb);

				// String name = jsonObj.getString("subcatname");
				// String id = jsonObj.getString("subcatid");
				//
				// if((name != null && name.length() > 0) && (id != null &&
				// id.length() > 0)){
				// SubCateBean pb = new SubCateBean();
				// pb.setSubcatname(name);
				// pb.setSubcatid(id);
				// result.add(pb);
				// }
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (l_urlStream != null) {
				try {
					l_urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static ArrayList<SubCateBean> getContactUS()// getBusinessOpportunity
	{
		String requesturl = Config.JSON_GET_CONTACTUS + "&lang="
				+ Config.APP_USER_LANGUAGE;
		ArrayList<SubCateBean> result = new ArrayList<SubCateBean>();
		Log.i("getCategory request = ", requesturl);
		String strResult = "";
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;
		try {
			URL url = new URL(requesturl);
			connection = (HttpURLConnection) url.openConnection();
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";

			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "utf-8"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			strResult = sTotalString;
			Log.i("strResult=", strResult);
			if (strResult.equals("{\"productlist\":null}")) {

				return null;
			}

			JSONObject jsonObjs = new JSONObject(strResult)
					.getJSONObject("category");
			String cateid = jsonObjs.getString("cateid");
			String catename = jsonObjs.getString("catename");

			Log.i("cateid", cateid);
			Log.i("catename", catename);

			String subarray = jsonObjs.getString("subcatinfo");
			subarray = subarray.replaceAll("\"17\":", "");
			subarray = subarray.replaceAll("\"18\":", "");
			subarray = subarray.replaceAll("\"19\":", "");
			subarray = subarray.replaceAll("\"20\":", "");
			// Log.i("subarray",subarray);
			// subarray = subarray.substring(1, subarray.length() - 1);
			// subarray = "[" + subarray + "]";
			Log.i("subarray", subarray);

			JSONArray myarray = new JSONArray(subarray);
			// Log.i("myarray count",myarray.length() + "");

			for (int i = 0; i < myarray.length(); i++) {
				JSONObject jsonObj = ((JSONObject) myarray.opt(i));

				String catid = jsonObj.getString("subcatid");
				String cname = jsonObj.getString("subcatname");
				SubCateBean pb = new SubCateBean();
				pb.setSubcatname(cname);
				pb.setSubcatid(catid);
				result.add(pb);

				// String name = jsonObj.getString("subcatname");
				// String id = jsonObj.getString("subcatid");
				//
				// if((name != null && name.length() > 0) && (id != null &&
				// id.length() > 0)){
				// SubCateBean pb = new SubCateBean();
				// pb.setSubcatname(name);
				// pb.setSubcatid(id);
				// result.add(pb);
				// }
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (l_urlStream != null) {
				try {
					l_urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static ArrayList<SubCateBean> getFAQ()// getBusinessOpportunity
	{
		String requesturl = Config.JSON_GET_FAQ + "&lang="
				+ Config.APP_USER_LANGUAGE;
		ArrayList<SubCateBean> result = new ArrayList<SubCateBean>();
		Log.i("getCategory request = ", requesturl);
		String strResult = "";
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;
		try {
			URL url = new URL(requesturl);
			connection = (HttpURLConnection) url.openConnection();
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "utf-8"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			strResult = sTotalString;
			Log.i("strResult=", strResult);
			if (strResult.equals("{\"productlist\":null}")) {

				return null;
			}

			JSONObject jsonObjs = new JSONObject(strResult)
					.getJSONObject("category");
			String cateid = jsonObjs.getString("cateid");
			String catename = jsonObjs.getString("catename");

			Log.i("cateid", cateid);
			Log.i("catename", catename);

			String subarray = jsonObjs.getString("subcatinfo");
			subarray = subarray.replaceAll("\"17\":", "");
			subarray = subarray.replaceAll("\"18\":", "");
			subarray = subarray.replaceAll("\"19\":", "");
			subarray = subarray.replaceAll("\"20\":", "");
			// Log.i("subarray",subarray);
			// subarray = subarray.substring(1, subarray.length() - 1);
			// subarray = "[" + subarray + "]";
			Log.i("subarray", subarray);

			JSONArray myarray = new JSONArray(subarray);
			// Log.i("myarray count",myarray.length() + "");

			for (int i = 0; i < myarray.length(); i++) {
				JSONObject jsonObj = ((JSONObject) myarray.opt(i));

				String catid = jsonObj.getString("subcatid");
				String cname = jsonObj.getString("subcatname");
				SubCateBean pb = new SubCateBean();
				pb.setSubcatname(cname);
				pb.setSubcatid(catid);
				result.add(pb);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (l_urlStream != null) {
				try {
					l_urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static ArrayList<ProductBean> getProductList(int catid) {

		String requesturl = Config.JSON_GET_PRODUCT_LIST + catid + "&lang="
				+ Config.APP_USER_LANGUAGE;
		ArrayList<ProductBean> result = new ArrayList<ProductBean>();
		Log.i("getCategory request = ", requesturl);
		String strResult = "";
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;

		try {
			URL url = new URL(requesturl);
			connection = (HttpURLConnection) url.openConnection();
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "utf-8"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			strResult = sTotalString;
			Log.i("strResult=", strResult);
			if (strResult.equals("{\"productlist\":null}")) {

				return null;
			}

			JSONArray jsonObjs = new JSONObject(strResult)
					.getJSONArray("productlist");
			for (int i = 0; i < jsonObjs.length(); i++) {
				JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
				String name = jsonObj.getString("title");
				String id = jsonObj.getString("id");
				// String desc = jsonObj.getString("desc");
				String producthtml = jsonObj.getString("producthtml");

				if ((name != null && name.length() > 0)
						&& (id != null && id.length() > 0)) {
					ProductBean pb = new ProductBean();
					pb.setProductid(id);
					pb.setProductname(name);
					// pb.setProoductdesc(desc);
					pb.setProducthtml(producthtml);
					result.add(pb);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (l_urlStream != null) {
				try {
					l_urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static ArrayList<ProductCategoryBean> getCategory(int catid) {
		String requesturl = Config.JSON_GET_ROOT_CATEGORY + catid + "&lang="
				+ Config.APP_USER_LANGUAGE;
		ArrayList<ProductCategoryBean> result = new ArrayList<ProductCategoryBean>();
		Log.i("getCategory request = ", requesturl);
		String strResult = "";
		HttpURLConnection connection = null;
		InputStream l_urlStream = null;
		try {
			URL url = new URL(requesturl);
			connection = (HttpURLConnection) url.openConnection();
			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
			l_urlStream = connection.getInputStream();
			BufferedReader l_reader = new BufferedReader(new InputStreamReader(
					l_urlStream, "utf-8"));
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString += sCurrentLine;
			}
			strResult = sTotalString;
			Log.i("strResult=", strResult);
			if (strResult.equals("{\"productlist\":null}")) {

				return null;
			}
			JSONArray jsonObjs = new JSONObject(strResult)
					.getJSONArray("child");
			for (int i = 0; i < jsonObjs.length(); i++) {
				JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i));
				String name = jsonObj.getString("name");
				String id = jsonObj.getString("id");

				if ((name != null && name.length() > 0)
						&& (id != null && id.length() > 0)) {
					ProductCategoryBean pb = new ProductCategoryBean();
					pb.setCategoryname(name);
					pb.setCategoryid(id);
					result.add(pb);
				}
				// Log.i("name = ",name);
				// Log.i("id = ",id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (l_urlStream != null) {
				try {
					l_urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	// get page html
	public static String getPageHtmlString(String url) {
		if (url != null && url.length() > 0) {
			String strResult = "";
			HttpGet httpRequest = new HttpGet(url);
			try {
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					strResult = EntityUtils.toString(httpResponse.getEntity());
				}
				if (strResult != null && strResult.length() > 0) {
					return strResult;
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

}
