package com.sptd.eyun.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;


public class ZhanVPagerAdapter extends PagerAdapter {

	private Context mContext;
	
	private List<View> mGuideViews;
	public ZhanVPagerAdapter(Context context,List<View> guideViews) {
		super();
		this.mContext = context;
		this.mGuideViews=guideViews;
	}

	@Override
	public int getCount() {
//		return resId.length;
		return mGuideViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container,final int position) {
//		View view=createImageView(resId[position]);
		View view=mGuideViews.get(position);
		container.addView(view);
		return view;
	}
	

}

