package com.sptd.eyun;

import com.sptd.eyun.obj.BaseModel;


/**
 * 含网络通信的基本Activity类
 * 
 * @author lanyan
 */
@SuppressWarnings("rawtypes")
public abstract class BaseInteractActivity extends BaseActivity {
	/**
	 * 构造方法，默认为有公用title
	 * 
	 * @param resId
	 *            布局id
	 */
	public BaseInteractActivity(int resId) {
		super(resId);
	}

	/**
	 * 构造方法，手动设置有无公用title
	 * 
	 * @param resId
	 *            布局id
	 * @param hasTitle
	 *            是否显示Title
	 */
	public BaseInteractActivity(int resId, boolean hasTitle) {
		super(resId, hasTitle);
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
	
	public  void onSuccess(BaseModel res ,Object obj){
		
	}

	/**
	 * 通信终止，在有上拉下拉控件时需要
	 * 
	 * @param res
	 *            通信返回的数据
	 */
	public void onCancel(BaseModel res) {
	}
}
