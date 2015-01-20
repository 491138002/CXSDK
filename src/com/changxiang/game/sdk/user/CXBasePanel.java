package com.changxiang.game.sdk.user;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.net.AsyncHttpGet;
import com.changxiang.game.sdk.net.AsyncHttpPost;
import com.changxiang.game.sdk.net.BaseRequest;
import com.changxiang.game.sdk.net.DefaultThreadPool;
import com.changxiang.game.sdk.net.RequestParameter;
import com.changxiang.game.sdk.net.ThreadCallBack;
import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.util.CheckNetWorkUtil;
import com.changxiang.game.sdk.util.DeviceUtil;
import com.changxiang.game.sdk.util.LogUtil;
import com.changxiang.game.sdk.util.MD5Util;

public class CXBasePanel implements ThreadCallBack {

	protected static String ID_TYPE = "id";
	protected static String ICON_TYPE = "drawable";
	protected static String LAYOUT_TYPE = "layout";
	protected static String STRING_TYPE = "string";
	protected static String ANIM_TYPE = "anim";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 上下文
	 */
	protected Context mContext;
	
	/**
	 * 布局获取控制器
	 */
	protected LayoutInflater mInflater;

	/**
	 * 当前activity所持有的所有请求
	 */
	List<BaseRequest> requestList = null;

	protected boolean isRegistQuestionReceiver = true;

	protected String accountFileSavePath;// 配置文件的保存路径
	protected static final String accountFileName = "account.dat";// 配置文件名

	public Context getContext() {
		return mContext;
	}

	public CXBasePanel(Context context) {
		this.mContext = context;
		requestList = new ArrayList<BaseRequest>();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initFilePath();
	}

	public void onDestory() {
		/**
		 * 在view销毁的时候同时设置停止请求，停止线程请求回调
		 */
		cancelRequest();
	}

	/**
	 * 对参数进行签名
	 * 
	 * @param original
	 *            明文
	 * @return MD5加密后的密文
	 */
	protected String generateSign(String original) {
		LogUtil.d("sign", original);
		String confidential = MD5Util.MD5(original);
		LogUtil.d("sign md5", confidential);
		return confidential.toUpperCase();
	}

	protected void showToast(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	}

	protected void showToast(int code) {
		String msg = "";
		switch (code) {
		case CXSDKStatusCode.USER_NOT_EXIST:
			msg = "用户存在";
			break;
		case CXSDKStatusCode.PHONE_AUTH_CODE_PAST_DUE:
			msg = "验证码过期";
			break;
		case CXSDKStatusCode.UNBIND_MOBILE:
			msg = "没有绑定手机";
			break;
		case CXSDKStatusCode.BIND_HAS:
			msg = "已经绑定手机";
			break;
		case CXSDKStatusCode.USER_EXIST:
			msg = "用户已经存在";
			break;
		case CXSDKStatusCode.USER_NOT_LOGIN:
			showToast("用户未登录");
			break;
		case CXSDKStatusCode.USERNAME_INVALID:
			msg = "用户名不合法";
			break;
		case CXSDKStatusCode.FAIL:
			msg = "操作失败";
			break;
		case CXSDKStatusCode.PARAM_ERROR:
			msg = "参数错误";
			break;
		case CXSDKStatusCode.ORDER_NOT_EXIST:
			msg = "订单不存在";
			break;
		case CXSDKStatusCode.NEWPWD_AND_OLDPWD_MATE:
			msg = "绑定验证失败";
			break;
		case CXSDKStatusCode.PAY_ERROR:
			msg = " 充值失败";
			break;
		case CXSDKStatusCode.PASSWORD_ERROR:
			msg = "原始密码输入错误";
			break;
		case CXSDKStatusCode.PHONE_AUTH_CODE_ERROR:
			msg = "验证码不正确";
			break;
		case CXSDKStatusCode.PHONE_HAS_BIND:
			msg = "手机已经被绑定";
			break;
		case CXSDKStatusCode.THIS_IP_NOT_REGISTER_TEN:
			msg = "本IP 10分钟之内只允许注册一次";
			break;
		case CXSDKStatusCode.THIS_PHONE_NOT_REGISTER_DAY:
			msg = "此手机24小时之内只能注册一次";
			break;
		default:
			msg = "操作失败,请重试";
		}
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	private void initFilePath() {

		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? Environment
				.getExternalStorageDirectory().getAbsolutePath() : mContext
				.getCacheDir().getPath();

		accountFileSavePath = cachePath + File.separator + "CXSDK"
				+ File.separator + "account";
		File file = new File(accountFileSavePath);
		if (!file.exists())
			file.mkdirs();
		File accountFile = new File(accountFileSavePath + File.separator
				+ accountFileName);
		if (!accountFile.exists()) {
			try {
				accountFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 验证是否全为字母
	 */
	protected boolean isAllCharacter(String value) {
		Pattern pattern = Pattern.compile("\\W");
		Matcher matcher = pattern.matcher(value);
		if (matcher.find()) {
			return false;
		}
		return true;
	}

	/**
	 * 验证是否为手机号
	 */
	protected boolean isPhoneNumber(String number) {
		Pattern pattern = Pattern.compile("[1][3578]\\d{9}");
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}

	public void cancelRequest() {
		if (requestList != null && requestList.size() > 0) {
			for (BaseRequest request : requestList) {
				if (request.getRequest() != null) {
					try {
						request.getRequest().abort();
						requestList.remove(request.getRequest());

					} catch (UnsupportedOperationException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void onCallbackFromThread(String resultJson) {
		// TODO Auto-generated method stub

	}

	protected void startHttpRequst(String requestType, String url,
			List<RequestParameter> parameter, boolean isShowLoadingDialog) {
		startHttpRequst(requestType, url, parameter, isShowLoadingDialog, -1);
	}

	protected void startHttpRequst(String requestType, String url,
			List<RequestParameter> parameter, boolean isShowLoadingDialog,
			int resultCode) {
		if (isShowLoadingDialog) {
			if (!CheckNetWorkUtil.checkNetWork(mContext)) {
				return;
			}
		}
		if (null != parameter) {
			parameter.add(new RequestParameter("imei",CXGameConfig.PHONE_IMEI));
			parameter.add(new RequestParameter("ver",
					CXGameConfig.CXSDK_VERSION));
			parameter.add(new RequestParameter("channel_id", String.valueOf(CXGameConfig.CHANNEL_ID)));
			parameter.add(new RequestParameter("os_version", DeviceUtil.getSDKVersion()));
			parameter.add(new RequestParameter("system", "android"));
			parameter.add(new RequestParameter("device_name", String.valueOf(DeviceUtil.getDeviceModel()).replace(" ", "")));

		}
		BaseRequest httpRequest = null;
		if ("POST".equalsIgnoreCase(requestType)) {
			httpRequest = new AsyncHttpPost(this, url, parameter,
					isShowLoadingDialog, "", false, resultCode);
		} else {
			httpRequest = new AsyncHttpGet(this, url, parameter,
					isShowLoadingDialog, "", false, resultCode);
		}
		DefaultThreadPool.getInstance().execute(httpRequest);
		this.requestList.add(httpRequest);
	}

	protected void startHttpRequst(String requestType, String url,
			List<RequestParameter> parameter, boolean isShowLoadingDialog,
			int connectTimeout, int readTimeout) {
		if (isShowLoadingDialog) {
			if (!CheckNetWorkUtil.checkNetWork(mContext)) {
				return;
			}
		}
		
		if (null != parameter) {
			parameter.add(new RequestParameter("imei",CXGameConfig.PHONE_IMEI));
			parameter.add(new RequestParameter("ver",
					CXGameConfig.CXSDK_VERSION));
			parameter.add(new RequestParameter("channel_id", String.valueOf(CXGameConfig.CHANNEL_ID)));
			parameter.add(new RequestParameter("os_version", DeviceUtil.getSDKVersion()));
			parameter.add(new RequestParameter("system", "android"));
			parameter.add(new RequestParameter("device_name", String.valueOf(DeviceUtil.getDeviceModel()).replace(" ", "")));

		}
		BaseRequest httpRequest = null;
		if ("POST".equalsIgnoreCase(requestType)) {
			httpRequest = new AsyncHttpPost(this, url, parameter,
					isShowLoadingDialog, connectTimeout, readTimeout);
		} else {
			httpRequest = new AsyncHttpGet(this, url, parameter,
					isShowLoadingDialog, connectTimeout, readTimeout);
		}
		DefaultThreadPool.getInstance().execute(httpRequest);
		this.requestList.add(httpRequest);
	}

	public void startHttpRequst(String requestType, String url,
			List<RequestParameter> parameter, boolean isShowLoadingDialog,
			String loadingDialogContent) {
		startHttpRequst(requestType, url, parameter, isShowLoadingDialog,
				loadingDialogContent, true, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT);
	}

	protected void startHttpRequst(String requestType, String url,
			List<RequestParameter> parameter, boolean isShowLoadingDialog,
			String loadingDialogContent, boolean isHideCloseBtn,
			int connectTimeout, int readTimeout) {
		startHttpRequst(requestType, url, parameter, isShowLoadingDialog,
				loadingDialogContent, isHideCloseBtn, connectTimeout,
				readTimeout, -1);
	}

	protected void startHttpRequst(String requestType, String url,
			List<RequestParameter> parameter, boolean isShowLoadingDialog,
			String loadingDialogContent, boolean isHideCloseBtn,
			int connectTimeout, int readTimeout, int resultCode) {
		if (isShowLoadingDialog) {
			if (!CheckNetWorkUtil.checkNetWork(mContext)) {
				return;
			}
		}
		if (null != parameter) {
			parameter.add(new RequestParameter("imei",CXGameConfig.PHONE_IMEI));
			parameter.add(new RequestParameter("ver",
					CXGameConfig.CXSDK_VERSION));
			parameter.add(new RequestParameter("channel_id", String.valueOf(CXGameConfig.CHANNEL_ID)));
			parameter.add(new RequestParameter("os_version", DeviceUtil.getSDKVersion()));
			parameter.add(new RequestParameter("system", "android"));
			parameter.add(new RequestParameter("device_name", String.valueOf(DeviceUtil.getDeviceModel()).replace(" ", "")));

		}
		BaseRequest httpRequest = null;
		if ("POST".equalsIgnoreCase(requestType)) {
			httpRequest = new AsyncHttpPost(this, url, parameter,
					isShowLoadingDialog, loadingDialogContent, isHideCloseBtn,
					connectTimeout, readTimeout, resultCode);
		} else {
			httpRequest = new AsyncHttpGet(this, url, parameter,
					isShowLoadingDialog, loadingDialogContent, isHideCloseBtn,
					connectTimeout, readTimeout, resultCode);
		}
		DefaultThreadPool.getInstance().execute(httpRequest);
		this.requestList.add(httpRequest);
	}

	@Override
	public void onCallBackFromThread(String resultJson, int resultCode) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
