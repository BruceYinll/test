package com.sptd.eyun.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class OPItemImageButton extends ImageView{

	public OPItemImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public OPItemImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public OPItemImageButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int width = getMeasuredWidth();
		int widthAndHeightSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, widthAndHeightSpec);
		
	}

}
