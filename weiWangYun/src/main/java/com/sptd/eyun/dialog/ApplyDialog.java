package com.sptd.eyun.dialog;

import com.sptd.eyun.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

public class ApplyDialog extends Dialog{

	private Context context;
	private TextView tv_content;
	private TextView tuichu;
	public ApplyDialog(Context context) {
		super(context);
		this.context=context;
	}
	
	public ApplyDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
       this.setContentView(R.layout.apply_dialog);
       tuichu=(TextView) findViewById(R.id.tuichu);
       tv_content=(TextView)findViewById(R.id.tv_content);
	}

	public TextView getTuichu() {
		return tuichu;
	}

	public void setTuichu(TextView tuichu) {
		this.tuichu = tuichu;
	}

	public TextView getTv_content() {
		return tv_content;
	}

	public void setTv_content(TextView tv_content) {
		this.tv_content = tv_content;
	}
   

	
}
