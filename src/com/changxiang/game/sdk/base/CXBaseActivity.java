package com.changxiang.game.sdk.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.net.AsyncHttpGet;
import com.changxiang.game.sdk.net.AsyncHttpPost;
import com.changxiang.game.sdk.net.BaseRequest;
import com.changxiang.game.sdk.net.DefaultThreadPool;
import com.changxiang.game.sdk.net.RequestParameter;
import com.changxiang.game.sdk.net.ThreadCallBack;
import com.changxiang.game.sdk.util.CheckNetWorkUtil;
import com.changxiang.game.sdk.util.DeviceUtil;
import com.changxiang.game.sdk.util.LogUtil;

public class CXBaseActivity extends Activity implements ThreadCallBack {

	private static final long serialVersionUID = 1L;

	/**
	 * 上下文
	 */
	protected Activity context = this;

	/**
	 * 当前activity所持有的所有请求
	 */
	List<BaseRequest> requestList = null;

	/**
	 * 主线程Handler
	 */
	protected Handler handler = new Handler();

	protected int SUCCESS = 1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestList = new ArrayList<BaseRequest>();
		super.onCreate(savedInstanceState);
		if (CXResources.context == null) {
			CXResources.context = this;
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		/**
		 * 在activity销毁的时候同时设置停止请求，停止线程请求回调
		 */
		// cancelRequest();
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		/**
		 * 在activity销毁的时候同时设置停止请求，停止线程请求回调
		 */
		cancelRequest();
		super.onDestroy();
	}

	public void cancelRequest() {
		if (requestList != null && requestList.size() > 0) {
			for (BaseRequest request : requestList) {
				if (request.getRequest() != null) {
					try {
						request.getRequest().abort();
						requestList.remove(request.getRequest());

						// Log.d("netlib", "netlib ,onDestroy request to  "
						// + request.getRequest().getURI()
						// + "  is removed");
					} catch (UnsupportedOperationException e) {
						// do nothing .
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
			if (!CheckNetWorkUtil.checkNetWork(this)) {
				return;
			}
		}
		if (null != parameter) {
			parameter
					.add(new RequestParameter("imei", CXGameConfig.PHONE_IMEI));
			parameter.add(new RequestParameter("ver",
					CXGameConfig.CXSDK_VERSION));
			parameter.add(new RequestParameter("os_version", DeviceUtil
					.getSDKVersion()));
			parameter.add(new RequestParameter("system", "android"));
			parameter.add(new RequestParameter("channel_id", String.valueOf(CXGameConfig.CHANNEL_ID)));
			parameter.add(new RequestParameter("device_name", String.valueOf(
					DeviceUtil.getDeviceModel()).replace(" ", "")));

		}
		for (int i = 0; i < parameter.size(); i++) {
			RequestParameter requestParameter = parameter.get(i);
			LogUtil.d("requestParameter", requestParameter.getName() + "="
					+ requestParameter.getValue());
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
			if (!CheckNetWorkUtil.checkNetWork(this)) {
				return;
			}
		}
		if (null != parameter) {
			parameter
					.add(new RequestParameter("imei", CXGameConfig.PHONE_IMEI));
			parameter.add(new RequestParameter("ver",
					CXGameConfig.CXSDK_VERSION));
			parameter.add(new RequestParameter("os_version", DeviceUtil
					.getSDKVersion()));
			parameter.add(new RequestParameter("system", "android"));
			parameter.add(new RequestParameter("channel_id", String.valueOf(CXGameConfig.CHANNEL_ID)));
			parameter.add(new RequestParameter("device_name", String.valueOf(
					DeviceUtil.getDeviceModel()).replace(" ", "")));

		}
		for (int i = 0; i < parameter.size(); i++) {
			RequestParameter requestParameter = parameter.get(i);
			LogUtil.d("requestParameter", requestParameter.getName() + "="
					+ requestParameter.getValue());
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
				loadingDialogContent, true,
				CXGameConfig.CONNECTION_SHORT_TIMEOUT,
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
			if (!CheckNetWorkUtil.checkNetWork(this)) {
				return;
			}
		}

		if (null != parameter) {
			parameter
					.add(new RequestParameter("imei", CXGameConfig.PHONE_IMEI));
			parameter.add(new RequestParameter("ver",
					CXGameConfig.CXSDK_VERSION));
			parameter.add(new RequestParameter("os_version", DeviceUtil
					.getSDKVersion()));
			parameter.add(new RequestParameter("system", "android"));
			parameter.add(new RequestParameter("channel_id", String.valueOf(CXGameConfig.CHANNEL_ID)));
			parameter.add(new RequestParameter("device_name", String.valueOf(
					DeviceUtil.getDeviceModel()).replace(" ", "")));

		}
		for (int i = 0; i < parameter.size(); i++) {
			RequestParameter requestParameter = parameter.get(i);
			LogUtil.d("requestParameter", requestParameter.getName() + "="
					+ requestParameter.getValue());
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

			switch (resultCode) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Context getContext() {
		return this;
	}

	public void showToast(String msg) {
		Toast t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		t.show();
	}
}
