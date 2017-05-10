package com.sptd.eyun.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baidu.platform.comapi.map.r;
import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseFragment;
import com.sptd.eyun.R;
import com.sptd.eyun.adapter.ZhanVPagerAdapter;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.obj.UserStation;
import com.sptd.eyun.widget.SyncHorizontalScrollView;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.resource.PreferencesUtil;











import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 站
 * @author ldy
 *
 */
public class ZhanFragment extends BaseFragment{

			
	public static final String ARGUMENTS_NAME = "arg";
	private RelativeLayout rl_nav;
	private SyncHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private ViewPager mViewPager;
	private int indicatorWidth;
	
	
	public static String[] tabTitle = { "福田区微电站", "南山区美女深南大道微电站", "罗湖区微电站", "宝安区微电站", "龙岗区微电站" ,"FFFF"};	//����
    //我的站列表
	private List<UserStation> userStationList=new ArrayList<UserStation>();
	
	
	
	private LayoutInflater mInflater;
	private int currentIndicatorLeft = 0;

	
	private int currentSelect=-1;
	
	private List<View> list=new ArrayList<View>();
	
	
	private ZhanVPagerAdapter zvpAdapter;


//	private View view;
	private ImageView iv_position;//图片位置
	private TextView viewpager_view_text_location;//文本位置
	 
	
	
	
	/** 可扩展listview控件 */
	private ExpandableListView exlistview;
	/** 父数据源 */
	private List<String> parents;
	private final int[] resId1 = { R.drawable.home_left_2, R.drawable.home_left_3,
			R.drawable.home_left_4 };
	private final int[] resId2 = { R.drawable.home_left_2_1, R.drawable.home_left_3_1,
			R.drawable.home_left_4_1 };
	/** 子数据源 */
	private List<List<String>> childs;
	
	public ZhanFragment() {
		super(R.layout.fragment_zhan, NoTitle);
		// TODO Auto-generated constructor stub
	}
/*	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	    view=inflater.inflate(R.layout.fragment_zhan, container, false);
		findViewById();
		initView();
		setListener();
		//初始checked
	  ((RadioButton)rg_nav_content.getChildAt(0)).performClick();
		return view;
	}
*/	
	private void findViewById() {
		
		rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		
		mHsv = (SyncHorizontalScrollView) findViewById(R.id.mHsv);
		
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
	}
	private void initView() {
		
//		DisplayMetrics dm = new DisplayMetrics();
//		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		
//		indicatorWidth = dm.widthPixels / 4;
			
		mHsv.setSomeParam(rl_nav,getActivity());
		
		//��ȡ���������
		mInflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

		//��һ�ַ�ʽ��ȡ
//		LayoutInflater mInflater = LayoutInflater.from(this);  
		
		initNavigationHSV();
		initViewPagerView();
	
		
		zvpAdapter=new ZhanVPagerAdapter(getActivity(), list);
		mViewPager.setAdapter(zvpAdapter);
		
	}
	private void initNavigationHSV() {
		
		rg_nav_content.removeAllViews();
		
		for(int i=0;i<userStationList.size();i++){
			
			RadioButton rb = (RadioButton) mInflater.inflate(R.layout.nav_radiogroup_item, null);
			rb.setId(i);
			rb.setText(userStationList.get(i).getStation().getName());	
			
			RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(
			        LayoutParams.MATCH_PARENT,
			        30*2);
			int margin = (int)(8*2);
			params_rb.setMargins(margin, margin, margin, margin);
			rg_nav_content.addView(rb,params_rb);
		}
		
	}
	private void initViewPagerView(){
		list.clear();
		for(int i=0;i<userStationList.size();i++){
			View view=mInflater.inflate(R.layout.viewpager_view, null);			
			onCreateExpandlistView(view,i);
		    list.add(view);
		}
	}
	private void setListener() {
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				if(rg_nav_content!=null && rg_nav_content.getChildCount()>position){
					((RadioButton)rg_nav_content.getChildAt(position)).performClick();
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		rg_nav_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				if(rg_nav_content.getChildAt(checkedId)!=null){
							
					TranslateAnimation animation = new TranslateAnimation(
							currentIndicatorLeft ,
							((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(), 0f, 0f);
					animation.setInterpolator(new LinearInterpolator());
					animation.setDuration(100);
					animation.setFillAfter(true);
					
					
					mViewPager.setCurrentItem(checkedId);	//ViewPager ����һ�� �л�
					
					//��¼��ǰ �±�ľ������� ����
					currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();
					
					mHsv.smoothScrollTo(
							(checkedId > 0 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(1)).getLeft(), 0);
				

				    currentSelect=checkedId;
				
				}
			}
		});
	}

	
	private void onCreateExpandlistView(View view,final int k){
		 Station station=userStationList.get(k).getStation();
		
		iv_position=(ImageView) view.findViewById(R.id.iv_position);
		displayImage(iv_position, InterfaceFinals.URL_HEAD+station.getPicture());
		viewpager_view_text_location=(TextView) view.findViewById(R.id.viewpager_view_text_location);
		viewpager_view_text_location.setText(station.getPosition());
		
		//组内容
		parents = new ArrayList<String>();
		parents.add("属性");
		parents.add("数据");
		parents.add("设备");
		
		//子内容
		childs = new ArrayList<List<String>>();
		for (int i = 0; i < parents.size(); i++) {
			 List<String> c = new ArrayList<String>();
		  switch (i) {
		   
		   case 0:
			  
				c.add("站类型:  "+station.getKind());
				c.add("可再生能源容量:  "+station.getRenewableCapacity()+"可再生能源容量可再生能源容量可再生能源容量可再生能源容量可再生能源容量");
				c.add("传统能源容量:  "+station.getTraditionalCapacity());
				c.add("储能容量:  "+station.getStorageCapacity());
				c.add("负荷容量:  "+station.getLoadingCapacity());
				c.add("并网日期:  "+station.getGridDate());
				c.add("详细介绍:  "+station.getDescription());	
				
			  break;

		   case 1:
				c.add("总发电量:  "+station.getCapacity());
				c.add("总发电功率:  "+station.getPower());
				c.add("风速:  "+station.getWind());
				c.add("温度:  "+station.getTemperature());
				c.add("天气信息:  "+station.getWeather());
				
			  break;
		   
		   case 2:
			    
			   break;
			   
		 }
		  childs.add(c); 	
			
		}

		exlistview = (ExpandableListView) view.findViewById(R.id.exlistview);

		exlistview.setAdapter(new MyAdapter());
		/** 设置展开的监听事件 */
		exlistview.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				/** 循环遍历，把不是当前选中的其他组关闭 */
			/*	
				for (int i = 0; i < parents.size(); i++) {
					if (i != groupPosition) {
						exlistview.collapseGroup(i);
					}
				}
			*/
//				exlistview.expandGroup(groupPosition);
/*
				if(groupPosition==2){
					 Station station=userStationList.get(k).getStation();
					  Long stationtid= station.getTid();

				}
*/				
				
			}
		});
		/** 默认选中第一组 */
//		exlistview.expandGroup(0);
		/** 去除每组对应的显示效果，默认是有箭头的，这里我们需要自己的效果，就把默认的去除掉 */
		exlistview.setGroupIndicator(null);

		/** 设置子item的单击事件 */
		exlistview.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
		       if(groupPosition==2){		
				Toast.makeText(getActivity(),
						childs.get(groupPosition).get(childPosition), 0).show();
		       }
				return false;
			}
		});
	}
	class MyAdapter extends BaseExpandableListAdapter {

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childs.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
/*			
			TextView tv = new TextView(getActivity());
			tv.setHeight(50);
	        tv.setGravity(Gravity.CENTER_VERTICAL);
	        tv.setPadding(50, 0, 0, 0);
			tv.setText(childs.get(groupPosition).get(childPosition));
			tv.setBackgroundColor(Color.parseColor("#2d3036"));
			return tv;
*/
			View view=mInflater.inflate(R.layout.child_item_expandlistview, null);
			TextView tv=(TextView) view.findViewById(R.id.textView1);
			tv.setText(childs.get(groupPosition).get(childPosition));
			return view;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return childs.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return parents.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return parents.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.parent_item_expandlistview, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.textView1);
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.imageview1);
			tv.setText(parents.get(groupPosition));
			/** 判断当前是展开还是闭合，为了显示不同的箭头 */
			if (isExpanded) {
				iv.setImageResource(R.drawable.listview_hide);
			} else {
				iv.setImageResource(R.drawable.listview_show);
			}
			
			ImageView iv2 = (ImageView) convertView
					.findViewById(R.id.imageview2);
			ImageView iv3 = (ImageView) convertView
					.findViewById(R.id.imageview3);
			iv2.setImageResource(resId1[groupPosition]);
			iv3.setImageResource(resId2[groupPosition]);
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			/** 注意这里必须返回true 不然点击不到子item */
			return true;
		}

	}
	
	
	private void getMyzhan(){
		UserObj userObj=(UserObj) PreferencesUtil.getPreferences(getActivity(),
				PreferenceFinals.KEY_USER);
		Type t = new TypeToken<BaseModel<List<UserStation>>>() {
		}.getType();
		BaseAsyncTask<List<UserStation>> task = new BaseAsyncTask<List<UserStation>>(this,t,
				InterfaceFinals.INF_FINDUSERSTATIONLIST,true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token",userObj.getToken() );
		params.put("status", ""+2);
		task.execute(params);
	}
/*	
	private void getDeviceTypeListByStationid(Long id){
		Type t = new TypeToken<BaseModel<List<UserStation>>>() {
		}.getType();
		BaseAsyncTask<List<UserStation>> task = new BaseAsyncTask<List<UserStation>>(this,t,
				InterfaceFinals.INF_FINDUSERSTATIONLIST,true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token",userObj.getToken() );
		params.put("status", ""+2);
		task.execute(params);
	}
*/
	@Override
	protected void getData() {
		getMyzhan();
		
	}

	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_FINDUSERSTATIONLIST:
			  userStationList=(List<UserStation>) res.getObject();
			  findViewById();
			  initView();
			  setListener();
			  //初始checked
			  ((RadioButton)rg_nav_content.getChildAt(0)).performClick(); 
			break;
/*
		case :
			break;
*/
		}
		
	}
}
