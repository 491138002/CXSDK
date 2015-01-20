package com.changxiang.game.sdk.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.base.CXBaseActivity;
import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.net.RequestParameter;
import com.changxiang.game.sdk.type.CXOrientation;
import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.type.OperateType;
import com.changxiang.game.sdk.ui.CXLoadingDialog;
import com.changxiang.game.sdk.user.HomePanel;
import com.changxiang.game.sdk.user.MainBodyItem;
import com.changxiang.game.sdk.user.home.CXAccountInfoView;
import com.changxiang.game.sdk.user.home.CXBindPhoneAgainView;
import com.changxiang.game.sdk.user.home.CXBindPhoneView;
import com.changxiang.game.sdk.user.home.CXChangeBindPhoneAgainView;
import com.changxiang.game.sdk.user.home.CXChangePasswordView;
import com.changxiang.game.sdk.user.home.CXResetPasswordView;
import com.changxiang.game.sdk.util.DeviceUtil;
import com.changxiang.game.sdk.util.ParseUtil;
import com.changxiang.game.sdk.util.StringUtil;
import com.changxiang.game.sdk.util.Util;
import com.changxiang.game.sdk.util.BXDialogUtil.OnAlertSelectId;

public class CXMainActivity extends CXBaseActivity implements OnAlertSelectId {

	private static final int REQUEST_CONFIG_KEY = 2;
	private static final int REQUEST_PAYCONFIG_KEY = 3;
	private CXLoadingDialog cxLoadingDialog;
	private RelativeLayout body;
	private MainBodyItem mHomeBody;
	private RelativeLayout.LayoutParams childLayoutParams = new RelativeLayout.LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

	private String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
			.getExternalStorageState()) ? Environment
			.getExternalStorageDirectory().getAbsolutePath() : this
			.getCacheDir().getPath();
	private String jsonSavePath = cachePath + File.separator + "CXSDK";

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			int retCode = msg.getData().getInt("retCode");
			int retType = msg.getData().getInt("retType");

			if (retType == OperateType.LOGIN) {
				switch (retCode) {
				case CXSDKStatusCode.SUCCESS:
					showToast("登录成功");
					if (CXGameConfig.CALLBACK_LISTENER != null)
						CXGameConfig.CALLBACK_LISTENER.callback(
								CXSDKStatusCode.SUCCESS, CXGameConfig.USER_ID);
					finish();
					break;
				case CXSDKStatusCode.USER_NOT_EXIST:
					showToast("用户不存在");
					break;
				case CXSDKStatusCode.PASSWORD_ERROR:
					showToast("用户名或密码错误");
					break;
				case CXSDKStatusCode.USER_NOT_LOGIN:
					showToast("用户未登录");
					break;
				case CXSDKStatusCode.SYSTEM_ERROR:
					showToast("业务繁忙请稍后再试");
					break;
				case CXSDKStatusCode.FAIL:
					showToast("登陆失败");
					break;
				case CXSDKStatusCode.LOGIN_EXIT:
					if (CXGameConfig.CALLBACK_LISTENER != null)
						CXGameConfig.CALLBACK_LISTENER.callback(
								CXSDKStatusCode.LOGIN_EXIT, "");
					finish();
					break;
				case CXSDKStatusCode.PARAM_ERROR:
					showToast("参数错误");
					break;

				default:
					showToast("登陆失败");
					break;
				}
			} else if (retType == OperateType.QUICK_REGISTER) {
				switch (retCode) {
				case CXSDKStatusCode.SUCCESS:
					if (CXGameConfig.CALLBACK_LISTENER != null)
						CXGameConfig.CALLBACK_LISTENER.callback(
								CXSDKStatusCode.SUCCESS, CXGameConfig.USER_ID);
					finish();
					break;
				case CXSDKStatusCode.PASSWORD_ERROR:
					showToast("用户名密码错误");
					break;
				case CXSDKStatusCode.USER_EXIST:
					showToast("用户已存在");
					break;
				case CXSDKStatusCode.USER_NOT_LOGIN:
					showToast("用户未登录");
					break;
				case CXSDKStatusCode.SYSTEM_ERROR:
					showToast("业务繁忙请稍后再试");
					break;
				case CXSDKStatusCode.FAIL:
					showToast("登陆失败");
					break;
				case CXSDKStatusCode.PARAM_ERROR:
					showToast("参数错误");
					break;
				default:
					showToast("登陆失败");
					break;
				}
			} else if (retType == OperateType.NO_INIT) {
				if (CXGameConfig.CALLBACK_LISTENER != null) {
					CXGameConfig.CALLBACK_LISTENER.callback(
							CXSDKStatusCode.NO_INIT, "");
				}
			}
		}

	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		int width = Util.getScreenRect(this).right;
		int height = Util.getScreenRect(this).bottom;

		if (CXGameConfig.SCREEN_ORIENTATION == CXOrientation.VERTICAL) {

			if (width > height) {
				int temp = width;
				width = height;
				height = temp;
			}

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

			if (DeviceUtil.isTablet(context)) {
				width = (int) (width * CXGameConfig.SCREEN_PROPORTION * 0.6);
				height = (int) (height * CXGameConfig.SCREEN_PROPORTION * 0.4);
			} else {

				width = (int) (width * CXGameConfig.SCREEN_PROPORTION);
				height = (int) (height * CXGameConfig.SCREEN_PROPORTION * 0.7);
			}
		} else {
			if (width < height) {
				int temp = width;
				width = height;
				height = temp;
			}

			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			if (DeviceUtil.isTablet(context)) {
				width = (int) (width * CXGameConfig.SCREEN_PROPORTION * 0.4);
				height = (int) (height * CXGameConfig.SCREEN_PROPORTION * 0.6);
			} else {

				width = (int) (width * CXGameConfig.SCREEN_PROPORTION * 0.6);
				height = (int) (height * CXGameConfig.SCREEN_PROPORTION * 1.1);
			}

		}

		View view = LayoutInflater.from(this).inflate(
				CXResources.layout.bx_main_activity, null);
		setContentView(view, new RelativeLayout.LayoutParams(width, height));

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);

		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view
				.getLayoutParams();
		lp.gravity = Gravity.CENTER;
		view.setLayoutParams(lp);
		if (null != CXGameConfig.CONTEXT) {
			CXGameConfig.PACKAGE_NAME = CXGameConfig.CONTEXT.getPackageName();
		}

		PackageInfo pinfo;
		try {
			pinfo = getPackageManager().getPackageInfo(
					CXGameConfig.PACKAGE_NAME, 0);
			CXGameConfig.VERSION_CODE = pinfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		getConfig();
		initView();

	}


	/**
	 * 请求登录处理
	 */
	public void getConfig() {
		cxLoadingDialog = new CXLoadingDialog(context, "请求登录...", false);
		cxLoadingDialog.show();
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("app_id", String
				.valueOf(CXGameConfig.GAMEPARAM.getApp_id())));
		if (StringUtil.isEmpty(getConfigVer())) {
			CXGameConfig.CONFIG_VER = "1";
		}else {
			CXGameConfig.CONFIG_VER=getConfigVer();
		}
		parameter
				.add(new RequestParameter("configver", CXGameConfig.CONFIG_VER));
		startHttpRequst(CXGameConfig.HTTP_GET, CXGameConfig.SERVER_KEY,
				parameter, false, "", false,
				CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, REQUEST_CONFIG_KEY);
	}

	/**
	 * 获取配置版本
	 * @return
	 */
	public String getConfigVer() {
		File file = new File(jsonSavePath + File.separator+"config_ver.json");
		if (!file.exists()) {
			return "";
		} else {
			String resultjson = Util.readJson(jsonSavePath+File.separator
					+ "config_ver.json");
			HashMap<String, Object> result = ParseUtil.getConfig(resultjson);
			return String.valueOf(result.get("VERSION"));
		}
	}

	public void getPayConfig(String url) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("app_id", String
				.valueOf(CXGameConfig.GAMEPARAM.getApp_id())));
		startHttpRequst(CXGameConfig.HTTP_GET, url, parameter, false, "",
				false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, REQUEST_PAYCONFIG_KEY);
	}

	private void initView() {
		body = (RelativeLayout) findViewById(CXResources.id.body);
		mHomeBody = new HomePanel(this);
		setBody(mHomeBody.getView(CXMainActivity.this));
	}

	private void cancelMyRequest() {
		if (mHomeBody != null)
			((HomePanel) mHomeBody).onDestory();
	}

	public void loginResult(int retType, int retCode) {
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putInt("retType", retType);
		data.putInt("retCode", retCode);

		msg.setData(data);

		mHandler.sendMessage(msg);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return true;

			// loginResult(OperateType.LOGIN, BFSDKStatusCode.LOGIN_EXIT);
			// BFResources.context = null;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCallBackFromThread(String resultJson, int resultCode) {
		super.onCallBackFromThread(resultJson, resultCode);
		if (!TextUtils.isEmpty(resultJson)) {
			try {
				switch (resultCode) {
				case REQUEST_CONFIG_KEY:

					HashMap<String, Object> result = ParseUtil
							.getConfig(resultJson);

					if (result.get("SERVER_KEY") != null) {
						CXGameConfig.SERVERKEY = String.valueOf(result
								.get("SERVER_KEY"));
						if (StringUtil.isNotEmpty(CXGameConfig.ACCOUNT)) {
							CXAccountInfoView.getInstance(context).initValue();
						}
						Util.writeJson(jsonSavePath+File.separator, resultJson, "config_ver");
						if ("1".equals(String.valueOf(result
								.get("IS_UPDATE_PAYMENT_CONFIG")))) {
							
							getPayConfig(String.valueOf(result
									.get("PAYMENT_CONFIG")));
						} else {
							File file = new File(jsonSavePath+File.separator
									+ "payconfig.json");
							if (!file.exists()) {
								getPayConfig(String.valueOf(result
										.get("PAYMENT_CONFIG")));
							} else {
								cxLoadingDialog.dismiss();
							}
						}
					} else {
						cxLoadingDialog.dismiss();
						showToast("请求失败，请检查网络后重试");
						retry();
						return;
					}

					break;

				case REQUEST_PAYCONFIG_KEY:
					HashMap<String, Object> payCres = ParseUtil
							.getPayCOnfig(resultJson);
					if (payCres.get("alipay_CreditCard") != null) {
						Util.writeJson(jsonSavePath+File.separator, resultJson, "payconfig");
						cxLoadingDialog.dismiss();
					}else {
						cxLoadingDialog.dismiss();
						showToast("请求失败，请检查网络后重试");
						retry();
						return;
					}
					break;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				cxLoadingDialog.dismiss();
				showToast("请求失败，请检查网络后重试");
				retry();
				return;
			}
		}
	}

	@Override
	protected void onDestroy() {
		cancelMyRequest();
		CXAccountInfoView.releaseViews();
		CXBindPhoneAgainView.releaseViews();
		CXBindPhoneView.releaseViews();
		CXChangePasswordView.releaseViews();
		CXResetPasswordView.releaseViews();
		CXChangeBindPhoneAgainView.releaseViews();
		super.onDestroy();
	}

	private void setBody(View child) {
		body.removeAllViews();
		child.setLayoutParams(childLayoutParams);
		body.addView(child);
	}

	private void retry() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("请求失败，请检查网络后重试");
		builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				getConfig();
			}
		});
		builder.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						|| keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				}
				return false;
			}
		});
		builder.show();
	}

	@Override
	public void onClick(int whichButton, Object o) {
		if (whichButton == 1) {
			HashMap<String, Object> data = (HashMap<String, Object>) o;
			if (!TextUtils.isEmpty((String) data.get("downloadurl"))) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse((String) data.get("downloadurl"));
				intent.setData(content_url);
				context.startActivity(intent);
			} else {
				Toast.makeText(context, "下载地址错误", Toast.LENGTH_SHORT).show();
			}
		}

	}
}
