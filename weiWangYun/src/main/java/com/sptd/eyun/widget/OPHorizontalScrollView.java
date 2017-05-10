package com.sptd.eyun.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class OPHorizontalScrollView extends HorizontalScrollView {

	private OPScrollChangedListener changedListener;

	public OPScrollChangedListener getChangedListener() {
		return changedListener;
	}

	public void setChangedListener(OPScrollChangedListener changedListener) {
		this.changedListener = changedListener;
	}

	public OPHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public OPHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public OPHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if(changedListener!=null){
			changedListener.onScrollChanged(l, t, oldl, oldt);
		}
	}

	public interface OPScrollChangedListener {
		void onScrollChanged(int l, int t, int oldl, int oldt);
	}
}

