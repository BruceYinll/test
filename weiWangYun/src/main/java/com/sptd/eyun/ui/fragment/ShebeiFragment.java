package com.sptd.eyun.ui.fragment;

import com.sptd.eyun.R;
import com.sptd.eyun.widget.ShebeiHorizontalScrollView;







import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ShebeiFragment extends Fragment{

	private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    private ArrayAdapter<String> cityAdapter = null;    //地级适配器
    private static int provincePosition = 0;
    
    
    //省级选项值
    private String[] province = new String[] {"北京北京北京北京北京","上海","天津","广东"};//,"重庆","黑龙江","江苏","山东","浙江","香港","澳门"};
    //地级选项值
    private String[][] city = new String[][] 
            {
                    { "东城区东城区东城区东城区东城区", "西城区", "崇文区", "宣武区", "朝阳区", "海淀区", "丰台区", "石景山区", "门头沟区",
                            "房山区", "通州区", "顺义区", "大兴区", "昌平区", "平谷区", "怀柔区", "密云县",
                            "延庆县" },
                    { "长宁区", "静安区", "普陀区", "闸北区", "虹口区" },
                    { "和平区", "河东区", "河西区", "南开区", "河北区", "红桥区", "塘沽区", "汉沽区", "大港区",
                            "东丽区" },
                    { "广州", "深圳", "韶关" // ,"珠海","汕头","佛山","湛江","肇庆","江门","茂名","惠州","梅州",
                    // "汕尾","河源","阳江","清远","东莞","中山","潮州","揭阳","云浮"
                    }
            };


	public static final String ARGUMENTS_NAME = "arg";
	private RelativeLayout rl_nav;
	private ShebeiHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private ViewPager mViewPager;
	private int indicatorWidth;
//	public static String[] tabTitle = { "设备1", "设备2" };	//����
	public static String[] tabTitle = { "设备五个字", "设备2", "设备3", "设备4", "设备5", "设备6", "设备7", "设备8", "设备9" };	//����

	private LayoutInflater mInflater;
	private TabFragmentPagerAdapter mAdapter;
	private int currentIndicatorLeft = 0;

	
	private int currentButtonId=0;
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_shebei, container, false);
		
		getData();
		
		setSpinner(view);
		
		findViewById(view);
		initView();
		setListener();
		//初始checked
		  ((RadioButton)rg_nav_content.getChildAt(0)).performClick();
		return view;
	}
    private void getData(){
	  if(getArguments()!=null){
    	String deviceType = getArguments().getString("deviceType");  
    	String device=getArguments().getString("device");
    	Toast.makeText(getActivity(), deviceType+":  "+device, 3000).show();
	  }
    }
    
    
    /*
     * 设置下拉框
     */
    private void setSpinner(View view)
    {        
        provinceSpinner = (Spinner)view.findViewById(R.id.spin_province);
        citySpinner = (Spinner)view.findViewById(R.id.spin_city);
        
        //绑定适配器和值
        provinceAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.simple_spinner_item, province);
 //       provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinceAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        provinceSpinner.setAdapter(provinceAdapter);
//        provinceSpinner.setPopupBackgroundResource(Color.parseColor("#2d3037"));
        provinceSpinner.setSelection(0,true);  //设置默认选中项，此处为默认选中第4个值
        
        cityAdapter = new ArrayAdapter<String>(getActivity(), 
                R.layout.simple_spinner_item, city[0]);
        cityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0,true);  //默认选中第0个

        
        
        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                //position为当前省级选中的值的序号
                
                //将地级适配器的值改变为city[position]中的值
                cityAdapter = new ArrayAdapter<String>(
                       getActivity(), R.layout.simple_spinner_item, city[position]);
                cityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                // 设置二级下拉列表的选项内容适配器
                citySpinner.setAdapter(cityAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
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
            	
            	Toast.makeText(getActivity(), city[provincePosition][position], 3000).show();              
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                
            }
        });
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
													
					mViewPager.setCurrentItem(checkedId);	//ViewPager ����һ�� �л�
					
					//��¼��ǰ �±�ľ������� ����
					currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();
			      if(rg_nav_content.getChildCount()>2){
					mHsv.smoothScrollTo(
							(checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(2)).getLeft(), 0);
			      }
			    }
				currentButtonId=checkedId;
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
				if(currentButtonId<tabTitle.length-1){
					currentButtonId=currentButtonId+1;
					((RadioButton)rg_nav_content.getChildAt(currentButtonId)).performClick();
				}else {
					((RadioButton)rg_nav_content.getChildAt(currentButtonId)).performClick();
				}
				
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
		
		mAdapter = new TabFragmentPagerAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mHsv.setSomeParam(rl_nav, iv_nav_left, iv_nav_right, getActivity());
	}

	private void initNavigationHSV() {
		
		rg_nav_content.removeAllViews();
		
		for(int i=0;i<tabTitle.length;i++){
			
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
			rg_nav_content.addView(rb,params_rb);

		}
		
	}

	private void findViewById(View view) {
		
		rl_nav = (RelativeLayout) view.findViewById(R.id.rl_nav);
		
		mHsv = (ShebeiHorizontalScrollView) view.findViewById(R.id.mHsv);
		
		rg_nav_content = (RadioGroup) view.findViewById(R.id.rg_nav_content);
		iv_nav_left = (ImageView) view.findViewById(R.id.iv_nav_left);
		iv_nav_right = (ImageView) view.findViewById(R.id.iv_nav_right);
		
		mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
	}

	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter{

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
//			CommonUIFragment ft = null;
			ShebeiUIFragment ft=null;
			switch (arg0) {
			default:
//				ft = new CommonUIFragment(tabTitle[arg0]);
				ft=new ShebeiUIFragment(tabTitle[arg0]);
				Bundle args = new Bundle();
				args.putString(ARGUMENTS_NAME, tabTitle[arg0]);
				ft.setArguments(args);
				
				break;
			}
			return ft;
		}

		@Override
		public int getCount() {
			
			return tabTitle.length;
		}
		
	}


}
