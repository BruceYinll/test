package com.sptd.eyun.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sptd.eyun.R;
import com.sptd.eyun.adapter.GuanlianZhanAdapter.DeleteZhanOnClickListener;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.obj.UserStation;

public class ModifyGuanlianZhanAdapter extends BaseAdapter{

	private Context context;
	private List<UserStation> list;
	
	private DeleteZhanOnClickListener delZhan;
	

	
	public ModifyGuanlianZhanAdapter(Context context, List<UserStation> list) {
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
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater=LayoutInflater.from(context);
//		View view=inflater.inflate(R.layout.guanlianzhan_item, null);
		View view=inflater.inflate(R.layout.guanlizhan_item_2, null);
		ImageView imageView1=(ImageView) view.findViewById(R.id.modifyguanlianzhan_imageView1);
		if(list.get(position).getStatus()==1)  //1待审核,2已审核
			imageView1.setImageResource(R.drawable.daishenhe);
		    
		if(list.get(position).getStatus()==2)
			imageView1.setImageResource(R.drawable.yishenhe);
		TextView textView=(TextView) view.findViewById(R.id.textView1);
		Button button1=(Button) view.findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			        delZhan.deleteZhan(position);
				
			}
		});
		textView.setText(list.get(position).getStation().getName());
		return view;
	}
	public void setListeners(DeleteZhanOnClickListener delZhan){
		this.delZhan=delZhan;
	}
	public interface DeleteZhanOnClickListener{
		public void deleteZhan(int position);
	}

}