package com.sptd.eyun.ui.fragment;

import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.reflect.TypeToken;
import com.sptd.eyun.BaseFragment;
import com.sptd.eyun.R;

import com.sptd.eyun.dialog.ApplyDialog;
import com.sptd.eyun.dialog.KefuPhoneDialog;
import com.sptd.eyun.dialog.LoginOutDialog;
import com.sptd.eyun.dialog.VersionUpdateDialog;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.finals.PreferenceFinals;
import com.sptd.eyun.obj.BaseModel;
import com.sptd.eyun.obj.UserObj;
import com.sptd.eyun.obj.Version;
import com.sptd.eyun.service.UpdateService;
import com.sptd.eyun.ui.loginregister.LoginActivity;
import com.sptd.eyun.ui.loginregister.RegisterActivity;
import com.sptd.eyun.ui.myinfo.MyinfoActivity;
import com.sptd.eyun.ui.myinfo.MyyunweiActivity;
import com.sptd.log.Log;
import com.sptd.net.BaseAsyncTask;
import com.sptd.util.resource.PreferencesUtil;

import com.umeng.analytics.MobclickAgent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 更多页面
 * 
 * @author ldy
 *
 */
public class MoreFragment extends BaseFragment implements OnClickListener {

	// 我的运维,客服电话,版本,退出登录
	private LinearLayout ll_myinfo, ll_myyunwei, ll_kefuphone, ll_version, ll_loginout;

	private UserObj userObj;// 用户
	private ImageView iv_head;
	private TextView tv_userName;
	private TextView tv_mobile;

	private KefuPhoneDialog kefuPhoneDialog;
	private TextView kefuPhoneDialog_quxiao;
	private TextView kefuPhoneDialog_queding;

	private VersionUpdateDialog versionUpdateDialog;
	private TextView update_quxiao;
	private TextView update_queding;
	private TextView tv_version;
	private String versionName;
	private Integer versionCode;
	private Version app;

	private LoginOutDialog loginOutDialog;
	private TextView loginOutDialog_quxiao;
	private TextView loginOutDialog_queding;

	public MoreFragment() {
		super(R.layout.fragment_more, NoTitle);
	}

	/*
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle savedInstanceState) { View
	 * view=inflater.inflate(R.layout.fragment_ceshi, container, false);
	 * TextView textView=(TextView) view.findViewById(R.id.textView1);
	 * textView.setText("morefragment"); return view; }
	 */
	@Override
	protected void getData() {
		userObj = getUserData();

	}

	@Override
	protected void findView() {
		ll_myinfo = (LinearLayout) findViewById(R.id.ll_myinfo);
		ll_myinfo.setOnClickListener(this);
		ll_myyunwei = (LinearLayout) findViewById(R.id.ll_myyunwei);
		ll_myyunwei.setOnClickListener(this);
		ll_kefuphone = (LinearLayout) findViewById(R.id.ll_kefuphone);
		ll_kefuphone.setOnClickListener(this);
		ll_version = (LinearLayout) findViewById(R.id.ll_version);
		ll_version.setOnClickListener(this);

		tv_version = (TextView) findViewById(R.id.tv_version);

		ll_loginout = (LinearLayout) findViewById(R.id.ll_loginout);
		ll_loginout.setOnClickListener(this);

		iv_head = (ImageView) findViewById(R.id.iv_head);
		tv_userName = (TextView) findViewById(R.id.tv_userName);
		tv_mobile = (TextView) findViewById(R.id.tv_mobile);
	}

	/*
	 * 查询当前系统版本号
	 */
	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getActivity().getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
		String version = packInfo.versionName;
		versionCode=packInfo.versionCode;
		return version;
	}

	@Override
	protected void refreshView() {
		userObj = getUserData();
		Log.v("touxiang", userObj.getHead());
		if (userObj.getHead() != null && !"".equals(userObj.getHead())) {
			Log.v("head-1", InterfaceFinals.URL_HEAD + userObj.getHead());
			displayImage(iv_head, InterfaceFinals.URL_HEAD + userObj.getHead(), 76);
		} else {
			iv_head.setImageResource(R.drawable.touxiang);
		}
		tv_userName.setText("用户名: " + userObj.getUserName());
		tv_mobile.setText("手机号码: " + userObj.getMobile());
		try {
			versionName = getVersionName();
			tv_version.setText(versionName);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onSuccess(BaseModel res) {
		switch (res.getInfCode()) {
		case InterfaceFinals.INF_APPVERSION:// 版本更新
			app = (Version) res.getObject();
			isUpdate();
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_myinfo:
			startActivity(MyinfoActivity.class);
			break;

		case R.id.ll_myyunwei:
			startActivity(MyyunweiActivity.class);
			break;
		case R.id.ll_kefuphone:
			kefuPhoneDialog = new KefuPhoneDialog(getActivity(), R.style.MyDialog);
			kefuPhoneDialog.setCancelable(false);
			kefuPhoneDialog.show();
			kefuPhoneDialog_quxiao = kefuPhoneDialog.getQuxiao();
			kefuPhoneDialog_queding = kefuPhoneDialog.getQueding();
			if (kefuPhoneDialog_quxiao == null) {
				Log.v("tuichu", "null");
			}
			kefuPhoneDialog_quxiao.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					kefuPhoneDialog.dismiss();

				}
			});
			kefuPhoneDialog_queding.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					kefuPhoneDialog.dismiss();
					Uri uri = Uri.parse("tel:10086");
					Intent intent = new Intent(Intent.ACTION_CALL, uri);
					startActivity(intent);
				}
			});
			break;
		case R.id.ll_version:
			try {
				updateVersionInfo();
				// isUpdate();
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;
		case R.id.ll_loginout:
			loginOutDialog = new LoginOutDialog(getActivity(), R.style.MyDialog);
			loginOutDialog.setCancelable(false);
			loginOutDialog.show();
			loginOutDialog_quxiao = loginOutDialog.getLoginout_quxiao();
			loginOutDialog_queding = loginOutDialog.getLoginout_queding();
			loginOutDialog_quxiao.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					loginOutDialog.dismiss();

				}
			});
			loginOutDialog_queding.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					loginOutDialog.dismiss();
					PreferencesUtil.remove(getActivity(), PreferenceFinals.KEY_USER);
					startActivity(LoginActivity.class);

				}
			});

			break;
		}

	}

	private void isUpdate() {
		if (Integer.parseInt(app.getNumber())<=versionCode) {
			showToast("已经是最新版本");
		} else {
			versionUpdateDialog = new VersionUpdateDialog(getActivity(), R.style.MyDialog);
			versionUpdateDialog.setCancelable(false);
			versionUpdateDialog.show();
			update_quxiao = versionUpdateDialog.getUpdate_quxiao();
			update_queding = versionUpdateDialog.getUpdate_queding();
			update_quxiao.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					versionUpdateDialog.dismiss();

				}
			});
			update_queding.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					versionUpdateDialog.dismiss();
//					if (!TextUtils.isEmpty(app.getUrl()) && !TextUtils.isEmpty(app.getApk())) {
					if (!TextUtils.isEmpty(app.getApk())) {
						Intent intent = new Intent();
                        intent.putExtra("urlapk", InterfaceFinals.URL_HEAD + app.getApk());
                        intent.setClass(getActivity(), UpdateService.class);
                        getActivity().startService(intent);
						
					}else{
						showToast("地址有误,更新出错");
					}

				}
			});
		}

		/*
		 * AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		 * builder.setMessage("是否更新最新版本?") .setCancelable(false)
		 * .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int id) { if
		 * (versionName.equals(app.getName())) { showToast("已经是最新版本"); }else {
		 * if (!TextUtils.isEmpty(app.getUrl())) { Uri uri =
		 * Uri.parse(app.getUrl()); Intent intent = new
		 * Intent(Intent.ACTION_VIEW,uri); startActivity(intent); }
		 * 
		 * } } }) .setNegativeButton("取消", new DialogInterface.OnClickListener()
		 * { public void onClick(DialogInterface dialog, int id) {
		 * dialog.cancel(); } }).show(); AlertDialog alert = builder.create();
		 */
	}

	/**
	 * 版本更新
	 * 
	 * @throws Exception
	 */
	private void updateVersionInfo() throws Exception {
		Type t = new TypeToken<BaseModel<Version>>() {
		}.getType();
		BaseAsyncTask<Version> task = new BaseAsyncTask<Version>(this, t, InterfaceFinals.INF_APPVERSION, true);
		HashMap<String, String> params = new HashMap<String, String>();
		// params.put("userDevice", "1");
		// params.put("appVersion", getVersionName());
		task.execute(params);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("MoreFragment");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshView();
		MobclickAgent.onPageStart("MoreFragment"); // 统计页面，"MainScreen"为页面名称，可自定义
	}

}
