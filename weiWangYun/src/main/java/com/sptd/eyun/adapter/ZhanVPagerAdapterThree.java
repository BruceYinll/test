package com.sptd.eyun.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sptd.eyun.R;
import com.sptd.eyun.adapter.ZhanVPagerAdapterTwo.ShuxingClick;
import com.sptd.eyun.adapter.ZhanVPagerAdapterTwo.TextPositionClick;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.obj.UserStation;
import com.sptd.eyun.ui.zhan.ZhanOverlayActivity;
import com.sptd.log.Log;
import com.sptd.util.ListViewUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ZhanVPagerAdapterThree extends PagerAdapter{

	private Context context;
	
	private List<UserStation> userStationList=new ArrayList<UserStation>();
	
	private List<String> position_pictureList;
	private List<String> position_textList;
	private List<List<String>> shuxingList=new ArrayList<List<String>>();
	private List<List<String>> shujuList=new ArrayList<List<String>>();
	private HashMap<Integer, List<DeviceType>> shebeiHashMap=new HashMap<Integer, List<DeviceType>>();


	
	

	

	

	public ZhanVPagerAdapterThree(Context context,
			List<UserStation> userStationList,
			List<String> position_pictureList, List<String> position_textList,
			List<List<String>> shuxingList, List<List<String>> shujuList,
			HashMap<Integer, List<DeviceType>> shebeiHashMap) {
		super();
		this.context = context;
		this.userStationList = userStationList;
		this.position_pictureList = position_pictureList;
		this.position_textList = position_textList;
		this.shuxingList = shuxingList;
		this.shujuList = shujuList;
		this.shebeiHashMap = shebeiHashMap;
	}

	@Override
	public int getCount() {
//		return resId.length;
		return position_textList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	
     //5s刷新数据
		@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
//			return POSITION_NONE;
		//获得当前iew	
		View view =(View) object;
		ListView lv_shuju=(ListView) view.findViewById(R.id.lv_shuju);
		Integer position=(Integer) view.getTag();
		ShuxingAdapter shujuAdapter=new ShuxingAdapter(context, shujuList.get(position));
		lv_shuju.setAdapter(shujuAdapter);
		ListViewUtil.setListViewHeightBasedOnChildren(lv_shuju);
		//为何notifyDataSetChanged没有刷新
//		((ShuxingAdapter)lv_shuju.getAdapter()).notifyDataSetChanged();
		Log.v("zvpa_refresh", "5s");
	    //不销毁view
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view=LayoutInflater.from(context).inflate(R.layout.viewpager_view_two, null);
		
		ImageView iv_position=(ImageView) view.findViewById(R.id.iv_position);
		displayImage(iv_position, InterfaceFinals.URL_HEAD+position_pictureList.get(position));
		
		TextView viewpager_view_text_location=(TextView) view.findViewById(R.id.viewpager_view_text_location);
		viewpager_view_text_location.setText(position_textList.get(position));
		LinearLayout ll_text_location=(LinearLayout) view.findViewById(R.id.ll_text_location);
		ll_text_location.setOnClickListener(new TextPositionClick(userStationList.get(position).getStation()));
		
		
		LinearLayout ll_shuxing=(LinearLayout) view.findViewById(R.id.ll_shuxing);
		LinearLayout ll_shuju=(LinearLayout) view.findViewById(R.id.ll_shuju);
		LinearLayout ll_shebei=(LinearLayout) view.findViewById(R.id.ll_shebei);
		
		ListView lv_shuxing=(ListView) view.findViewById(R.id.lv_shuxing);
		ListView lv_shuju=(ListView) view.findViewById(R.id.lv_shuju);
		ListView lv_shebei=(ListView) view.findViewById(R.id.lv_shebei);
		
		
		ShuxingAdapter shuxingAdapter=new ShuxingAdapter(context, shuxingList.get(position));
		lv_shuxing.setAdapter(shuxingAdapter);
		ListViewUtil.setListViewHeightBasedOnChildren(lv_shuxing);
//		ListViewUtil.tiaoGaoDu(lv_shuxing);
		
		ShuxingAdapter shujuAdapter=new ShuxingAdapter(context, shujuList.get(position));
		Log.v("size", ""+shujuList.get(position).size());
		lv_shuju.setAdapter(shujuAdapter);
		view.setTag(position);
		ListViewUtil.setListViewHeightBasedOnChildren(lv_shuju);
//		ListViewUtil.tiaoGaoDu(lv_shuju);
		
		
        if(shebeiHashMap.get(position)!=null){
		 ZhanShebeiAdapter zhanShebeiAdapter=new ZhanShebeiAdapter(context, shebeiHashMap.get(position));
		 lv_shebei.setAdapter(zhanShebeiAdapter);

		 ListViewUtil.setListViewHeightBasedOnChildren(lv_shebei);
//		 ListViewUtil.tiaoGaoDu(lv_shebei);
        }
		
		
		ll_shuxing.setOnClickListener(new ShuxingClick(ll_shuxing, lv_shuxing));
		ll_shuju.setOnClickListener(new ShuxingClick(ll_shuju, lv_shuju));
		ll_shebei.setOnClickListener(new ShuxingClick(ll_shebei, lv_shebei));
		
		
		container.addView(view);
		
		return view;
	}

	public void displayImage(ImageView imageView,String url){
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		ImageLoader.getInstance().displayImage(url,imageView,options);
	}
    
	class ShuxingClick implements OnClickListener{
        private LinearLayout ll_shuxing;
        private ListView lv_shuxing;
        
		public ShuxingClick(LinearLayout ll_shuxing, ListView lv_shuxing) {
			super();
			this.ll_shuxing = ll_shuxing;
			this.lv_shuxing = lv_shuxing;
		}

		@Override
		public void onClick(View v) {
			if(ll_shuxing.isSelected()){
				ll_shuxing.setSelected(false);
				lv_shuxing.setVisibility(View.GONE);
			}else {
				ll_shuxing.setSelected(true);
				lv_shuxing.setVisibility(View.VISIBLE);
			}
			
		}
		
	}

	
    class TextPositionClick implements OnClickListener{
    	
        private Station station;
        
        
		public TextPositionClick(Station station) {
			super();
			this.station = station;
		}


		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(context, ZhanOverlayActivity.class);
			Bundle bundle=new Bundle();
			bundle.putSerializable("station", station);
			intent.putExtras(bundle);
			context.startActivity(intent);
						
		}
    	
    }
	
	
	

}

