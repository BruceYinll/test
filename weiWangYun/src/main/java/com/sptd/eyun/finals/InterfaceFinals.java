package com.sptd.eyun.finals;

import android.R.integer;

/**
 * 网络通信常量类，只存放服务器信息以及接口编码
 * 
 * @author Lanyan
 * 
 */
public class InterfaceFinals {
	// --------------服务器信息-----------------------
	//public static final String URL_HEAD = "http://www.3tixa.com:10000/power
//		public static final String URL_HEAD = "http://show.3tichina.com:8004/power";
		public static final String URL_HEAD = "http://114.55.236.86:8080/power";

//    public static final String URL_HEAD = "http://www.3tixa.com:20000/power";
	// public static final String URL_HEAD = "http://192.168.1.116:8080/power";
//	public static final String URL_HEAD = "http://192.168.0.167:8080/power";
	// public static final String URL_HEAD = "http://192.168.1.129:8080/power";
	// public static final String URL_HEAD =
	// "http://www.3tixa.com:28088/weidianzhan";
	// public static final String URL_HEAD =
	// "http://120.27.145.49:8080/jiyiqiang";
	// public static final String URL_FILE_HEAD =
	// "http://120.27.145.49:8080/jiyiqiang/";
	public static final String URL_FILE_HEAD = "http://www.3tixa.com:28088/weidianzhan/";//似乎没用到
	// --------------服务器基本信息-----------------------

	// ----------上海电站--------------
	public static final int INF_FINDSTATIONLIST = 200;// 获得所有电站
	public static final int INF_FINDSTATIONLIST_FILTER_NULL = 199;// 获得所有电站
	//
	public static final int INF_FINDUSERSTATIONNESTATUSPAGE = 201;// 获得我的站
	public static final int INF_FINDUSERSTATIONLIST = 202;// 获得我的站列表
	public static final int INF_FINDDEVICETYPELIST = 203;// 根据stationid获得DeviceTypeList

	public static final int INF_FINDUSERSTATIONLISTNESTATUS = 204;// 获得我的站,不是status的站
	public static final int INF_UPDATESELFSTATIONS = 205;// 更新我的站

   public static final int INF_FINDDEVICEBYID=206;//根据deviceid获得device数据
   
	public static final int INF_MODIFYPASSWORD = 301;// 修改密码
	// public static final int INF_MODIFYUSERNAME=205;//修改用户名
	public static final int INF_UPDATESELF = 302;// 修改用户名,用户简介
	
	public static final int INF_GETMOBILEEXISTSYZM=303;//获取验证码,顺便判断手机号是否注册
	
	public static final int INF_APPVERSION = 37; // 版本更新

	public static final int INF_UPDATESELFSTATIONSBYMOBILE=304; //关联站3页面申请关联站
	
	// ----------------mran--------------------------
	public static final int INF_VERIFYCODE = 2; // 获取验证码（注册）
	public static final int INF_REGIST = 3; // （注册）
	public static final int INF_THIRDLOGIN = 4; // 三方登录
	public static final int INF_HOT = 5; // 获取热门图片、视频、电子相册清单
	public static final int INF_PICTUREDETAIL = 6; // 获取照片详情
	public static final int INF_LOADPAGE = 7; // 加载页
	public static final int INF_PLATINFO = 8; // 平台协议
	public static final int INF_COMMENT = 9; // 评论表
	public static final int INF_ATTENTION = 10; // 添加关注
	public static final int INF_SUBJECT = 11; // 专题列表
	public static final int INF_PRAISE = 12; // 点赞
	public static final int INF_CANCELPRAISE = 13; // 取消赞
	public static final int INF_SAVECOMMENT = 14; // 评论
	public static final int INF_TRANSFER = 15; // 转发
	public static final int INF_INTERESTUSER = 16; // 选择@的人
	public static final int INF_SUBJECTDETAIL = 17; // 获取专题对应图片信息
	public static final int INF_POSETYPE = 18; // 查询pose库分类
	public static final int INF_POSE = 19; // 查询类型对应的Pose库
	public static final int INF_MEMBERRECOMMEND = 20; // 根据定位返回按距离倒序的达人列表
	public static final int INF_FRIENDS = 21; // 获取我关注的人发布的图片清单
	public static final int INF_CANCELINTEREST = 22; // 取消关注
	public static final int INF_PRAISEME = 23; // 获取对我点赞的信息
	public static final int INF_COMMENTME = 24; // 获取评论我的信息
	public static final int INF_ATME = 25; // 获取@我的信息
	public static final int INF_GETMYINFO = 26; // 获取个人信息
	public static final int INF_GETSYSMSG = 27; // 获取系统消息
	public static final int INF_ODERVO = 28; // 订单列表
	public static final int INF_ORDERDETAIL = 29; // 订单详情
	public static final int INF_PUBLISH = 30; // 我发表的
	public static final int INF_FENSI = 31; // 获取关注我的用户清单
	public static final int INF_GUANZHU = 32; // 获取我关注的用户清单
	public static final int INF_SYSMSGLIST = 33; // 查询所有系统消息表
	public static final int INF_MYMEMBER = 34; // 我的记忆
	public static final int INF_PHOTOUPLOAD = 35; // 上传头像
	public static final int INF_EDITUSERINFO = 36; // 编辑用户信息

	public static final int INF_ORDERMAKING = 38; // 直接下单制作的图片
	public static final int INF_ORDERAFTER = 39; // 发布后下单
	public static final int INF_WXPLACEORDER = 40; // 微信支付回调
	public static final int INF_REORDER = 41; // 已有订单重新下单
	public static final int INF_MEMBER_PICTURES = 42; // 获取我关注的人发布的图片清单
	public static final int INF_UNREADNEWS = 43; // 查询未读取的消息数量
	public static final int INF_FINDMSG = 44; // 用户查看消息内容接口
	public static final int INF_CONTACTS = 45; // 获取手机通讯录好友
	public static final int INF_DELETEPICTURE = 46; // 删除图片
	public static final int INF_SAVEMARKER = 47; // 新增制作人地址信息表
	public static final int INF_UPDATEMARKER = 48; // 修改制作人地址信息表
	public static final int INF_REPORT = 49; // 举报
	public static final int INF_SEARCH = 50; // 用户搜索
	public static final int TANSFER = 51; // 转发
	public static final int THIRDLOGIN = 52; // 三方登录
	public static final int HASID_BEFOR_ORDER = 53; // 已经发布后需要下单时,调用此接口返回可下单信息
	public static final int INF_SUREORDER = 54; // 确认收货
	public static final int INF_CANCELORDER = 55; // 取消订单
	public static final int INF_DELETEORDER = 56; // 删除订单
	public static final int INF_REFUNDORDER = 57; // 申请退款
	public static final int INF_CITYLIST = 58; // 城市列表
	public static final int INF_ATUSER = 59; // 新增@信息
	public static final int INF_PUBLISHOFMINE = 60; // 获取我发布的最新的图片(前3张或其他分页)
	public static final int INF_ATTENTIONALL = 61; // 全部关注
	public static final int INF_CANCELATTENTIONALL = 62; // 全部取消关注
	public static final int INF_SHAREAFTER = 63; // 分享成功
	public static final int INF_RECOMMENDMEMBER = 64; // 智能推荐

	// ------------------chiw-----------------------
	public static final int INF_LOGIN = 100; // 登录
	public static final int INF_MODIFY = 101;// 修改密码
	// ------------------zgt-----------------------
	public static final int INF_FRAGMENT = 70; // 相框
	public static final int INF_PUBLICSHOW = 71; // 发布
	public static final int INF_FINDABLUM = 72; // 查询相册
	public static final int INF_MAKEABLUM = 73; // 相册
	public static final int INF_ALBUMMUSIC = 74; // 相册背景音乐
	public static final int INF_HOTJIGSAW = 75; // 热门海报
	public static final int INF_ADDPOSE = 76; // 加入pose库
	public static final int INF_BACKGRAND = 77; // 自由拼图背景素材
	public static final int INF_ABLUMPUBLISH = 78; // 相册发布
	public static final int INF_MAKEALBUM = 79; // 制作相册下单
	public static final int INF_MAKEAFTERPUBLISH = 80; // 制作后发布
	// ------------------sunbo-----------------------
	public static final int INF_SENDFEEDBACK = 151; // 发送反馈信息
	public static final int INF_COMPLETEPERSONINFO = 152; // 完善个人信息

	/**
	 * 运维
	 */
	public static final int INF_GET_QUESTION_LIST_UPDATA_OF_MINE = 174; // 获取我的运维列表
	public static final int INF_GET_QUESTION_LIST_LOADDATA_OF_MINE = 175; // 获取我的运维列表
	
	public static final int INF_GET_QUESTION_LIST_UPDATA = 160; // 获取运维列表
	public static final int INF_GET_QUESTION_LIST_LOADDATA = 161; // 获取运维列表
	public static final int INF_UP_LOAP_OP_PICTURE = 162; // 上传运维图片
	public static final int INF_UP_LOAP_OP_VIDEO = 163; // 上传运维video
	public static final int INF_SAVEA_QUESSION = 164; // 保存运维
	public static final int INF_SEARCH_REPLY_FOR_QUESTION_ID_UP = 165; // 保存运维
	public static final int INF_SEARCH_REPLY_FOR_QUESTION_ID_LOAD = 166; // 保存运维

	public static final int INF_UP_LOAP_OP_PICTURE_FOR_REPLY = 167; // 上传回帖图片
	public static final int INF_UP_LOAP_OP_VIDEO_FOR_REPLY = 168; // 上传回帖video
	public static final int INF_SAVEA_REPLY = 169; // 保存回帖
	public static final int INF_MODIFY_REPLY_STATE = 170; // 修改reply状态
	public static final int INF_MODIFY_QUESTION_STATE = 171; // 修改question状态
	
	public static final int INF_UPDATA_QUESTION = 172; // 修改运维
	public static final int INF_UPDATA_REPLY = 173; // 修改reply
}
