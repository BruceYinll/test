package com.sptd.eyun.ui.myinfo;

import java.util.ArrayList;
import java.util.List;









import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerDragListener;
import com.baidu.mapapi.model.LatLng;
import com.sptd.eyun.R;
import com.sptd.eyun.obj.Station;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 地图添加站
 * @author ldy
 *
 */
public class OverlayActivity extends Activity{
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	List<BitmapDescriptor> listBD=new ArrayList<BitmapDescriptor>();
	List<LatLng> listLL=new ArrayList<LatLng>();
    List<OverlayOptions> listOO=new ArrayList<OverlayOptions>();
    List<Marker> listM=new ArrayList<Marker>();
    List<String> listS=new ArrayList<String>();
    //所有站
    private List<Station> allStations=new ArrayList<Station>();
    
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
			
				Toast.makeText(OverlayActivity.this, "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置", Toast.LENGTH_LONG).show();
			} else if (s
					.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
	
				Toast.makeText(OverlayActivity.this, "网络出错", Toast.LENGTH_LONG).show();
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

        //点击电站,回传电站的信息
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(final Marker marker) {
				for(int i=0;i<listM.size();i++){
					if(marker==listM.get(i)){
						Toast.makeText(
								OverlayActivity.this,
								"选中的电站：" + listS.get(i),
								Toast.LENGTH_LONG).show();
//						OverlayDemo.this.finish();
//						allStations.get(i)
						Intent intent=getIntent();
//						intent.putExtra("zhan", listS.get(i));
						
						intent.putExtra("zhan", i);
						setResult(RESULT_OK, intent);
						OverlayActivity.this.finish();
					}
				}
				return false;
			}
		});;
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
				OverlayActivity.this.finish();
				
			}
		});
	}
	
	private void initOverlay(){
		getData();
		if (allStations.size()>0) {
			MapStatusUpdate u = MapStatusUpdateFactory
					.newLatLng(new LatLng(Double.parseDouble(allStations.get(0).getPositiony()), 
							Double.parseDouble(allStations.get(0).getPositionx())));
			mBaiduMap.setMapStatus(u);
		}
		
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
		allStations=(List<Station>) bundle.getSerializable("allzhan");
		for(int i=0;i<allStations.size();i++){
			listLL.add(new LatLng(Double.parseDouble(allStations.get(i).getPositiony()), 
					Double.parseDouble(allStations.get(i).getPositionx())));
			listS.add(allStations.get(i).getName());
		}
		
		/*
		listLL.add(new LatLng(22.540162, 114.011791));
		listLL.add(new LatLng(22.560162, 114.031791));
		listLL.add( new LatLng(22.510162, 114.041791));
		listLL.add(new LatLng(22.590162, 114.071791));
		listS.add("福田区微电站");
		listS.add("宝安区微电站");
		listS.add("南山区微电站");
		listS.add("罗湖区微电站");
*/
		LayoutInflater inflater =LayoutInflater.from(OverlayActivity.this);
		View view=inflater.inflate(R.layout.overlay_mark, null);
		Button icon_mark_button=(Button) view.findViewById(R.id.icon_mark_button);
		
		BitmapDescriptor bd=null;
		OverlayOptions oo=null;
		Marker m=null;
		for(int i=0;i<listLL.size();i++){
			icon_mark_button.setText(listS.get(i));
			bd=BitmapDescriptorFactory
					.fromView(view);
			listBD.add(bd);
			oo=new MarkerOptions().position(listLL.get(i)).icon(bd)
					.zIndex(9).draggable(true);
			listOO.add(oo);
			
			m=(Marker) mBaiduMap.addOverlay(oo);
			
			listM.add(m);							
		}

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
		for(BitmapDescriptor bd:listBD){
			bd.recycle();
		}
	}
}
