package com.sptd.eyun;

/**
 * 全局静态变量类，用于放置程序运行过程中都用的到的变量对象。
 * 使用时，需要判断该对象的值是否因内存不足或程序崩溃等原因而被清空，因此建议能不放这个里面就不放。
 * 
 * @author lanyan
 * 
 */
public class AppSession {
	public static int Wid = 0;
	public static int Hei = 0;
	public static float Den = 0f;
}
