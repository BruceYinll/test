package com.sptd.eyun.ui.zhan;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.sptd.eyun.R;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.ui.myinfo.OverlayActivity;
import com.sptd.eyun.ui.myinfo.OverlayActivity.SDKReceiver;

public class ZhanOverlayActivity extends Activity{
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private BitmapDescriptor bd;
	private LatLng lL;
    private OverlayOptions oo;
    private Marker m;
    private String s;
    //站
    private Station station;
    
	private ImageView back;
	
	private static final String LTAG = OverlayActivity.class.getSimpleName();

	/**
	 * 构造广播监听类，监听 SDK key 验证以及网络异常广播
	 */
	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			Log.d(LTAG, "action: " + s);

			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
			
				Toast.makeText(ZhanOverlayActivity.this, "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置", Toast.LENGTH_LONG).show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
	
				Toast.makeText(ZhanOverlayActivity.this, "网络出错", Toast.LENGTH_LONG).show();
			}
		}
	}

	private SDKReceiver mReceiver;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		
		setContentView(R.layout.activity_myzhan_ditu);
		
		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);
		
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(12.0f);
		mBaiduMap.setMapStatus(msu);
		initOverlay();       
		findViews();
		setListeners();
	}
	private void findViews(){
		back=(ImageView) findViewById(R.id.back);
	}
	private void setListeners(){
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ZhanOverlayActivity.this.finish();
				
			}
		});
	}
	
	private void initOverlay(){
		getData();
		MapStatusUpdate u = MapStatusUpdateFactory
				.newLatLng(new LatLng(Double.parseDouble(station.getPositiony()), 
						Double.parseDouble(station.getPositionx())));
		mBaiduMap.setMapStatus(u);
/*
		mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			public void onMarkerDrag(Marker marker) {
			}

			public void onMarkerDragEnd(Marker marker) {
				Toast.makeText(
						OverlayActivity.this,
						"拖拽结束，新位置：" + marker.getPosition().latitude + ", "
								+ marker.getPosition().longitude,
						Toast.LENGTH_LONG).show();
			}

			public void onMarkerDragStart(Marker marker) {
			}
		});
*/		
	}

	private void getData(){
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		station=(Station) bundle.getSerializable("station");
		lL=new LatLng(Double.parseDouble(station.getPositiony()),
				Double.parseDouble(station.getPositionx()));
		s=station.getName();
				
		LayoutInflater inflater =LayoutInflater.from(ZhanOverlayActivity.this);
		View view=inflater.inflate(R.layout.overlay_mark, null);
		Button icon_mark_button=(Button) view.findViewById(R.id.icon_mark_button);
		icon_mark_button.setText(s);
		bd=BitmapDescriptorFactory
				.fromView(view);
		oo=new MarkerOptions().position(lL).icon(bd)
				.zIndex(9).draggable(true);
		m=(Marker) mBaiduMap.addOverlay(oo);
		
	}
	
	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
				mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();
		super.onDestroy();
		// 取消监听 SDK 广播
		unregisterReceiver(mReceiver);
			bd.recycle();

	}
}

