package com.sptd.eyun.ui;


import java.util.ArrayList;

import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.R;
import com.sptd.eyun.adapter.VPagerFirstInAdapter;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.util.resource.PreferencesUtil;






import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 引导页
 * @author ldy
 *
 */
public class LoadingActivity extends BaseInteractActivity {

	

	private ViewPager vp_loading;
	
	
    private VPagerFirstInAdapter vpAdapter;
    
    
    private ArrayList<View> mGuideViews;
    private View view1,view2,view5;
    private TextView ok;
    //圆点
    private ImageView[] points;
    //记录当前选中位置
    private int currentIndex;
    
    
    private TextView textView1;
    
    
    public LoadingActivity() {
		super(R.layout.act_loading_2,false);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//隐藏标题栏
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_loading_2);
        findViews();
        initData();
        setAdapters();
        setListeners();
	}
	
	private void findViews(){
		LayoutInflater mLi=LayoutInflater.from(this);		
		view1=mLi.inflate(R.layout.guide_page1, null);
		view2=mLi.inflate(R.layout.guide_page2, null);
		view5=mLi.inflate(R.layout.guide_page5, null);
	    mGuideViews=new ArrayList<View>();
	    mGuideViews.add(view1);
	    mGuideViews.add(view2);
	    mGuideViews.add(view5);
	    ok=(TextView) view5.findViewById(R.id.ok);
	    
	    
		vp_loading=(ViewPager) findViewById(R.id.vp_loading);
		
		textView1=(TextView) findViewById(R.id.textView1);
		
	}
	
	private void setAdapters(){		
		vp_loading.setAdapter(vpAdapter);
        
	}
	
	private void setListeners(){
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PreferencesUtil.setPreferences(LoadingActivity.this, PreferenceFinals.KEY_IS_FIRST,
						false);
				Intent intent=new Intent();
				intent.setClass(LoadingActivity.this, LoginActivity.class);
				startActivity(intent);
				LoadingActivity.this.finish();
				
			}
		});
	}
	
	private void initData(){
//		vpAdapter=new VPagerFirstInAdapter(this);
		vpAdapter=new VPagerFirstInAdapter(this, mGuideViews);
		vp_loading.setOnPageChangeListener(new OnPageChangeListener() {
//		vp_loading.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
//				setCurDot(arg0);
				
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
//		initPoint();
		
	}
	
	//初始化圆点
	private void initPoint(){
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_point);
        points = new ImageView[3];
        for(int i=0;i<3;i++){
        	points[i]=(ImageView) linearLayout.getChildAt(i);
        	points[i].setEnabled(true);
        	
        }
        currentIndex=0;
        points[currentIndex].setEnabled(false);
	}
    
	
	//设置当前圆点
	private void setCurDot(int position){
		points[position].setEnabled(false);
		points[currentIndex].setEnabled(true);
		currentIndex=position;
	}

	@Override
	public void onSuccess(BaseModel res) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void refreshView() {
		// TODO Auto-generated method stub
		
	}
}
