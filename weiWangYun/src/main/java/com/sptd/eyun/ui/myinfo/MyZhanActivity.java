package com.sptd.eyun.ui.myinfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baidu.lbsapi.auth.i;
import com.google.gson.Gson;
import com.sptd.eyun.R;
import com.sptd.eyun.adapter.GuanlianZhanAdapter;
import com.sptd.eyun.adapter.GuanlianZhanAdapter.DeleteZhanOnClickListener;
import com.sptd.eyun.dialog.LoginOutDialog;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.ui.loginregister.RegisterActivity;
import com.sptd.log.Log;
import com.sptd.util.ListViewUtil;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 *注册时我的站
 * @author ldy
 *
 */
public class MyZhanActivity extends Activity implements DeleteZhanOnClickListener{
	private final static int MAP_GREQUEST_CODE=0;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private final static int SELECT_GREQUEST_CODE = 2;

	
	
	
    //提交
	private Button tijiao;
	private ImageView iv_left; 
	
	
	private GuanlianZhanAdapter guanlianZhanAdapter;
	//关联站
	private List<Station> list=new ArrayList<Station>();
	//所有站
	private List<Station> allStations=new ArrayList<Station>();
	
	private ListView listView1;
	
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
	
	
	
	//删除站
			private LoginOutDialog loginOutDialog;
			private TextView title;
			private TextView content;
			private TextView loginOutDialog_quxiao;
			private TextView loginOutDialog_queding;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myzhan);
		getData();
		findViews();
		setAdapters();
		setListeners();
	}
	
	private void findViews(){
		tijiao=(Button) findViewById(R.id.tijiao);
		iv_left=(ImageView) findViewById(R.id.iv_left);
		
		listView1=(ListView) findViewById(R.id.listView1);
		
		myzhan_ditutianjia=(LinearLayout) findViewById(R.id.myzhan_ditutianjia);
		myzhan_saotianjia=(LinearLayout) findViewById(R.id.myzhan_saotianjia);
		show_dianzhan=(LinearLayout) findViewById(R.id.show_dianzhan);
		myzhan_xuanzezhan=(ImageView) findViewById(R.id.myzhan_xuanzezhan);
		
		
		new_dianzhan=(TextView) findViewById(R.id.new_dianzhan);
		
		
		addzhan_queding=(Button) findViewById(R.id.addzhan_queding);
	}
	private void setAdapters(){
//		guanlianZhanAdapter=new GuanlianZhanAdapter(MyZhanActivity.this, list);
		guanlianZhanAdapter=new GuanlianZhanAdapter(MyZhanActivity.this, list);
		guanlianZhanAdapter.setListeners(this);
		listView1.setAdapter(guanlianZhanAdapter);
//		setListViewHeightBasedOnChildren(listView1);
	}
	private void setListeners(){
		tijiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(list.size()==0){
				   Toast.makeText(MyZhanActivity.this,
					   "您还未选择关联站，请选择关联站", 3000).show();
				}else {
					Intent intent=getIntent();
					Bundle bundle=intent.getExtras();				
					bundle.putSerializable("new_list", (Serializable) list);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					MyZhanActivity.this.finish();
				}
				
			}
		});
		
		iv_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyZhanActivity.this.finish();
				
			}
		});
		
		myzhan_ditutianjia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MyZhanActivity.this, OverlayActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("allzhan", (ArrayList<Station>) allStations);
				intent.putExtras(bundle);
			    startActivityForResult(intent, MAP_GREQUEST_CODE);
				
			}
		});
		myzhan_saotianjia.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MyZhanActivity.this, MipcaActivityCapture.class);
//				Bundle bundle=new Bundle();
//				bundle.putSerializable("allzhan", (ArrayList<Station>) allStations);
//				intent.putExtras(bundle);
			    startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
				
			}
		});
		show_dianzhan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MyZhanActivity.this, SelectZhanActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("allzhan", (ArrayList<Station>) allStations);
				intent.putExtras(bundle);
			    startActivityForResult(intent, SELECT_GREQUEST_CODE);
				
			}
		});
		myzhan_xuanzezhan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MyZhanActivity.this, SelectZhanActivity.class);
				Bundle bundle=new Bundle();
				bundle.putSerializable("allzhan", (ArrayList<Station>) allStations);
				intent.putExtras(bundle);
			    startActivityForResult(intent, SELECT_GREQUEST_CODE);
				
			}
		});
		
	
		addzhan_queding.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {								
				if("选择的电站名显示在这里".equals(new_dianzhan.getText().toString())){
					Toast.makeText(MyZhanActivity.this,
							"请选择电站", 3000).show();
				}else{					
					for(int i=0;i<list.size();i++){
						if(list.get(i).getTid().equals(allStations.get(allStations_i).getTid())){
							Toast.makeText(MyZhanActivity.this,
									"已经添加过该站，不能重复添加！", 3000).show();
							break;
						}
						if (i==list.size()-1) {
							list.add(allStations.get(allStations_i));
							guanlianZhanAdapter=new GuanlianZhanAdapter(MyZhanActivity.this, list);
							guanlianZhanAdapter.setListeners(MyZhanActivity.this);
							listView1.setAdapter(guanlianZhanAdapter);
							Toast.makeText(MyZhanActivity.this,
									"已添加", 3000).show();
							break;
						}
					}
					if (list.size()==0) {
						list.add(allStations.get(allStations_i));
						guanlianZhanAdapter=new GuanlianZhanAdapter(MyZhanActivity.this, list);
						guanlianZhanAdapter.setListeners(MyZhanActivity.this);
						listView1.setAdapter(guanlianZhanAdapter);
						Toast.makeText(MyZhanActivity.this,
								"已添加", 3000).show();
					}
					
					
				}
				
				
				
				
			}
		});
	
	
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
					Toast.makeText(MyZhanActivity.this,
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
							Toast.makeText(MyZhanActivity.this,
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
					Toast.makeText(MyZhanActivity.this,
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
							Toast.makeText(MyZhanActivity.this,
									"无设备,不能关联该站", 3000).show();
							new_dianzhan.setText("选择的电站名显示在这里");
						}
					}					
				}
				*/
			  
//			  new_dianzhan.setText(allStations.get(allStations_i).getName());
//			  Toast.makeText(this, bundle.getString("result"), 3000).show();
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
						Toast.makeText(MyZhanActivity.this,
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
								Toast.makeText(MyZhanActivity.this,
										"无设备,不能关联该站", 3000).show();
								new_dianzhan.setText("选择的电站名显示在这里");
							}
						}					
					}
					*/
//				  Toast.makeText(this, bundle.getString("result"), 3000).show();
				  Log.v("erweima",""+bundle.getLong("zhan"));
			}
			break;
		}
		
		
	}
	
	private void getData(){
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		list=(List<Station>) bundle.getSerializable("guanlianzhan_list");
		allStations=(List<Station>) bundle.getSerializable("allzhan");
	}
	
	public  void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
		Log.v("myzhan", "back1");
/*
		Intent intent=getIntent();	
		Bundle bundle=intent.getExtras();
		List<String> list=bundle.getStringArrayList("guanlianzhan_list");
		bundle.putStringArrayList("new_list", (ArrayList<String>) list);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
*/
		MyZhanActivity.this.finish();
//		MyZhanActivity.this.finish();
	}

	@Override
	public void deleteZhan(final int position) {
		loginOutDialog = new LoginOutDialog(MyZhanActivity.this, R.style.MyDialog);
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
				list.remove(position);		
				listView1.setAdapter(guanlianZhanAdapter);

			}
		});
//		list.remove(position);		
//		listView1.setAdapter(guanlianZhanAdapter);
		
		
	}
}
