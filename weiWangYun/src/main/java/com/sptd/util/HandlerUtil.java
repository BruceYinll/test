package com.sptd.util;



import android.app.Activity;
import android.os.Handler;
import android.os.Message;

/**
 * handler封装工具类
 * 
 *  <br/> Author:罗文忠
 *  <br/> Date: 2013-03-23
 * 
 * @updaateAuthor wujainxing
 * @updaateInfo 添加了取消操作的检测
 *  <br/> Date: 2014-03-17
 */
public class HandlerUtil {

	/**
	 * 发送消息.
	 * 
	 * @param handler
	 *            异步处理对象.
	 * @param what
	 *            消息.
	 *  <br/> Author:罗文忠
	 *  <br/> Version: 1.0, 2013-3-23
	 */
	public static void sendMessage(Handler handler, int what) {
		
		Message message = handler.obtainMessage();
		message.what = what;
		handler.sendMessage(message);
	}

	/**
	 * 发送消息.
	 * 
	 * @param handler
	 *            异步处理对象.
	 * @param what
	 *            消息.
	 * @param object
	 *            传递对象.
	 *  <br/> Author:罗文忠
	 *  <br/> Version: 1.0, 2013-3-23
	 */
	public static void sendMessage(Handler handler, int what, Object object) {
		
		Message message = handler.obtainMessage();
		message.what = what;
		message.obj = object;
		handler.sendMessage(message);
	}

	/**
	 * 发送消息
	 * 
	 *  <br/> Version: 1.0
	 *  <br/> CreateTime:  2014年2月21日 下午3:27:38
	 *  <br/> UpdateTime:  2014年2月21日 下午3:27:38
	 *  <br/> CreateAuthor:  liuyue
	 *  <br/> UpdateAuthor:  liuyue
	 *  <br/> UpdateInfo:  (此处输入修改内容,若无修改可不写.)
	 * 
	 * @param handler
	 * @param what
	 * @param arg1
	 * @param object
	 */
	public static void sendMessage(Handler handler, int what, int arg1, Object object) {
		
		Message message = handler.obtainMessage();
		message.what = what;
		message.obj = object;
		message.arg1 = arg1;
		handler.sendMessage(message);
	}

	/**
	 * 发送消息.
	 * 
	 * @param handler
	 *            异步处理对象.
	 * @param what
	 *            消息.
	 * @param arg1
	 *            消息.
	 * @param arg2
	 *            消息.
	 *  <br/> Author:罗文忠
	 *  <br/> Version: 1.0, 2013-3-23
	 */
	public static void sendMessage(Handler handler, int what, int arg1, int arg2) {
		Message message = handler.obtainMessage();
		message.what = what;
		message.arg1 = arg1;
		message.arg2 = arg2;
		handler.sendMessage(message);
	}

	/**
	 * 发送消息.
	 * 
	 * @param handler
	 *            异步处理对象.
	 * @param what
	 *            消息.
	 * @param arg1
	 *            消息.
	 * @param arg2
	 *            消息.
	 * @param object
	 *            传递对象.
	 *  <br/> Author:罗文忠
	 *  <br/> Version: 1.0, 2013-3-23
	 */
	public static void sendMessage(Handler handler, int what, int arg1, int arg2, Object object) {
		
		Message message = handler.obtainMessage();
		message.what = what;
		message.arg1 = arg1;
		message.arg2 = arg2;
		message.obj = object;
		handler.sendMessage(message);
	}

	/**
	 * 发送全局消息
	 * 
	 *  <br/> Version: 1.0
	 *  <br/> CreateTime:  2013-8-15,下午3:05:54
	 *  <br/> UpdateTime:  2013-8-15,下午3:05:54
	 *  <br/> CreateAuthor:  罗文忠
	 *  <br/> UpdateAuthor:  罗文忠
	 *  <br/> UpdateInfo:
	 * 
	 * @param what
	 */
	public static void sendEmptyMessage(int what) {
		Handler handler = new Handler();
		handler.sendEmptyMessage(what);
	}

	public static void post(Handler handler, Object object, Runnable r) {
		
		handler.post(r);
	}

	public static void post(Activity activity, Object object, Runnable r) {
		
		activity.runOnUiThread(r);
	}

}
