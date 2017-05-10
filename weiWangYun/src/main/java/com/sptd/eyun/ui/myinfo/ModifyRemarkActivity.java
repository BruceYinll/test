package com.sptd.eyun.ui.myinfo;



import java.lang.reflect.Type;
import java.util.HashMap;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.StringUtils;


public class ModifyRemarkActivity extends BaseInteractActivity implements OnClickListener{

	private ImageView iv_back;
	private EditText et_user_description;
	private Button submit;
	
	private TextView tv_sum;  //显示已有字数
	
	
	
	private UserObj userObj;
	
	public ModifyRemarkActivity(){
		super(R.layout.activity_modifyremark2, false);
	}

	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_UPDATESELF:
			 Intent i = new Intent();
			 i.putExtra("sign", et_user_description.getText().toString());
			 setResult(3, i);
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
		tv_sum=(TextView) findViewById(R.id.tv_sum);
		
		
		
		iv_back=(ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		et_user_description=(EditText) findViewById(R.id.et_user_description);
		et_user_description.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				tv_sum.setText("" + s.length());
			}
		});
		
		
		
		
		
		
		submit=(Button) findViewById(R.id.submit);
		submit.setOnClickListener(this);
		
	}

	@Override
	protected void refreshView() {
		et_user_description.setText(userObj.getRemark());
		
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
	 * 编辑用户简介
	 */
	private void updateMember() {
		Type t = new TypeToken<BaseModel<UserObj>>() {
		}.getType();
		BaseAsyncTask<UserObj> task = new BaseAsyncTask<UserObj>(this, t,
				InterfaceFinals.INF_UPDATESELF, false);
		HashMap<String, String> params = new HashMap<String, String>();		
			params.put("token", getUserData().getToken());	
		    params.put("remark",et_user_description.getText().toString());			
		task.execute(params);
	}
	
    private boolean check(){
		
		
		if(StringUtils.isNull(et_user_description.getText().toString())){
			showToast("用户简介不能为空");
			return false;
		}
		return true;
	}
}
