package com.changxiang.game.sdk.user.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.activity.CXMainActivity;
import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.net.RequestParameter;
import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.type.OperateType;
import com.changxiang.game.sdk.user.CXBasePanel;
import com.changxiang.game.sdk.user.HomePanel;
import com.changxiang.game.sdk.util.ParseUtil;
import com.changxiang.game.sdk.util.StringUtil;
import com.changxiang.game.sdk.vo.CXUser;

public class CXResetPasswordView extends CXBasePanel {
	private static CXResetPasswordView mBXResetPasswordView;

	private HomePanel mHomePanel;
	private View mBaseView;

	private EditText mResetPasswordVerifyCode;
	private EditText mResetPasswordInput;
	private Button mGetVerifyCodeBtn;
	private Button mRestPasswordSubmitBtn;
	private Button mBackBtn;
	private boolean isGetVerityCode = false;
	private final int REQUEST_SEND_PHONENUMBER_CODE = 10001;
	private final int REQUEST_RESET_PASSWORD_KEY = 10003;
	private final int REQUEST_LOGIN_KEY = 10004;

	public synchronized static CXResetPasswordView getInstance(Context context) {
		if (mBXResetPasswordView == null) {
			mBXResetPasswordView = new CXResetPasswordView(context);
		}
		return mBXResetPasswordView;
	}

	private CXResetPasswordView(Context context) {
		super(context);

		init();
		initView();
		initListener();
		initValue();
	}

	public void init() {
		mBaseView = mInflater.inflate(CXResources.layout.bx_reset_password,
				null);
	}

	public void initView() {
		mResetPasswordVerifyCode = (EditText) mBaseView
				.findViewById(CXResources.id.bx_reset_password_verifycode);
		mResetPasswordInput = (EditText) mBaseView
				.findViewById(CXResources.id.bx_reset_password_input);
		mGetVerifyCodeBtn = (Button) mBaseView
				.findViewById(CXResources.id.bx_reset_password_get_veryficode);
		mRestPasswordSubmitBtn = (Button) mBaseView
				.findViewById(CXResources.id.bx_reset_password_btn);
		mBackBtn = (Button) mBaseView.findViewById(CXResources.id.bx_back);
	}

	public void initListener() {

		mGetVerifyCodeBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (CXGameConfig.GAMEPARAM != null) {
					getPhoneVerityCode(CXGameConfig.USER_ID,
							CXGameConfig.ACCOUNT, mHomePanel.phone_Number);
				}
			}
		});

		mRestPasswordSubmitBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if (isGetVerityCode) {
					if (StringUtil.isEmpty(mResetPasswordInput.getText()
							.toString())) {
						showToast("密码不能为空");
					} else if (mResetPasswordInput.getText().toString()
							.length() < 6) {
						showToast("请填写6位以上数字与字母组合密码");
					} else if (StringUtil.isEmpty(mResetPasswordVerifyCode
							.getText().toString())) {
						showToast("验证码不正确");
					} else {
						resetPassword(CXGameConfig.USER_ID,
								CXGameConfig.ACCOUNT, mResetPasswordInput
										.getText().toString(),
								mHomePanel.phone_Number,
								mResetPasswordVerifyCode.getText().toString());
					}

				} else {
					showToast("请先获取验证码");
				}
			}
		});

		mBackBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				mHomePanel.showViewType(HomePanel.SHOW_HOME_PANEL);
			}
		});
	}

	public void initValue() {
	}

	public View getView() {
		return mBaseView;
	}

	public void setHomePanel(HomePanel homePanel) {
		this.mHomePanel = homePanel;
	}

	public static void releaseViews() {
		mBXResetPasswordView = null;
	}

	/**
	 * 找回密码手机号发送短信验证码 调用的前提是用户已经登录
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
	private void getPhoneVerityCode(String user_id, String username,
			String phone) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("user_id", String.valueOf(user_id)));
		parameter.add(new RequestParameter("username", username));
		parameter.add(new RequestParameter("phone", phone));

		startHttpRequst(CXGameConfig.HTTP_POST,
				CXGameConfig.BX_SERVICE_URL_GET_RESET_PASSWORD_VERITY_CODE,
				parameter, true, "", true,
				CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, REQUEST_SEND_PHONENUMBER_CODE);
	}

	/**
	 * 重置密码
	 * 
	 * @param account
	 *            用户账号
	 * @param phone_number
	 *            电话号码
	 * @param code
	 *            短信验证码
	 * @param password
	 *            用户新密码
	 */
	private void resetPassword(String user_id, String username,
			String password, String phone, String code) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();

		parameter.add(new RequestParameter("user_id", String.valueOf(user_id)));
		parameter.add(new RequestParameter("username", username));
		parameter.add(new RequestParameter("phone", phone));
		parameter.add(new RequestParameter("code", code));
		parameter.add(new RequestParameter("new_pwd", password));
		startHttpRequst(CXGameConfig.HTTP_POST,
				CXGameConfig.BX_SERVICE_URL_RESET_PASSWORD, parameter, true,
				"", true, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, REQUEST_RESET_PASSWORD_KEY);
	}

	/**
	 * 用户登录
	 * 
	 * @param app_id
	 *            合作者唯一编号,由冰雪游戏 网游戏生成并提供
	 * @param account
	 *            冰雪游戏 网账号
	 * @param password
	 *            冰雪游戏 网密码
	 * @param server_id
	 *            联运服务器编号:用户验证后需要跳转的服务器编号没有为0
	 */
	private void accountLogin(int app_id, String username, String password,
			int server_id, int key) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("username", username));
		parameter.add(new RequestParameter("password", password));
		parameter.add(new RequestParameter("app_id", String.valueOf(app_id)));
		parameter.add(new RequestParameter("origin", "0"));
		parameter.add(new RequestParameter("server_id", String
				.valueOf(server_id)));

		startHttpRequst(CXGameConfig.HTTP_GET,
				CXGameConfig.BX_SERVICE_URL_ACCOUNT_LOGIN, parameter, true, "",
				false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, key);
	}

	@Override
	public void onCallBackFromThread(String resultJson, int resultCode) {
		// TODO Auto-generated method stub
		super.onCallBackFromThread(resultJson, resultCode);
		try {
			switch (resultCode) {
			case REQUEST_SEND_PHONENUMBER_CODE:
				JSONObject resultObject = new JSONObject(resultJson);
				int code = 0;
				if (!resultObject.isNull("code")) {
					code = Integer.valueOf(resultObject.getString("code"));
				}
				if (code == CXSDKStatusCode.SUCCESS) {
					isGetVerityCode = true;
					new CountDownTimer(60000, 1000) {// 两个参数，前一个指倒计时的总时间，后一个指多长时间倒数一下。

						@Override
						public void onTick(long millisUntilFinished) {
							// TODO Auto-generated method stub
							mGetVerifyCodeBtn.setText(millisUntilFinished / 1000
									+ "s" + "重新获取");
							mGetVerifyCodeBtn.setClickable(false);
						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							mGetVerifyCodeBtn.setText("获取验证码");
							mGetVerifyCodeBtn.setClickable(true);
						}
					}.start();
					showToast("验证码已发送，请稍等…");
				} else {
					showToast("请求频繁，请稍后再试…");
				}
				break;
			case REQUEST_RESET_PASSWORD_KEY:
				resultObject = new JSONObject(resultJson);
				code = 0;
				if (!resultObject.isNull("code")) {
					code = resultObject.getInt("code");
				}
				if (code == CXSDKStatusCode.SUCCESS) {
					// 静态变量中设置当前密码 //FIXME
					CXGameConfig.PASSWORD = mResetPasswordInput.getText()
							.toString();
					// 将新的密码写入账户文件中
					mHomePanel.changeLocationAccountFile();
					// 是否初始化
					if (CXGameConfig.GAMEPARAM != null) {
						accountLogin(CXGameConfig.GAMEPARAM.getApp_id(),
								CXGameConfig.ACCOUNT, CXGameConfig.PASSWORD,
								CXGameConfig.GAMEPARAM.getServerId(),
								REQUEST_LOGIN_KEY);
					} else {

						((CXMainActivity) mContext).loginResult(
								OperateType.NO_INIT, OperateType.NO_INIT);

					}

					// showViewType(SHOW_HOME_PANEL);

				} else {
					showToast(code);
				}
				break;
			case REQUEST_LOGIN_KEY:
				HashMap<String, Object> loginResult = ParseUtil
						.getLoginUserData(resultJson);
				if (loginResult != null) {
					code = (Integer) loginResult.get("code");
					if (code == CXSDKStatusCode.SUCCESS) {
						CXUser user = (CXUser) loginResult.get("data");
						if (user != null) {
							if (!CXGameConfig.isQQLogin) {
								if (user.getQuick_game() == HomePanel.QUICK_GAME) {
									// 强制用户修改密码
									mHomePanel.showChangePasswordDialgo();
								} else {
									CXGameConfig.TICKET = user.getTicket();
									CXGameConfig.USER_ID = user.getUser_id();
									CXGameConfig.USER_NAME = CXGameConfig.ACCOUNT;
									mHomePanel.rememberAccount();
									((CXMainActivity) mContext).loginResult(
											OperateType.LOGIN, code);
								}
							} else {
								CXGameConfig.TICKET = user.getTicket();
								CXGameConfig.USER_ID = user.getUser_id();
								CXGameConfig.USER_NAME = CXGameConfig.ACCOUNT;
								((CXMainActivity) mContext).loginResult(
										OperateType.LOGIN, code);
							}

						}
					} else {
						((CXMainActivity) mContext).loginResult(
								OperateType.LOGIN, code);
					}

				} else {
					((CXMainActivity) mContext).loginResult(OperateType.LOGIN,
							CXSDKStatusCode.SYSTEM_ERROR);
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
