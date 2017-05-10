package com.sptd.eyun.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.sptd.eyun.R;

public class KefuPhoneDialog extends Dialog{

	private Context context;
	private TextView quxiao;
	private TextView queding;
	public KefuPhoneDialog(Context context) {
		super(context);
		this.context=context;
	}
	
	public KefuPhoneDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
       this.setContentView(R.layout.dialog_kefuphone);
       quxiao=(TextView) findViewById(R.id.quxiao);
       queding=(TextView) findViewById(R.id.queding);
	}

	public TextView getQuxiao() {
		return quxiao;
	}

	public void setQuxiao(TextView quxiao) {
		this.quxiao = quxiao;
	}

	public TextView getQueding() {
		return queding;
	}

	public void setQueding(TextView queding) {
		this.queding = queding;
	}

	
   

	
}