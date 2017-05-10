package com.sptd.net;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;

import com.google.gson.Gson;
import com.sptd.eyun.R;
import com.sptd.eyun.finals.InterfaceFinals;
import com.sptd.eyun.obj.BaseModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.text.TextUtils;

/**
 * 接口通信异步任务
 * 
 * @author lanyan
 * 
 */
@SuppressLint("NewApi")
public class NetAsyncTask<T, E> extends AsyncTask<HashMap<String, String>, Integer, BaseModel<T>> {
	protected ProgressDialog proDialog;
	protected Context mContext;
	protected boolean mIsShow = true, isException = false;
	protected int InfCode = -1;
	protected String msg;
	protected Type mType;
	protected E soureObj;
	protected CallBack<T> callBack;

	public interface CallBack<T> {
		abstract void onCancel(BaseModel<T> result);

		abstract void onSuccess(BaseModel<T> result);

		abstract void onFail(BaseModel<T> result);
	}

	/**
	 * 构造方法，默认显示进度条
	 * 
	 * @param ctx
	 * @param t
	 *            result字段返回的数据类型
	 * @param inf
	 *            接口编码
	 */
	public NetAsyncTask(Context ctx, Type t, int inf, E soureObj, CallBack<T> callBack) {
		this(ctx, t, inf, true, callBack);
		this.soureObj = soureObj;
	}

	/**
	 * 构造方法，手动设置是否需要显示进度条
	 * 
	 * @param ctx
	 * @param t
	 *            result字段返回的数据类型
	 * @param inf
	 *            接口编码
	 * @param isShow
	 *            是否显示进度条
	 */
	public NetAsyncTask(Context ctx, Type t, int inf, boolean isShow, CallBack<T> callBack) {
		mContext = ctx;
		InfCode = inf;
		mType = t;
		mIsShow = isShow;
		this.callBack = callBack;
		msg = mContext.getResources().getString(R.string.net_request);
		HttpUtil.setCancelled(false);
	}

	/**
	 * 自定义设置对话框的提示语，需要在执行exec之前调用
	 * 
	 * @param message
	 */
	public void setDialogMsg(String message) {
		msg = message;
	}

	/**
	 * 后台任务开始执行之前调用，用于进行一些界面上的初始化操作，比 如显示一个进度条对话框等
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		if (mContext != null && mIsShow && !((Activity) mContext).isFinishing()) {
			// can and need to show dialog
			proDialog = new ProgressDialog(mContext);
			proDialog.setMessage(msg);
			proDialog.setCanceledOnTouchOutside(false);
			proDialog.show();
			proDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// 在进度条关闭的时候，取消请求
					onCancelled();
				}
			});
		}
	}

	/**
	 * 处理所有的耗时任 任务一旦完成就可以通过 return 语句来将任务的执行结果返回
	 */
	@Override
	protected BaseModel<T> doInBackground(HashMap<String, String>... params) {
		BaseModel<T> resModel = null;
		String suffix = "";

		switch (InfCode) {
		case InterfaceFinals.INF_MODIFY_REPLY_STATE://
			suffix = "/m/reply/operateReplyById?";
			resModel = postData(suffix, params[0]);
			break;

		}
		return resModel;
	}

	/**
	 * Get方式进行通信
	 * 
	 * @param suffix
	 *            除URL_HEAD外的后缀
	 * @param data
	 *            Get参数
	 * @return
	 */
	protected BaseModel<T> getData(String suffix, HashMap<String, String> data) {
		String params = getParamsByMap(data);
		String res = HttpUtil.httpGet(InterfaceFinals.URL_HEAD + suffix + params);
		return parseJson(res);
	}

	/**
	 * post方式进行通信
	 * 
	 * @param suffix
	 *            除URL_HEAD外的后缀
	 * @param data
	 *            Post参数
	 * @return
	 */
	protected BaseModel<T> postData(String suffix, HashMap<String, String> data) {
		String res = HttpUtil.httpPost(InterfaceFinals.URL_HEAD + suffix, data);
		return parseJson(res);
	}

	/**
	 * post方式提交图片
	 * 
	 * @param suffix
	 * @param data
	 * @param pathMap
	 *            本地路径或url地址
	 * @return
	 */
	protected BaseModel<T> postBitmap(String suffix, HashMap<String, String> data, HashMap<String, String> pathMap) {
		String res = HttpUtil.postFile(InterfaceFinals.URL_HEAD + suffix, data, pathMap);
		return parseJson(res);
	}

	protected BaseModel<T> postFile(String suffix, HashMap<String, String> data, HashMap<String, String> pathMap) {
		String res = HttpUtil.postFile(InterfaceFinals.URL_HEAD + suffix, data, pathMap);
		return parseJson(res);
	}

	protected BaseModel<T> postBitmap1(String suffix, HashMap<String, String> data, HashMap<String, File[]> pathMap) {
		String res = HttpUtil.postFile1(InterfaceFinals.URL_HEAD + suffix, data, pathMap);
		return parseJson(res);
	}

	/**
	 * 返回数据解析
	 * 
	 * @param res
	 * @param model
	 * @return
	 */
	private BaseModel<T> parseJson(String res) {
		BaseModel<T> resModel = new BaseModel<T>();
		resModel.setInfCode(InfCode);
		try {
			if (TextUtils.isEmpty(res)) {// 返回数据为空
				resModel.setMessage(getStringById(R.string.err_net));
			} else if (res.contains("{")) {// 通信成功
				Gson mGson = new Gson();
				resModel = mGson.fromJson(res, mType);
				resModel.setInfCode(InfCode);
			} else {// 通信时的异常返回处理
				if (HttpUtil.ClientException.equals(res)) {
					resModel.setMessage(getStringById(R.string.err_client));
				} else if (HttpUtil.ParseException.equals(res)) {
					resModel.setMessage(getStringById(R.string.err_parse));
				} else if (HttpUtil.IllegalException.equals(res)) {
					resModel.setMessage(getStringById(R.string.err_illeagal));
				} else if (HttpUtil.IOException.equals(res)) {
					resModel.setMessage(getStringById(R.string.err_io));
				} else if (HttpUtil.UnsupportedException.equals(res)) {
					resModel.setMessage(getStringById(R.string.err_unsupport));
				} else {
					resModel.setMessage(getStringById(R.string.err_unknow));
				}
			}
		} catch (Exception e) {
			isException = true;
			e.printStackTrace();
		}

		return resModel;
	}

	/**
	 * 根据string的id获取对应的字符串内容
	 * 
	 * @param resId
	 * @return
	 */
	private String getStringById(int resId) {
		return mContext.getResources().getString(resId);
	}

	/**
	 * 当后台任务执行完毕并通过 return语句进行返回
	 */
	@Override
	protected void onPostExecute(BaseModel<T> result) {
		closeDialog();

		if (!HttpUtil.isCancelled()) {// 未终止
			{
				if (isException) {// 解析异常
					callBack.onCancel(result);
				} else if (result.getCode() == 1) {// success
					callBack.onSuccess(result);
				} else {// fail
					callBack.onFail(result);
				}
			}
		} else {// 终止

			callBack.onCancel(result);

		}

		super.onPostExecute(result);
	}

	/**
	 * 将map转换成字符串
	 * 
	 * @param map
	 * @return
	 */
	protected String getParamsByMap(HashMap<String, String> map) {
		String encode = "UTF-8";
		if (map == null || map.isEmpty()) {
			return "";
		}
		StringBuffer params = new StringBuffer();
		params.append("?");
		for (String key : map.keySet()) {
			try {
				params.append(key).append("=").append(URLEncoder.encode(map.get(key), encode)).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return params.substring(0, params.length() - 1);
	}

	@Override
	protected void onCancelled() {
		closeDialog();
		HttpUtil.setCancelled(true);
		super.onCancelled();
	}

	private void closeDialog() {
		try {
			if (proDialog != null && mIsShow) {
				proDialog.dismiss();
				proDialog = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
