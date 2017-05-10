package com.sptd.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import sz.itguy.utils.FileUtil;

public class HttpDownLoad extends AsyncTask<String, Long, File> {

	public HttpDownLoad(Context context, LoadListener loadlistener) {
		this.context = context;
		this.loadlistener = loadlistener;
	}

	Context context;
	LoadListener loadlistener;

	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (loadlistener != null) {
			loadlistener.onstart();
		}
	}

	@Override
	protected File doInBackground(String... params) {
		// TODO Auto-generated method stub
		String target = md5Key(params[0]);
		File needFile = new File(getDiskCacheDir(context, "video").getAbsoluteFile() +File.separator
				+ target + ".mp4");
		if (needFile.exists()) {
			publishProgress(100l, 100l);
			return needFile;
		}
		File file = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(params[0]);
		try {
			if (!isCancelled()) {
				HttpResponse response = client.execute(request);
				if (!isCancelled()) {
					HttpEntity entity = response.getEntity();
					if (TextUtils.isEmpty(params[0]) || params[0].trim().length() == 0) {
						file = null;
					} else {
						File fileMarks = getDiskCacheDir(context, "video");
						if (!fileMarks.exists()) {
							fileMarks.mkdirs();
						}
						File tempFile = new File(getDiskCacheDir(context, "video").getAbsolutePath() + File.separator
								+ target + "_temp.mp4");
						if (!tempFile.exists()) {
							tempFile.delete();
							tempFile.createNewFile();
						}
						long current = 0;
						if(entity.getContentLength()==0){
							return null;
						}
						if (tempFile.exists()) {
							FileOutputStream os = new FileOutputStream(tempFile);

							InputStream input = entity.getContent();
							long count = entity.getContentLength() + current;

							int readLen = 0;
							byte[] buffer = new byte[1024];
							while (!(current >= count) && ((readLen = input.read(buffer, 0, 1024)) > 0)) {// 未全部读取
								os.write(buffer, 0, readLen);
								current += readLen;
								publishProgress(count, current);
							}
							file = new File(getDiskCacheDir(context, "video").getAbsolutePath() + File.separator
									+ target + ".mp4");
							tempFile.renameTo(file);
							boolean is = file.exists();
							os.close();
							publishProgress(count, current);
						}
					}

				}
			}
			return file;
		} catch (UnknownHostException e) {
			file = null;
		} catch (IOException e) {
			file = null;
		} catch (NullPointerException e) {
			file = null;
		} catch (Exception e) {
			file = null;
		}

		return file;
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		if (loadlistener != null) {
			loadlistener.onLoad(values[0], values[1]);
		}
	}

	@Override
	protected void onPostExecute(File result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (loadlistener != null) {
			loadlistener.onSucess(result);
		}

	}

	public interface LoadListener {
		abstract void onstart();

		abstract void onLoad(long count, long curCount);

		abstract void onSucess(File file);
	}

	public String md5Key(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
