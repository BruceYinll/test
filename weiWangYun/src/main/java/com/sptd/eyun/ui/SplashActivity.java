package com.sptd.eyun.ui;

import com.sptd.eyun.BaseInteractActivity;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.log.Log;
import com.sptd.util.resource.PreferencesUtil;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

/**
 * 启动页
 * @author ldy
 *
 */
public class SplashActivity extends  BaseInteractActivity{
	
	


	private static final int GO_HOME = 1000;
	private static final int GO_LOADING = 1001;
    private static final long SPLASH_DELAY_MILLIS=3000;
    
    public SplashActivity() {
		super(R.layout.act_splash, false);
	}
 
    /**
	 * 描述：判断是否登录
	 * 
	 * @return
	 */
	private boolean isFirst() {
		return PreferencesUtil.getBooleanPreferences(this,
				PreferenceFinals.KEY_IS_FIRST, true);
	}


	@Override
	public void onBackPressed() {
		// 在加载页不能点击返回按钮，防止退到桌面后又打开登录页
		super.onBackPressed();
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
		if (isFirst()) {// 未登录
            new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					startActivity(LoadingActivity.class);
					SplashActivity.this.finish();
				}
			  } , SPLASH_DELAY_MILLIS);
			
	} else {
		 new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if(getUserData()!=null){
						startActivity(HomeActivity.class);
						SplashActivity.this.finish();
					}else{
						startActivity(LoginActivity.class);
						SplashActivity.this.finish();
					}
					
				}
			  } , SPLASH_DELAY_MILLIS);
		
		
	}
		
		
		
		
	}
	
	
   
   
}
