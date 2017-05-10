package com.sptd.eyun.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sptd.eyun.R;
import com.sptd.eyun.obj.Device;
import com.sptd.eyun.obj.Parameter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShebeiVPagerAdapter extends PagerAdapter{

	private Context context;
	private List<Device> deviceList=new ArrayList<Device>();
	
	
	public ShebeiVPagerAdapter(Context context, List<Device> deviceList) {
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
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	@Override
public int getItemPosition(Object object) {
	// TODO Auto-generated method stub
//		return POSITION_NONE;
	View view =(View) object;
	ListView viewpager_view_shebei_listView1=(ListView) view.findViewById(R.id.viewpager_view_shebei_listView1);
	ShebeiDataListViewAdapter shebeiDataListViewAdapter=(ShebeiDataListViewAdapter)viewpager_view_shebei_listView1.getAdapter();
	int position=(Integer)view.getTag();
	Log.v("deviceList_size", ""+deviceList.size());
	 List<Parameter> parameterList;
	   if(deviceList.get(position).getParameterList()==null){
		   parameterList=new ArrayList<Parameter>();
	   }else {
		   parameterList=deviceList.get(position).getParameterList();
	   }
	   shebeiDataListViewAdapter.setParameterList(parameterList);
 
	   ((ShebeiDataListViewAdapter)viewpager_view_shebei_listView1.getAdapter()).notifyDataSetChanged();

	//Toast当前设备
//	Toast.makeText(context, "adpter当前设备是--:"+deviceList.get(position).getName(), 3000).show();

	
	return super.getItemPosition(object);
}
	
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		 View view = LayoutInflater.from(context).inflate(R.layout.viewpager_view_shebei, container, false);
			
	        view.setTag(position);
			  TextView tv_tabName = (TextView) view.findViewById(R.id.viewpager_view_shebei_shebeiname);
			
			   tv_tabName.setText(deviceList.get(position).getName());
			   
			   ListView viewpager_view_shebei_listView1=(ListView) view.findViewById(R.id.viewpager_view_shebei_listView1);
			   //设备的参数列表
			   List<Parameter> parameterList;
			   if(deviceList.get(position).getParameterList()==null){
				   parameterList=new ArrayList<Parameter>();
			   }else {
				   parameterList=deviceList.get(position).getParameterList();
			   }
			   ShebeiDataListViewAdapter shebeiDataListViewAdapter=new ShebeiDataListViewAdapter(context,
					   parameterList);
			    
				Log.v("parameterlist_size", ""+parameterList.size());
				viewpager_view_shebei_listView1.setAdapter(shebeiDataListViewAdapter);
		//		ListViewUtil.setListViewHeightBasedOnChildren(lv_shuju);
		   container.addView(view);
		return view;
	}
	
	

}
