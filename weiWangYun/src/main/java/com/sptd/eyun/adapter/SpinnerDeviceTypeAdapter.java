package com.sptd.eyun.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sptd.eyun.R;
import com.sptd.eyun.obj.DeviceType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerDeviceTypeAdapter extends BaseAdapter{
	
	private Context context;
	private List<DeviceType>  deviceTypeList=new ArrayList<DeviceType>();

	
	public SpinnerDeviceTypeAdapter(Context context,
			List<DeviceType> deviceTypeList) {
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
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return deviceTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=LayoutInflater.from(context).inflate(R.layout.spinner_userstation_item, null);
		TextView textView=(TextView) view.findViewById(R.id.tv_spinner_userstation_item);
		textView.setText(deviceTypeList.get(position).getName());
		
		return view;
	}

}
