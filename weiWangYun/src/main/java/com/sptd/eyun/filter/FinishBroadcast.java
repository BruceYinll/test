package com.sptd.eyun.filter;

import com.sptd.eyun.finals.OtherFinals;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * 关闭当前页面的广播
 * 
 * @author lanyan
 * 
 */
public class FinishBroadcast extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (OtherFinals.BROAD_ACTION.equals(intent.getAction())) {
			((Activity) context).finish();
		}
	}
}