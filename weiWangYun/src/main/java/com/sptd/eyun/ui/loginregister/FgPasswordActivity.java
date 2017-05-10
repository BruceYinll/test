package com.sptd.eyun.ui.loginregister;

import java.lang.reflect.Type;
import java.util.HashMap;

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
import com.sptd.eyun.widget.EditTextCheckNumber;
import com.sptd.eyun.widget.MyCount;
import com.sptd.log.Log;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.StringUtils;
import com.sptd.util.VerifyUtil;


public class FgPasswordActivity extends BaseInteractActivity implements OnClickListener{

	private ImageView iv_left;
	
	private EditText phone_number;
	
	private EditTextCheckNumber check_number;
	private EditText editText;
	private Button button;
	
	private EditText password_text;
	private EditText password_confirm;
	private Button apply_button;
	
	private MyCount myCount;
	
	
	private String serial_number="";
	public FgPasswordActivity() {
		super(R.layout.activity_fgpassword,false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_VERIFYCODE:
			showToast("验证码短信已发送，请查收");
			if (null != myCount) {
				myCount.start();
			}
			serial_number=(String) res.getObject();
			break;
		case InterfaceFinals.INF_MODIFY:
			showToast("修改成功");
			finish();
			break;
			
		case InterfaceFinals.INF_GETMOBILEEXISTSYZM:
			showToast("验证码短信已发送，请查收");
			if (null != myCount) {
				myCount.start();
			}
			serial_number=(String) res.getObject();
			break;
		}
		
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findView() {
		iv_left=(ImageView) findViewById(R.id.iv_left);
		iv_left.setOnClickListener(this);
		phone_number=(EditText) findViewById(R.id.phone_number);
		
		check_number=(EditTextCheckNumber) findViewById(R.id.check_number);
		editText=check_number.getEditText();
		button=check_number.getButton();
		button.setOnClickListener(this);
		
		password_text=(EditText) findViewById(R.id.password_text);
		password_confirm=(EditText) findViewById(R.id.password_confirm);
		apply_button=(Button) findViewById(R.id.apply_button);
		apply_button.setOnClickListener(this);
		
	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_left:
			FgPasswordActivity.this.finish();
			break;
	    case R.id.button:
	    	if(checkPhone()){
	    	  myCount = new MyCount(60000, 1000);
			  myCount.setTextView(button, FgPasswordActivity.this);
			  sendVerityCode();
	    	}
			break;
	    case R.id.apply_button:
	    	if(check()){
		    	   modify();
		    	}
	    	break;
	    
		}

		
	}
	private void modify() {
		Log.v("yzm", editText.getText().toString());
		Type t = new TypeToken<BaseModel<UserObj>>() {
		}.getType();
		BaseAsyncTask<UserObj> task = new BaseAsyncTask<UserObj>(this, t,
				InterfaceFinals.INF_MODIFY, true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", phone_number.getText().toString());
		params.put("yzm", editText.getText().toString());
		params.put("password", password_text.getText().toString());
		params.put("serial_number", serial_number);
		task.execute(params);
	}
	
	/**
	 * 获取验证码
	 */
	private void sendVerityCode() {
		Type t = new TypeToken<BaseModel<Object>>() {
		}.getType();
		/*旧接口
		BaseAsyncTask<Object> task = new BaseAsyncTask<Object>(this, t,
				InterfaceFinals.INF_VERIFYCODE, true);
				*/
		BaseAsyncTask<Object> task = new BaseAsyncTask<Object>(this, t,
				InterfaceFinals.INF_GETMOBILEEXISTSYZM, true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", phone_number.getText().toString());
		
		task.execute(params);
	}

	private boolean checkPhone(){
		if(phone_number.getText().toString()==null ||
				"".equals(phone_number.getText().toString())){
			showToast("手机号不能为空");
			return false;
		}
		
		if(!VerifyUtil.isMobileNO(phone_number.getText().toString())){
			showToast("输入的手机号码格式错误，请重新输入");
			return false;
		}
		return true;
	}	
	
	private boolean check(){
		
		if(phone_number.getText().toString()==null ||
				"".equals(phone_number.getText().toString())){
			showToast("手机号不能为空");
			return false;
		}
		if(!VerifyUtil.isMobileNO(phone_number.getText().toString())){
			showToast("输入的手机号码格式错误，请重新输入");
			return false;
		}
		/*
		if(StringUtils.isNull(serial_number)){
			showToast("请先获取验证码");
			return false;
		}
		*/
		if(StringUtils.isNull(check_number.getEditText().getText().toString())){
			showToast("验证码不能为空");
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
		if(!password_text.getText().toString().equals(password_confirm.getText().toString())){
			showToast("两次密码不一致");
			return false;
		}
		
		return true;
	}
}
