package com.sptd.eyun.adapter;

import java.util.List;

import com.sptd.eyun.R;
import com.sptd.eyun.obj.Device;
import com.sptd.util.StringUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShebeiGridViewAdapter extends BaseAdapter{

	private Context context;
	private List<Device> deviceList;
	
	
	public ShebeiGridViewAdapter(Context context, List<Device> deviceList) {
		super();
		this.context = context;
		this.deviceList = deviceList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return deviceList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return deviceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.shebeigridview_item, null);
		TextView textView=(TextView) view.findViewById(R.id.textView1);
		textView.setText("["+StringUtils.cutString(deviceList.get(position).getName(), 13)+"]");
		return view;
	}

}
