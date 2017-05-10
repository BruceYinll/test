package com.sptd.net;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;

import com.google.gson.Gson;
import com.sptd.eyun.BaseFragment;
import com.sptd.eyun.BaseInteractActivity;
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
import android.util.Log;

/**
 * 接口通信异步任务
 * 
 * @author lanyan
 * 
 */
@SuppressLint("NewApi")
public class BaseAsyncTask<T> extends AsyncTask<HashMap<String, String>, Integer, BaseModel<T>> {
	protected ProgressDialog proDialog;
	protected Context mContext;
	protected boolean mIsShow = true, isException = false;
	protected int InfCode = -1;
	protected String msg;
	protected Type mType;

	protected BaseFragment fragment;
	private boolean isFragment;

	/**
	 * 构造方法，默认显示进度条
	 * 
	 * @param ctx
	 * @param t
	 *            result字段返回的数据类型
	 * @param inf
	 *            接口编码
	 */
	public BaseAsyncTask(Context ctx, Type t, int inf) {
		this(ctx, t, inf, true);
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
	public BaseAsyncTask(Context ctx, Type t, int inf, boolean isShow) {
		mContext = ctx;
		InfCode = inf;
		mType = t;
		mIsShow = isShow;
		msg = mContext.getResources().getString(R.string.net_request);
		HttpUtil.setCancelled(false);
	}

	/**
	 * 构造方法，默认显示进度条
	 *
	 * @param t
	 *            result字段返回的数据类型
	 * @param inf
	 *            接口编码
	 */
	public BaseAsyncTask(BaseFragment baseFragment, Type t, int inf) {
		fragment = baseFragment;
		// 是否添加下面这行
		mContext = fragment.getActivity();
		isFragment = true;
		InfCode = inf;
		mType = t;
		msg = fragment.getResources().getString(R.string.net_request);
		HttpUtil.setCancelled(false);
	}

	public BaseAsyncTask(BaseFragment baseFragment, Type t, int inf, boolean isShow) {
		fragment = baseFragment;
		// 是否添加下面这行
		mContext = fragment.getActivity();
		InfCode = inf;
		isFragment = true;
		mType = t;
		mIsShow = isShow;
		msg = fragment.getResources().getString(R.string.net_request);
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
		if (isFragment && mIsShow) {
			proDialog = new ProgressDialog(fragment.getActivity());
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
		} else {
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
	}

	/**
	 * 处理所有的耗时任 任务一旦完成就可以通过 return 语句来将任务的执行结果返回
	 */
	@Override
	protected BaseModel<T> doInBackground(HashMap<String, String>... params) {
		BaseModel<T> resModel = null;
		String suffix = "";

		switch (InfCode) {
		// case InterfaceFinals.INF_LOGIN://
		// suffix = "phoneIdentCode.html?";
		// resModel = postData(suffix, params[0]);
		// break;

		case InterfaceFinals.INF_FINDSTATIONLIST:// 获得所有站
			suffix = "/m/station/findStationList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_FINDSTATIONLIST_FILTER_NULL:// 获得所有站
			suffix = "/m/userStation/findUserStationListFilterNull?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_FINDUSERSTATIONNESTATUSPAGE:// 获得我的站
			suffix = "/m/station/findStationList?";
			resModel = postData(suffix, params[0]);
			break;

		case InterfaceFinals.INF_FINDUSERSTATIONLIST:// 获得我的 站列表
			suffix = "/m/userStation/findUserStationList?";
			resModel = postData(suffix, params[0]);
			break;

		case InterfaceFinals.INF_FINDDEVICETYPELIST:// 根据stationid获得DeviceTypeList
			suffix = "/m/deviceType/findDeviceTypeList?";
			resModel = postData(suffix, params[0]);
			break;

		case InterfaceFinals.INF_FINDUSERSTATIONLISTNESTATUS:// 获得我的站,不是status的站
			suffix = "/m/userStation/findUserStationListNEStatus?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_UPDATESELFSTATIONS:// 更新我的站
			suffix = "/m/user/updateSelfStations?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_FINDDEVICEBYID:// 根据deviceid获得device数据
			suffix = "/m/device/findDeviceById?";
			resModel = postData(suffix, params[0]);
			break;

		case InterfaceFinals.INF_MODIFYPASSWORD: // 修改密码
			suffix = "/m/user/modifyPassword?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_UPDATESELF: // 修改用户名,用户简介
			suffix = "/m/user/updateSelf?";
			resModel = postData(suffix, params[0]);
			break;

		case InterfaceFinals.INF_GETMOBILEEXISTSYZM: //获取验证码,顺便判断手机号是否注册	
			 suffix = "/m/user/getMobileExistsYzm?";
			 resModel = postData(suffix, params[0]);
			 break;
			
		case InterfaceFinals.INF_APPVERSION:// 版本更新
			suffix = "/m/version/findNewVersion?";
			resModel = postData(suffix, params[0]);
			break;
			
		case InterfaceFinals.INF_UPDATESELFSTATIONSBYMOBILE://关联站3页面申请关联站
			suffix = "/m/user/updateSelfStationsByMobile?";
			resModel = postData(suffix, params[0]);
			break;

		case InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA: // 获取运维列表
			suffix = "/m/question/findQuestionEQStatusPage4ClientStation?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_GET_QUESTION_LIST_LOADDATA: // 获取运维列表
			suffix = "/m/question/findQuestionEQStatusPage4ClientStation?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_GET_QUESTION_LIST_UPDATA_OF_MINE: // 获取我的运维列表
			suffix = "/m/question/findQuestionEQStatusPage4Client?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_GET_QUESTION_LIST_LOADDATA_OF_MINE: // 获取我的运维列表
			suffix = "/m/question/findQuestionEQStatusPage4Client?";
			resModel = postData(suffix, params[0]);
			break;

		case InterfaceFinals.INF_UP_LOAP_OP_PICTURE:// 上传运维图片
			suffix = "/m/question/photoUpload?";
			resModel = postImage(suffix, params[0], params[1]);
			break;
		case InterfaceFinals.INF_UP_LOAP_OP_PICTURE_FOR_REPLY:// 上传回帖图片
			suffix = "/m/reply/photoUpload?";
			resModel = postImage(suffix, params[0], params[1]);
			break;
		case InterfaceFinals.INF_UP_LOAP_OP_VIDEO:// 上传运维video
			suffix = "/m/question/videoUpload?";
			resModel = postFile(suffix, params[0], params[1]);
			break;
		case InterfaceFinals.INF_UP_LOAP_OP_VIDEO_FOR_REPLY:// 上传回帖video
			suffix = "/m/reply/videoUpload?";
			resModel = postFile(suffix, params[0], params[1]);
			break;
		case InterfaceFinals.INF_SAVEA_QUESSION:// 新建运维
			suffix = "/m/question/saveQuestion?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SAVEA_REPLY:// 回帖
			suffix = "/m/reply/saveReply?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SEARCH_REPLY_FOR_QUESTION_ID_UP:// 根据运维id查询
			suffix = "/m/reply/findReplyList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SEARCH_REPLY_FOR_QUESTION_ID_LOAD:// 根据运维id查询
			suffix = "/m/reply/findReplyList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_UPDATA_QUESTION:// 修改运维
			suffix = "/m/question/updateQuestion?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_UPDATA_REPLY:// 修改reply
			suffix = "/m/reply/updateReply?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_MODIFY_QUESTION_STATE:// 修改question状态
			suffix = "/m/question/operateQuestionById?";
			resModel = postData(suffix, params[0]);
			break;

		// --------------------mran----------------------
		case InterfaceFinals.INF_VERIFYCODE:// 获取验证码
			suffix = "/m/user/getMobileYzm?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_LOADPAGE:// 加载页
			suffix = "/m/loadHomePage/findLastestLoadHomePage?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_PLATINFO:// 平台协议
			suffix = "/m/platinfo/getPlatInfo?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_COMMENT:// 评论列表
			suffix = "/m/comment/findCommentListByPictureId?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_ATTENTION:// 添加关注
			suffix = "/m/interestUser/saveInterestUser?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SUBJECT:// 专题列表
			suffix = "/m/subject/findSubjectVoList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_PRAISE:// 点赞
			suffix = "/m/praise/savePraise?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_CANCELPRAISE:// 取消赞
			suffix = "/m/praise/cancelPraise?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SAVECOMMENT:// 评论
			suffix = "/m/comment/saveComment?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_TRANSFER:// 转发
			suffix = "/m/transfer/saveTransfer?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_INTERESTUSER:// 选择@的人
			suffix = "/m/interestUser/findMyInterestUserList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SUBJECTDETAIL:// 获取专题对应图片信息
			suffix = "/m/picture/findPictureBySubjectId?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_POSETYPE:// 查询pose库分类
			suffix = "/m/publicType/findPublicTypeListByType?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_POSE:// 查询类型对应的Pose库
			suffix = "/m/poseLibrary/findPoseLibraryListByType?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_MEMBERRECOMMEND:// 根据定位返回按距离倒序的达人列表
			suffix = "/m/member/findFamousPersonByLonLat?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_FRIENDS:// 获取我关注的人发布的图片清单
			suffix = "/m/picture/findPictureListByInterestUserId?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_CANCELINTEREST:// 取消关注
			suffix = "/m/interestUser/cancelInterestUser?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_PRAISEME:// 获取对我点赞的信息
			suffix = "/m/praise/findPraiseListByMemberId?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_COMMENTME:// 获取评论我的信息
			suffix = "/m/comment/findCommentListByMemberId?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_ATME:// 获取@我的信息
			suffix = "/m/atUser/findAtUserListByMemberId?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_GETMYINFO:// 获取个人信息
			suffix = "/m/member/findMemberInfoById?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_GETSYSMSG:// 获取系统消息
			suffix = "/m/member/findMemberInfoById?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_ODERVO:// 获取订单列表
			suffix = "/m/order/findMyOrderList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_ORDERDETAIL:// 获取订单详情
			suffix = "/m/order/findOrderById?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_PUBLISH:// 我发表的
			suffix = "/m/member/findMyDeployedPictureList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_FENSI:// 获取关注我的用户清单
			suffix = "/m/interestUser/findConernMyInterestUserList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_GUANZHU:// 获取我关注的用户清单
			suffix = "/m/interestUser/findMyInterestUserList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SYSMSGLIST:// 查询所有系统消息表
			suffix = "/m/sysMessage/findSysMessageList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_MYMEMBER:// 我的记忆
			suffix = "/m/picture/findMyPictureList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_PHOTOUPLOAD:// 上传头像
			suffix = "/m/user/photoUpload?";
			resModel = postBitmap(suffix, params[0], params[1]);
			break;
		case InterfaceFinals.INF_EDITUSERINFO:// 编辑用户信息
			suffix = "/m/member/updateMember?";
			resModel = postData(suffix, params[0]);
			break;

		case InterfaceFinals.INF_ORDERMAKING:// 直接下单制作的图片
			suffix = "/m/picture/orderBeforePublicPicture?";
			resModel = postBitmap(suffix, params[0], params[1]);
			break;
		case InterfaceFinals.INF_ORDERAFTER:// 发布后下单
			suffix = "/m/order/orderAfterPublicPicture?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_WXPLACEORDER:// 微信支付回调
			suffix = "/m/order/wxPlaceOrder?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_REORDER:// 已有订单重新下单
			suffix = "/m/order/resetOrderPicture?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_MEMBER_PICTURES:// 获取我关注的人发布的图片清单
			suffix = "/m/picture/findPictureListByInterestUserId?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_UNREADNEWS:// 查询未读取的消息数量
			suffix = "/m/sysMessage/findSysMessageNotReadNumber?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_FINDMSG:// 用户查看消息内容接口
			suffix = "/m/sysMessage/findSysMessageById?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_CONTACTS:// 获取手机通讯录好友信息
			suffix = "/m/member/checkUserInfoByContactList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_DELETEPICTURE:// 删除图片
			suffix = "/m/picture/deletePitures?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SAVEMARKER:// 新增制作人地址信息表
			suffix = "/m/maker/saveMaker?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_UPDATEMARKER:// 新增制作人地址信息表
			suffix = "/m/maker/updateMaker?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_REPORT:// 举报
			suffix = "/m/accusation/saveAccusation?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_REGIST:// 注册
			suffix = "/m/user/registerUser?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SEARCH:// 用户搜索
			suffix = "/m/member/findMemberByNickName?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.TANSFER:// 转发
			suffix = "/m/transfer/findMyTransferPictureList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.THIRDLOGIN:// 三方登录
			suffix = "/m/member/thirdLoginApp?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.HASID_BEFOR_ORDER:// 已经发布后需要下单时,调用此接口返回可下单信息
			suffix = "/m/picture/toOrderPictureById?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SUREORDER:// 确认收货
			suffix = "/m/order/confirmReceiveOrderById?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_CANCELORDER:// 取消订单
			suffix = "/m/order/cancelOrderById?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_DELETEORDER:// 删除订单
			suffix = "/m/order/deleteOrderById?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_REFUNDORDER:// 申请退款
			suffix = "/m/order/refundOrder?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_CITYLIST:// 城市列表
			suffix = "/m/city/findCityList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_PUBLISHOFMINE:// 获取我发布的最新的图片(前3张或其他分页)
			suffix = "/m/member/findMyDeployedPictureList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_ATTENTIONALL:// 全部关注
			suffix = "/m/interestUser/saveAllInterestUser?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_CANCELATTENTIONALL:// 全部取消关注
			suffix = "/m/interestUser/cancelAllInterestUser?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_SHAREAFTER:// 分享成功
			suffix = "/m/picture/sharePicture?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_RECOMMENDMEMBER:// 智能推荐
			suffix = "/m/member/recommendMember?";
			resModel = postData(suffix, params[0]);
			break;

		// ----------------chiw-------------------
		case InterfaceFinals.INF_LOGIN:// 登录
			suffix = "/m/user/login?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_MODIFY: // 忘记密码
			suffix = "/m/user/resetPassword?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_THIRDLOGIN:// 三方登录
			suffix = "/m/member/thirdLoginApp?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_HOT:// 获取热门图片、视频、电子相册清单
			suffix = "/m/picture/findPopularPictureList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_PICTUREDETAIL:// 获取照片详情
			suffix = "/m/picture/findPictureDetailInfo?";
			resModel = postData(suffix, params[0]);
			break;

		// ----------------sunbo-------------------
		case InterfaceFinals.INF_SENDFEEDBACK:// 发送反馈信息
			suffix = "/m/suggestion/sendSuggestion?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_COMPLETEPERSONINFO:// 完善个人信息
			suffix = "/m/member/completeInfo?";
			resModel = postData(suffix, params[0]);
			break;
		// ----------------zgt-------------------
		case InterfaceFinals.INF_FRAGMENT:// 相框
			suffix = "/m/pictureFrame/findPictureFrameList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_PUBLICSHOW:// 发布
			suffix = "/m/picture/publicPicture?";
			resModel = postBitmap(suffix, params[0], params[1]);
			break;
		case InterfaceFinals.INF_FINDABLUM:// 所有相册
			suffix = "/m/album/findAlbumList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_MAKEABLUM:// 相册
			suffix = "/m/album/findAlbumVoById?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_ALBUMMUSIC:// 相册背景音乐
			suffix = "/m/albumMusic/findAlbumMusicList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_HOTJIGSAW:// 热门海报
			suffix = "/m/posterPicture/findTopPosterPictureList?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_ADDPOSE:// 加入pose库
			suffix = "/m/poseLibrary/savePoseLibrary?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_BACKGRAND:// 自由拼图背景素材
			suffix = "/m/frame/findFrameListByType?";
			resModel = postData(suffix, params[0]);
			break;
		case InterfaceFinals.INF_ABLUMPUBLISH:// 相册 发布
			suffix = "/m/picture/publicPictureAlbum?";
			resModel = postBitmap(suffix, params[0], params[1]);
			break;
		case InterfaceFinals.INF_MAKEAFTERPUBLISH:// 制作后发布
			suffix = "/m/picture/publicAfterOrderPicture?";
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

	protected BaseModel<T> postImage(String suffix, HashMap<String, String> data, HashMap<String, String> pathMap) {
		String res = HttpUtil.postImage(InterfaceFinals.URL_HEAD + suffix, data, pathMap);
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
		if (res == null) {
			Log.v("res", res + "空么null");
		}
		Log.v("res", res + "空么1");
		try {
			if (TextUtils.isEmpty(res)) {// 返回数据为空
				Log.v("res", "执行了1");
				resModel.setMessage(getStringById(R.string.err_net));
				Log.v("res", "执行了2");
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
			if (isFragment) {
				if (isException) {// 解析异常
					((BaseFragment) fragment).onCancel(result);
				} else if (result.getCode() == 1) {// success
					((BaseFragment) fragment).onSuccess(result);
				} else {// fail
					((BaseFragment) fragment).onFail(result);
				}
			} else {
				if (isException) {// 解析异常
					((BaseInteractActivity) mContext).onCancel(result);
				} else if (result.getCode() == 1) {// success
					((BaseInteractActivity) mContext).onSuccess(result);
				} else {// fail
					((BaseInteractActivity) mContext).onFail(result);
				}
			}
		} else {// 终止
			if (isFragment) {
				((BaseFragment) fragment).onCancel(result);
			} else {
				((BaseInteractActivity) mContext).onCancel(result);
			}
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
//		HttpUtil.setCancelled(true);
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
