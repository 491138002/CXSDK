package com.changxiang.game.sdk.user.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.net.RequestParameter;
import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.user.CXBasePanel;
import com.changxiang.game.sdk.user.HomePanel;

public class CXBindPhoneView extends CXBasePanel {
	private static CXBindPhoneView BXBindPhoneView;

	private HomePanel mHomePanel;
	private View mBaseView;

	private TextView mUserNameEt;
	private EditText mPhoneNumberEt;
	private EditText mVerifycodeEt;
	private Button mGetVerifycodeBtn;
	private Button mBindPhoneBtn;
	private Button mBackBtn;

	private String mPhoneNumber;
	private String mPhoneVerityCode;
	private boolean isGetVerityCode = false;

	private final int GET_VERITY_CODE_KEY = 10001;
	private final int BIND_PHONENUMBER_KEY = 10002;

	public synchronized static CXBindPhoneView getInstance(Context context) {
		if (BXBindPhoneView == null) {
			BXBindPhoneView = new CXBindPhoneView(context);
		}
		return BXBindPhoneView;
	}

	private CXBindPhoneView(Context context) {
		super(context);

		init();
		initView();
		initListener();
		initValue();
	}

	public void init() {
		mBaseView = mInflater.inflate(CXResources.layout.bx_bind_phone, null);
	}

	public void initView() {
		mUserNameEt = (TextView) mBaseView
				.findViewById(CXResources.id.bx_bind_phone_user);
		mPhoneNumberEt = (EditText) mBaseView
				.findViewById(CXResources.id.bx_bind_phone_input_phone);
		mVerifycodeEt = (EditText) mBaseView
				.findViewById(CXResources.id.bx_bind_phone_input_veritycode);
		mGetVerifycodeBtn = (Button) mBaseView
				.findViewById(CXResources.id.bx_bind_phone_get_verifycode);
		mBindPhoneBtn = (Button) mBaseView
				.findViewById(CXResources.id.bx_bind_phone_now_bind);
		mBackBtn = (Button) mBaseView.findViewById(CXResources.id.bx_back);

		// mGetVerifycodeBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}

	private class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			mGetVerifycodeBtn.setText("获取验证码");
			mGetVerifycodeBtn
					.setBackgroundResource(CXResources.drawable.cx_btn_selector_3);
			mGetVerifycodeBtn.setClickable(true);

		}

		@Override
		public void onTick(long millisUntilFinished) {
			mGetVerifycodeBtn.setClickable(false);
			mGetVerifycodeBtn
					.setBackgroundResource(CXResources.drawable.bx_btn_account_info_bind_phone);
			mGetVerifycodeBtn.setText(millisUntilFinished / 1000 + "s重新发送");
		}

	}

	public void initListener() {
		mGetVerifycodeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				mPhoneNumber = mPhoneNumberEt.getText().toString().trim();
				if (TextUtils.isEmpty(mPhoneNumber)) {
					showToast("请输入手机号");
					return;
				}
				if (!isPhoneNumber(mPhoneNumber)) {
					showToast("请输入正确格式的手机号");
					return;
				}

				if (CXGameConfig.GAMEPARAM != null) {
					getPhoneVerityCode(CXGameConfig.USER_ID,
							 mPhoneNumber);
				}
			}
		});

		mBindPhoneBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (isGetVerityCode) {
					// 请求过验证码，进行手机号绑定
					mPhoneVerityCode = mVerifycodeEt.getText().toString()
							.trim();
					if (TextUtils.isEmpty(mPhoneVerityCode)) {
						showToast("请输入短信验证码");
						return;
					}

					bindPhoneNumber(CXGameConfig.USER_ID, mPhoneNumber,
							mPhoneVerityCode);
				} else {
					// 没有请求过验证码，提示先请求验证码
					showToast("请先获取验证码");
				}
			}
		});

		mBackBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				mHomePanel.showViewType(HomePanel.SHOW_ACCOUNT_INFO);
				CXAccountInfoView.getInstance(mContext).initValue();
			}
		});
	}

	public void initValue() {
		mUserNameEt.setText(CXGameConfig.ACCOUNT);
		mPhoneNumberEt.setText("");
	}

	public View getView() {
		return mBaseView;
	}

	public void setHomePanel(HomePanel homePanel) {
		this.mHomePanel = homePanel;
	}

	/**
	 * 绑定手机号发送短信验证码 调用的前提是用户已经登录
	 * 
	 * @param app_id
	 *            合作者编号，CP唯一编号
	 * @param server_id
	 *            服务器编号
	 * @param account
	 *            用户账号
	 * @param phoneNumber
	 *            电话号码
	 */
	private void getPhoneVerityCode(String user_id, String phone) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("user_id", String.valueOf(user_id)));
		parameter.add(new RequestParameter("phone", phone));

		startHttpRequst(CXGameConfig.HTTP_POST,
				CXGameConfig.BX_SERVICE_URL_GET_VERITY_CODE, parameter, true,
				"", true, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, GET_VERITY_CODE_KEY);
	}

	/**
	 * 用户绑定手机 调用的前提是用户已经登录
	 * 
	 * @param account
	 *            用户账号
	 * @param phoneNumber
	 *            电话号码
	 * @param verityCode
	 *            验证码
	 */
	private void bindPhoneNumber(String useID, String phone,
			String verityCode) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("user_id", String.valueOf(useID)));
		parameter.add(new RequestParameter("phone", phone));
		parameter.add(new RequestParameter("code", verityCode));
		startHttpRequst(CXGameConfig.HTTP_POST,
				CXGameConfig.BX_SERVICE_URL_BIND_PHONENUMBER, parameter, true,
				"", true, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, BIND_PHONENUMBER_KEY);
	}

	@Override
	public void onCallBackFromThread(String resultJson, int resultCode) {
		// TODO Auto-generated method stub
		super.onCallBackFromThread(resultJson, resultCode);
		try {
			int code = 0;
			String msg = "";
			if (!TextUtils.isEmpty(resultJson)) {
				JSONObject resultObject = new JSONObject(resultJson);
				if (!resultObject.isNull("code")) {
					code = resultObject.getInt("code");
				}
				if (!resultObject.isNull("msg")) {
					msg = resultObject.getString("msg");
				}

				switch (resultCode) {
				case GET_VERITY_CODE_KEY:
					if (code == CXSDKStatusCode.SUCCESS) {
						isGetVerityCode = true;
						new MyCount(60000, 1000).start();
						showToast("验证码已发送，请稍等…");
					} else {
						showToast(code);
					}
					break;
				case BIND_PHONENUMBER_KEY:
					if (code == CXSDKStatusCode.SUCCESS) {
						showToast("手机号绑定成功");
						mHomePanel.showViewType(HomePanel.SHOW_ACCOUNT_INFO);
						CXAccountInfoView.getInstance(mContext).initValue();
					} else {
						
						showToast(code);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void releaseViews() {
		BXBindPhoneView = null;
	}
}
