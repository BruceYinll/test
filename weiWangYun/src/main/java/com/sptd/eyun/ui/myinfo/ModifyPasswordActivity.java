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
import com.sptd.eyun.BaseActivity;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.StringUtils;
import com.sptd.util.VerifyUtil;
/**
 * 修改密码
 * @author ldy
 *
 */
public class ModifyPasswordActivity extends BaseInteractActivity implements OnClickListener{

	private ImageView iv_back;
	private EditText old_passwrod;
	private EditText new_passwrod;
	private EditText new_password_confirm;
	private Button submit;
	public ModifyPasswordActivity(){
		super(R.layout.activity_modifypassword, false);
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findView() {
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		old_passwrod=(EditText) findViewById(R.id.old_password);
		new_passwrod=(EditText) findViewById(R.id.new_password);
		new_password_confirm=(EditText) findViewById(R.id.new_password_confirm);
		submit=(Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submit:
			if(check())				   
			  modifyPassword();
			break;

		case R.id.iv_back:
			  finish();
			break;
		}
		
	}
	/**
	 * 
	 *修改密码
	 */
	private void modifyPassword() {
		//TODO Object,String
		Type t = new TypeToken<BaseModel<Object>>() {
		}.getType();
		BaseAsyncTask<String> task = new BaseAsyncTask<String>(this,t,
				InterfaceFinals.INF_MODIFYPASSWORD,false);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("oldPassword", old_passwrod.getText().toString());
		params.put("newPassword",new_passwrod.getText().toString());
		params.put("token",getUserData().getToken());	
		task.execute(params);
	}

	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_MODIFYPASSWORD:
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
	private boolean check(){		
		if(StringUtils.isNull(old_passwrod.getText().toString())){
			showToast("请输入原密码");
			return false;
		}
		if(!VerifyUtil.isPassword(old_passwrod.getText().toString())){
			showToast("原密码是6-12位英文或数字");
			return false;
		}
				
		if(new_passwrod.getText().toString()==null ||
				"".equals(new_passwrod.getText().toString())){
			showToast("新密码不能为空");
			return false;
		}
		if(!VerifyUtil.isPassword(new_passwrod.getText().toString())){
			showToast("新密码是6-12位英文或数字");
			return false;
		}
		if(!new_passwrod.getText().toString().equals(new_password_confirm.getText().toString())){
			showToast("两次密码不一致");
			return false;
		}		
		return true;
	}		
	
	
}
