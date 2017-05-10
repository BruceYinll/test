package com.sptd.eyun.obj;

/**
 * @Title: XaConstant.java（基础）
 * @Description: 常量类
 * @author hchen
 * @date 2014年8月2日 上午10:07:48
 * @version V1.0
 */
public class XaConstant {

	/**
	 * @ClassName: UserStatus
	 * @Description: 后台用户状态常量
	 * @author hchen
	 * @date 2014年8月2日 上午10:10:09
	 */
	public static class UserStatus {
		/**
		 * @Fields status_normal : 正常
		 */
		public static final int status_normal = 1;
		/**
		 * @Fields status_lock : 锁定或禁用
		 */
		public static final int status_lock = 0;
		/**
		 * @Fields status_delete : 已删除
		 */
		public static final int status_delete = 2;

	}

	/**
	 * @ClassName: RoleStatus
	 * @Description: 角色的状态
	 * @author hchen
	 * @date 2014年8月2日 上午10:53:08
	 *
	 */
	public static class RoleStatus {
		/**
		 * @Fields status_normal : 正常
		 */
		public static final int status_normal = 1;
		/**
		 * @Fields status_delete : 已删除
		 */
		public static final int status_delete = 0;
	}

	/**
	 * @ClassName: ResourcesStatus
	 * @Description: 资源的状态
	 * @author hchen
	 * @date 2014年8月2日 上午11:14:46
	 *
	 */
	public static class ResourcesStatus {
		/**
		 * @Fields status_normal : 正常
		 */
		public static final int status_normal = 1;
		/**
		 * @Fields status_delete : 已删除
		 */
		public static final int status_delete = 0;
	}

	/**
	 * @ClassName: ResourceShowType
	 * @Description: 资源级别
	 * @author hchen
	 * @date 2014年8月8日 下午2:23:32
	 *
	 */
	public static class ResourceShowType {
		/**
		 * @Fields page_level : 页面级资源
		 */
		public static final int page_level = 0;
		/**
		 * @Fields menu_level : 菜单级资源
		 */
		public static final int menu_level = 2;
		/**
		 * @Fields button_level : 按钮级资源
		 */
		public static final int button_level = 1;
	}

	public static class SessionKey {

		/**
		 * @Fields currentUser : session中存放和获取当前用户信息的key
		 */
		public static final String currentUser = "currentUser";

		/**
		 * @Fields currentMenuData : session中存放和获取当前用户菜单的key
		 */
		public static final String currentMenuData = "currentMenuData";

		/**
		 * @Fields currentMessageList : 发消息的队列
		 */
		public static final String currentMessageList = "messageUserList";
	}

	/**
	 * @ClassName: Status
	 * @Description: 常用状态类型
	 * @author hchen
	 * @date 2014年8月13日 下午2:45:25
	 *
	 */
	public static class Status {
		/**
		 * @Fields locked : 锁定状态
		 */
		public static final int locked = -1;

		/**
		 * @Fields invalid : 无效状态
		 */
		public static final int invalid = 0;

		/**
		 * @Fields valid : 有效/正常状态
		 */
		public static final int valid = 1;

		/**
		 * @Fields publish : 发布
		 */
		public static final int publish = 2;

		/**
		 * @Fields delete : 删除
		 */
		public static final int delete = 3;

		/**
		 * @Fields publish : 预发布
		 */
		public static final int prePublish = 4;
	}

	/**
	 * @author hchen 上下架状态
	 *
	 */
	public static class onStatus {
		/**
		 * 下架
		 */
		public static final int down = 0;
		/**
		 * 上架
		 */
		public static final int up = 1;
		/**
		 * 预上架
		 */
		public static final int beforup = 2;
	}

	public static class AuditStatus {
		/**
		 * @Fields noAudit : 待审核
		 */
		public static final int noAudit = 0;

		/**
		 * @Fields preAudit : 待审核
		 */
		public static final int preAudit = 2;

		/**
		 * @Fields noPassAudit : 审核不通过
		 */
		public static final int noPassAudit = -1;

		/**
		 * @Fields preAudit : 已审核
		 */
		public static final int audited = 1;
	}

	/**
	 * @ClassName: TreeNodeIcon
	 * @Description: 节点类型
	 * @author hchen
	 * @date 2014年8月13日 下午1:59:01
	 *
	 */
	public static class TreeNodeIcon {
		/**
		 * @Fields html_24 : 24*24的页面级菜单图片
		 */
		public static final String html_24 = "image/html_24.png";
		/**
		 * @Fields menu_24 : 24*24的菜单级菜单图片
		 */
		public static final String menu_24 = "image/menu_24.png";
		/**
		 * @Fields action_24 : 24*24的按钮级菜单图片
		 */
		public static final String action_24 = "image/action_24.png";
	}

	/**
	 * @ClassName: Code
	 * @Description: xaResult的code
	 * @author hchen
	 * @date 2014年8月15日 下午5:34:20
	 *
	 */
	public static final class Code {
		/**
		 * @Fields ok : 成功
		 */
		public static final int success = 1;
		/**
		 * @Fields error : 失败
		 */
		public static final int error = 0;
		/**
		 * token失效
		 */
		public static final int code_failure_token = 101;
		/**
		 * fresh_token失效
		 */
		public static final int code_failure_fresh_token = 102;

		public static final int code_system_failure = 10001;

		public static final int code_business_failure = 20001;

		public static final int code_validation_failure = 30001;

		public static final int code_database_failure = 40001;

		public static final int code_json_failure = 50001;
	}

	/**
	 * @ClassName: Message
	 * @Description: xaResult的message
	 * @author hchen
	 * @date 2014年8月15日 下午5:34:20
	 *
	 */
	public static final class Message {
		/**
		 * @Fields ok : 成功
		 */
		public static final String success = "成功!";
		/**
		 * @Fields error : 失败
		 */
		public static final String error = "系统错误!";
		public static final String object_not_find = "找不到要操作的记录!";
	}

	/**
	 * 登录请求
	 * 
	 * @author zj
	 *
	 */
	public static class Login {
		/**
		 * web登录请求
		 */
		public static final int REQUEST_TYPE_WEB = 0;
		/**
		 * 手机终端登录请求,可根据需要更改为其他如wap登录请求等
		 */
		public static final int REQUEST_TYPE_MOBILE = 1;
		/**
		 * login.html登录请求时请求方式的参数,和j_username,j_password一样不可指定其他名称
		 */
		public static final String SPRING_REQUEST_KEY = "SPRING_REQUEST_KEY";
	}

	public static class Lock {

		public static final int lock = 1;

		public static final int noLock = 0;
	}

	/**
	 * 图片类型
	 *
	 * @author sjm
	 */
	public static class pictureType {

		/**
		 * 电站图片
		 */
		public static final Integer station = 1;
		/**
		 * 维护图片
		 */
		public static final Integer question = 2;
		/**
		 * 回复图片
		 */
		public static final Integer reply = 3;

	}

	/**
	 * 文件类型
	 *
	 * @author sjm
	 */
	public static class fileType {
		/**
		 * 视频文件
		 */
		public static final Integer picture = 1;
		/**
		 * 上年审计结果
		 */
		public static final Integer movie = 2;
	}
	
	
	/**
	 * 站点类型
	 *
	 * @author sjm
	 */
	public static class stationType {
		/**
		 * 待授权
		 */
		public static final Integer wait = 1;
		/**
		 * 已经授权
		 */
		public static final Integer ok = 2;
		/**
		 * 删除
		 */
		public static final Integer del = 3;
		 
		
	}
	
	/**
	 * 站点参数类型
	 *
	 * @author sjm
	 */
	public static class stationParameter {
		/**
		 * 发电总量
		 */
		public static final Long capacity = 1l;
		/**
		 * 发电总功率
		 */
		public static final Long power = 2l;
		/**
		 * 风速
		 */
		public static final Long wind = 3l;
		/**
		 * 温度
		 */
		public static final Long temperature = 4l;
		/**
		 * 天气信息
		 */
		public static final Long weather = 5l;
		
	}
	
	

}
