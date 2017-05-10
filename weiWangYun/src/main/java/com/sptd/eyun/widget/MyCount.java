package com.sptd.eyun.widget;

import com.sptd.eyun.ui.loginregister.RegisterActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

public class MyCount extends CountDownTimer {
	private Button btn;
	private Context r;
	public MyCount(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}
	public void setTextView(Button btn,Context r){
		this.btn=btn;
		this.r=r;
	}
	@SuppressWarnings("static-access")
	@Override
	public void onFinish() {
//		btn.setBackgroundColor(Color.parseColor("#404247"));;
//		r.iscount =false;
		btn.setText("获取验证码");
//		btn.setTextColor(r.getResources().getColor(Color.parseColor("#53b0f1")));
//		tv.setBackgroundColor(Color.parseColor("#4DD4D0"));
		btn.setEnabled(true);
	}
	@SuppressLint("ResourceAsColor")
	@SuppressWarnings("static-access")
	@Override
	public void onTick(long millisUntilFinished) {
//		btn.setBackgroundColor(Color.parseColor("#3b3e42"));
//		r.iscount=true;
		btn.setText("获取验证码("+millisUntilFinished / 1000 + "s" + ")");
//		btn.setTextColor(Color.parseColor("#c8c8c8"));
//		tv.setBackgroundColor(Color.parseColor("#b7b7b7"));
		btn.setEnabled(false);
	}	
}
