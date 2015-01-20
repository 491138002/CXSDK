package com.changxiang.game.sdk;

import java.net.URLEncoder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.changxiang.game.sdk.activity.CXMainActivity;
import com.changxiang.game.sdk.activity.CXPayActivity;
import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.exception.CXCallbackListenerNullException;
import com.changxiang.game.sdk.listener.CXGameCallbackListener;
import com.changxiang.game.sdk.listener.CXPayCallbackListener;
import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.util.DeviceUtil;
import com.changxiang.game.sdk.util.StringUtil;
import com.changxiang.game.sdk.vo.CXGameParamInfo;
import com.changxiang.game.sdk.vo.CXGamePayParamInfo;

public class CXGameSDK {
	private static CXGameSDK SDK = null;
	public static synchronized CXGameSDK defaultSDK() {
		if (SDK == null) {
			SDK = new CXGameSDK();
		}
		return SDK;
	}

	public void initSDK(Context ctx, CXGameParamInfo gameParams,
			CXGameCallbackListener<String> listener)
			throws CXCallbackListenerNullException {
		if (ctx == null) {
			throw new CXCallbackListenerNullException(
					CXResources.ERROR_CONTEXT_NULL);
		}

		if (listener == null) {
			throw new CXCallbackListenerNullException(
					CXResources.ERROR_CALLBACKLISTENER_NULL);
		}
		
		
		CXGameConfig.PHONE_IMEI = DeviceUtil.getIMEI(ctx);
		
		
		String channel="";
		ApplicationInfo appInfo;
		try {
			appInfo = ctx.getPackageManager().getApplicationInfo(
					ctx.getPackageName(), PackageManager.GET_META_DATA);
			
			if (appInfo.metaData!=null) {
				
				channel = appInfo.metaData.getString("UMENG_CHANNEL");
				System.out.println("UMENG_CHANNEL=="+channel);
				if (StringUtil.isNotEmpty(channel)) {
					CXGameConfig.CHANNEL_ID=channel;
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		CXGameConfig.CONTEXT = ctx;
		CXGameConfig.GAMEPARAM = gameParams;
		CXGameConfig.is_hasSMS = gameParams.getSMS();

		// 初始化SDK成功
		listener.callback(CXSDKStatusCode.SUCCESS, CXResources.SUCCESS_INIT);
	}

	public void login(final Context context, CXGameCallbackListener listener)
			throws CXCallbackListenerNullException {
		if (context == null) {
			throw new CXCallbackListenerNullException(
					CXResources.ERROR_CONTEXT_NULL);
		}

		if (listener == null) {
			throw new CXCallbackListenerNullException(
					CXResources.ERROR_CALLBACKLISTENER_NULL);
		}

		CXGameConfig.CONTEXT = context;
		CXGameConfig.CALLBACK_LISTENER = listener;

		context.startActivity(new Intent(context, CXMainActivity.class));
	}

	public void pay(Context ctx, CXPayCallbackListener<String> listener,
			CXGamePayParamInfo paymentParams)
			throws CXCallbackListenerNullException {

		if (listener == null) {
			throw new CXCallbackListenerNullException("需要有回掉函数");
		}
		if (ctx == null) {
			throw new CXCallbackListenerNullException("需要应用程序上下文");
		}

		if (paymentParams == null) {
			throw new CXCallbackListenerNullException("需要支付参数");
		}

		// 存储回掉函数
		CXGameConfig.PAYCALLBACK_LISTENER = listener;
		CXGameConfig.CONTEXT = ctx;
		CXGameConfig.PAYMENT_PARAM = paymentParams;

		// 启动支付界面

		ctx.startActivity(new Intent(ctx, CXPayActivity.class));

	}

	public String getTicket() {
		if (StringUtil.isNotEmpty(CXGameConfig.TICKET)) {
			return URLEncoder.encode(CXGameConfig.TICKET);
		} else {
			return null;
		}
	}
}
