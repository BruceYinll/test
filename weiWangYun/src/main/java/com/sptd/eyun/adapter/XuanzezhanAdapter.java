package com.sptd.eyun.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sptd.eyun.R;
import com.sptd.eyun.obj.Station;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class XuanzezhanAdapter extends BaseAdapter{

	private Context context;
	private List<Station> list;
	
	
	
	public XuanzezhanAdapter(Context context, List<Station> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LayoutInflater inflater=LayoutInflater.from(context);
//		View view=inflater.inflate(R.layout.guanlianzhan_item, null);
		View view=inflater.inflate(R.layout.xuanzezhan_item, null);
		TextView textView=(TextView) view.findViewById(R.id.textView1);
		
		textView.setText(list.get(arg0).getName());
		return view;
	}

}
