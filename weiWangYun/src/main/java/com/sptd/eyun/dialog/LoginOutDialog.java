package com.sptd.eyun.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.sptd.eyun.R;

public class LoginOutDialog extends Dialog{

	private Context context;
	private TextView title;
	private TextView content;
	private TextView loginout_quxiao;
	private TextView loginout_queding;
	
	public LoginOutDialog(Context context) {
		super(context);
		this.context=context;
	}
	
	public LoginOutDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
       this.setContentView(R.layout.dialog_kefuphone);
       title=(TextView) findViewById(R.id.title);
       title.setText("退出登录");
       content=(TextView) findViewById(R.id.content);
       content.setText("确定要退出登录吗?");
       loginout_quxiao=(TextView) findViewById(R.id.quxiao);
       loginout_queding=(TextView) findViewById(R.id.queding);
       
      
	}

	public TextView getLoginout_quxiao() {
		return loginout_quxiao;
	}

	public void setLoginout_quxiao(TextView loginout_quxiao) {
		this.loginout_quxiao = loginout_quxiao;
	}

	public TextView getLoginout_queding() {
		return loginout_queding;
	}

	public void setLoginout_queding(TextView loginout_queding) {
		this.loginout_queding = loginout_queding;
	}

	public TextView getTitle() {
		return title;
	}

	public void setTitle(TextView title) {
		this.title = title;
	}

	public TextView getContent() {
		return content;
	}

	public void setContent(TextView content) {
		this.content = content;
	}

	
   
	
   

	
}