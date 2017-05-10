package com.sptd.eyun.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sptd.eyun.R;
import com.sptd.eyun.obj.Parameter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShebeiDataListViewAdapter extends BaseAdapter{

	private Context context;
	private List<Parameter> parameterList=new ArrayList<Parameter>();
	
	
	public ShebeiDataListViewAdapter(Context context,
			List<Parameter> parameterList) {
		super();
		this.context = context;
		this.parameterList = parameterList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return parameterList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return parameterList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.shebei_data_listview_item, null);		
		TextView shebei_data_listview_item_xuhao=(TextView) view.findViewById(R.id.shebei_data_listview_item_xuhao);
		TextView shebei_data_listview_item_mingcheng=(TextView) view.findViewById(R.id.shebei_data_listview_item_mingcheng);
		TextView shebei_data_listview_item_shuzhi=(TextView) view.findViewById(R.id.shebei_data_listview_item_shuzhi);
		TextView shebei_data_listview_item_danwei=(TextView) view.findViewById(R.id.shebei_data_listview_item_danwei);

		//序号从1开始
		  shebei_data_listview_item_xuhao.setText(""+(position+1));
		  shebei_data_listview_item_mingcheng.setText(parameterList.get(position).getName());
		  shebei_data_listview_item_shuzhi.setText(parameterList.get(position).getData());
		  shebei_data_listview_item_danwei.setText(parameterList.get(position).getUnit());

		return view;
	}

	public List<Parameter> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<Parameter> parameterList) {
		this.parameterList = parameterList;
	}
	
	

}
