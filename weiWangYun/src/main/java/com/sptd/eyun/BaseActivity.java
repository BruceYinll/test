package com.sptd.eyun;

import java.io.Serializable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.sptd.eyun.R;
import com.sptd.eyun.filter.FinishBroadcast;
import com.sptd.eyun.finals.OtherFinals;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.UserObj;
import com.sptd.util.resource.PreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 基本Activity类，统一处理相同操作，减少冗余代码
 * 所有Activity类都必须继承它，因为友盟的统计分析以及退出程序时的关闭所有Activity，都在这个父类中进行了处理。
 * 
 * @author lanyan
 * 
 */
public abstract class BaseActivity extends Activity {
	protected ProgressDialog proDialog;
	protected TextView tv_title,tv_right;
	protected ImageView iv_left,iv_plus,iv_info;
	protected LinearLayout ll_circle,ll_topiccontent,ll_left;
	protected int mLayoutId = 0;// 布局Id
	protected static boolean mHasTitle = true;// 是否有Title Bar
	protected Toast mToast;
	protected FinishBroadcast broadcast;
	protected int w,h;
	/**
	 * 构造方法，默认为有公用title
	 * 
	 * @param resId
	 *            布局文件id
	 */
	public BaseActivity(int resId) {
		mLayoutId = resId;
		mHasTitle = true;
	}

	/**
	 * 构造方法，手动设置有无公用title
	 * 
	 * @param resId
	 *            布局文件id
	 * @param hasTitle
	 *            是否有公用title
	 */
	public BaseActivity(int resId, boolean hasTitle) {
		mLayoutId = resId;
		mHasTitle = hasTitle;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(mLayoutId);
		
		EyunApplication.getInstance().addActivity(this);
		
		
		
		WindowManager manage = getWindowManager();
		Display display = manage.getDefaultDisplay();
		w = display.getWidth();
		h = display.getHeight();
		AppSession.Wid = display.getWidth();
		AppSession.Hei = display.getHeight();
		// 注册广播，退出程序时关闭该Activity
		registerBoradcastReceiver(100);

		initTitle();
		getData();
		findView();
		refreshView();
	}

	/**
	 * 简便获取本地的用户数据
	 * 
	 * @return
	 */
	protected UserObj getUserData() {
		return (UserObj) PreferencesUtil.getPreferences(this,
				PreferenceFinals.KEY_USER);
	}

	/**
	 * 重置本地的用户数据
	 * 
	 * @param user
	 *            需要重置的用户数据
	 */
	protected void setUserData(UserObj user) {
		PreferencesUtil.setPreferences(this, PreferenceFinals.KEY_USER, user);
	}

	/**
	 * 根据string.xml中的id获取字符串
	 * 
	 * @param resId
	 * @return
	 */
	protected String getResString(int resId) {
		return getResources().getString(resId);
	}

	/**
	 * 设置优先级，注册广播。 用于程序退出时，关闭所有页面。优先级值越小，执行的越后面。
	 * 
	 * @param prior
	 *            广播的优先级
	 */
	public void registerBoradcastReceiver(int prior) {
		broadcast = new FinishBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(prior);
		filter.addAction(OtherFinals.BROAD_ACTION);
		registerReceiver(broadcast, filter);
	}

	/**
	 * 显示提示信息
	 * 
	 * @param msg
	 */
	public void showToast(String msg) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		if (mToast == null) {
			mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
		}

		mToast.setText(msg);
		mToast.show();
	}

	/**
	 * 从本页面跳转到另外一个页面
	 * 
	 * @param cls
	 *            需要跳转到的页面
	 */
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/**
	 * 带着数据，从本页面跳转到另外一个页面
	 * 
	 * @param cls
	 *            需要跳转到的页面
	 * @param obj
	 *            传递给下个页面的数据
	 */
	public void startActivity(Class<?> cls, Object obj) {
		Intent intent = new Intent(this, cls);
		if (obj != null) {
			intent.putExtra("data", (Serializable) obj);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}

	/**
	 * 带着数据，设置返回码，从本页面跳转到下个页面，重写onActivityResult可以获取从下个页面带回来的数据
	 * 
	 * @param cls
	 *            需要跳转到的页面
	 * @param obj
	 *            传递给下个页面的数据
	 */
	public void startActivityForResult(Class<?> cls, Object obj,int requestCode) {
		Intent intent = new Intent(this, cls);
		if (obj != null) {
			intent.putExtra("data", (Serializable) obj);
		}
		startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

	/**
	 * 初始化标题栏上的左右按钮以及标题Text，同时添加左按钮的关闭事件
	 */
	protected void initTitle() {
 	 //TODO
		/*
		if (!mHasTitle) {
			return;
		}
		ll_left = (LinearLayout) findViewById(R.id.ll_left);
		iv_left = (ImageView) this.findViewById(R.id.iv_left);
		ll_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideKeyboard();
				finish();
			}
		});

		tv_right = (TextView) this.findViewById(R.id.tv_right);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
*/
//		ll_topiccontent = (LinearLayout) this.findViewById(R.id.ll_topiccontent);
//		iv_plus = (ImageView) this.findViewById(R.id.iv_plus);
//		iv_info = (ImageView) this.findViewById(R.id.iv_info);
		// tv_title.getPaint().setFakeBoldText(true);// 字体加粗
	}

	/**
	 * 获取从上个页面传递过来的数据，或者需要从本地读取的数据，如用户数据。
	 */
	protected abstract void getData();

	/**
	 * 初始化控件，并设置监听事件
	 */
	protected abstract void findView();

	
	/**
	 * 获取版本号
	 * @return 当前应用的版本号
	 */
	public String getVersion() {
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			String version = info.versionName;
			return  version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 页面数据更新，在开始的时候，需要判断对象是否为空，更安全；在页面刷新时，只需要调这个方法，减少冗余代码
	 */
	protected abstract void refreshView();
	
	public void displayImage(ImageView imageView,String url,int radius){
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(radius))
				.build();
		ImageLoader.getInstance().displayImage(url,imageView,options);
	}
	/*
	 * 带进度的加载图片
	 */
	public void displayImageLoading(ImageView imageView,String url,int radius){
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(radius))
				.build();
		ImageLoader.getInstance().displayImage(url,imageView,options,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				proDialog=new ProgressDialog(BaseActivity.this);
				proDialog.setMessage("正在加载");
				proDialog.setCanceledOnTouchOutside(false);
				proDialog.show();
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				proDialog.dismiss();
				proDialog=null;
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				proDialog.dismiss();
				proDialog=null;
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				proDialog.dismiss();
				proDialog=null;
			}
		});
	}
	public void hideKeyboard() {
		InputMethodManager imm = ((InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE));
		if (imm != null &&this. getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		//TODO
		/*
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
	    */
	}
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		//TODO
		/*
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
	    */
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(broadcast);
		super.onDestroy();
	}
}
