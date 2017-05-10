package com.sptd.util;

import android.R.integer;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewUtil {

	
	public static  void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
//            listItem.measure(desiredWidth, 0);  
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
	
	public static void tiaoGaoDu(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        int hangshu=listAdapter.getCount();
        int totalHeight=0;
        for(int i=0;i<hangshu;i++){
        	View view=listAdapter.getView(i, null, listView);
        	view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        	totalHeight=totalHeight+view.getMeasuredHeight();
 //           totalHeight=totalHeight+view.getHeight();

        }
        int totalDivider=listView.getDividerHeight()*(hangshu-1);
        LayoutParams params=(LayoutParams) listView.getLayoutParams();
        params.height=totalHeight+totalDivider;
        listView.setLayoutParams(params);
	}
}
