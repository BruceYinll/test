package com.sptd.eyun.finals;

import android.os.Environment;

/**
 * 其他常量类，不好分类的，放在这里面
 * 
 * @author lanyan
 * 
 */
public class OtherFinals {
	// --------------应用缓存文件基本信息-----------------------
	public static final String PATH_SD = Environment
			.getExternalStorageDirectory().getPath() + "/Empty/";
	public static final String DIR_IMG = PATH_SD + "image/";
	public static final String DIR_CACHE = PATH_SD + "cache/";
	public static final String DIR_FILEPHOTO = PATH_SD + "photo/";
	// public static final String DIR_VIDEO = PATH_SD + "video/";
	// public static final String DIR_AUDIO = PATH_SD + "audio/";

	public static final String SAHRE_URL = "http://www.baidu.com";// 每页的条数
	public static final String PAGE_SIZE = "20";// 每页的条数
	public static final String BROAD_ACTION = "com.threeti.Empty.finshAllActivity";
}
