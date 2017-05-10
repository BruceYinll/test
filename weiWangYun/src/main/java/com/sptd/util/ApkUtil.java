package com.sptd.util;



import java.io.File;
import java.util.List;

import com.sptd.eyun.EyunApplication;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


/**
 * apk工具类
 * 
 *  <br/> Description: TODO
 *  <br/> Author:CodeApe
 *  <br/> Version: 1.0
 *  <br/> Date: 2013-11-18
 *  <br/> @Copyright: Copyright (c) 2013 Shenzhen Tentinet Technology Co., Ltd. Inc.
 *             All rights reserved.
 * 
 */
public class ApkUtil {
//	public static Context mContext;

	/**
	 * 检测应用是否已经安装
	 * 
	 *  <br/> Version: 1.0
	 *  <br/> CreateTime:  2013-11-18,上午12:41:45
	 *  <br/> UpdateTime:  2013-11-18,上午12:41:45
	 *  <br/> CreateAuthor:  CodeApe
	 *  <br/> UpdateAuthor:  CodeApe
	 *  <br/> UpdateInfo:  (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param packageName
	 *            路径包名
	 * @return true 已经安装 ， false 未安装
	 */
	public static boolean checkPackageInstall(String packageName,Context context) {
		PackageInfo packageInfo;
		try {
			packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 安装apk
	 *
	 *  <br/> Version: 1.0
	 *  <br/> CreateTime:  2013-11-18,上午1:43:05
	 *  <br/> UpdateTime:  2013-11-18,上午1:43:05
	 *  <br/> CreateAuthor:  CodeApe
	 *  <br/> UpdateAuthor:  CodeApe
	 *  <br/> UpdateInfo:  (此处输入修改内容,若无修改可不写.)
	 *
	 * @param file
	 */
	public static void installApk(File file,Context mContext){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		if(EyunApplication.getInstance()==null){
	    	
	       Log.v("apkcotext", "is null");
	 	}else {
	 		mContext.startActivity(intent);
		}
	}
	/**
	 * 获取该app是否在进程里面并且是否在前台进程 
	 *
	 *  <br/> Version: 1.0
	 *  <br/> CreateTime:  2015年2月4日,下午4:03:41
	 *  <br/> UpdateTime:  2015年2月4日,下午4:03:41
	 *  <br/> CreateAuthor:  WangYuWen
	 *  <br/> UpdateAuthor:  WangYuWen
	 *  <br/> UpdateInfo:  (此处输入修改内容,若无修改可不写.)
	 *
	 *  @param context
	 *  @return true 在前台进程 false不在前台进程
	 */
	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getApplicationContext().getPackageName();
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			// 判断该工程的包名是否在前台 是就返回true
			if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

}
