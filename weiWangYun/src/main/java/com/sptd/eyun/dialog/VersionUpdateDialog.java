package com.sptd.eyun.dialog;

import com.sptd.eyun.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
/**
 * 版本更新对话框
 * @author ldy
 *
 */
public class VersionUpdateDialog extends Dialog{

	private Context context;
	private TextView title;
	private TextView content;
	private CheckBox check_box;
	private TextView update_quxiao;
	private TextView update_queding;
	
	public VersionUpdateDialog(Context context) {
		super(context);
		this.context=context;
	}
	
	public VersionUpdateDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
       this.setContentView(R.layout.dialog_updateversion);
       title=(TextView) findViewById(R.id.title);
       content=(TextView) findViewById(R.id.content);
       check_box=(CheckBox) findViewById(R.id.check_box);
       update_quxiao=(TextView) findViewById(R.id.quxiao);
       update_queding=(TextView) findViewById(R.id.queding);
       
      
	}

	public CheckBox getCheck_box() {
		return check_box;
	}

	public void setCheck_box(CheckBox check_box) {
		this.check_box = check_box;
	}

	public TextView getUpdate_quxiao() {
		return update_quxiao;
	}

	public void setUpdate_quxiao(TextView update_quxiao) {
		this.update_quxiao = update_quxiao;
	}

	public TextView getUpdate_queding() {
		return update_queding;
	}

	public void setUpdate_queding(TextView update_queding) {
		this.update_queding = update_queding;
	}



	
   

}
