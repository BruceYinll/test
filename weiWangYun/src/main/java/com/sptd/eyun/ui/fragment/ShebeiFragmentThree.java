package com.sptd.eyun.ui.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseFragment;
import com.sptd.eyun.EyunApplication;
import com.sptd.eyun.R;
import com.sptd.eyun.adapter.ShebeiDataListViewAdapter;
import com.sptd.eyun.adapter.ShebeiVPagerAdapter;
import com.sptd.eyun.dialog.ApplyDialog;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.Device;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.Parameter;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.obj.UserStation;
import com.sptd.eyun.ui.HomeActivity;
import com.sptd.eyun.ui.fragment.ShebeiFragmentTwo.FiveHandler;
import com.sptd.eyun.ui.fragment.ShebeiFragmentTwo.TenHandler;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.eyun.widget.ShebeiHorizontalScrollView;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.StringUtils;
import com.sptd.util.resource.PreferencesUtil;
import com.umeng.analytics.MobclickAgent;

public class ShebeiFragmentThree extends BaseFragment{

	private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市

    private ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    private ArrayAdapter<String> cityAdapter = null;    //地级适配器
    
   //站,设备类----position
    private static int provincePosition = 0;
    private static int cityPosition = 0;

	public static final String ARGUMENTS_NAME = "arg";
	private RelativeLayout rl_nav;
	private ShebeiHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private int indicatorWidth;
//	public static String[] tabTitle = { "设备1", "设备2" };	//����
	public static String[] tabTitle = { "设备五个字", "设备2", "设备3", "设备4", "设备5", "设备6", "设备7", "设备8", "设备9" };	//����

	private LayoutInflater mInflater;
//	private TabFragmentPagerAdapter mAdapter;
	private ShebeiVPagerAdapter shebeiVPagerAdapter;
	
	
	private int currentIndicatorLeft = 0;

	
	private int currentButtonId=0;
	
	
	
	//我的站列表
	private List<UserStation> userStationList=new ArrayList<UserStation>();
	private List<UserStation> spinnerUserStationList=new ArrayList<UserStation>();
	
	private List<DeviceType>  deviceTypeList=new ArrayList<DeviceType>();
	
	private List<Device> deviceList=new ArrayList<Device>();
	
	private UserObj userObj;
	
		
		//当前用户站,设备类,设备
	    private UserStation currentUserStation;
	    private DeviceType  currentDeviceType;
	    private Device      currentDevice;		
	    
	    
		
		//5s刷新一次
			private FiveHandler fiveHandler=new FiveHandler();
			private static final int UPDATE_JIANGE=5*1000;
			private boolean isStartUpdate=false;
			
		//设备数据5s刷新一次	
			private TenHandler tenHandler=new TenHandler();
			private boolean isDeviceStartUpdate=false;
			
			
			
			
			//AsyncTask任务队列
		    List<AsyncTask> taskList=new ArrayList<AsyncTask>();	
		    
		    
		    
		    //没有设备,刷新fragment
		    private ApplyDialog dialog;
		    private TextView tv_content;
			private TextView tuichu;
			
			//没有关联站,提示到更多页面关联
			private ApplyDialog dialog1;
		    private TextView tv_content1;
			private TextView tuichu1;
			
			//设备名称,数据列表
			private TextView viewpager_view_shebei_shebeiname;
	        private ListView viewpager_view_shebei_listView1;
	        private ShebeiDataListViewAdapter shebeiDataListViewAdapter;
	        private List<Parameter> parameterList=new ArrayList<Parameter>();
	        
    public ShebeiFragmentThree() {
		super(R.layout.fragment_shebei3,NoTitle);
	}
    
    
    private void  taskInterrupt(){   //线程中止
    	if(tenHandler!=null && runnableTen!=null){
   		 tenHandler.removeCallbacks(runnableTen);
   	 }              	 
  		for(AsyncTask task:taskList){
  	    	if (task.isCancelled()) {
  				
  			}else {
  				task.cancel(true);
  			}
  	    }
  		 taskList.clear();
  	    isStartUpdate=false;	  	                 	   
  	    isDeviceStartUpdate=false;
    }
    
    /**
     * 同步用户站
     */
    private  void syncUserStation(){
    	
    }
    private void syncDeviceType(){
    	
    }
    private void syncDevice(){
    	
    }
    
    
    
    
    /*
     * 设置下拉框
     */
    private void setSpinner()
    {      
    	spinnerUserStationList.clear();  
    	
        for(int i=0;i<userStationList.size();i++){        	
				spinnerUserStationList.add(userStationList.get(i));				
        }
        //绑定适配器和值 
        //用我的初始方法试验
        String[] pronvices=new String[spinnerUserStationList.size()];
        for(int i=0;i<spinnerUserStationList.size();i++){
        	pronvices[i]=spinnerUserStationList.get(i).getStation().getName();
        }
        //绑定适配器和值
        provinceAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.simple_spinner_item, pronvices);
 //       provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        provinceSpinner.setAdapter(provinceAdapter);
//        provinceSpinner.setPopupBackgroundResource(Color.parseColor("#2d3037"));

        if(currentUserStation==null){
        	currentUserStation=spinnerUserStationList.get(0);
        	provincePosition=0;
        }
        
        for(int i=0;i<spinnerUserStationList.size();i++){
        	if (currentUserStation.getStationId().equals(spinnerUserStationList.get(i).getStationId())) {
        		 //TODO 初始selected,似乎不会触发setOnItemSelectedListener事件,有待验证
        		provinceSpinner.setSelection(i,true);  //根据站fragment设置默认值
        		provincePosition=i;
        		break;
			}
        }
        currentUserStation=spinnerUserStationList.get(provincePosition);
        ((HomeActivity)getActivity()).setCurrentUserStation(currentUserStation);
        
        
                 
        List<DeviceType>  oldDeviceTypeList=spinnerUserStationList.get(provincePosition).getStation().getDeviceTypeList();
        List<DeviceType>  newDeviceTypeList=new ArrayList<DeviceType>();
        if(oldDeviceTypeList==null || oldDeviceTypeList.size()==0){
//        	return;//无设备类则直接跳出方法
        }else {
        	for(int j=0;j<oldDeviceTypeList.size();j++){
    			if(oldDeviceTypeList.get(j).getDeviceList()!=null
    					&& oldDeviceTypeList.get(j).getDeviceList().size()>0)
    				newDeviceTypeList.add(oldDeviceTypeList.get(j));
    		}
		}
        
//        deviceTypeList=newDeviceTypeList;
        deviceTypeList.clear();
        deviceTypeList.addAll(newDeviceTypeList);
        
        if(deviceTypeList==null || deviceTypeList.size()==0){

        	currentDeviceType=null;
        	currentDevice=null;
        	return;
        }
        
                
//      cityAdapter = new SpinnerDeviceTypeAdapter(getActivity(), newDeviceTypeList); 
      //用我的初始方法试验
      String[] citys=new String[newDeviceTypeList.size()];
      for(int i=0;i<newDeviceTypeList.size();i++){
    	  citys[i]=newDeviceTypeList.get(i).getName();
      }
      cityAdapter = new ArrayAdapter<String>(getActivity(), 
              R.layout.simple_spinner_item, citys);
      cityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
      citySpinner.setAdapter(cityAdapter);

//        cityAdapter = new SpinnerDeviceTypeAdapter(getActivity(), spinnerUserStationList.get(0).getStation().getDeviceTypeList());   

      
      
      
        if(currentDeviceType==null){
        	//TODO 初始selected,似乎不会触发setOnItemSelectedListener事件,有待验证
           if(deviceTypeList.size()>0){	
        	citySpinner.setSelection(0, true);
        	cityPosition=0;
           }else {
			
		   }
        }else {
        	for(int i=0;i<deviceTypeList.size();i++){
            	if(currentDeviceType.getTid().equals(deviceTypeList.get(i).getTid())){
            		//TODO 初始selected,似乎不会触发setOnItemSelectedListener事件,有待验证
            		citySpinner.setSelection(i,true);  //设置devictype默认值
            		cityPosition=i;
            		break;
            	}
            }
		}
        
        currentDeviceType=deviceTypeList.get(cityPosition);
        ((HomeActivity)getActivity()).setCurrentDeviceType(currentDeviceType);
        

        List<Device> devices=(List<Device>) deviceTypeList.get(cityPosition).getDeviceList();
        if(devices==null || devices.size()==0){
        	
        }else {
        	deviceList.clear();
        	deviceList.addAll(devices);
		}
        
        

        
        
    }

	private void setListener() {						
		rg_nav_content.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
			 
				if(rg_nav_content.getChildAt(checkedId)!=null){
																		
					//��¼��ǰ �±�ľ������� ����
					currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();
			      if(rg_nav_content.getChildCount()>2){
					mHsv.smoothScrollTo(
							(checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(2)).getLeft(), 0);
			      }
			    }


	              if(tenHandler!=null && runnableTen!=null){
	            		 tenHandler.removeCallbacks(runnableTen);
	               }              	 
	           		for(AsyncTask task:taskList){
	           	    	if (task.isCancelled()) {
	           				
	           			}else {
	           				task.cancel(true);
	           			}
	           	    }
	           		 taskList.clear();  	                 	   
	           	    isDeviceStartUpdate=false;
	              	
					currentButtonId=checkedId;
					currentDevice=deviceList.get(checkedId);
					((HomeActivity)getActivity()).setCurrentDevice(currentDevice);
					
	           	    
				getDeviceData();
			}
			
		});
	
	    iv_nav_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentButtonId>0){
					currentButtonId=currentButtonId-1;
					((RadioButton)rg_nav_content.getChildAt(currentButtonId)).performClick();
				}else {
					((RadioButton)rg_nav_content.getChildAt(currentButtonId)).performClick();
				}
				
			}
		});
	    iv_nav_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentButtonId<deviceList.size()-1){
					currentButtonId=currentButtonId+1;
					((RadioButton)rg_nav_content.getChildAt(currentButtonId)).performClick();
				}else {
					((RadioButton)rg_nav_content.getChildAt(currentButtonId)).performClick();
				}
				
			}
		});
	    
	    
	    //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
            	List<DeviceType>  oldDeviceTypeList=spinnerUserStationList.get(position).getStation().getDeviceTypeList();
                List<DeviceType>  newDeviceTypeList=new ArrayList<DeviceType>();
                if(oldDeviceTypeList==null || oldDeviceTypeList.size()==0){
                	
                	
          //      	return;//无设备类则直接跳出方法
                }else {
                	for(int j=0;j<oldDeviceTypeList.size();j++){
                		List<Device> devices=oldDeviceTypeList.get(j).getDeviceList();
                		int dls=oldDeviceTypeList.get(j).getDeviceList().size();
            			if(oldDeviceTypeList.get(j).getDeviceList()!=null && oldDeviceTypeList.get(j).getDeviceList().size()>0){
            				newDeviceTypeList.add(oldDeviceTypeList.get(j));
            			}
            		}
        		}
                deviceTypeList.clear();
                deviceTypeList.addAll(newDeviceTypeList);
//              cityAdapter = new SpinnerDeviceTypeAdapter(getActivity(), newDeviceTypeList);  
               
            
                
         
                String []  ssStrings=new String[0];
              //用我的初始方法试验
              String[] citys=new String[newDeviceTypeList.size()];
              for(int i=0;i<newDeviceTypeList.size();i++){
            	  citys[i]=newDeviceTypeList.get(i).getName();
              }
              cityAdapter = new ArrayAdapter<String>(getActivity(), 
                      R.layout.simple_spinner_item, citys);
              cityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
           	
           	
           	
               //position为当前省级选中的值的序号           
               //将地级适配器的值改变为city[position]中的值
//               cityAdapter = new SpinnerDeviceTypeAdapter(getActivity(), spinnerUserStationList.get(position).getStation().getDeviceTypeList());   
               // 设置二级下拉列表的选项内容适配器
               citySpinner.setAdapter(cityAdapter);
               
               if(deviceTypeList.size()==0){
            	   citySpinner.setClickable(false);
                }else {
					citySpinner.setClickable(true);
				}
                
               provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
               ((HomeActivity)getActivity()).setCurrentUserStation(spinnerUserStationList.get(provincePosition));
          
               if(newDeviceTypeList.size()==0){
            	 if(tenHandler!=null && runnableTen!=null){
            		 tenHandler.removeCallbacks(runnableTen);
            	 }              	 
           		for(AsyncTask task:taskList){
           	    	if (task.isCancelled()) {
           				
           			}else {
           				task.cancel(true);
           			}
           	    }
           		 taskList.clear();
           	    isStartUpdate=false;	  	                 	   
           	    isDeviceStartUpdate=false;
           	    
           	   
              	deviceList.clear();
              	initView();
              	if(newDeviceTypeList.size()==0){
              		viewpager_view_shebei_shebeiname.setText("设备名称");
        	    	parameterList.clear();
        	    	shebeiDataListViewAdapter.notifyDataSetChanged();
              	}
              	
              	
              	return;
              }
               
               
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                
            }
            
        });
        
        
      //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int position, long arg3)
            {
            	List<DeviceType>  oldDeviceTypeList=spinnerUserStationList.get(provincePosition).getStation().getDeviceTypeList();
                if(oldDeviceTypeList==null || oldDeviceTypeList.size()==0){
                	if(tenHandler!=null && runnableTen!=null){
               		 tenHandler.removeCallbacks(runnableTen);
               	 }              	 
              		for(AsyncTask task:taskList){
              	    	if (task.isCancelled()) {
              				
              			}else {
              				task.cancel(true);
              			}
              	    }
              		 taskList.clear();
              	    isStartUpdate=false;	  	                 	   
              	    isDeviceStartUpdate=false;
              	    
              	   
                 	deviceList.clear();
                 	initView();
                 	
                 	return;
                	
          //      	return;//无设备类则直接跳出方法
                }
            	 
                if(tenHandler!=null && runnableTen!=null){
           		 tenHandler.removeCallbacks(runnableTen);
              }              	 
          		for(AsyncTask task:taskList){
          	    	if (task.isCancelled()) {
          				
          			}else {
          				task.cancel(true);
          			}
          	    }
          		 taskList.clear();  	                 	   
          	    isDeviceStartUpdate=false;
          	    
/*提示当前设备类名          	    
            	Toast.makeText(getActivity(), 
            			spinnerUserStationList.get(provincePosition).getStation().getDeviceTypeList().get(position).getName()
            			,3000).show();  
  */          	
            	
            	 //           	deviceList=spinnerUserStationList.get(provincePosition).getStation()
            	 //           			.getDeviceTypeList().get(position).getDeviceList();
            	                
            	 //           	deviceList=deviceTypeList.get(position).getDeviceList();
            	
                /*
            	if(devices==null || devices.size()==0){
                	 tenHandler.removeCallbacks(runnableTen);
              		for(AsyncTask task:taskList){
              	    	if (task.isCancelled()) {
              				
              			}else {
              				task.cancel(true);
              			}
              	    }
              		 taskList.clear();
              	    isStartUpdate=false;	  	                 	   
              	    isDeviceStartUpdate=false;
              	    
              	   
                 	deviceList.clear();
                 	initView();
                 	
                 	return;
                }else {
                	deviceList=devices;
        		}
                */
            	            	cityPosition=position;	
            	            	
            	            	currentDeviceType=deviceTypeList.get(position);
            	            	((HomeActivity)getActivity()).setCurrentDeviceType(currentDeviceType);
            	            	
            	    List<Device> devices=(List<Device>) deviceTypeList.get(cityPosition).getDeviceList();
            	         deviceList.clear();
            	         deviceList.addAll(devices);
            	         
            					
					initView();
//					setListener();
					
					
					//初始checked
					if(rg_nav_content.getChildCount()>0){
					  ((RadioButton)rg_nav_content.getChildAt(0)).performClick();
            	      currentDevice=deviceList.get(0);
            	      ((HomeActivity)getActivity()).setCurrentDevice(currentDevice);
					}
					
					getDeviceData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                
            }
        });
	}

	private void initView() {
		
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		indicatorWidth = dm.widthPixels / 3;
		
		
		
		//��ȡ���������
		mInflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

		//��һ�ַ�ʽ��ȡ
//		LayoutInflater mInflater = LayoutInflater.from(this);  
		
		initNavigationHSV();
		
		mHsv.setSomeParam(rl_nav, iv_nav_left, iv_nav_right, getActivity());
	}

	private void initNavigationHSV() {
		
		rg_nav_content.removeAllViews();
		for(int i=0;i<deviceList.size();i++){
			
			RadioButton rb = (RadioButton) mInflater.inflate(R.layout.shebei_nav_radiogroup_item, null);
			rb.setId(i);
			rb.setText(StringUtils.cutString(deviceList.get(i).getName(), 5));
//			rb.setLayoutParams(new LayoutParams(indicatorWidth,
//					LayoutParams.MATCH_PARENT));
			
			RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(
					(int) getResources().getDimension(R.dimen.dim180),
							(int) getResources().getDimension(R.dimen.dim180));
			//屏幕适配
			final float scale = getActivity().getResources().getDisplayMetrics().density;
			Log.v("density", ""+scale);
			int margin=0;
			/*
			if(scale==2.0){
				margin = (int)(22.5);
			}
			if (scale==3.0) {
				margin = (int)(22.5*3/2);
			}
			*/
			margin=(int) (22.5*scale/2);
			
			
			params_rb.setMargins(margin, 0, margin, 0);
			rg_nav_content.addView(rb,params_rb);
		
/*		for(int i=0;i<tabTitle.length;i++){
			
			RadioButton rb = (RadioButton) mInflater.inflate(R.layout.shebei_nav_radiogroup_item, null);
			rb.setId(i);
			rb.setText(tabTitle[i]);
//			rb.setLayoutParams(new LayoutParams(indicatorWidth,
//					LayoutParams.MATCH_PARENT));
			
			RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(
			        LayoutParams.WRAP_CONTENT,
			        LayoutParams.WRAP_CONTENT);
			int margin = (int)(22.5);
			params_rb.setMargins(margin, 0, margin, 0);
			rg_nav_content.addView(rb,params_rb);*/

		}
		
	}

	public  class TabFragmentPagerAdapter extends FragmentPagerAdapter{
		private List<Device> deviceList=new ArrayList<Device>();

        
		public TabFragmentPagerAdapter(FragmentManager fm,List<Device> deviceList) {
			super(fm);
			this.deviceList=deviceList;
		}

		@Override
		public Fragment getItem(int arg0) {
//			CommonUIFragment ft = null;
			ShebeiUIFragment ft=null;			
				ft=new ShebeiUIFragment(deviceList.get(arg0).getName());
				Bundle args = new Bundle();
				args.putString(ARGUMENTS_NAME, deviceList.get(arg0).getName());
				ft.setArguments(args);

			return ft;
		}

		@Override
		public int getCount() {
			
			return deviceList.size();
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
		params.put("token",userObj.getToken());
		params.put("status", ""+2);
		task.execute(params);
		
		taskList.add(task);
	}
	
	//获取设备的数据
		private void getDeviceData(){
			Type t = new TypeToken<BaseModel<Device>>() {
			}.getType();
			
			//获得设备数据时,就不显示进度条,false为不显示进度条
			BaseAsyncTask<Device> task = new BaseAsyncTask<Device>(this,t,
					InterfaceFinals.INF_FINDDEVICEBYID,false);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("modelId",""+currentDevice.getTid());
			task.execute(params);
			
			taskList.add(task);
		}
	

	@Override
	protected void getData() {
		 userObj=(UserObj) PreferencesUtil.getPreferences(getActivity(),
				PreferenceFinals.KEY_USER);
		 
		 currentUserStation=((HomeActivity)getActivity()).getCurrentUserStation();
		 currentDeviceType=((HomeActivity)getActivity()).getCurrentDeviceType();		 
		 currentDevice=((HomeActivity)getActivity()).getCurrentDevice();
		 
/*	Toast站fragment所点击的设备	 
		 if(getArguments()!=null){
		    	String deviceType = getArguments().getString("deviceType");  
		    	String device=getArguments().getString("device");
		    	Toast.makeText(getActivity(), deviceType+":  "+device, 3000).show();
			  }
			  
			  
			  */
			  getMyzhan();
	}

	@Override
	protected void findView() {
		provinceSpinner = (Spinner)findViewById(R.id.spin_province);
        citySpinner = (Spinner)findViewById(R.id.spin_city);
         rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		
		mHsv = (ShebeiHorizontalScrollView) findViewById(R.id.mHsv);
		
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		iv_nav_left = (ImageView) findViewById(R.id.iv_nav_left);
		iv_nav_right = (ImageView) findViewById(R.id.iv_nav_right);
					
		viewpager_view_shebei_shebeiname=(TextView) findViewById(R.id.viewpager_view_shebei_shebeiname);
		viewpager_view_shebei_listView1=(ListView) findViewById(R.id.viewpager_view_shebei_listView1);
		shebeiDataListViewAdapter=new ShebeiDataListViewAdapter(getActivity(), parameterList);
		viewpager_view_shebei_listView1.setAdapter(shebeiDataListViewAdapter);
	
		
		
	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_FINDUSERSTATIONLIST:
			      //第一次进入展示数据
                 userStationList=(List<UserStation>) res.getObject();
                 //没有已审核的关联站,提示他到更多->个人信息->关联站,申请关联站
                 if(userStationList.size()==0){
                	 dialog1=new ApplyDialog(getActivity(),R.style.MyDialog);
     				dialog1.setCancelable(false);
     				dialog1.show();
     				tv_content1=dialog1.getTv_content();
     				tv_content1.setText("没有已审核的关联站,可以到更多->个人信息->关联站,申请关联站");
     				tuichu1=dialog1.getTuichu();
     				tuichu1.setText("我知道了");
     				if(tuichu1==null){
     					Log.v("tuichu1", "null");
     				}
     				tuichu1.setOnClickListener(new OnClickListener() {
     					
     					@Override
     					public void onClick(View v) {
     						//退出应用
//     						RegisterActivity.this.finish();
     		
     						dialog1.dismiss();
     					    for(AsyncTask task:taskList){
     					    	if (task.isCancelled()) {
     								
     							}else {
     								task.cancel(true);
     							}
     					    }
     					   
     					    taskList.clear();     						
     						 
     					}
     				});
     				return;
                 }
				 
				 setSpinner();					
				 initView();
				 setListener();
				
				//是否有设备
			    if(deviceList.size()==0){
			    	
			    }else {
			    	//初始checked
					if(currentDevice==null){
						
						((RadioButton)rg_nav_content.getChildAt(0)).performClick();
						currentDevice=deviceList.get(0);
					}else{
						for(int i=0;i<deviceList.size();i++){
							if(currentDevice.getTid().equals(deviceList.get(i).getTid())){
//								((RadioButton)rg_nav_content.getChildAt(0)).performClick();
								((RadioButton)rg_nav_content.getChildAt(i)).performClick();
								currentButtonId=i;
								
								//TODO 这只是一种欺骗的做法
								new Handler().postDelayed(new Runnable() {

							        @Override

							        public void run() {

							        	if(rg_nav_content.getChildCount()>2){
											mHsv.smoothScrollTo(
													(currentButtonId > 1 ? ((RadioButton) rg_nav_content.getChildAt(currentButtonId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(2)).getLeft(), 0);
									      }

							        }

							    }, 100);
								break;
							}
						}
					}
					
					

//					currentDevice=deviceList.get(0);
						
					getDeviceData();
										
//					 fiveHandler.postDelayed(runnable, UPDATE_JIANGE	);
				}
											
				 
			break;
			
			
		 case	InterfaceFinals.INF_FINDDEVICEBYID:
			    if(isDeviceStartUpdate){//5s刷新
//			    	Toast.makeText(getActivity(), "当前设备是--:"+currentDevice.getName(), 3000).show();
			    	currentDevice= (Device) res.getObject();
			    	viewpager_view_shebei_shebeiname.setText(currentDevice.getName());
			    	parameterList.clear();
			    	parameterList.addAll(currentDevice.getParameterList());
                    shebeiDataListViewAdapter.notifyDataSetChanged();
			    }else {//第一次载入数据
			    	currentDevice= (Device) res.getObject();
			    	viewpager_view_shebei_shebeiname.setText(currentDevice.getName());
			    	parameterList.clear();
			    	parameterList.addAll(currentDevice.getParameterList());
                    shebeiDataListViewAdapter.notifyDataSetChanged();
					 //开启5s刷新一次
					 tenHandler.postDelayed(runnableTen, UPDATE_JIANGE	);
				}
			
	          break;
	  }
	}
	
	@Override
	public void onFail(BaseModel res) {
		showToast(res.getMessage());
		String failInf=res.getMessage();
		if("未登陆".equals(failInf)){
			EyunApplication.getInstance().removeAllActivity();
			startActivity(LoginActivity.class);
		}
		if("找不到要操作的记录!".equals(failInf)){
			
			viewpager_view_shebei_shebeiname.setText("设备名称");
	    	parameterList.clear();
	    	shebeiDataListViewAdapter.notifyDataSetChanged();
	    	
	    	
			if(tenHandler!=null && runnableTen!=null){
          		 tenHandler.removeCallbacks(runnableTen);
             }              	 
         		for(AsyncTask task:taskList){
         	    	if (task.isCancelled()) {
         				
         			}else {
         				task.cancel(true);
         			}
         	    }
         		 taskList.clear();  	                 	   
         	    isDeviceStartUpdate=false;
         	    deviceList.clear();
         	    
         	   dialog=new ApplyDialog(getActivity(),R.style.MyDialog);
   			dialog.setCancelable(false);
   			dialog.show();
   			tv_content=dialog.getTv_content();
   			tv_content.setText("没找到该设备,为你刷新页面");
   			tuichu=dialog.getTuichu();
   			tuichu.setText("确定");
   			if(tuichu==null){
   				Log.v("tuichu", "null");
   			}
   			tuichu.setOnClickListener(new OnClickListener() {
   				
   				@Override
   				public void onClick(View v) {
   					dialog.dismiss();
   					getMyzhan();
   					 
   				}
   			});
		}
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
	//设备数据5s刷新一次
	class TenHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {			
			
		}
	}
	
	Runnable  runnableTen=new Runnable() {
		
		@Override
		public void run() {
			isDeviceStartUpdate=true;
			getDeviceData();
			tenHandler.postDelayed(this, UPDATE_JIANGE	);
			
		}
	};
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
//	    fiveHandler.removeCallbacks(runnable);
		
		//切换fragment,设置异步线程结束标志
//		HttpUtil.setCancelled(true);
		Log.v("taskList_size", ""+taskList.size());
		 tenHandler.removeCallbacks(runnableTen);
		for(AsyncTask task:taskList){
	    	if (task.isCancelled()) {
				
			}else {
				task.cancel(true);
			}
	    }
		
	    isStartUpdate=false;	  	    
	   
	    isDeviceStartUpdate=false;
	    
	    taskList.clear();
	    deviceList.clear();
	    deviceTypeList.clear();
	    parameterList.clear();
	    
	    cityPosition=0;
	    provincePosition=0;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("ShebeiFragmentTwo"); 
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 MobclickAgent.onPageStart("ShebeiFragmentTwo"); //统计页面，"MainScreen"为页面名称，可自定义
	}

}

