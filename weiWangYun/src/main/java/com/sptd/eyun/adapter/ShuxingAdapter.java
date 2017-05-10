package com.sptd.eyun.adapter;

import java.util.List;

import com.sptd.eyun.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ShuxingAdapter extends BaseAdapter{

	private Context context;
	private List<String> list;
	public ShuxingAdapter(Context context, List<String> list) {
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
		return  list.get(arg0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.child_item_expandlistview, null);
		TextView textView=(TextView) view.findViewById(R.id.textView1);
		textView.setText(list.get(position));
		return view;
		
	}
		/*ViewHolder viewHolder=null;
		if(null==convertView){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.child_item_expandlistview, null);
			viewHolder.shuxing_TextView=(TextView) convertView.findViewById(R.id.textView1);
		    convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		viewHolder.shuxing_TextView.setText(list.get(position));
		
		return convertView;
	}
	
	private static class ViewHolder
	{
		TextView shuxing_TextView;
	}*/
	
}
