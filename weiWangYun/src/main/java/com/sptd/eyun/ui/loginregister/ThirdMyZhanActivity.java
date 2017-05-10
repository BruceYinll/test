package com.sptd.eyun.ui.loginregister;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.EyunApplication;
import com.sptd.eyun.R;
import com.sptd.eyun.adapter.GuanlianZhanAdapter;
import com.sptd.eyun.adapter.ModifyGuanlianZhanAdapter;
import com.sptd.eyun.adapter.ModifyGuanlianZhanAdapter.DeleteZhanOnClickListener;
import com.sptd.eyun.dialog.ApplyDialog;
import com.sptd.eyun.dialog.LoginOutDialog;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.obj.UserStation;
import com.sptd.eyun.ui.myinfo.MipcaActivityCapture;
import com.sptd.eyun.ui.myinfo.ModifyMyZhanActivity;
import com.sptd.eyun.ui.myinfo.MyZhanActivity;
import com.sptd.eyun.ui.myinfo.OverlayActivity;
import com.sptd.eyun.ui.myinfo.SelectZhanActivity;
import com.sptd.log.Log;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.StringUtils;

/**
 * 没有授权通过的站,可以在这个页面再申请关联站
 * @author ldy
 *
 */
public class ThirdMyZhanActivity extends BaseInteractActivity 
implements OnClickListener,DeleteZhanOnClickListener{
private final static int MAP_GREQUEST_CODE=0;
private final static int SCANNIN_GREQUEST_CODE = 1;
private final static int SELECT_GREQUEST_CODE = 2;

//提交
private Button tijiao;	
private ImageView iv_left; 
//要提交的关联站
private StringBuffer stations;


//关联站,之前关联站oldUserStations和将要被提交关联站newUserStations
private List<UserStation> oldUserStations=new ArrayList<UserStation>();
private List<UserStation> newUserStations=new ArrayList<UserStation>();
//所有站
private List<Station> allStations=new ArrayList<Station>();

private ListView listView1;
private ModifyGuanlianZhanAdapter modifyGuanlianZhanAdapter;



//地图添加,扫一扫添加,列表添加
private LinearLayout myzhan_ditutianjia;
private LinearLayout myzhan_saotianjia;
private LinearLayout show_dianzhan;
private ImageView myzhan_xuanzezhan;

private TextView new_dianzhan;

//当前选择的电站在列表中的index
private int allStations_i;
//确定添加
private Button addzhan_queding;

private UserObj userObj;

//申请成功弹出提示对话框
private ApplyDialog dialog;
private TextView tv_content;
private TextView tuichu;


//根据手机号关联站
private String mobile;

       //删除站
		private LoginOutDialog loginOutDialog;
		private TextView title;
		private TextView content;
		private TextView loginOutDialog_quxiao;
		private TextView loginOutDialog_queding;



public ThirdMyZhanActivity(){
super(R.layout.activity_myzhan, false);
}

@Override
public void onSuccess(BaseModel res) {
switch (res.getInfCode()) {
case InterfaceFinals.INF_FINDUSERSTATIONLISTNESTATUS://获取待审核和已审核的关联站			
	oldUserStations=(List<UserStation>) res.getObject();
	newUserStations.addAll(oldUserStations);
	
	refreshView();
	break;
	
case  InterfaceFinals.INF_FINDSTATIONLIST://获取所有站成功
//	   showToast(res.getMessage());
	   allStations=(List<Station>) res.getObject();
	   break; 
	   
	   
case  InterfaceFinals.INF_UPDATESELFSTATIONSBYMOBILE://更新我的站成功
//	   showToast(res.getMessage());
//	   ThirdMyZhanActivit.this.finish();
	   dialog=new ApplyDialog(ThirdMyZhanActivity.this,R.style.MyDialog);
		dialog.setCancelable(false);
		dialog.show();
		tv_content=dialog.getTv_content();
		tv_content.setText("授权信息已提交,请等待验证");
		tuichu=dialog.getTuichu();
		if(tuichu==null){
			Log.v("tuichu", "null");
		}
		tuichu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//退出应用
//				RegisterActivity.this.finish();

				dialog.dismiss();
				
				EyunApplication.getInstance().removeAllActivity();
				System.exit(0);
//				 android.os.Process.killProcess(android.os.Process.myPid());
//				 RegisterActivity.this.finish();
//				 System.exit(0); 
//				 ActivityManager activityMgr= (ActivityManager) RegisterActivity.this.getSystemService(ACTIVITY_SERVICE);

//				 activityMgr.restartPackage(getPackageName());
				
				 
			}
		});
	   break;
	
/*		case InterfaceFinals.INF_FINDDEVICETYPELIST:
	List<DeviceType> deviceTypeList=(List<DeviceType>) res.getObject();
	if(deviceTypeList.size()>0){
	  for(int i=0;i<userStationList.size();i++){
		Station station=userStationList.get(i).getStation();
		
		if(deviceTypeList.get(0).getStationId()==station.getTid())
			 shebeiHashMap.put(i, 
			    		deviceTypeList);
	  }
 
	  zvpAdapterTwo.notifyDataSetChanged();
	}	
	break;*/
/*
case :
	break;
*/
}


}


@Override
public void onFail(BaseModel res) {
showToast(res.getMessage());
/*
String failInf=res.getMessage();
if("请先登录".equals(failInf)){
	startActivity(LoginActivity.class);
	finish();
}
*/
}

@Override
protected void getData() {
userObj=getUserData();
Intent intent=getIntent();
mobile=intent.getStringExtra("data");
//getUserStations();
findAllStations();

}

@Override
protected void findView() {
tijiao=(Button) findViewById(R.id.tijiao);
tijiao.setOnClickListener(this);
iv_left=(ImageView) findViewById(R.id.iv_left);
//iv_left.setVisibility(View.INVISIBLE);
iv_left.setOnClickListener(this);

listView1=(ListView) findViewById(R.id.listView1);
modifyGuanlianZhanAdapter=new ModifyGuanlianZhanAdapter(this, newUserStations);
modifyGuanlianZhanAdapter.setListeners(this);
listView1.setAdapter(modifyGuanlianZhanAdapter);



myzhan_ditutianjia=(LinearLayout) findViewById(R.id.myzhan_ditutianjia);
myzhan_ditutianjia.setOnClickListener(this);
myzhan_saotianjia=(LinearLayout) findViewById(R.id.myzhan_saotianjia);
myzhan_saotianjia.setOnClickListener(this);
show_dianzhan=(LinearLayout) findViewById(R.id.show_dianzhan);
show_dianzhan.setOnClickListener(this);
myzhan_xuanzezhan=(ImageView) findViewById(R.id.myzhan_xuanzezhan);
myzhan_xuanzezhan.setOnClickListener(this);

new_dianzhan=(TextView) findViewById(R.id.new_dianzhan);


addzhan_queding=(Button) findViewById(R.id.addzhan_queding);
addzhan_queding.setOnClickListener(this);
}

@Override
protected void refreshView() {
modifyGuanlianZhanAdapter.notifyDataSetChanged();

}

//获得我的站,未审核和待审核都返回
private void getUserStations(){
Type t = new TypeToken<BaseModel<List<UserStation>>>() {
}.getType();
BaseAsyncTask<List<UserStation>> task = new BaseAsyncTask<List<UserStation>>(this,t,
		InterfaceFinals.INF_FINDUSERSTATIONLISTNESTATUS,true);
HashMap<String, String> params = new HashMap<String, String>();
params.put("token",userObj.getToken() );
//params.put("status", ""+2);
task.execute(params);
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
/**
* 更新我的站
*/
private void  updateSelfStations(String stationids){
Type t = new TypeToken<BaseModel<UserObj>>() {
}.getType();
BaseAsyncTask<UserObj> task = new BaseAsyncTask<UserObj>(this,t,
		InterfaceFinals.INF_UPDATESELFSTATIONSBYMOBILE,true);
HashMap<String, String> params = new HashMap<String, String>();
params.put("mobile", mobile);
params.put("stationids", stationids);
Log.v("stationids_modify", stationids);
task.execute(params);
}


@Override
public void onClick(View v) {
Intent intent=new Intent();
Bundle bundle=new Bundle();
switch (v.getId()) {

case R.id.tijiao:
		 stations=new StringBuffer();
		 for(int i=0;i<newUserStations.size();i++){
			stations.append(newUserStations.get(i).getStationId());
			if(i<newUserStations.size()-1)
				stations.append(",");
		}
		 if(check()){
		   updateSelfStations(stations.toString());
		 }
	break;

case R.id.iv_left:
	  finish();
	break;

	
	
case  R.id.myzhan_ditutianjia:			
	intent.setClass(ThirdMyZhanActivity.this, OverlayActivity.class);			
	bundle.putSerializable("allzhan", (ArrayList<Station>) allStations);
	intent.putExtras(bundle);
  startActivityForResult(intent, MAP_GREQUEST_CODE);
	 break;			 			 
case   R.id.myzhan_saotianjia:
	intent.setClass(ThirdMyZhanActivity.this, MipcaActivityCapture.class);
  startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
	break;	
case   R.id.show_dianzhan:
	intent.setClass(ThirdMyZhanActivity.this, SelectZhanActivity.class);
	bundle.putSerializable("allzhan", (ArrayList<Station>) allStations);
	intent.putExtras(bundle);
  startActivityForResult(intent, SELECT_GREQUEST_CODE);
	break;
case   R.id.myzhan_xuanzezhan:
	intent.setClass(ThirdMyZhanActivity.this, SelectZhanActivity.class);
	bundle.putSerializable("allzhan", (ArrayList<Station>) allStations);
	intent.putExtras(bundle);
  startActivityForResult(intent, SELECT_GREQUEST_CODE);
	break;

	
	
case  R.id.addzhan_queding:			
	 if("选择的电站名显示在这里".equals(new_dianzhan.getText().toString())){
			Toast.makeText(ThirdMyZhanActivity.this,
					"请选择电站", 3000).show();
		}else {
			for(int i=0;i<newUserStations.size();i++){
				if(newUserStations.get(i).getStationId().equals(allStations.get(allStations_i).getTid())){
					Toast.makeText(ThirdMyZhanActivity.this,
							"已经添加过该站，不能重复添加！", 3000).show();
					break;
				}
				if (i==newUserStations.size()-1) {
					UserStation userStation=new UserStation();
					userStation.setStationId(allStations.get(allStations_i).getTid());
					userStation.setStation(allStations.get(allStations_i));
					userStation.setStatus(1);
					newUserStations.add(userStation);					
					Toast.makeText(ThirdMyZhanActivity.this,
							"已添加", 3000).show();
					break;
				}
			}
			if (newUserStations.size()==0) {
				UserStation userStation=new UserStation();
				userStation.setStationId(allStations.get(allStations_i).getTid());;
				userStation.setStation(allStations.get(allStations_i));
				userStation.setStatus(1);
				newUserStations.add(userStation);	
			
				Toast.makeText(ThirdMyZhanActivity.this,
						"已添加", 3000).show();
			
			}
			/*
			for(int i=0;i<oldUserStations.size();i++){
				//旧的有
				if(oldUserStations.get(i).getStationId().equals(allStations.get(allStations_i).getTid())){
					//新的没有
					if(!newUserStations.contains(oldUserStations.get(i))){
						newUserStations.add(oldUserStations.get(i));
					    break;
					}
				}else {//旧的没有
					if(i==oldUserStations.size()-1){
						for(int j=0;j<newUserStations.size();j++){
							//新的有
							if(newUserStations.get(j).getStationId().equals(allStations.get(allStations_i).getTid())){
								break;
							}
							//新的没有
							if (j==newUserStations.size()-1) {
								 UserStation userStation=new UserStation();
								 userStation.setStationId(allStations.get(allStations_i).getTid());
								 userStation.setStation(allStations.get(allStations_i));
								 userStation.setStatus(1);
								 newUserStations.add(userStation);
							}
						}
					}
				}

			 }
			*/
//			Toast.makeText(ThirdMyZhanActivity.this,
//					"已添加", 3000).show();
		}
	refreshView();
	break;
	

}

}
//获得地图页面的返回值
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
switch (requestCode) {
case MAP_GREQUEST_CODE:
	if(data!=null){
		allStations_i=data.getIntExtra("zhan",-1);
		new_dianzhan.setText(allStations.get(allStations_i).getName());
		/*
		Station stationFromMap=allStations.get(allStations_i);
		//至少有一个设备的站才可以被关联
		if(stationFromMap.getDeviceTypeList()==null
				|| stationFromMap.getDeviceTypeList().size()==0){
			Toast.makeText(ThirdMyZhanActivity.this,
					"无设备,不能关联该站", 3000).show();
			new_dianzhan.setText("选择的电站名显示在这里");
		}else {
			List<DeviceType> deviceTypeList=stationFromMap.getDeviceTypeList();
			for(int i=0;i<deviceTypeList.size();i++){
				if(deviceTypeList.get(i)!=null
						&& deviceTypeList.get(i).getDeviceList().size()>0){
					new_dianzhan.setText(allStations.get(allStations_i).getName());
					break;
				}
				if(i==deviceTypeList.size()-1){
					Toast.makeText(ThirdMyZhanActivity.this,
							"无设备,不能关联该站", 3000).show();
					new_dianzhan.setText("选择的电站名显示在这里");
				}
			}					
		}
	   */
	  }	
	break;

case SCANNIN_GREQUEST_CODE:
	if(data!=null){
		  Bundle bundle = data.getExtras();	
		  String erweima=bundle.getString("result");
	try {
 
		  Gson gson=new Gson();
		  Station station= gson.fromJson(erweima, Station.class);

		  long tid=station.getTid();
		  for(int i=0;i<allStations.size();i++){
			  if(allStations.get(i).getTid()==tid)
				  allStations_i=i;
		  }	
		  new_dianzhan.setText(allStations.get(allStations_i).getName());
	} catch (Exception e) {
		Toast.makeText(this, "无法识别此二维码", 3000).show();
	}	  
		  /*
		  Station stationFromScannin=allStations.get(allStations_i);
			//至少有一个设备的站才可以被关联
			if(stationFromScannin.getDeviceTypeList()==null
					|| stationFromScannin.getDeviceTypeList().size()==0){
				Toast.makeText(ThirdMyZhanActivity.this,
						"无设备,不能关联该站", 3000).show();
				new_dianzhan.setText("选择的电站名显示在这里");
			}else {
				List<DeviceType> deviceTypeList=stationFromScannin.getDeviceTypeList();
				for(int i=0;i<deviceTypeList.size();i++){
					if(deviceTypeList.get(i)!=null
							&& deviceTypeList.get(i).getDeviceList().size()>0){
						new_dianzhan.setText(allStations.get(allStations_i).getName());
						break;
					}
					if(i==deviceTypeList.size()-1){
						Toast.makeText(ThirdMyZhanActivity.this,
								"无设备,不能关联该站", 3000).show();
						new_dianzhan.setText("选择的电站名显示在这里");
					}
				}					
			}
			*/
//		  Toast.makeText(this, bundle.getString("result"), 3000).show();
		  Log.v("erweima",bundle.getString("result"));
		}
		break;
	
case SELECT_GREQUEST_CODE:
	if(data!=null){
		  Bundle bundle = data.getExtras();	
		  long tid=bundle.getLong("zhan");
		  Log.v("erweimaselect", ""+tid);
		  for(int i=0;i<allStations.size();i++){
			  if(allStations.get(i).getTid()==tid)
				  allStations_i=i;
		  }	
		  new_dianzhan.setText(allStations.get(allStations_i).getName());
		  /*
		  Station stationFromSelect=allStations.get(allStations_i);
			//至少有一个设备的站才可以被关联
			if(stationFromSelect.getDeviceTypeList()==null
					|| stationFromSelect.getDeviceTypeList().size()==0){
				Toast.makeText(ThirdMyZhanActivity.this,
						"无设备,不能关联该站", 3000).show();
				new_dianzhan.setText("选择的电站名显示在这里");
			}else {
				List<DeviceType> deviceTypeList=stationFromSelect.getDeviceTypeList();
				for(int i=0;i<deviceTypeList.size();i++){
					if(deviceTypeList.get(i)!=null
							&& deviceTypeList.get(i).getDeviceList().size()>0){
						new_dianzhan.setText(allStations.get(allStations_i).getName());
						break;
					}
					if(i==deviceTypeList.size()-1){
						Toast.makeText(ThirdMyZhanActivity.this,
								"无设备,不能关联该站", 3000).show();
						new_dianzhan.setText("选择的电站名显示在这里");
					}
				}					
			}
			*/
//		  Toast.makeText(this, bundle.getString("result"), 3000).show();
		  Log.v("erweima",""+bundle.getLong("zhan"));
	}
	break;
}


}

private boolean check(){
if(StringUtils.isNull(stations.toString())){
	showToast("请申请关联一个站！");
	return false;
}
return true;
}

@Override
public void deleteZhan(final int position) {
	loginOutDialog = new LoginOutDialog(ThirdMyZhanActivity.this, R.style.MyDialog);
	loginOutDialog.setCancelable(false);
	loginOutDialog.show();
	title=loginOutDialog.getTitle();
	title.setText("提示");
	content=loginOutDialog.getContent();
	content.setText("从列表中删除该站?");
	loginOutDialog_quxiao = loginOutDialog.getLoginout_quxiao();
	loginOutDialog_queding = loginOutDialog.getLoginout_queding();
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
			newUserStations.remove(position);		
			refreshView();

		}
	});
}


}
