package com.sptd.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.sptd.util.CompressImage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

/**
 * http工具类，负责进行http通信服务
 */
public class HttpUtil {
	public final static String ClientException = "ClientProtocolException";
	public final static String ParseException = "ParseException";
	public final static String IllegalException = "IllegalArgumentException";
	public final static String IOException = "IOException";
	public final static String UnsupportedException = "UnsupportedEncodingException";

	private final static boolean DEBUG = true;
	private final static int timeout = 30 * 1000;
	private static boolean isCancelled = false;

	/**
	 * check the connection status, return true if it can be on net
	 */
	public static boolean checkConnection(Context context) {
		ConnectivityManager conneManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conneManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	public static boolean isCancelled() {
		return isCancelled;
	}

	public static void setCancelled(boolean isCancelled) {
		HttpUtil.isCancelled = isCancelled;
	}

	public static void Log(String txt) {
		if (DEBUG) {
			Log.e("test", txt);
		}
	}

	/**
	 * get methord
	 */
	public static String httpGet(String httpUrl) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpParams params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);

		Log("url=" + httpUrl);
		try {
			HttpGet httpget = new HttpGet(httpUrl);

			if (isCancelled) {
				return "";
			}
			HttpResponse response = httpclient.execute(httpget);
			// 得到http的内容
			HttpEntity entity = response.getEntity();
			// 得到具体的返回值，一般是xml文件
			String result = EntityUtils.toString(entity);
			entity.consumeContent();

			Log("result = " + result);
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ClientException;
		} catch (ParseException e) {
			e.printStackTrace();
			return ParseException;
		} catch (UnknownHostException e) {
			return "";
		} catch (SocketTimeoutException e) {
			return "";
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return IllegalException;
		} catch (IOException e) {
			e.printStackTrace();
			return IOException;
		}
	}

	/**
	 * http post methord
	 */
	public static String httpPost(String httpUrl, HashMap<String, String> data) {
		Log("url=" + httpUrl + ",data:" + data.toString());
		DefaultHttpClient httpclient = new DefaultHttpClient();
		/*
		 * StringBuilder sb = new StringBuilder(); List<Cookie> cookies =
		 * httpclient.getCookieStore().getCookies(); for(Cookie cookie: cookies)
		 * sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
		 * 
		 * // 除了HttpClient自带的Cookie，自己还可以增加自定义的Cookie // 增加代码...
		 * 
		 * 
		 * String cookieString=sb.toString();
		 */
		// httpclient.getCookieStore().getCookies();
		HttpParams params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);
		// Log("url=" + httpUrl + ",data:" + data.toString());

		try {
			HttpPost httpPost = new HttpPost(httpUrl);
			/*
			 * httpPost.setHeader("Cookie", cookieString);
			 */
			httpPost.setEntity(new UrlEncodedFormEntity(getPostParams(data), HTTP.UTF_8));

			HttpResponse response = httpclient.execute(httpPost);
			/*
			 * String setCookie = response.getFirstHeader("Set-Cookie")
			 * .getValue(); if(setCookie!=null){ String JSESSIONID =
			 * setCookie.substring("JSESSIONID=".length(),
			 * setCookie.indexOf(";")); System.out.println("JSESSIONID:" +
			 * JSESSIONID); // 新建一个Cookie BasicClientCookie cookie = new
			 * BasicClientCookie("JSESSIONID", JSESSIONID); }
			 * 
			 * 
			 */

			HttpEntity entity = response.getEntity();

			String result = EntityUtils.toString(response.getEntity());
			entity.consumeContent();

			Log("result = " + result);
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return UnsupportedException;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ClientException;
		} catch (UnknownHostException e) {
			return "";
		} catch (SocketTimeoutException e) {
			return "";
		} catch (ParseException e) {
			e.printStackTrace();
			return ParseException;
		} catch (IOException e) {
			e.printStackTrace();
			return IOException;
		}
	}

	/**
	 * change Map to string
	 * 
	 * @param map
	 * @return
	 */
	private static ArrayList<NameValuePair> getPostParams(HashMap<String, String> data) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if (data == null) {
			return params;
		}

		for (Map.Entry<String, String> item : data.entrySet()) {
			if (item.getValue() != null) {
				params.add(new BasicNameValuePair(item.getKey(), item.getValue()));
			}
		}
		return params;
	}

	/**
	 * post a file to server
	 * 
	 * @param url
	 * @param params
	 * @param bitmap
	 * @return
	 */
	public static String postFile1(String url, HashMap<String, String> params, HashMap<String, File[]> fileParams) {
		Log("params:" + params.toString() + "," + fileParams.toString());
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
			if (params != null && !params.isEmpty()) {// 参数
				ContentType type = ContentType.create("text/plain", "UTF-8");
				for (String key : params.keySet()) {
					// 必须加上ContentType，否则乱码
					mpEntity.addTextBody(key, params.get(key), type);
				}
			}

			if (fileParams != null && !fileParams.isEmpty()) {// 文件
				ContentType type = ContentType.create("application/octet-stream", "UTF-8");
				for (String key : fileParams.keySet()) {
					for (int i = 0; i < fileParams.get(key).length; i++) {
						mpEntity.addBinaryBody(key, fileParams.get(key)[i], type, "pic.png");
					}
				}
			}

			post.setEntity(mpEntity.build());

			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);// 得到具体的返回值，一般是xml文件
			entity.consumeContent();// 如果entity不为空，则释放内存空间
			Log("result = " + result);
			return result;
		} catch (UnknownHostException e) {
			return "";
		} catch (SocketTimeoutException e) {
			return "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String postFile(String url, HashMap<String, String> params, HashMap<String, String> fileParams) {
		Log("params:" + params.toString() + "," + fileParams.toString());
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
			if (params != null && !params.isEmpty()) {// 参数
				ContentType type = ContentType.create("text/plain", "UTF-8");
				for (String key : params.keySet()) {
					// 必须加上ContentType，否则乱码
					mpEntity.addTextBody(key, params.get(key), type);
				}
			}

			if (fileParams != null && !fileParams.isEmpty()) {// 文件
				ContentType type = ContentType.create("application/octet-stream", "UTF-8");
				for (String key : fileParams.keySet()) {
					File sendFile = new File(fileParams.get(key));
					mpEntity.addBinaryBody(key, sendFile, type, sendFile.getName());
				}
			}
			post.setEntity(mpEntity.build());

			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);// 得到具体的返回值，一般是xml文件
			entity.consumeContent();// 如果entity不为空，则释放内存空间
			Log("result = " + result);
			return result;
		} catch (UnknownHostException e) {
			return "";
		} catch (SocketTimeoutException e) {
			return "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String postImage(String url, HashMap<String, String> params, HashMap<String, String> fileParams) {
		Log("params:" + params.toString() + "," + fileParams.toString());
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);

			MultipartEntityBuilder mpEntity = MultipartEntityBuilder.create();
			if (params != null && !params.isEmpty()) {// 参数
				ContentType type = ContentType.create("text/plain", "UTF-8");
				for (String key : params.keySet()) {
					// 必须加上ContentType，否则乱码
					mpEntity.addTextBody(key, params.get(key), type);
				}
			}

			if (fileParams != null && !fileParams.isEmpty()) {// 文件
				ContentType type = ContentType.create("application/octet-stream", "UTF-8");
				for (String key : fileParams.keySet()) {
					File sendFile = new File(fileParams.get(key));
					if (CompressImage.getFileSizes(sendFile) > 6 * 1024 * 1024) {
						mpEntity.addBinaryBody(key, CompressImage.getInputStream(fileParams.get(key)), type,
								sendFile.getName());
					} else {
						mpEntity.addBinaryBody(key, sendFile, type, sendFile.getName());
					}

				}
			}

			post.setEntity(mpEntity.build());

			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);// 得到具体的返回值，一般是xml文件
			entity.consumeContent();// 如果entity不为空，则释放内存空间
			Log("result = " + result);
			return result;
		} catch (UnknownHostException e) {
			return "";
		} catch (SocketTimeoutException e) {
			return "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
