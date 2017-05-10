package com.sptd.eyun.ui.loginregister;


import java.lang.reflect.Type;
import java.util.HashMap;






import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.R;
import com.sptd.eyun.dialog.LoginOutDialog;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.ui.HomeActivity;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.MyToast;
import com.sptd.util.VerifyUtil;





import com.sptd.util.resource.PreferencesUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 登陆页面
 * @author ldy
 *
 */
public class LoginActivity extends BaseInteractActivity{

	

	private Button login_button;
	private TextView newUser_register;
	
	
	
	//手机号,密码
	private EditText phone_num;
	private EditText password_text;
	
	private TextView fg_password;
	
	
	
	//登录时未有站被授权
	private LoginOutDialog loginOutDialog;
	private TextView title;
	private TextView content;
	private TextView loginOutDialog_quxiao;
	private TextView loginOutDialog_queding;
	public LoginActivity() {
		super(R.layout.activity_login, false);
	}

	private void findViews(){
		login_button=(Button) findViewById(R.id.login_button);
		newUser_register=(TextView) findViewById(R.id.newUser_register);
		
		phone_num=(EditText) findViewById(R.id.phone_num);
		password_text=(EditText) findViewById(R.id.password_text);
		
		fg_password=(TextView) findViewById(R.id.fg_password);
	}
	
	private void setListeners(){
		login_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if(VerifyUtil.isMobileNO(phone_num.getText().toString())
//						&& VerifyUtil.isPassword(password_text.getText().toString())){
////					UserObj userObj=new UserObj();
////					userObj.setMobile("13760215082");
////					userObj.setPassword("123456");
////					setUserData(userObj);
//					
					if(check()){
					  loginApp();
					}
//				}else {
//					MyToast.showLong(LoginActivity.this, "手机号或密码错误");
//					return;
//				}
				
				
			}
		});
		newUser_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
//				LoginActivity.this.finish();
				
			}
		});
	    fg_password.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(FgPasswordActivity.class);
				
			}
		});
	}
	@Override
	public void onSuccess(BaseModel res) {
		showToast("登录成功");
		UserObj  userObj = (UserObj) res.getObject();
		if(null!=userObj){
			setUserData(userObj);
			/*
			
			JPushInterface.setAlias(this, getUserData().getTid(), null);
			//设置别名
			WeiMeiApplication.loginName = getUserData().getLoginName();
			*/
			//TODO
			startActivity(HomeActivity.class);
			finish();
		}
		
	}
	
	
	
	@Override
	public void onFail(BaseModel res) {
		String failInf=res.getMessage();
		if("授权被拒绝！无法登录".equals(failInf)){
			loginOutDialog=new LoginOutDialog(this, R.style.MyDialog);
			loginOutDialog.setCancelable(false);
        	loginOutDialog.show();
			title=loginOutDialog.getTitle();
			title.setText("提示");
			content=loginOutDialog.getContent();
			content.setText("关联站已失效.去关联站页面申请关联站么?");
        	
        	loginOutDialog_quxiao=loginOutDialog.getLoginout_quxiao();
        	loginOutDialog_queding=loginOutDialog.getLoginout_queding();
        	loginOutDialog_quxiao.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loginOutDialog.dismiss();
					
				}
			});
        	loginOutDialog_queding.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loginOutDialog.dismiss();
//					finish();
					String phone=phone_num.getText().toString();
					startActivity(ThirdMyZhanActivity.class, phone);

					
				}
			});
//			startActivity(LoginActivity.class);
//			finish();
		}else {
			showToast(res.getMessage());
		}
		
		
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void findView() {
		 findViews();
		 setListeners();
		
	}
	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 登录
	 */
	private void loginApp() {
		Type t = new TypeToken<BaseModel<UserObj>>() {
		}.getType();
		BaseAsyncTask<UserObj> task = new BaseAsyncTask<UserObj>(this,t,
				InterfaceFinals.INF_LOGIN,true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", phone_num.getText().toString());
		params.put("password", password_text.getText().toString());
		task.execute(params);
	}
	//登录校验
	private boolean check(){
		
		if(phone_num.getText().toString()==null ||
				"".equals(phone_num.getText().toString())){
			showToast("手机号不能为空");
			return false;
		}
		if(!VerifyUtil.isMobileNO(phone_num.getText().toString())){
			showToast("手机号格式错误");
			return false;
		}
		if(password_text.getText().toString()==null ||
				"".equals(password_text.getText().toString())){
			showToast("密码不能为空");
			return false;
		}
		if(!VerifyUtil.isPassword(password_text.getText().toString())){
			showToast("密码只能是6-12位英文或数字");
			return false;
		}
		return true;
	}  
}
