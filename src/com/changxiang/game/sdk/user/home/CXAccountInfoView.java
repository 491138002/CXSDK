package com.changxiang.game.sdk.user.home;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.activity.CXMainActivity;
import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.net.RequestParameter;
import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.type.OperateType;
import com.changxiang.game.sdk.user.CXBasePanel;
import com.changxiang.game.sdk.user.HomePanel;
import com.changxiang.game.sdk.util.AccountPersistenceUtil;
import com.changxiang.game.sdk.util.AccountPersistenceUtil.OnAccountFileReadComplete;
import com.changxiang.game.sdk.util.ParseUtil;
import com.changxiang.game.sdk.util.StringUtil;
import com.changxiang.game.sdk.vo.CXUser;

public class CXAccountInfoView extends CXBasePanel {
	private static CXAccountInfoView mBXAccountInfoView;
	private ArrayList<HashMap<String, String>> loggedAccounts;// 之前在本机登录过的账号集合
	private HomePanel mHomePanel;
	private View mBaseView;

	private TextView mAccountUserTv;
	private ImageView mAccountSwitchBtn;
	private Button mAccountBindPhoneBtn;
	private Button mAccountChangePasswordBtn;
	private Button mAccountShowGameBtn;

	private boolean isBinded = false;

	private final int REQUEST_LOGIN_KEY = 10002;
	private final int BIND_CHECK_PHONENUMBER_KEY = 10003;

	public synchronized static CXAccountInfoView getInstance(Context context) {
		if (mBXAccountInfoView == null) {
			mBXAccountInfoView = new CXAccountInfoView(context);
		}
		return mBXAccountInfoView;
	}

	private CXAccountInfoView(Context context) {
		super(context);
		initLoggedAccountData();
		init();
		initView();
		initListener();
		initValue();
	}

	public void init() {
		mBaseView = mInflater.inflate(CXResources.layout.bx_account_info, null);
	}

	public void initView() {
		mAccountUserTv = (TextView) mBaseView
				.findViewById(CXResources.id.bx_account_info_name);
		mAccountSwitchBtn = (ImageView) mBaseView
				.findViewById(CXResources.id.bx_account_switch_info_btn);
		mAccountBindPhoneBtn = (Button) mBaseView
				.findViewById(CXResources.id.bx_account_info_bind_phone);
		if (isBinded) {
			mAccountBindPhoneBtn.setClickable(false);
			mAccountBindPhoneBtn.setTextColor(Color.GRAY);
		} else {
			mAccountBindPhoneBtn.setClickable(true);
			mAccountBindPhoneBtn.setTextColor(Color.BLACK);
		}
		mAccountChangePasswordBtn = (Button) mBaseView
				.findViewById(CXResources.id.bx_account_info_change_password);
		mAccountShowGameBtn = (Button) mBaseView
				.findViewById(CXResources.id.bx_account_info_show_game);
		if (StringUtil.isNotEmpty(CXGameConfig.ACCOUNT)) {
			mAccountUserTv.setText(CXGameConfig.NICK_NAME);
		}
	}

	public void initListener() {
		mAccountSwitchBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				mHomePanel.showViewType(HomePanel.SHOW_HOME_PANEL);
			}
		});
		mAccountBindPhoneBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (isBinded) {
					mAccountBindPhoneBtn.setClickable(false);
					mAccountBindPhoneBtn.setTextColor(Color.GRAY);
					showToast("您已绑定过手机!");
				} else {

					mHomePanel.showViewType(HomePanel.SHOW_BIND_PHONE);
				}
			}
		});
		mAccountChangePasswordBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				mHomePanel.showViewType(HomePanel.SHOW_CHANGE_PASSWORD);
			}
		});
		mAccountShowGameBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// 是否初始化
				if (CXGameConfig.GAMEPARAM != null) {

					accountLogin(CXGameConfig.GAMEPARAM.getApp_id(),
							CXGameConfig.ACCOUNT, CXGameConfig.PASSWORD,
							CXGameConfig.GAMEPARAM.getServerId(),
							CXGameConfig.USER_ID, REQUEST_LOGIN_KEY);
				} else {
					((CXMainActivity) mContext).loginResult(
							OperateType.NO_INIT, OperateType.NO_INIT);

				}
			}
		});
	}

	public void initValue() {
		if ("2".equals(CXGameConfig.ORIGIN) || "1".equals(CXGameConfig.ORIGIN)) {
			mAccountChangePasswordBtn.setClickable(false);
			mAccountChangePasswordBtn.setTextColor(Color.GRAY);
			mAccountUserTv.setTextColor(Color.GREEN);
			mAccountBindPhoneBtn.setClickable(false);
			mAccountBindPhoneBtn.setTextColor(Color.GRAY);
		} else {
			mAccountChangePasswordBtn.setClickable(true);
			mAccountChangePasswordBtn.setTextColor(Color.BLACK);
			mAccountUserTv.setTextColor(Color.WHITE);
			mAccountBindPhoneBtn.setClickable(true);
			mAccountBindPhoneBtn.setTextColor(Color.BLACK);
			if (StringUtil.isNotEmpty(CXGameConfig.ACCOUNT)
					&& StringUtil.isNotEmpty(CXGameConfig.SERVER_KEY)) {
				bindCheckPhoneNumber(CXGameConfig.USER_ID, CXGameConfig.ACCOUNT);
			}

		}
		if (StringUtil.isNotEmpty(CXGameConfig.ACCOUNT)) {
			mAccountUserTv.setText(CXGameConfig.NICK_NAME);
		}

	}

	// 异步读取以保存的账户数据
	private void initLoggedAccountData() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				loggedAccounts = (ArrayList<HashMap<String, String>>) msg.obj;
				if (loggedAccounts != null && loggedAccounts.size() > 0) {
					HashMap<String, String> account = loggedAccounts.get(0);
					CXGameConfig.ACCOUNT = account.get("name");
					CXGameConfig.NICK_NAME = account.get("nickname");
				}
			}
		};
		AccountPersistenceUtil.readAccountByFile(accountFileSavePath
				+ File.separator + accountFileName,
				new OnAccountFileReadComplete() {

					@Override
					public void onFileReadCompleteListener(
							ArrayList<HashMap<String, String>> accounts) {
						Message msg = handler.obtainMessage();
						msg.obj = accounts;
						msg.sendToTarget();

					}
				});

	}

	public View getView() {
		return mBaseView;
	}

	public void setHomePanel(HomePanel homePanel) {
		this.mHomePanel = homePanel;
	}

	public static void releaseViews() {
		mBXAccountInfoView = null;
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
			int server_id, String user_id, int key) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("username", username));
		parameter.add(new RequestParameter("password", password));
		parameter.add(new RequestParameter("user_id", String.valueOf(user_id)));
		parameter.add(new RequestParameter("app_id", String.valueOf(app_id)));
		parameter.add(new RequestParameter("origin", String
				.valueOf(CXGameConfig.ORIGIN)));
		parameter.add(new RequestParameter("server_id", String
				.valueOf(server_id)));

		startHttpRequst(CXGameConfig.HTTP_GET,
				CXGameConfig.BX_SERVICE_URL_ACCOUNT_LOGIN, parameter, true, "",
				false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, key);
	}

	/**
	 * 判断用户是否绑定了手机
	 * 
	 * @param account
	 *            用户账号
	 */
	private void bindCheckPhoneNumber(String user_id, String username) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("user_id", String.valueOf(user_id)));
		parameter.add(new RequestParameter("username", username));
		startHttpRequst(CXGameConfig.HTTP_POST,
				CXGameConfig.BX_SERVICE_URL_ALREADY_BIND_PHONE, parameter,
				true, "", false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, BIND_CHECK_PHONENUMBER_KEY);
	}

	@Override
	public void onCallBackFromThread(String resultJson, int resultCode) {
		// TODO Auto-generated method stub
		super.onCallBackFromThread(resultJson, resultCode);
		try {
			switch (resultCode) {
			case REQUEST_LOGIN_KEY:
				HashMap<String, Object> loginResult = ParseUtil
						.getLoginUserData(resultJson);
				if (loginResult != null) {
					int code = (Integer) loginResult.get("code");
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
									CXGameConfig.USER_NAME = user.getUsername();
									CXGameConfig.ACCOUNT = user.getUsername();
									CXGameConfig.NICK_NAME = user
											.getNick_name();
									CXGameConfig.ORIGIN = user.getOrigin();
									mHomePanel.rememberAccount();

									if ("3".equals(CXGameConfig.ORIGIN)
											&& isBinded == false) {
										mHomePanel
												.showViewType(HomePanel.SHOW_BIND_PHONE_AGAIN);
										return;
									}
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
			case BIND_CHECK_PHONENUMBER_KEY:
				// 是否这么判断 待确定
				int code = 0;
				String msg;
				JSONObject resultObject = new JSONObject(resultJson);
				if (!resultObject.isNull("code")) {
					code = resultObject.getInt("code");
				}
				if (!resultObject.isNull("msg")) {
					msg = resultObject.getString("msg");
				}
				if (code == CXSDKStatusCode.SUCCESS) {
					isBinded = true;
					mAccountBindPhoneBtn.setClickable(false);
					mAccountBindPhoneBtn.setTextColor(Color.GRAY);
				} else {
					mAccountBindPhoneBtn.setClickable(true);
					mAccountBindPhoneBtn.setTextColor(Color.BLACK);
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
