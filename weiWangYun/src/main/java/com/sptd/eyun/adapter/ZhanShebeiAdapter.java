package com.sptd.eyun.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sptd.eyun.R;
import com.sptd.eyun.obj.Device;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.others.LinkMovementClickMethod;
import com.sptd.eyun.ui.HomeActivity;
import com.sptd.eyun.ui.fragment.ShebeiFragment;
import com.sptd.eyun.ui.fragment.ShebeiFragmentThree;
import com.sptd.eyun.ui.fragment.ShebeiFragmentTwo;
import com.sptd.util.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ZhanShebeiAdapter extends BaseAdapter{

	private Context context;
	private  List<DeviceType> deviceTypeList=new ArrayList<DeviceType>();
	
	
	public ZhanShebeiAdapter(Context context, List<DeviceType> deviceTypeList) {
		super();
		this.context = context;
		this.deviceTypeList = deviceTypeList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return deviceTypeList.size();
	}
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return deviceTypeList.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		/*LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.child_item_expandlistview, null);
		TextView textView=(TextView) view.findViewById(R.id.textView1);
		
		DeviceType  deviceType=deviceTypeList.get(arg0);
		List<Device> deviceList=deviceType.getDeviceList();
		StringBuffer sb=new StringBuffer();
		sb.append(deviceType.getName()+" : ");
		for(int i=0;i<deviceList.size();i++){
			Device device=deviceList.get(i);
			if(i<deviceList.size()-1){
				sb.append(device.getName()+" , ");
				
			}else {
				sb.append(device.getName());
			}
		}
		
		textView.setText(sb.toString());
		return view;*/
		
/*		
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.child_item_expandlistview, null);
		TextView textView=(TextView) view.findViewById(R.id.textView1);
		
		DeviceType  deviceType=deviceTypeList.get(arg0);
		List<Device> deviceList=deviceType.getDeviceList();
		
		textView.setText(deviceType.getName()+"       :       ");				
		for(int i=0;i<deviceList.size();i++){
			Device device=deviceList.get(i);
			SpannableString spStr = new SpannableString(device.getName());
			spStr.setSpan(new ShebeiClickableSpan(device, deviceType),
					0, device.getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			textView.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明，否则会一直出现高亮
			textView.append(spStr);
//			textView.setMovementMethod(LinkMovementMethod.getInstance());//开始响应点击事件
			textView.setMovementMethod(LinkMovementClickMethod.getInstance());//开始响应点击事件
			
			
			if(i<deviceList.size()-1){
				textView.append("          ,           ");
				
			}else {
				
			}
		}		
		view.setEnabled(false);
		view.setClickable(false);
		view.setFocusable(false);
		return view;
		
		*/
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.child_item_expandlistview1, null);
		TextView textView=(TextView) view.findViewById(R.id.textView1);
		GridView gridView=(GridView) view.findViewById(R.id.gridView1);
		final DeviceType  deviceType=deviceTypeList.get(arg0);
		final List<Device> deviceList=deviceType.getDeviceList();
		textView.setText(StringUtils.cutString(deviceType.getName(), 5));
		gridView.setAdapter(new ShebeiGridViewAdapter(context, deviceList));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				((HomeActivity)context).getLl_zhan().setSelected(false);
				((HomeActivity)context).getLl_shebei().setSelected(true);
				((HomeActivity)context).getLl_yunwei().setSelected(false);
				((HomeActivity)context).getLl_more().setSelected(false);

				FragmentManager fm=((FragmentActivity) context).getSupportFragmentManager();
				FragmentTransaction transaction=fm.beginTransaction();
//				ShebeiFragment shebeiFragment=new ShebeiFragment();
//				ShebeiFragmentTwo shebeiFragmentTwo=new ShebeiFragmentTwo();
				ShebeiFragmentThree shebeiFragmentThree=new ShebeiFragmentThree();
				Bundle bundle = new Bundle();  
	            bundle.putString("deviceType", deviceType.getName());  
	            bundle.putString("device", deviceList.get(position).getName());  
//	            shebeiFragment.setArguments(bundle);  
//	            shebeiFragmentTwo.setArguments(bundle);
	            shebeiFragmentThree.setArguments(bundle);
	            
	            ((HomeActivity)context).setCurrentDeviceType(deviceType);
	            ((HomeActivity)context).setCurrentDevice(deviceList.get(position));
	            
//			    transaction.replace(R.id.fl_content, shebeiFragment);
//	            transaction.replace(R.id.fl_content, shebeiFragmentTwo);
	            transaction.replace(R.id.fl_content, shebeiFragmentThree);

			    transaction.commit();
				
			}
		});
		return view;
		
	}
	
	
	class ShebeiClickableSpan extends ClickableSpan{
        private Device device;
        private DeviceType deviceType;
		
		
		public ShebeiClickableSpan(Device device, DeviceType deviceType) {
			super();
			this.device = device;
			this.deviceType = deviceType;
		}

		@Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
              ds.setColor(Color.parseColor("#f7b341"));
//            ds.setColor(Color.WHITE);       //设置文件颜色
//            ds.setUnderlineText(true);      //设置下划线
        }

		@Override
		public void onClick(View widget) {
			
			((HomeActivity)context).getLl_zhan().setSelected(false);
			((HomeActivity)context).getLl_shebei().setSelected(true);
			((HomeActivity)context).getLl_yunwei().setSelected(false);
			((HomeActivity)context).getLl_more().setSelected(false);

			FragmentManager fm=((FragmentActivity) context).getSupportFragmentManager();
			FragmentTransaction transaction=fm.beginTransaction();
//			ShebeiFragment shebeiFragment=new ShebeiFragment();
			ShebeiFragmentTwo shebeiFragmentTwo=new ShebeiFragmentTwo();
			Bundle bundle = new Bundle();  
            bundle.putString("deviceType", deviceType.getName());  
            bundle.putString("device", device.getName());  
//            shebeiFragment.setArguments(bundle);  
            shebeiFragmentTwo.setArguments(bundle);
            
            ((HomeActivity)context).setCurrentDeviceType(deviceType);
            ((HomeActivity)context).setCurrentDevice(device);
            
//		    transaction.replace(R.id.fl_content, shebeiFragment);
            transaction.replace(R.id.fl_content, shebeiFragmentTwo);
		    transaction.commit();
						
		}
		
	}
		

}
