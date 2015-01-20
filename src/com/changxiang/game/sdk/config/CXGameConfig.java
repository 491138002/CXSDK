package com.changxiang.game.sdk.config;

import android.content.Context;
import android.view.WindowManager;

import com.changxiang.game.sdk.listener.CXGameCallbackListener;
import com.changxiang.game.sdk.listener.CXPayCallbackListener;
import com.changxiang.game.sdk.type.CXOrientation;
import com.changxiang.game.sdk.vo.CXGameParamInfo;
import com.changxiang.game.sdk.vo.CXGamePayParamInfo;

public class CXGameConfig {
	/** 接口地址 **/
	// public static final String BASE_SERVICE_URL =
	// "http://sdk.gcenter.baofeng.com/";
//	public static final String BASE_SERVICE_URL = "http://14.17.126.90:8091/";
	 public static final String BASE_SERVICE_URL = "http://sdkapi.ak.cc/";
	/** 登录 **/
	public static final String BX_SERVICE_URL_ACCOUNT_LOGIN = BASE_SERVICE_URL
			+ "user/login";
	public static final String BX_SERVICE_URL_ACCOUNT_INFO = BASE_SERVICE_URL
			+ "user/getuserinfo";

	// public static final String SERVER_KEY = BASE_SERVICE_URL +
	// "user/ServerKey";
	public static final String SERVER_KEY = BASE_SERVICE_URL + "config";
	/** 注册 **/
	public static final String BX_SERVICE_URL_ACCOUNT_REGISTER = BASE_SERVICE_URL
			+ "user/register";

	/** 发送验证码，带验证码返回 **/
	public static final String BX_SERVICE_URL_SEND_CODE = BASE_SERVICE_URL
			+ "sendpasswordcodesdk2";

	/** 验证验证码的正确性 **/
	public static final String BX_SERVICE_URL_CHECK_CODE = BASE_SERVICE_URL
			+ "checkpasswordcode";

	/** 是否已绑定手机,返回电话号码 **/
	public static final String BX_SERVICE_URL_ALREADY_BIND_PHONE = BASE_SERVICE_URL
			+ "user/isbindmobile";

	/** 重置密码 **/
	public static final String BX_SERVICE_URL_RESET_PASSWORD = BASE_SERVICE_URL
			+ "user/resetpwd";

	/** 推荐游戏列表 **/
	public static final String BX_SERVICE_URL_GAME_LIST = BASE_SERVICE_URL
			+ "games";

	/** 平台活动列表 **/
	public static final String BX_SERVICE_URL_NEWS_LIST = BASE_SERVICE_URL
			+ "news";
	/** 平台活动详情 **/
	public static final String BX_SERVICE_URL_NEWS_DETAIL = BASE_SERVICE_URL
			+ "activestatus";
	/** 获取游戏版本 **/
	public static final String BX_SERVICE_URL_GET_VERSION = BASE_SERVICE_URL
			+ "getgameversion";

	/** 修改密码 **/
	public static final String BX_SERVICE_URL_CHANGE_PASSWORD = BASE_SERVICE_URL
			+ "user/changepwd";

	/** 获取绑定短信验证码 **/
	public static final String BX_SERVICE_URL_GET_VERITY_CODE = BASE_SERVICE_URL
			+ "user/bindmobilesendcode";

	/** 获取找回密码短信验证码 **/
	public static final String BX_SERVICE_URL_GET_RESET_PASSWORD_VERITY_CODE = BASE_SERVICE_URL
			+ "user/sendforgetpwdcode";

	/** 绑定手机号 **/
	public static final String BX_SERVICE_URL_BIND_PHONENUMBER = BASE_SERVICE_URL
			+ "user/bindmobile";

	/** 快速注册用户修改密码 **/
	public static final String BX_QUICK_RESET_PASSWORD = BASE_SERVICE_URL
			+ "quickresetpwd";

	/** 手机卡支付 **/
	public static final String BX_SERVICE_URL_PAY_BY_PHONE = BASE_SERVICE_URL
			+ "wappay/pay";

	/** 支付 **/
	public static final String BX_SERVICE_URL_PAY_BY_ALIPAY = BASE_SERVICE_URL
			+ "pay/alipay";
	public static final String BX_SERVICE_URL_PAY_BY_TELE = BASE_SERVICE_URL
			+ "pay/telecardpay";
	public static final String BX_SERVICE_URL_PAY_BY_CARD = BASE_SERVICE_URL
			+ "pay/gamecardpay";
	public static final String BX_SERVICE_URL_PAY_BY_UNIONPAY = BASE_SERVICE_URL
			+ "pay/unionpay";
	public static final String BX_SERVICE_URL_PAY_BY_SMSPAY = BASE_SERVICE_URL
			+ "pay/smspay";

	/** 短信支付 **/
	public static final String BX_SERVICE_URL_PAY_BY_SMS = BASE_SERVICE_URL
			+ "mobilepay";

	/** 支付 结果查询 **/
	public static final String BX_SERVICE_URL_PAY_BY_QUERY = BASE_SERVICE_URL
			+ "pay/szfsearch";

	/** 版本升级 **/
	public static final String BX_SERVICE_URL_APP_UPGRADE = BASE_SERVICE_URL
			+ "getgameversion";

	/** 验证用户名密码 **/
	public static final String BX_SERVICE_URL_CHECK_PASSWORD = BASE_SERVICE_URL
			+ "checkPassword";

	/** 获取交易记录 **/
	public static final String BX_SERVICE_URL_TRANSACTION = BASE_SERVICE_URL
			+ "pay";

	public static final String OTHER_LOGIN = BASE_SERVICE_URL
			+ "user/rollbacks";

	public static final String BBS = "bbs";
	public static final String MAIN = "main";

	/** 屏幕方向,默认为横屏 **/
	public static CXOrientation SCREEN_ORIENTATION = CXOrientation.HORIZONTAL;
	/** 登陆或注册成功后ID **/
	public static String USER_ID = "0";
	/** 登陆或注册成功后 用户名 **/
	public static String USER_NAME;
	/** 游戏客户端登录后从SDK 取得的票据 **/
	public static String TICKET = "";

	/** 记录当前的账号密码，或者是没登录状态时的上次登录用户的账号密码 **/
	public static String ACCOUNT;
	public static String PASSWORD;
	public static String USERTYPE;
	public static String ORIGIN = "0";
	public static String NICK_NAME;
	public static String TOKEN = "";

	public static final String userTypeCX = "0";
	public static final String userTypeQQ = "1";
	public static final String userTypeXinLang = "2";
	public static final String userTypeQuick = "3";

	public static Context CONTEXT = null;

	public static String CHANNEL_ID = "213";
	/** 支付参数 **/
	public static CXGamePayParamInfo PAYMENT_PARAM = null;

	public static CXGameParamInfo GAMEPARAM = null;

	/** 是否是快速注册 **/
	public static boolean ISFASTREGISTER = false;

	/** 老用户登录Title **/
	public static String GAMETITLE = "";

	/** 记录手机号码 **/
	public static String PHONE_NUMBER = "";
	public static String PACKAGE_NAME = "";

	public static String QQ_QA = "";
	public static String TEL_QA = "";

	/** IMEI：International Mobile Equipment Identity 国际移动设备身份码 **/
	public static String PHONE_IMEI = "";

	public static int VERSION_CODE = 0;

	/** 记录回掉函数 **/
	public static CXGameCallbackListener<String> CALLBACK_LISTENER = null;

	/** 记录回掉函数 **/
	public static CXPayCallbackListener<String> PAYCALLBACK_LISTENER = null;

	public static boolean isDebug = false;

	public static final float SCREEN_PROPORTION = 0.8f;

	public static final int CONNECTION_COUNT = 3;// 超时重试次数
	public static final String ERROR_MESSAGE = "数据拉取失败，请重试";
	public static String LOADING_CONTENTS = "加载中，请稍候...";

	public static boolean isGzip;// 是否启用Gzip
	public static boolean IS_STOP_REQUEST = false;// 请求线程是否停止
	public static final int CONNECTION_SHORT_TIMEOUT = 10000;// 连接超时 10s
	public static final int READ_MIDDLE_TIMEOUT = 10000;// 读取超时 10s

	public static final String HTTP_GET = "GET";
	public static final String HTTP_POST = "POST";

	/**
	 * 是否有短息支付
	 */
	public static boolean is_hasSMS = false;

	/**
	 * 当前短息支付是否成功
	 */
	public static boolean isSMSOK = false;
	/**
	 * 当前短息支付是否成功
	 */
	public static boolean isSMSOK_Online = true;
	/**
	 * 当前为短信支付返回的结果
	 */
	public static int payBackTypeSMS = 1;
	/**
	 * 当前为其他支付方式返回的结果
	 */
	public static int payBackTypeOther = 0;
	/**
	 * 当前返回的类型
	 */
	public static boolean isSMSCallBack;
	/**
	 * 是否为QQ登陆
	 */
	public static boolean isQQLogin;
	/**
	 * 三方登陆
	 */

	public static String OPEN_ID;
	/**
	 * SDK版本号
	 */
	public static final int SDK_VERSION_CODE = 100;
	/**
	 * 新浪登录url
	 */
	public static final String XINLANG_LOGIN_URL = BASE_SERVICE_URL
			+ "user/threelogin?client=sina";

	public static final WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	public static String SERVERKEY = "";
	public static String CONFIG_VER = "1";

	/**
	 * SDK版本号
	 */
	public static final String CXSDK_VERSION = "1.1.0";
}
