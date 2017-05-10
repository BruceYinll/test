package com.sptd.eyun;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sptd.eyun.finals.OtherFinals;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class EyunApplication extends Application {

	private List<Activity> activityList=new ArrayList<Activity>();
	
	private static EyunApplication instance;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initImageLoader(getApplicationContext());
		makeDirects();

	}

	/**
	 * 一次创建程序所需要的文件夹
	 */
	private void makeDirects() {
		File img = new File(OtherFinals.DIR_IMG);
		if (!img.exists()) {
			img.mkdirs();
		}

		img = new File(OtherFinals.DIR_CACHE);
		if (!img.exists()) {
			img.mkdirs();
		}
		img = new File(OtherFinals.DIR_FILEPHOTO);
		if (!img.exists()) {
			img.mkdirs();
		}
	}

	/**
	 * init ImageLoader
	 * 
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.

		File cacheDir = new File(OtherFinals.DIR_CACHE);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.diskCache(new UnlimitedDiscCache(cacheDir)).memoryCache(new WeakMemoryCache())
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		// Initialize ImageLoader with configuration.

		ImageLoader.getInstance().init(config);
	}
	
	/*
	 * 单例模式中获取唯一的MyApplication实例
	 */
	public static EyunApplication getInstance() { 
	if(null == instance) { 
		
		instance = new EyunApplication(); 
		} 
	
		return instance; 
	} 
	public void addActivity(Activity activity){
		activityList.add(activity);
	}
	
	
	
	
	public void removeAllActivity(){
		for (Activity activity   :   activityList) {
//			if(activity!=null)
			    activity.finish();
		}
		//TODO 要加activityList.clear么
		activityList.clear();
		
//		System.exit(0);
	}
	
}
