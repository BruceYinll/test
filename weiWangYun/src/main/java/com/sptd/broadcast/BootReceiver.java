package com.sptd.broadcast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

	

	@Override
	public void onReceive(Context context, Intent intent) {
		// 接收安装广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString();
			Log.v("remove", "应用被安装");
		}
		// 接收卸载广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			String packageName = intent.getDataString();
			Log.v("remove", "应用被卸载");
		}	
	}
}
