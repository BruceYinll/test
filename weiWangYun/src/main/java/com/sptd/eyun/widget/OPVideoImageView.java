package com.sptd.eyun.widget;

import com.sptd.eyun.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class OPVideoImageView extends ImageView {

	int withSize = 0;

	public OPVideoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public OPVideoImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public OPVideoImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int heightSpec = 0;
		if (withSize == 0) {
			withSize = getMeasuredWidth();
			int size = (int) getResources().getDimension(R.dimen.dim10);
			heightSpec = MeasureSpec.makeMeasureSpec((withSize - size * 2) / 3, MeasureSpec.EXACTLY);
			super.onMeasure(widthMeasureSpec, heightSpec);
		} else {
			int height = getMeasuredHeight();
			setMeasuredDimension(height, height);
		}

	}
}
