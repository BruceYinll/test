package com.sptd.eyun;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sptd.eyun.filter.FinishBroadcast;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.ui.HomeActivity;
import com.sptd.util.resource.PreferencesUtil;

@SuppressWarnings("rawtypes")
public abstract class BaseFragment extends Fragment {
	public static Activity activity;
	protected TextView tv_title,tv_right;
	protected ImageView iv_left,iv_collect,iv_share;
	protected LinearLayout ll_cs,ll_left;

	protected static final int NO_DEFAULT = -1;// 有图片但是没有默认图
	protected static final int NO_IMAGE = 0;// 没有图片

	protected boolean mHasTitle = true;// 是否有Title Bar
	protected FinishBroadcast broadcast;
	protected int mLayoutId = 0;// 布局Id
	protected View rootView;
	protected int w,h;
	protected  static final int NoTitle = 0;
	protected  static final int DefTitle = 1;
	protected  static final int SearchTitle = 2;
	protected int titleType;
	protected Toast mToast;
	protected HomeActivity homeActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	protected BaseFragment(int mLayoutId, int titleType) {
		this.mLayoutId = mLayoutId;
		this.titleType = titleType;
	}

	protected BaseFragment(int mLayoutId) {
		this.mLayoutId = mLayoutId;
		this.titleType = DefTitle;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = getActivity().getLayoutInflater().inflate(mLayoutId,
				null);
		WindowManager manage = getActivity().getWindowManager();
		Display display = manage.getDefaultDisplay();
		w = display.getWidth();
		h = display.getHeight();

		initTitle();
		findView();
		getData();
		refreshView();
		return rootView;
	}



	/**
	 * 获取从上个页面传递过来的数据，或者需要从本地读取的数据...........................................，如用户数据。
	 */
	protected abstract void getData();


	/**
	 * 初始化控件，并设置监听事件
	 */
	protected abstract void findView();


	/**
	 * 页面数据更新，在开始的时候，需要判断对象是否为空，更安全；在页面刷新时，只需要调这个方法，减少冗余代码
	 */
	protected abstract void refreshView();


	public void hideKeyboard(View v) {
		InputMethodManager imm = ((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE));
		if (imm != null &&getActivity(). getCurrentFocus() != null) {
			imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 初始化标题栏上的左右按钮以及标题Text，同时添加左按钮的关闭事件
	 */
	protected void initTitle() {
		switch (titleType){
		case NoTitle:
			return;
		case DefTitle:
			//TODO
			/*
			ll_left = (LinearLayout) rootView.findViewById(R.id.ll_left);
			iv_left = (ImageView) rootView.findViewById(R.id.iv_left);
			ll_left.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getFragmentManager().popBackStack();
				}
			});

			tv_right = (TextView)rootView.findViewById(R.id.tv_right);
			tv_title = (TextView) rootView.findViewById(R.id.tv_title);
			// tv_title.getPaint().setFakeBoldText(true);// 字体加粗
		
			 */
			return;

		}
	}


	/**
	 * 通信失败，默认弹出失败提示
	 *
	 * @param res
	 *            通信返回的数据
	 */
	public void onFail(BaseModel res) {
		showToast(res.getMessage());
	}

	/**
	 * 通信成功
	 *
	 * @param res
	 *            通信返回的数据
	 */
	public abstract void onSuccess(BaseModel res);






	/**
	 * 通信终止，在有上拉下拉控件时需要
	 *
	 * @param res
	 *            通信返回的数据
	 */
	public void onCancel(BaseModel res) {
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
			mToast = Toast.makeText(this.getActivity(), "", 5000);
		}

		mToast.setText(msg);
		mToast.show();
	}


	/**
	 * 显示提示信息
	 *
	 * @param id
	 */
	public void showToast(int id) {
		if (id==0||id==-1) {
			return;
		}
		if (mToast == null) {
			mToast = Toast.makeText(this.getActivity(), "", 5000);
		}

		mToast.setText(getResString(id));
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
		Intent intent = new Intent(getActivity(), cls);
		if (obj != null) {
			intent.putExtra("data", (Serializable) obj);
		}
		startActivity(intent);
		//		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}

	/**
	 * 带着数据，设置返回码，从本页面跳转到下个页面，重写onActivityResult可以获取从下个页面带回来的数据
	 *
	 * @param cls
	 *            需要跳转到的页面
	 * @param obj
	 *            传递给下个页面的数据
	 * @param requestCode
	 *            返回码
	 */
	public void startActivityForResult(Class<?> cls, Object obj, int requestCode) {
		Intent intent = new Intent(getActivity(), cls);
		if (obj != null) {
			intent.putExtra("data", (Serializable) obj);
		}
		startActivityForResult(intent, requestCode);
		//		overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}
	/**
	 * 简便获取本地的用户数据
	 *
	 * @return
	 */
	protected UserObj getUserData() {
		return (UserObj) PreferencesUtil.getPreferences(getActivity(),
				PreferenceFinals.KEY_USER);
	}

	/**
	 * 重置本地的用户数据
	 *
	 * @param user
	 *            需要重置的用户数据
	 */
	protected void setUserData(UserObj user) {
		PreferencesUtil.setPreferences(getActivity(), PreferenceFinals.KEY_USER, user);
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
	 * 根据id获取View
	 *
	 * @param id
	 * @return View
	 */
	public View findViewById(int id){
		return  rootView.findViewById(id);
	}


	public void displayImage(ImageView imageView,String url){
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		ImageLoader.getInstance().displayImage(url,imageView,options);
	}
	public void displayImage(ImageView imageView,String url,int radius){
		if(url.equals(InterfaceFinals.URL_FILE_HEAD)){
			return;
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(radius))
				.cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中  
				.build();
		ImageLoader.getInstance().displayImage(url,imageView,options);
	}
	
}



