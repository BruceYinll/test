package com.sptd.eyun.ui.myinfo;



import java.lang.reflect.Type;
import java.util.HashMap;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.StringUtils;
import com.sptd.util.VerifyUtil;

public class ModifyUserNameActivity extends BaseInteractActivity implements OnClickListener{

	private ImageView iv_back;
	private EditText et_username;
	private Button submit;
	
	private UserObj userObj;
	
	public static final int USER_NAME   =2;
	public ModifyUserNameActivity(){
		super(R.layout.activity_modifyusername, false);
	}

	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_UPDATESELF:
			 Intent i = new Intent();
			 i.putExtra("username", et_username.getText().toString());
			 setResult(USER_NAME, i);
			 showToast("修改成功");
			 finish();
			break;

		}
		
	}
	@Override
	public void onFail(BaseModel res) {
		showToast(res.getMessage());
		String failInf=res.getMessage();
		if("未登陆".equals(failInf)){
			startActivity(LoginActivity.class);
			finish();
		}
	}

	@Override
	protected void getData() {
		userObj=getUserData();
		
	}

	@Override
	protected void findView() {
		
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		et_username=(EditText) findViewById(R.id.et_username);
		submit=(Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		
	}

	@Override
	protected void refreshView() {
		et_username.setText(userObj.getUserName());
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			if(check())
			 updateMember();
			break;

		case R.id.iv_back:
			  finish();
			break;
		}
		
	}
	/**
	 * 
	 *修改用户名
	 */	
	private void updateMember() {
		Type t = new TypeToken<BaseModel<UserObj>>() {
		}.getType();
		BaseAsyncTask<UserObj> task = new BaseAsyncTask<UserObj>(this,t,
				InterfaceFinals.INF_UPDATESELF,false);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", getUserData().getToken());				
		params.put("userName",et_username.getText().toString());	

		task.execute(params);
	}
	
	
	private boolean check(){
		
		
		if(StringUtils.isNull(et_username.getText().toString())){
			showToast("用户名不能为空");
			return false;
		}
/*		if(et_username.getText().toString().length()<5){
			showToast("用户名长度为5-20个字符");
			return false;
		}*/
		if(!VerifyUtil.isEnglishOrChinese(et_username.getText().toString())){
			showToast("用户名为字母或汉字");
			return false;
		}
		return true;
	}


}
