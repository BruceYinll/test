package com.sptd.eyun.ui;

import com.sptd.eyun.EyunApplication;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.Device;
import com.sptd.eyun.obj.DeviceType;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.obj.UserStation;
import com.sptd.eyun.ui.fragment.MoreFragment;
import com.sptd.eyun.ui.fragment.ShebeiFragment;
import com.sptd.eyun.ui.fragment.ShebeiFragmentThree;
import com.sptd.eyun.ui.fragment.ShebeiFragmentTwo;
import com.sptd.eyun.ui.fragment.YunweiFragment;
import com.sptd.eyun.ui.fragment.ZhanFragment;
import com.sptd.eyun.ui.fragment.ZhanFragmentThree;
import com.sptd.eyun.ui.fragment.ZhanFragmentTwo;
import com.sptd.util.resource.PreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;


/**
 * 首页,切换fragment
 * @author ldy
 *
 */
/**
*
* ━━━━━━神兽出没━━━━━━
* 　　　┏┓　　　┏┓
* 　　┏┛┻━━━┛┻┓
* 　　┃　　　　　　　┃
* 　　┃　　　━　　　┃
* 　　┃　┳┛　┗┳　┃
* 　　┃　　　　　　　┃
* 　　┃　　　┻　　　┃
* 　　┃　　　　　　　┃
* 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
* 　　　　┃　　　┃    神兽保佑,代码无bug
* 　　　　┃　　　┃
* 　　　　┃　　　┗━━━┓
* 　　　　┃　　　　　　　┣┓
* 　　　　┃　　　　　　　┏┛
* 　　　　┗┓┓┏━┳┓┏┛
* 　　　　　┃┫┫　┃┫┫
* 　　　　　┗┻┛　┗┻┛
*
* ━━━━━━感觉萌萌哒━━━━━━
*/
 
/**
* 　　　　　　　　┏┓　　　┏┓
* 　　　　　　　┏┛┻━━━┛┻┓
* 　　　　　　　┃　　　　　　　┃ 　
* 　　　　　　　┃　　　━　　　┃
* 　　　　　　　┃　＞　　　＜　┃
* 　　　　　　　┃　　　　　　　┃
* 　　　　　　　┃...　⌒　...　┃
* 　　　　　　　┃　　　　　　　┃
* 　　　　　　　┗━┓　　　┏━┛
* 　　　　　　　　　┃　　　┃　Code is far away from bug with the animal protecting　　　　　　　　　　
* 　　　　　　　　　┃　　　┃   神兽保佑,代码无bug
* 　　　　　　　　　┃　　　┃　　　　　　　　　　　
* 　　　　　　　　　┃　　　┃  　　　　　　
* 　　　　　　　　　┃　　　┃
* 　　　　　　　　　┃　　　┃　　　　　　　　　　　
* 　　　　　　　　　┃　　　┗━━━┓
* 　　　　　　　　　┃　　　　　　　┣┓
* 　　　　　　　　　┃　　　　　　　┏┛
* 　　　　　　　　　┗┓┓┏━┳┓┏┛
* 　　　　　　　　　　┃┫┫　┃┫┫
* 　　　　　　　　　　┗┻┛　┗┻┛
*/
 
/**
*　　　　　　　　┏┓　　　┏┓+ +
*　　　　　　　┏┛┻━━━┛┻┓ + +
*　　　　　　　┃　　　　　　　┃ 　
*　　　　　　　┃　　　━　　　┃ ++ + + +
*　　　　　　 ████━████ ┃+
*　　　　　　　┃　　　　　　　┃ +
*　　　　　　　┃　　　┻　　　┃
*　　　　　　　┃　　　　　　　┃ + +
*　　　　　　　┗━┓　　　┏━┛
*　　　　　　　　　┃　　　┃　　　　　　　　　　　
*　　　　　　　　　┃　　　┃ + + + +
*　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting　　　　　　　
*　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug　　
*　　　　　　　　　┃　　　┃
*　　　　　　　　　┃　　　┃　　+　　　　　　　　　
*　　　　　　　　　┃　 　　┗━━━┓ + +
*　　　　　　　　　┃ 　　　　　　　┣┓
*　　　　　　　　　┃ 　　　　　　　┏┛
*　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
*　　　　　　　　　　┃┫┫　┃┫┫
*　　　　　　　　　　┗┻┛　┗┻┛+ + + +
*/


public class HomeActivity extends FragmentActivity implements OnClickListener{

	private LinearLayout ll_zhan,ll_shebei,ll_yunwei,ll_more;
//	private ZhanFragment zhanFragment;
//	private ZhanFragmentTwo zhanFragmentTwo;
	private ZhanFragmentThree zhanFragmentThree;
//	private ShebeiFragment shebeiFragment;
//	private ShebeiFragmentTwo shebeiFragmentTwo;
	private ShebeiFragmentThree shebeiFragmentThree;
	private YunweiFragment yunweiFragment;
	private MoreFragment moreFragment;
	
	/**
	 * 用户对象
	 */
    private UserObj userObj;
    
    
  //当前用户站,设备类,设备
    private UserStation currentUserStation;
    private DeviceType  currentDeviceType;
    private Device      currentDevice;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		
		EyunApplication.getInstance().addActivity(this);
		
		findViews();
		setlisteners();
		
		setDefaultFragment();
	}
	
	private void findViews(){
		ll_zhan=(LinearLayout) findViewById(R.id.ll_zhan);
		ll_shebei=(LinearLayout) findViewById(R.id.ll_shebei);
		ll_yunwei=(LinearLayout) findViewById(R.id.ll_yunwei);
		ll_more=(LinearLayout) findViewById(R.id.ll_more);
		
		userObj=(UserObj) PreferencesUtil.getPreferences(this,
				PreferenceFinals.KEY_USER);
	}
	private void setlisteners(){
		ll_zhan.setOnClickListener(this);
		ll_shebei.setOnClickListener(this);
		ll_yunwei.setOnClickListener(this);
		ll_more.setOnClickListener(this);

	}
	
	private void setDefaultFragment(){
		ll_zhan.setSelected(true);
		ll_shebei.setSelected(false);
		ll_yunwei.setSelected(false);
		ll_more.setSelected(false);
		FragmentManager fm=getSupportFragmentManager();
		FragmentTransaction transaction=fm.beginTransaction();
//				zhanFragment=new ZhanFragment();	
//			transaction.replace(R.id.fl_content, zhanFragment);
//		zhanFragmentTwo=new ZhanFragmentTwo();
//		transaction.replace(R.id.fl_content, zhanFragmentTwo);
		zhanFragmentThree=new ZhanFragmentThree();
		transaction.replace(R.id.fl_content, zhanFragmentThree);
		
			transaction.commit();
	}

	@Override
	public void onClick(View v) {
		FragmentManager fm=getSupportFragmentManager();

		FragmentTransaction transaction=fm.beginTransaction();

		switch (v.getId()) {
		case R.id.ll_zhan:
			ll_zhan.setSelected(true);
			ll_shebei.setSelected(false);
			ll_yunwei.setSelected(false);
			ll_more.setSelected(false);
			/*
			if(zhanFragment==null){
				zhanFragment=new ZhanFragment();
			}
			transaction.replace(R.id.fl_content, zhanFragment);
			*/
/*			if (zhanFragmentTwo==null) {
				zhanFragmentTwo=new ZhanFragmentTwo();
			}
			transaction.replace(R.id.fl_content, zhanFragmentTwo);*/
			if (zhanFragmentThree==null) {
				zhanFragmentThree=new ZhanFragmentThree();
			}
			if (!zhanFragmentThree.isAdded()) {
				transaction.replace(R.id.fl_content, zhanFragmentThree);

			}
			
			

			break;

		case R.id.ll_shebei:
			ll_zhan.setSelected(false);
			ll_shebei.setSelected(true);
			ll_yunwei.setSelected(false);
			ll_more.setSelected(false);
			/*if(shebeiFragment==null){
				shebeiFragment=new ShebeiFragment();
			}
			transaction.replace(R.id.fl_content, shebeiFragment);*/
			/*
			if(shebeiFragmentTwo==null){
				shebeiFragmentTwo=new ShebeiFragmentTwo();
			}
			if (!shebeiFragmentTwo.isAdded()) {
				transaction.replace(R.id.fl_content, shebeiFragmentTwo);

			}
			*/
			if(shebeiFragmentThree==null){
				shebeiFragmentThree=new ShebeiFragmentThree();
			}
			if (!shebeiFragmentThree.isAdded()) {
				transaction.replace(R.id.fl_content, shebeiFragmentThree);

			}
			break;
		case R.id.ll_yunwei:
			ll_zhan.setSelected(false);
			ll_shebei.setSelected(false);
			ll_yunwei.setSelected(true);
			ll_more.setSelected(false);
			if(yunweiFragment==null){
				yunweiFragment=new YunweiFragment();
			}
			if (!yunweiFragment.isAdded()) {
				transaction.replace(R.id.fl_content, yunweiFragment);
			}
			
			
			break;
		case R.id.ll_more:
			ll_zhan.setSelected(false);
			ll_shebei.setSelected(false);
			ll_yunwei.setSelected(false);
			ll_more.setSelected(true);
			if(moreFragment==null){
				moreFragment=new MoreFragment();
			}
			if (!moreFragment.isAdded()) {
				transaction.replace(R.id.fl_content, moreFragment);

			}
			
			break;	
		}
		
		transaction.commit();
		
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 MobclickAgent.onResume(this);       //统计时长
	}

	public UserObj getUserObj() {
		return userObj;
	}

	public void setUserObj(UserObj userObj) {
		this.userObj = userObj;
	}

	public LinearLayout getLl_zhan() {
		return ll_zhan;
	}

	public void setLl_zhan(LinearLayout ll_zhan) {
		this.ll_zhan = ll_zhan;
	}

	public LinearLayout getLl_shebei() {
		return ll_shebei;
	}

	public void setLl_shebei(LinearLayout ll_shebei) {
		this.ll_shebei = ll_shebei;
	}

	public LinearLayout getLl_yunwei() {
		return ll_yunwei;
	}

	public void setLl_yunwei(LinearLayout ll_yunwei) {
		this.ll_yunwei = ll_yunwei;
	}

	public LinearLayout getLl_more() {
		return ll_more;
	}

	public void setLl_more(LinearLayout ll_more) {
		this.ll_more = ll_more;
	}
	
	public UserStation getCurrentUserStation() {
		return currentUserStation;
	}

	public void setCurrentUserStation(UserStation currentUserStation) {
		this.currentUserStation = currentUserStation;
	}


	public DeviceType getCurrentDeviceType() {
		return currentDeviceType;
	}

	public void setCurrentDeviceType(DeviceType currentDeviceType) {
		this.currentDeviceType = currentDeviceType;
	}

	public Device getCurrentDevice() {
		return currentDevice;
	}

	public void setCurrentDevice(Device currentDevice) {
		this.currentDevice = currentDevice;
	}

    
}
