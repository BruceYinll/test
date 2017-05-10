package com.sptd.eyun.ui.fragment;

import android.os.AsyncTask;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseFragment;
import com.sptd.eyun.EyunApplication;
import com.sptd.eyun.R;
import com.sptd.eyun.adapter.ZhanVPagerAdapterThree;
import com.sptd.eyun.dialog.ApplyDialog;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.Device;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.Station;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.obj.UserStation;
import com.sptd.eyun.ui.HomeActivity;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.eyun.widget.SyncHorizontalScrollView;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.resource.PreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 站Fragment3
 *
 * @author ldy
 */
public class ZhanFragmentThree extends BaseFragment implements OnClickListener {

    private RelativeLayout rl_nav;
    private SyncHorizontalScrollView mHsv;
    private RadioGroup rg_nav_content;
    private ViewPager mViewPager;

//	private LinearLayout ll_shuxing,ll_shuju,ll_shebei;
//	private boolean shuxing_isShow=false,shuju_isShow=false,shebei_isShow=false;


    private UserObj userObj;

    //我的站列表
    private List<UserStation> userStationList = new ArrayList<UserStation>();

    private LayoutInflater mInflater;

    //viewpager
    private List<String> position_pictureList = new ArrayList<String>();
    private List<String> position_textList = new ArrayList<String>();
    private List<List<String>> shuxingList = new ArrayList<List<String>>();
    private HashMap<Integer, List<DeviceType>> shebeiHashMap = new HashMap<Integer, List<DeviceType>>();
    //数据列表
    private List<List<String>> shujuList = new ArrayList<List<String>>();
    //	private ZhanVPagerAdapterTwo zvpAdapterTwo;
    private ZhanVPagerAdapterThree zvpAdapterThree;


    private int currentIndicatorLeft = 0;
    private int currentSelect = -1;


    //5s刷新一次
    private FiveHandler fiveHandler = new FiveHandler();
    private static final int UPDATE_JIANGE = 5 * 1000;
    private boolean isStartUpdate = false;


    //当前用户站,设备类,设备
    private UserStation currentUserStation;
    private DeviceType currentDeviceType;
    private Device currentDevice;


    //AsyncTask任务队列
    List<AsyncTask> taskList = new ArrayList<AsyncTask>();

    //没有已审核的站,退出应用
    private ApplyDialog dialog;
    private TextView tv_content;
    private TextView tuichu;


    private int lastZhanCount = 0;
    private int currentZhanCount = 0;

    //是否是第一个task
    private boolean isFistTask = true;

    public ZhanFragmentThree() {
        super(R.layout.fragment_zhan, NoTitle);
    }

    @Override
    protected void getData() {
        userObj = (UserObj) PreferencesUtil.getPreferences(getActivity(),
                PreferenceFinals.KEY_USER);

        currentUserStation = ((HomeActivity) getActivity()).getCurrentUserStation();


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
//		case InterfaceFinals.INF_FINDUSERSTATIONLIST:
            case InterfaceFinals.INF_FINDSTATIONLIST_FILTER_NULL:
                userStationList = (List<UserStation>) res.getObject();
                currentZhanCount = userStationList.size();
                //没有已审核的关联站,提示他到更多->个人信息->关联站,申请关联站
                if (currentZhanCount == 0) {
                    fiveHandler.removeCallbacks(runnable);
                    for (AsyncTask task : taskList) {
                        if (task.isCancelled()) {

                        } else {
                            task.cancel(true);
                        }
                    }

                    taskList.clear();
                    dialog = new ApplyDialog(getActivity(), R.style.MyDialog);
                    dialog.setCancelable(false);
                    dialog.show();
                    tv_content = dialog.getTv_content();
                    tv_content.setText("没有已审核的关联站,可以到更多->个人信息->关联站,申请关联站");
                    tuichu = dialog.getTuichu();
                    tuichu.setText("我知道了");
                    if (tuichu == null) {
                        Log.v("tuichu", "null");
                    }
                    tuichu.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //退出应用
//						RegisterActivity.this.finish();

                            dialog.dismiss();
                            initView();

//						EyunApplication.getInstance().removeAllActivity();

//						 android.os.Process.killProcess(android.os.Process.myPid());
//						 RegisterActivity.this.finish();
//						 System.exit(0); 
//						 ActivityManager activityMgr= (ActivityManager) RegisterActivity.this.getSystemService(ACTIVITY_SERVICE);

//						 activityMgr.restartPackage(getPackageName());


                        }
                    });
                } else {
                    if (currentZhanCount == lastZhanCount) { //5s刷新展示数据

                        shujuList.clear();
                        for (int i = 0; i < userStationList.size(); i++) {
                            Station station = userStationList.get(i).getStation();
                            List<String> d = new ArrayList<String>();
                            d.add("总发电量:  " + (station.getCapacity() == null ? "" : station.getCapacity()));
                            d.add("总发电功率:  " + (station.getPower() == null ? "" : station.getPower()));
                            d.add("风速:  " + (station.getWind() == null ? "" : station.getWind()));
                            d.add("温度:  " + (station.getTemperature() == null ? "" : station.getTemperature()));
                            d.add("天气信息:  " + (station.getWeather() == null ? "" : station.getWeather()));
                            shujuList.add(d);
                        }
//					zvpAdapterTwo.notifyDataSetChanged();
                        zvpAdapterThree.notifyDataSetChanged();
                    } else { //第一次进入展示数据
                        lastZhanCount = currentZhanCount;
                        isStartUpdate = false;
                        initView();

//				 zvpAdapterTwo.notifyDataSetChanged();
                        zvpAdapterThree.notifyDataSetChanged();
                        setListener();
                        if (currentUserStation == null) {

                            //TODO 初始checked,似乎不会触发setOnCheckedChangeListener事件,有待验证
                            ((RadioButton) rg_nav_content.getChildAt(0)).performClick();
                            mViewPager.setCurrentItem(0);
                            currentUserStation = userStationList.get(0);
                            ((HomeActivity) getActivity()).setCurrentUserStation(currentUserStation);

                        } else {
                            for (int i = 0; i < userStationList.size(); i++) {
                                if (currentUserStation.getStationId().equals(userStationList.get(i).getStationId())) {

                                    //TODO 初始checked,似乎不会触发setOnCheckedChangeListener事件,有待验证
                                    ((RadioButton) rg_nav_content.getChildAt(i)).performClick();
                                    mViewPager.setCurrentItem(i);
                                    break;
                                }
                                //当前站被删除,默认第一个站
                                if (i == userStationList.size() - 1) {
                                    //TODO 初始checked,似乎不会触发setOnCheckedChangeListener事件,有待验证
                                    ((RadioButton) rg_nav_content.getChildAt(0)).performClick();
                                    mViewPager.setCurrentItem(0);
                                    currentUserStation = userStationList.get(0);
                                    ((HomeActivity) getActivity()).setCurrentUserStation(currentUserStation);

                                }
                            }
                        }

                        fiveHandler.postDelayed(runnable, UPDATE_JIANGE);

                    }
                }


                break;

            case InterfaceFinals.INF_FINDDEVICETYPELIST:
                List<DeviceType> deviceTypeList = (List<DeviceType>) res.getObject();
                if (deviceTypeList.size() > 0) {
                    for (int i = 0; i < userStationList.size(); i++) {
                        Station station = userStationList.get(i).getStation();

                        if (deviceTypeList.get(0).getStationId() == station.getTid())
                            shebeiHashMap.put(i,
                                    deviceTypeList);
                    }

//			  zvpAdapterTwo.notifyDataSetChanged();
                    zvpAdapterThree.notifyDataSetChanged();
                }
                break;
/*
		case :
			break;
*/
        }


    }

    @Override
    public void onFail(BaseModel res) {
        showToast(res.getMessage());
        String failInf = res.getMessage();
        if ("未登陆".equals(failInf)) {
            EyunApplication.getInstance().removeAllActivity();
            startActivity(LoginActivity.class);
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


    /*  findStationList后台时江明过滤 没有设备的设备类		   */
    //获得我的站
    private void getMyzhan() {
//			UserObj userObj=(UserObj) PreferencesUtil.getPreferences(getActivity(),
//					PreferenceFinals.KEY_USER);
        Type t = new TypeToken<BaseModel<List<UserStation>>>() {
        }.getType();
        BaseAsyncTask<List<UserStation>> task;
        //true为有进度条,false为无进度条
        if (!isFistTask) {
            //开始5s刷新,无进度条
            task = new BaseAsyncTask<List<UserStation>>(this, t,
                    InterfaceFinals.INF_FINDSTATIONLIST_FILTER_NULL, false);
        } else {
            //第一次获取数据,有进度条
            task = new BaseAsyncTask<List<UserStation>>(this, t,
                    InterfaceFinals.INF_FINDSTATIONLIST_FILTER_NULL, true);
            isFistTask = false;
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", userObj.getToken());
        params.put("status", "" + 2);
        task.execute(params);

        taskList.add(task);
    }
		/*  findStationList后台时江明过滤 没有设备的设备类		   */		
		
	
	/*
	 * findStationList前端自己过滤 没有设备的设备类			
	
	//获得我的站
	private void getMyzhan(){
//		UserObj userObj=(UserObj) PreferencesUtil.getPreferences(getActivity(),
//				PreferenceFinals.KEY_USER);
		Type t = new TypeToken<BaseModel<List<UserStation>>>() {
		}.getType();
		BaseAsyncTask<List<UserStation>> task;
		//true为有进度条,false为无进度条
		if(isStartUpdate){
			//开始5s刷新,无进度条
			task = new BaseAsyncTask<List<UserStation>>(this,t,
					InterfaceFinals.INF_FINDUSERSTATIONLIST,false);
		}else {
			//第一次获取数据,有进度条
			task = new BaseAsyncTask<List<UserStation>>(this,t,
					InterfaceFinals.INF_FINDUSERSTATIONLIST,true);
		}
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token",userObj.getToken() );
		params.put("status", ""+2);
		task.execute(params);
	}
	*/


    //根据站的tid获得站中的设备类列表
    private void getDeviceTypeListByStationId(Long tid) {
        Type t = new TypeToken<BaseModel<List<DeviceType>>>() {
        }.getType();
        BaseAsyncTask<List<DeviceType>> task = new BaseAsyncTask<List<DeviceType>>(this, t,
                InterfaceFinals.INF_FINDDEVICETYPELIST, true);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("jsonFilter", "{search_EQ_stationId:" + tid + "}");
        task.execute(params);
    }


    private void initView() {
        mHsv.setSomeParam(rl_nav, getActivity());
        mInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);


        initNavigationHSV();
        initViewPagerView();
	
/*		zvpAdapterTwo=new ZhanVPagerAdapterTwo(getActivity(),userStationList,
				position_pictureList,position_textList,shuxingList,shujuList,shebeiHashMap);
		mViewPager.setAdapter(zvpAdapterTwo);*/

        zvpAdapterThree = new ZhanVPagerAdapterThree(getActivity(), userStationList,
                position_pictureList, position_textList, shuxingList, shujuList, shebeiHashMap);
        mViewPager.setAdapter(zvpAdapterThree);
/*		
		zvpAdapterTwo=new ZhanVPagerAdapterTwo(getActivity(),
				position_pictureList,position_textList,shuxingList,shujuList);
		mViewPager.setAdapter(zvpAdapterTwo);
*/
    }

    private void initNavigationHSV() {

        rg_nav_content.removeAllViews();

        for (int i = 0; i < userStationList.size(); i++) {

            RadioButton rb = (RadioButton) mInflater.inflate(R.layout.nav_radiogroup_item, null);
            rb.setId(i);
            rb.setText(userStationList.get(i).getStation().getName());

//            float scale = getContext().getResources().getDisplayMetrics().density;


            float scale = getActivity().getResources().getDisplayMetrics().density;
            RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(
                    LayoutParams.WRAP_CONTENT, //之前是LayoutParams.MATCH_PARENT
                    (int) (30 * scale));
            int margin = (int) (8 * scale);
            params_rb.setMargins(margin, margin, margin, margin);
            rg_nav_content.addView(rb, params_rb);
        }

    }

    private void initViewPagerView() {
        position_pictureList.clear();
        position_textList.clear();
        shuxingList.clear();
        shujuList.clear();
        for (int i = 0; i < userStationList.size(); i++) {
            Station station = userStationList.get(i).getStation();
            position_pictureList.add(station.getPicture());
            position_textList.add(station.getPosition());

            List<String> c = new ArrayList<String>();
            c.add("站类型:  " + station.getKind());
            c.add("可再生能源容量:  " + station.getRenewableCapacity());
            c.add("传统能源容量:  " + station.getTraditionalCapacity());
            c.add("储能容量:  " + station.getStorageCapacity());
            c.add("负荷容量:  " + station.getLoadingCapacity());
            c.add("并网日期:  " + station.getGridDate());
            c.add("详细介绍:  " + station.getDescription());
            shuxingList.add(c);

            List<String> d = new ArrayList<String>();
            d.add("总发电量:  " + (station.getCapacity() == null ? "" : station.getCapacity()));
            d.add("总发电功率:  " + (station.getPower() == null ? "" : station.getPower()));
            d.add("风速:  " + (station.getWind() == null ? "" : station.getWind()));
            d.add("温度:  " + (station.getTemperature() == null ? "" : station.getTemperature()));
            d.add("天气信息:  " + (station.getWeather() == null ? "" : station.getWeather()));
            shujuList.add(d);
					
/*  findStationList后台时江明过滤 没有设备的设备类		   */
            List<DeviceType> deviceTypeList = (List<DeviceType>) station.getDeviceTypeList();
            shebeiHashMap.put(i, deviceTypeList);
/*  findStationList后台时江明过滤 没有设备的设备类		   */	
			
/*
 * findStationList前端自己过滤 没有设备的设备类			
			List<DeviceType> oldDeviceTypeList=(List<DeviceType>) station.getDeviceTypeList();			
			List<DeviceType> deviceTypeList=new ArrayList<DeviceType>();
			for(int j=0;j<oldDeviceTypeList.size();j++){
				if(oldDeviceTypeList.get(j).getDeviceList()!=null
						&& oldDeviceTypeList.get(j).getDeviceList().size()>0)
					deviceTypeList.add(oldDeviceTypeList.get(j));
			}
			shebeiHashMap.put(i, deviceTypeList);
*/			
			/*if(deviceTypeList.size()>0){
			  for(int j=0;j<userStationList.size();j++){				
				if(deviceTypeList.get(0).getStationId()==station.getTid())
					 shebeiHashMap.put(j, 
					    		deviceTypeList);
			  }
			}	*/
//			getDeviceTypeListByStationId(station.getTid());
        }


    }

    private void setListener() {

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                if (rg_nav_content != null && rg_nav_content.getChildCount() > position) {
                    ((RadioButton) rg_nav_content.getChildAt(position)).performClick();
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

                if (rg_nav_content.getChildAt(checkedId) != null) {

                    TranslateAnimation animation = new TranslateAnimation(
                            currentIndicatorLeft,
                            ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(), 0f, 0f);
                    animation.setInterpolator(new LinearInterpolator());
                    animation.setDuration(100);
                    animation.setFillAfter(true);


                    mViewPager.setCurrentItem(checkedId);    //ViewPager ����һ�� �л�

                    //��¼��ǰ �±�ľ������� ����
                    currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();

                    mHsv.smoothScrollTo(
                            (checkedId > 0 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(1)).getLeft(), 0);


                    currentSelect = checkedId;

                    currentUserStation = userStationList.get(checkedId);
                    ((HomeActivity) getActivity()).setCurrentUserStation(currentUserStation);

                }
            }
        });
    }

    class FiveHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

        }


    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            isStartUpdate = true;
            getMyzhan();
            fiveHandler.postDelayed(this, UPDATE_JIANGE);

        }
    };

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        //切换fragment,设置异步线程结束标志
//		HttpUtil.setCancelled(true);
        Log.v("taskList_size", "" + taskList.size());
        fiveHandler.removeCallbacks(runnable);
        for (AsyncTask task : taskList) {
            if (task.isCancelled()) {

            } else {
                task.cancel(true);
            }
        }

        isStartUpdate = false;

        lastZhanCount = 0;
        currentZhanCount = 0;
        isFistTask = true;
        taskList.clear();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("ZhanFragmentThree");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onPageStart("ZhanFragmentThree"); //统计页面，"MainScreen"为页面名称，可自定义
    }


}
