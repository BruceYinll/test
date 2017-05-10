package com.sptd.eyun.ui.fragment;

import com.sptd.eyun.R;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CommonUIFragment extends Fragment {
	private String param;
	
	public CommonUIFragment(String param) {
		super();
		this.param = param;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_selection_common, container, false);
		
		TextView tv_tabName = (TextView) rootView.findViewById(R.id.tv_tabName);
		
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
