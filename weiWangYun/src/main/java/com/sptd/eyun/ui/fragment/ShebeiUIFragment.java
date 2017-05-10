package com.sptd.eyun.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sptd.eyun.R;

public class ShebeiUIFragment extends Fragment {
	private String param;
	
	public ShebeiUIFragment(String param) {
		super();
		this.param = param;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.viewpager_view_shebei, container, false);
		
		TextView tv_tabName = (TextView) rootView.findViewById(R.id.viewpager_view_shebei_shebeiname);
		
		Bundle bundle = getArguments();
		
//		tv_tabName.setText(bundle.getString(getActivity().ARGUMENTS_NAME, ""));
		
		tv_tabName.setText(param);
		return rootView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
}
