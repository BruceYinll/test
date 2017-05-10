package com.sptd.eyun.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogRecord;

import android.R.integer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseFragment;
import com.sptd.eyun.R;
import com.sptd.eyun.adapter.ZhanVPagerAdapter;
import com.sptd.eyun.adapter.ZhanVPagerAdapterTwo;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.obj.UserStation;
import com.sptd.eyun.widget.SyncHorizontalScrollView;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.resource.PreferencesUtil;
/**
 *站Fragment2
 * @author ldy
 *
 */
public class ZhanFragmentTwo extends BaseFragment implements OnClickListener{

	private RelativeLayout rl_nav;
	private SyncHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ViewPager mViewPager;
	
//	private LinearLayout ll_shuxing,ll_shuju,ll_shebei;
//	private boolean shuxing_isShow=false,shuju_isShow=false,shebei_isShow=false;
	
	
	private UserObj userObj;
	
	//我的站列表
	private List<UserStation> userStationList=new ArrayList<UserStation>();
	
	private LayoutInflater mInflater;
	
	//viewpager
	private List<String> position_pictureList=new ArrayList<String>();
	private List<String> position_textList=new ArrayList<String>();
	private List<List<String>> shuxingList=new ArrayList<List<String>>();
	private HashMap<Integer, List<DeviceType>> shebeiHashMap=new HashMap<Integer, List<DeviceType>>();
	//数据列表
	private List<List<String>> shujuList=new ArrayList<List<String>>();
	private ZhanVPagerAdapterTwo zvpAdapterTwo;
	
	
	
    private int currentIndicatorLeft = 0;	
	private int currentSelect=-1;
	
	
	//5s刷新一次
	private FiveHandler fiveHandler=new FiveHandler();
	private static final int UPDATE_JIANGE=5*1000;
	private boolean isStartUpdate=false;
	
	public ZhanFragmentTwo(){
		super(R.layout.fragment_zhan, NoTitle);
	}

	@Override
	protected void getData() {
		userObj=(UserObj) PreferencesUtil.getPreferences(getActivity(),
				PreferenceFinals.KEY_USER);
		Log.v("stationids", userObj.getStationids());
		getMyzhan();
	}

	@Override
	protected void findView() {
        rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		
		mHsv = (SyncHorizontalScrollView) findViewById(R.id.mHsv);
		
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);

		
/*		
		ll_shuxing=(LinearLayout) findViewById(R.id.ll_shuxing);
		ll_shuxing.setOnClickListener(this);
		ll_shuju=(LinearLayout) findViewById(R.id.ll_shuju);
		ll_shuju.setOnClickListener(this);
		ll_shebei=(LinearLayout) findViewById(R.id.ll_shebei);
		ll_shebei.setOnClickListener(this);
*/		
	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_FINDUSERSTATIONLIST:
			if (isStartUpdate) {   //5s刷新展示数据
				userStationList=(List<UserStation>) res.getObject();
				shujuList.clear();
				for(int i=0;i<userStationList.size();i++){
					Station station=userStationList.get(i).getStation();									
					List<String> d=new ArrayList<String>();
					d.add("总发电量:  "+station.getCapacity());
					d.add("总发电功率:  "+station.getPower());
					d.add("风速:  "+station.getWind());
					d.add("温度:  "+station.getTemperature());
					d.add("天气信息:  "+station.getWeather());
					shujuList.add(d);
				}
				zvpAdapterTwo.notifyDataSetChanged();
				
			}else {      //第一次进入展示数据
				 userStationList=(List<UserStation>) res.getObject();
				 initView();
				 
				 zvpAdapterTwo.notifyDataSetChanged();
				 
				 setListener();
				 //初始checked
				 ((RadioButton)rg_nav_content.getChildAt(0)).performClick();
				 fiveHandler.postDelayed(runnable, UPDATE_JIANGE	);
				 
			}
			  
			break;
			
		case InterfaceFinals.INF_FINDDEVICETYPELIST:
			List<DeviceType> deviceTypeList=(List<DeviceType>) res.getObject();
			if(deviceTypeList.size()>0){
			  for(int i=0;i<userStationList.size();i++){
				Station station=userStationList.get(i).getStation();
				
				if(deviceTypeList.get(0).getStationId()==station.getTid())
					 shebeiHashMap.put(i, 
					    		deviceTypeList);
			  }
		   
			  zvpAdapterTwo.notifyDataSetChanged();
			}	
			break;
/*
		case :
			break;
*/
		}
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
/*		
		case R.id.ll_shuxing:
			if(shuxing_isShow){
				ll_shuxing.setSelected(false);
				shuxing_isShow=false;
			}else {
				ll_shuxing.setSelected(true);
	            shuxing_isShow=true;
			}
			break;

		case R.id.ll_shuju:
			if (shuju_isShow) {
				ll_shuju.setSelected(false);
				shuju_isShow=false;
			}else {
				ll_shuju.setSelected(true);
				shuju_isShow=true;
			}
			break;
	    case R.id.ll_shebei:
	    	if (shebei_isShow) {
				ll_shebei.setSelected(false);
				shebei_isShow=false;
			}else {
				ll_shebei.setSelected(true);
				shebei_isShow=true;
			}
	    	break;
*/	    	
		}
		
	}
	
	//获得我的站
	private void getMyzhan(){
//		UserObj userObj=(UserObj) PreferencesUtil.getPreferences(getActivity(),
//				PreferenceFinals.KEY_USER);
		Type t = new TypeToken<BaseModel<List<UserStation>>>() {
		}.getType();
		BaseAsyncTask<List<UserStation>> task = new BaseAsyncTask<List<UserStation>>(this,t,
				InterfaceFinals.INF_FINDUSERSTATIONLIST,true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token",userObj.getToken() );
		params.put("status", ""+2);
		task.execute(params);
	}
	
	//根据站的tid获得站中的设备类列表
	private void getDeviceTypeListByStationId(Long tid){
		Type t = new TypeToken<BaseModel<List<DeviceType>>>() {
		}.getType();
		BaseAsyncTask<List<DeviceType>> task = new BaseAsyncTask<List<DeviceType>>(this,t,
				InterfaceFinals.INF_FINDDEVICETYPELIST,true);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("jsonFilter","{search_EQ_stationId:"+tid+"}");
		task.execute(params);
	}
	
	
	
	private void initView() {		
		mHsv.setSomeParam(rl_nav,getActivity());
		mInflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);		
		
		zvpAdapterTwo=new ZhanVPagerAdapterTwo(getActivity(),userStationList,
				position_pictureList,position_textList,shuxingList,shujuList,shebeiHashMap);
		mViewPager.setAdapter(zvpAdapterTwo);
		
		initNavigationHSV();
		initViewPagerView();
	
/*		
		zvpAdapterTwo=new ZhanVPagerAdapterTwo(getActivity(),
				position_pictureList,position_textList,shuxingList,shujuList);
		mViewPager.setAdapter(zvpAdapterTwo);
*/		
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
		position_pictureList.clear();
		position_textList.clear();
		shuxingList.clear();
		shujuList.clear();
		for(int i=0;i<userStationList.size();i++){
			Station station=userStationList.get(i).getStation();
			position_pictureList.add(station.getPicture());
			position_textList.add(station.getPosition());
			
			List<String> c = new ArrayList<String>();
			c.add("站类型:  "+station.getKind());
			c.add("可再生能源容量:  "+station.getRenewableCapacity()+"可再生能源容量可再生能源容量可再生能源容量可再生能源容量可再生能源容量");
			c.add("传统能源容量:  "+station.getTraditionalCapacity());
			c.add("储能容量:  "+station.getStorageCapacity());
			c.add("负荷容量:  "+station.getLoadingCapacity());
			c.add("并网日期:  "+station.getGridDate());
			c.add("详细介绍:  "+station.getDescription());	
			shuxingList.add(c);
			
			List<String> d=new ArrayList<String>();
			d.add("总发电量:  "+station.getCapacity());
			d.add("总发电功率:  "+station.getPower());
			d.add("风速:  "+station.getWind());
			d.add("温度:  "+station.getTemperature());
			d.add("天气信息:  "+station.getWeather());
			shujuList.add(d);
			
			getDeviceTypeListByStationId(station.getTid());
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

    class FiveHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {			
			
		}

		
    	
    }
    
    Runnable runnable=new Runnable() {
		
		@Override
		public void run() {
			isStartUpdate=true;
			getMyzhan();
			fiveHandler.postDelayed(this, UPDATE_JIANGE	);
			
		}
	};

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	    fiveHandler.removeCallbacks(runnable);
	    isStartUpdate=false;
	}

   
}
