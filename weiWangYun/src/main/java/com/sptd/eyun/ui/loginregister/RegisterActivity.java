package com.sptd.eyun.ui.loginregister;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.EyunApplication;
import com.sptd.eyun.R;
import com.sptd.eyun.adapter.GuanlianZhanAdapter;
import com.sptd.eyun.adapter.GuanlianZhanAdapter.DeleteZhanOnClickListener;
import com.sptd.eyun.dialog.ApplyDialog;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.ui.HomeActivity;
import com.sptd.eyun.ui.myinfo.MyZhanActivity;
import com.sptd.eyun.widget.EditTextCheckNumber;
import com.sptd.eyun.widget.MyCount;
import com.sptd.log.Log;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.ListViewUtil;
import com.sptd.util.StringUtils;
import com.sptd.util.VerifyUtil;

import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends BaseInteractActivity implements DeleteZhanOnClickListener{

	private ImageView iv_back;
	

	public static boolean iscount = false;
	
	private TextView add_zhan_hide;
	private LinearLayout add_zhan;
	private ListView listView1;
	private GuanlianZhanAdapter guanlianZhanAdapter;
	//关联站
	private List<Station> list=new ArrayList<Station>();
	//所有站
	private List<Station> allStations=new ArrayList<Station>();
	
	
	private TextView yixuanze_zhan;
	private Button apply_button;
	private ApplyDialog dialog;
	private TextView tuichu;
	//当前站列表显示还是隐藏
	private boolean showZhan=false;
	
	
	private EditTextCheckNumber check_number;
	private Button  button;
	private MyCount myCount;
	//验证码key
	private String serial_number="";
	
	//params
	private EditText user_name;
	private EditText user_description;
	private EditText phone_number;
	private EditText check_number_edittext;
	private EditText password_text;
	private EditText password_confirm;
	
	private StringBuffer stations;
	
	private CheckBox check_box;
	
	private TextView user_protocol;
	public RegisterActivity() {
		super(R.layout.activity_register,false);
		// TODO Auto-generated constructor stub
	}
	
	private void findViews(){
		iv_back=(ImageView) findViewById(R.id.iv_back);

		add_zhan_hide=(TextView) findViewById(R.id.add_zhan_hide);
		add_zhan=(LinearLayout) findViewById(R.id.add_zhan);
		listView1=(ListView) findViewById(R.id.listView1);
		
		yixuanze_zhan=(TextView) findViewById(R.id.yixuanze_zhan);
		apply_button=(Button) findViewById(R.id.apply_button);
		
		
		
		
		check_number=(EditTextCheckNumber) findViewById(R.id.check_number);
		button=check_number.getButton();
		
		
		
		
	//params
		user_name=(EditText) findViewById(R.id.user_name);
		user_description=(EditText) findViewById(R.id.user_description);
		phone_number=(EditText) findViewById(R.id.phone_number);
		check_number_edittext=check_number.getEditText();
		password_text=(EditText) findViewById(R.id.password_text);
		password_confirm=(EditText) findViewById(R.id.password_confirm);
	
	
		check_box=(CheckBox) findViewById(R.id.check_box);
		user_protocol=(TextView) findViewById(R.id.user_protocol);
	}
	private void setAdapters(){
		
//		guanlianZhanAdapter=new GuanlianZhanAdapter(RegisterActivity.this, list);
		guanlianZhanAdapter=new GuanlianZhanAdapter(RegisterActivity.this, allStations);

		guanlianZhanAdapter.setListeners(this);
		
		listView1.setAdapter(guanlianZhanAdapter);
		ListViewUtil.setListViewHeightBasedOnChildren(listView1);
	}
	private void setListeners(){
		iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		add_zhan_hide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		      if (showZhan) {
		    	  add_zhan.setVisibility(View.GONE);
				  listView1.setVisibility(View.GONE);
				  yixuanze_zhan.setVisibility(View.GONE);
				  add_zhan_hide.setCompoundDrawablesWithIntrinsicBounds (null,
						  null, getResources().getDrawable(R.drawable.listview_show)
						  , null);
				  showZhan=false;
			  }else {
				  add_zhan.setVisibility(View.VISIBLE);
				  listView1.setVisibility(View.VISIBLE);
				  yixuanze_zhan.setVisibility(View.VISIBLE);
				  yixuanze_zhan.setText("已选择"+list.size()+"个站");
				  add_zhan_hide.setCompoundDrawablesWithIntrinsicBounds (null,
						  null, getResources().getDrawable(R.drawable.listview_hide)
						  , null);
				  showZhan=true;
			  }		
				
			
			}
		});
		
		add_zhan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(RegisterActivity.this, MyZhanActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("guanlianzhan_list", (ArrayList<Station>) list);
				bundle.putSerializable("allzhan", (ArrayList<Station>) allStations);
				intent.putExtras(bundle);
			    startActivityForResult(intent, 0);
				
			}
		});
		apply_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*
				list.remove(0);
				setListViewHeightBasedOnChildren(listView1);
				guanlianZhanAdapter.notifyDataSetChanged();
				*/
//				regist();
				
				stations=new StringBuffer();
				for(int i=0;i<list.size();i++){
					stations.append(list.get(i).getTid());
					if(i<list.size()-1)
						stations.append(",");
				}
				if(check()){
					regist();
				}
				
			}
		});
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Toast.makeText(RegisterActivity.this, "button click", 3000).show();
				if(checkPhone()){
				  myCount = new MyCount(60000, 1000);
				  myCount.setTextView(button, RegisterActivity.this);
				  sendVerityCode(); 
				}
				
				
			}
		});
		
        user_protocol.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(RegisterActivity.this, ProtocolActivity.class);
				startActivity(intent);
			}
		});
	
	    
	}
	


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
		Toast.makeText(this, "关联成功", 3000).show();
		Bundle bundle=data.getExtras();
		list=(List<Station>) bundle.getSerializable("new_list");
		yixuanze_zhan.setText("已选择"+list.size()+"个站");
/*		for(String s:list){
			Toast.makeText(this, s, 3000).show();
		}
		
		guanlianZhanAdapter.notifyDataSetChanged();
		setListViewHeightBasedOnChildren(listView1);
		super.onActivityResult(requestCode, resultCode, data);*/
 //       guanlianZhanAdapter=new GuanlianZhanAdapter(RegisterActivity.this, list);
		 guanlianZhanAdapter=new GuanlianZhanAdapter(RegisterActivity.this, list);
        guanlianZhanAdapter.setListeners(this);
		listView1.setAdapter(guanlianZhanAdapter);
		//调高度
	 	ListViewUtil.setListViewHeightBasedOnChildren(listView1);
	
		}
	}
	/**
	 * 获取验证码
	 */
	private void sendVerityCode() {
		Type t = new TypeToken<BaseModel<Object>>() {
		}.getType();
		BaseAsyncTask<Object> task = new BaseAsyncTask<Object>(this, t,
				InterfaceFinals.INF_VERIFYCODE, true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", phone_number.getText().toString());
		
		task.execute(params);
	}
	/**
	 * 注册用户
	 */
	private void regist() {
		Type t = new TypeToken<BaseModel<UserObj>>() {}.getType();
		BaseAsyncTask<UserObj> task = new BaseAsyncTask<UserObj>(this,t,
				InterfaceFinals.INF_REGIST,true);
		HashMap<String, String> params = new HashMap<String, String>();
		
		
		params.put("userName", user_name.getText().toString());
		params.put("mobile", phone_number.getText().toString());
		params.put("remark", user_description.getText().toString());
		params.put("yzm", check_number_edittext.getText().toString());
		params.put("password", password_text.getText().toString());
		params.put("serial_number", serial_number);
		

/*			
		UserObj userObj=new UserObj();
		userObj.setUserName(user_name.getText().toString());
		userObj.setMobile(phone_number.getText().toString());
		userObj.setRemark(user_description.getText().toString());
		userObj.setYzm(check_number_edittext.getText().toString());
		userObj.setPassword(password_text.getText().toString());
	
		userObj.setStationids(stations.toString());
		*/
/*		
		Type tt = new TypeToken<UserObj>() {
		}.getType();
		Gson mGson = new Gson();
		String str=mGson.toJson(userObj);
		Log.v("json", str+"--1");
		
		params.put("xaCmsUser", str);
*/		
		
		params.put("stationids", stations.toString());
		task.execute(params);
		
	}
	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_VERIFYCODE:
			    showToast("验证码短信已发送，请查收");
				myCount.start();
				serial_number=(String) res.getObject();
			break;
			
		case InterfaceFinals.INF_REGIST://注册成功	
			dialog=new ApplyDialog(RegisterActivity.this,R.style.MyDialog);
			dialog.setCancelable(false);
			dialog.show();
			tuichu=dialog.getTuichu();
			if(tuichu==null){
				Log.v("tuichu", "null");
			}
			tuichu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//退出应用
//					RegisterActivity.this.finish();
	
					dialog.dismiss();
					
					EyunApplication.getInstance().removeAllActivity();
					System.exit(0);
//					 android.os.Process.killProcess(android.os.Process.myPid());
//					 RegisterActivity.this.finish();
//					 System.exit(0); 
//					 ActivityManager activityMgr= (ActivityManager) RegisterActivity.this.getSystemService(ACTIVITY_SERVICE);

//					 activityMgr.restartPackage(getPackageName());
					
					 
				}
			});
			break;
			
	    case  InterfaceFinals.INF_FINDSTATIONLIST://获取所有站成功
//	    	   showToast(res.getMessage());
	    	   allStations=(List<Station>) res.getObject();
//	    	   showToast(allStations.get(0).getName());
//	    	   setAdapters();
	    	break;   
	    	   
		}
	}
	@Override
	protected void getData() { 
		
		
///		list.add("111");
///		list.add("222");
//		list.add("333");
		findAllStations();
		
	}
	
	
	/**
	 * 获得所有站
	 */
	private void findAllStations() {
		Type t = new TypeToken<BaseModel<List<Station>>>() {
		}.getType();
		BaseAsyncTask<List<Station>> task = new BaseAsyncTask<List<Station>>(this,t,
				InterfaceFinals.INF_FINDSTATIONLIST,true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("nextPage", "0");
		params.put("pageSize", "100");
		params.put("status", "1");
		task.execute(params);
		
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
		
		
		if(StringUtils.isNull(user_name.getText().toString())){
			showToast("用户名不能为空");
			return false;
		}
/*		if(user_name.getText().toString().length()<5){
			showToast("用户名长度为5-20个字符");
			return false;
		}*/
		if(!VerifyUtil.isEnglishOrChinese(user_name.getText().toString())){
			showToast("用户名为字母或汉字");
			return false;
		}
		if(StringUtils.isNull(user_description.getText().toString())){
			showToast("用户简介不能为空");
			return false;
		}
		
		if(!checkPhone()){
			return false;
		}
		/*
		if(StringUtils.isNull(serial_number)){
			showToast("请先获取验证码");
			return false;
		}
		*/
		if(StringUtils.isNull(check_number_edittext.getText().toString())){
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
		if(StringUtils.isNull(stations.toString())){
			showToast("你还未选择关联站");
			return false;
		}
		if(!check_box.isChecked()){
		   showToast("请先阅读并同意平台协议");
		   return false;
		}
		return true;
	}
	@Override
	public void deleteZhan(int position) {
		list.remove(position);
		yixuanze_zhan.setText("已选择"+list.size()+"个站");
		listView1.setAdapter(guanlianZhanAdapter);
		//调高度
	 	ListViewUtil.setListViewHeightBasedOnChildren(listView1);
		
	}
	
    
}
