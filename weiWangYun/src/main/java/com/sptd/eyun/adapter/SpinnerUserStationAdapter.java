package com.sptd.eyun.adapter;

import java.util.ArrayList;
import java.util.List;

import com.baidu.platform.comapi.map.n;
import com.sptd.eyun.R;
import com.sptd.eyun.obj.UserStation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SpinnerUserStationAdapter extends BaseAdapter{
	
	private Context context;
	private List<UserStation> userStationList=new ArrayList<UserStation>();

	
	
	public SpinnerUserStationAdapter(Context context,
			List<UserStation> userStationList) {
		super();
		this.context = context;
		this.userStationList = userStationList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userStationList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userStationList.get(position);
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
		textView.setText(userStationList.get(position).getStation().getName());
		
		return view;
	}

}
