package com.changxiang.game.sdk.type;

public class CXSDKStatusCode {

	// 登陆被踢出
	public static final int LOGIN_EXIT = -11;
	// 操作失败
	public static final int FAIL = -1;
	// 没有初始化
	public static final int NO_INIT = -2;
	// 初始化失败
	public static final int INIT_FAIL = -3;
	// 充值退出
	public static final int CHARGE_USER_EXIT = -12;
	// 操作成功
	public static final int SUCCESS = 1;
	// 系统错误
	public static final int SYSTEM_ERROR = 10000;
	// 用户不存在
	public static final int USER_NOT_EXIST = 10001;
	// 登录时密码错误
	public static final int PASSWORD_ERROR = 10002;
	// 用户已存在
	public static final int USER_EXIST = 10003;
	// 新密码与旧密码相同
	public static final int NEWPWD_AND_OLDPWD_MATE = 10004;
	// 验证码过期
	public static final int PHONE_AUTH_CODE_PAST_DUE = 10005;
	// 验证码错误
	public static final int PHONE_AUTH_CODE_ERROR = 10006;
	// 手机号已经被绑定
	public static final int PHONE_HAS_BIND = 10007;
	// 已经绑定手机
	public static final int BIND_HAS = 10008;
	// 没有绑定手机
	public static final int UNBIND_MOBILE = 10009;
	// 参数错误
	public static final int PARAM_ERROR = 10010;
	// 输入的手机号与绑定的手机号不一致
	public static final int NOW_PHONE_OR_BAND_PHONE_ERROR = 10011;
	// 用户名不合法
	public static final int USERNAME_INVALID = 10012;
	// 密码少于6位
	public static final int PWSSWORD_UNDER_SIX = 10013;
	// APPID不合法
	public static final int APP_ID_INVALID = 10014;
	// SERVERID不合法
	public static final int SERVER_ID_INVALID = 10015;
	// USER_ID不合法
	public static final int USER_ID_INVALID = 10016;
	// 金额小于零
	public static final int AMOUNT_INVALID = 10017;
	// 订单号少于24个字符
	public static final int ORDER_NUMBER_INVALID = 10018;
	// 本IP 10分钟之内只允许注册一次
	public static final int THIS_IP_NOT_REGISTER_TEN = 10019;
	// 此手机24小时之内只能注册一次
	public static final int THIS_PHONE_NOT_REGISTER_DAY = 10020;

	// 用户未登录
	public static final int USER_NOT_LOGIN = 10021;
	// 商品不存在
	public static final int SHOP_NOT_EXIST = 20000;
	// 商品不可用
	public static final int SHOP_NOT_USE = 20001;
	// 点卡卡号或密码不正确
	public static final int CARD_ERROR = 20002;
	// 生成订单失败
	public static final int CREATE_ORDER_ERROR = 20003;
	// 充值卡密码错误
	public static final int CODE_PASSOWRD_ERROR = 20004;
	// 支付错误
	public static final int PAY_ERROR = 20005;
	// 票据正确，但商品不存在
	public static final int APP_STORY_PRODUCT_ERROR = 20006;
	// 苹果支付错误
	public static final int APP_STORY_PAY_ERROR = 20007;
	// 订单不存在
	public static final int ORDER_NOT_EXIST = 20008;
	// 等待查询结果
	public static final int WAITE_FOR_CHANNEL_NOTIFY = 20009;
	// notify_url 不存在
	public static final int NOTIFYURL_ERR = 20010;
	// BUG
	public static final int BUG = 20011;
	// 新浪token失效
	public static final int SINA_ACCESS_TOKEN_PAST_DUE = 20012;
	// 应用名错误
	public static final int APP_NAME_ERROR = 20013;
	
	//金额大于30不能用短信支付
	public static final int PRICE_ERROR = -9999;
}
