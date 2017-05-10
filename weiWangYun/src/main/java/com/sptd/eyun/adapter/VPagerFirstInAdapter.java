package com.sptd.eyun.adapter;


import java.util.ArrayList;

import com.sptd.eyun.R;

import android.R.integer;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class VPagerFirstInAdapter extends PagerAdapter {

	private Context mContext;
	
	private ArrayList<View> mGuideViews;

	private final int[] resId = { R.drawable.loading_1, R.drawable.loading_2,
			R.drawable.loading_5 };
	public VPagerFirstInAdapter(Context context,ArrayList<View> guideViews) {
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
	
	private ImageView createImageView(int resId){
		ImageView im=new ImageView(mContext);
		im.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	    im.setScaleType(ScaleType.CENTER_CROP);
	    im.setImageResource(resId);
	    
	    return im;
	}

}
