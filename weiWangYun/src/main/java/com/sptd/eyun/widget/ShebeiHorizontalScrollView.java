package com.sptd.eyun.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class ShebeiHorizontalScrollView extends HorizontalScrollView {


	private View view;
	private ImageView leftImage;
	private ImageView rightImage;
	private int windowWitdh = 0;
	private Activity mContext;

	public void setSomeParam(View view, ImageView leftImage,
			ImageView rightImage, Activity context) {
		this.mContext = context;
		this.view = view;
		this.leftImage = leftImage;
		this.rightImage = rightImage;
		DisplayMetrics dm = new DisplayMetrics();
		this.mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
		windowWitdh = dm.widthPixels;
		
		showAndHideArrow();
	}


	public ShebeiHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}	
    
	public ShebeiHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	// ��ʾ�������������ߵļ�ͷ
	public void showAndHideArrow() {
		Log.v("fangfa", "zhixing-show");
		if (!mContext.isFinishing() && view != null) {
			this.measure(0, 0);
			Log.v("showhideparam", windowWitdh+"-"+this.getMeasuredWidth()+"-"+this.getLeft()+"-"+this.getRight());
			
			if (windowWitdh >= this.getMeasuredWidth()) {
				
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.INVISIBLE);
			} else {
				if (this.getLeft() == 0) {
					leftImage.setVisibility(View.INVISIBLE);
					rightImage.setVisibility(View.VISIBLE);
				} else if (this.getRight() == this.getMeasuredWidth()
						- windowWitdh) {
					leftImage.setVisibility(View.VISIBLE);
					rightImage.setVisibility(View.INVISIBLE);
				} else {
					leftImage.setVisibility(View.VISIBLE);
					rightImage.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		Log.v("fangfa", "zhixing-scroll");
		super.onScrollChanged(l, t, oldl, oldt);
//		Toast.makeText(mContext, "winWitdh-"+windowWitdh+"viewWidth-"+view.getWidth(),
//				3000).show();
		if (!mContext.isFinishing() && view != null && rightImage != null
				&& leftImage != null) {
			if (view.getWidth() <= windowWitdh) {
				leftImage.setVisibility(View.INVISIBLE);
				rightImage.setVisibility(View.INVISIBLE);
			} else {
				Log.v("scroll", view.getWidth()+"-"+l+"=="+windowWitdh);
				if (l == 0) {
					leftImage.setVisibility(View.INVISIBLE);
					rightImage.setVisibility(View.VISIBLE);
				} else if (view.getWidth() - l == windowWitdh) {
					leftImage.setVisibility(View.VISIBLE);
					rightImage.setVisibility(View.INVISIBLE);
				} else {
					Log.v("leftimage", "left-show");
					leftImage.setVisibility(View.VISIBLE);
					rightImage.setVisibility(View.VISIBLE);
				}
			}
		}
	}
}
