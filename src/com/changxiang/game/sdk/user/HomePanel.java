package com.changxiang.game.sdk.user;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.activity.CXMainActivity;
import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.net.RequestParameter;
import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.type.OperateType;
import com.changxiang.game.sdk.ui.CXCustomDialog;
import com.changxiang.game.sdk.ui.CXLoadingDialog;
import com.changxiang.game.sdk.user.home.CXAccountInfoView;
import com.changxiang.game.sdk.user.home.CXBindPhoneAgainView;
import com.changxiang.game.sdk.user.home.CXBindPhoneView;
import com.changxiang.game.sdk.user.home.CXChangeBindPhoneAgainView;
import com.changxiang.game.sdk.user.home.CXChangePasswordView;
import com.changxiang.game.sdk.user.home.CXResetPasswordView;
import com.changxiang.game.sdk.util.AccountPersistenceUtil;
import com.changxiang.game.sdk.util.AccountPersistenceUtil.OnAccountFileReadComplete;
import com.changxiang.game.sdk.util.DeviceUtil;
import com.changxiang.game.sdk.util.ParseUtil;
import com.changxiang.game.sdk.util.StringUtil;
import com.changxiang.game.sdk.vo.CXUser;



public class HomePanel extends CXBasePanel implements MainBodyItem {

	private FrameLayout mCurrentView;
	private View mLoginView = null;
	private View mRegisterView = null;
	private View mQuickPlayView = null;
	private View mFindPasswordView = null;
	private View mResetPasswordView = null;
	private View mXinLangWebView = null;
	private View mAccountInfoView = null;
	private View mBindPhoneAgainView = null;
	private View mBindPhoneView = null;
	private View mChangePasswordView = null;
	private View mChangeBindPhoneView = null;
	private CXLoadingDialog customLoadingDialog;
	private TextView mHomeAccount;
	private LinearLayout ll_text;
	private EditText mHomeAccount_text;
	private TextView mHomePassword;
	private EditText mHomePassword_text;
	private TextView mHomeFindPassword;
	private Button mHomeLogin;
	private Button mHomeRegister;
	private Button mHomeRegister1;
	private Button mXinLangLoginBtn;
	private ImageView mHomeSelectAccount;

	private Button mQuickStartPlay;
	private Button mQuickRegister;
	private Button mQuickSwitchAccount;
	private TextView mQuickAccount;
	private TextView mQuickPassword;
	private ImageView mQuickSaveRandomAccount;

	private EditText mRegisterAccount;
	private EditText mRegisterPassword;
	private EditText mRegisterPassword_sec;
	private Button mRegisterNowRegister;
	private Button mRegisterBack;
	private RelativeLayout rl_layout;
	private Button wv_back;
	private Button reload;
	private Button mFindPasswordBack;
	private ImageView mFindPWDSelectAccount;
	private EditText mFindPasswordAccount;
	private TextView mFindPasswordType;
	private WebView wv_xinlang;
	private Button mFindPasswordSendButton;

	public static final int SHOW_HOME_PANEL = 1;
	public static final int SHOW_QUICK_PALY = 2;
	public static final int SHOW_ACCOUNT_REGISTER = 3;
	public static final int SHOW_FIND_PASSWORD = 4;
	public static final int SHOW_RESET_PASSWORD = 5;
	public static final int SHOW_XINLANG_WEBVIEW = 6;
	public static final int SHOW_BIND_PHONE_AGAIN = 7;
	public static final int SHOW_BIND_PHONE = 8;
	public static final int SHOW_ACCOUNT_INFO = 9;
	public static final int SHOW_CHANGE_PASSWORD = 10;
	public static final int SHOW_CHANGE_BIND_PHONE = 11;

	private static final int OTHER_LOGIN_KEY = 30;
	private static final int REQUEST_LOGIN_KEY = 300;
	private static final int REQUEST_REGISTER_KEY = 301;
	private static final int REQUEST_RESET_PASSWORD_KEY = 302;
	private static final int REQUEST_QUICK_PLAY_REGISTER_KEY = 303;
	private static final int REQUEST_QUICK_PLAY_LOGIN_KEY = 304;
	private static final int REQUEST_PHONENUMBER_BIND_CHECK_KEY = 305;
	private static final int REQUEST_SEND_PHONENUMBER_CODE = 309;
	private static final int REQUEST_CHECK_PHONENUMBER_CODE = 310;
	private static final int REQUEST_RANDOM_ACCOUNT_CHANGE_PASSWORD_KEY = 306;
	private static final int REQUEST_GET_GAME_VERSION = 307;

	private boolean isRememdAccount = true;// 是否记住账户 值从checkBox控件获取

	private String stringRegisterAccount;// 注册账户
	private String stringRegisterPassword;// 注册密码
	private String stringRegisterPassword_sec;// 注册二次密码

	private String stringRandomAccount;// 随机账号
	private String stringRandomPassword;// 随机密码

	private String stringFindPasswordAccount;// 重置密码的账号
	private String stringResetPassword;// 重置后的密码

	private CXCustomDialog mForceChangePasswordDialog;// 强制修改密码的弹出对话框
	private String stringForceResetPassword;// 强制修改密码对话框中输入的密码

	public static final int QUICK_GAME = 1;
	public static final int COMMON_GAME = 0;
	private static final int THREE_LOGIN_KEY = 102;
	private static final int REQUEST_PLAY_LOGIN_KEY = 103;

	private ArrayList<HashMap<String, String>> loggedAccounts;// 之前在本机登录过的账号集合

	private CXMainActivity mContext;

	public String phone_Number = "";

	String yanZhengMa = "";

	public HomePanel(CXMainActivity context) {
		super(context);
		mContext = context;

	}

	@Override
	public View getView(Context context) {
		if (mCurrentView != null) {
			showViewType(SHOW_HOME_PANEL);
			return mCurrentView;
		}

		init();
		showViewType(SHOW_HOME_PANEL);
		customLoadingDialog = new CXLoadingDialog(mContext, "加载中", false);
		setInitValue();
		return mCurrentView;

	}

	private void init() {
		// 只有第一次进入程序的时候调用这个方法，做一些静态变量的重置操作
		initStaticValue();
		initLoggedAccountData();
	}

	// 异步读取以保存的账户数据
	private void initLoggedAccountData() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				loggedAccounts = (ArrayList<HashMap<String, String>>) msg.obj;
				if (mHomeAccount != null && mHomePassword != null) {
					if (loggedAccounts != null && loggedAccounts.size() > 0) {
						setInitValue();
					}
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

	// 异步读取以保存的账户数据,找回密码处
	private void initAccountData() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				loggedAccounts = (ArrayList<HashMap<String, String>>) msg.obj;
				if (mFindPasswordAccount != null
						&& mFindPasswordAccount != null) {
					if (loggedAccounts != null && loggedAccounts.size() > 0) {
						// setInitValue();
					}
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

	private void initStaticValue() {
		CXGameConfig.USER_NAME = "";
		CXGameConfig.TICKET = "";
		CXGameConfig.ACCOUNT = "";
		CXGameConfig.PASSWORD = "";
		CXGameConfig.USERTYPE = CXGameConfig.userTypeCX;
		CXGameConfig.isQQLogin = false;
	}

	private void initSubView(int type) {
		switch (type) {
		case SHOW_HOME_PANEL:
			if (mLoginView == null) {
				initLayoutByType(mContext, SHOW_HOME_PANEL);
				findLoginView();
				setSubListener(SHOW_HOME_PANEL);
			}
			break;
		case SHOW_QUICK_PALY:
			if (mQuickPlayView == null) {
				initLayoutByType(mContext, SHOW_QUICK_PALY);
				findQuickView();
				setSubListener(SHOW_QUICK_PALY);
			}
			break;
		case SHOW_FIND_PASSWORD:
			if (mFindPasswordView == null) {
				initLayoutByType(mContext, SHOW_FIND_PASSWORD);
				findFindPasswordView();
				setSubListener(SHOW_FIND_PASSWORD);
			}
			break;
		case SHOW_ACCOUNT_REGISTER:
			if (mRegisterView == null) {
				initLayoutByType(mContext, SHOW_ACCOUNT_REGISTER);
				findRegisterView();
				setSubListener(SHOW_ACCOUNT_REGISTER);
			}
			break;
		case SHOW_RESET_PASSWORD:
			if (mResetPasswordView == null) {
				initLayoutByType(mContext, SHOW_RESET_PASSWORD);
			}
			break;
		case SHOW_XINLANG_WEBVIEW:
			if (mXinLangWebView == null)
				initLayoutByType(mContext, SHOW_XINLANG_WEBVIEW);
			break;
		case SHOW_BIND_PHONE_AGAIN:
			if (mBindPhoneAgainView == null)
				initLayoutByType(mContext, SHOW_BIND_PHONE_AGAIN);
			break;
		case SHOW_BIND_PHONE:
			if (mBindPhoneView == null)
				initLayoutByType(mContext, SHOW_BIND_PHONE);
			break;
		case SHOW_ACCOUNT_INFO:
			if (mAccountInfoView == null)
				initLayoutByType(mContext, SHOW_ACCOUNT_INFO);
			break;
		case SHOW_CHANGE_PASSWORD:
			initLayoutByType(mContext, SHOW_CHANGE_PASSWORD);
			break;
		case SHOW_CHANGE_BIND_PHONE:
			if (mChangeBindPhoneView == null)
				initLayoutByType(mContext, SHOW_CHANGE_BIND_PHONE);
			break;
		}
	}

	private void initLayoutByType(Context context, int type) {
		if (mCurrentView == null)
			mCurrentView = new FrameLayout(context);
		switch (type) {
		case SHOW_HOME_PANEL:
			mLoginView = LayoutInflater.from(context).inflate(
					CXResources.layout.bx_home_panel, null);

			mCurrentView.addView(mLoginView, new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.FILL_PARENT,
					FrameLayout.LayoutParams.FILL_PARENT));
			break;
		case SHOW_QUICK_PALY:
			mQuickPlayView = LayoutInflater.from(context).inflate(
					CXResources.layout.bx_quick_play, null);
			mCurrentView.addView(mQuickPlayView, new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.FILL_PARENT,
					FrameLayout.LayoutParams.FILL_PARENT));
			break;
		case SHOW_FIND_PASSWORD:
			mFindPasswordView = LayoutInflater.from(context).inflate(
					CXResources.layout.bx_find_new_password, null);
			mCurrentView.addView(mFindPasswordView,
					new FrameLayout.LayoutParams(
							FrameLayout.LayoutParams.FILL_PARENT,
							FrameLayout.LayoutParams.FILL_PARENT));
			break;
		case SHOW_ACCOUNT_REGISTER:
			mRegisterView = LayoutInflater.from(context).inflate(
					CXResources.layout.bx_account_register, null);
			mCurrentView.addView(mRegisterView, new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.FILL_PARENT,
					FrameLayout.LayoutParams.FILL_PARENT));
			break;
		case SHOW_RESET_PASSWORD:
			mResetPasswordView = CXResetPasswordView.getInstance(mContext)
					.getView();
			mCurrentView.removeView(mResetPasswordView);
			mCurrentView.addView(mResetPasswordView,
					new FrameLayout.LayoutParams(
							FrameLayout.LayoutParams.FILL_PARENT,
							FrameLayout.LayoutParams.FILL_PARENT));
			CXResetPasswordView.getInstance(mContext).setHomePanel(this);
			break;
		case SHOW_BIND_PHONE_AGAIN:
			mBindPhoneAgainView = CXBindPhoneAgainView.getInstance(mContext)
					.getView();
			mCurrentView.removeView(mBindPhoneAgainView);
			mCurrentView.addView(mBindPhoneAgainView,
					new FrameLayout.LayoutParams(
							FrameLayout.LayoutParams.FILL_PARENT,
							FrameLayout.LayoutParams.FILL_PARENT));
			CXBindPhoneAgainView.getInstance(mContext).setHomePanel(this);
			break;
		case SHOW_BIND_PHONE:
			mBindPhoneView = CXBindPhoneView.getInstance(mContext).getView();
			mCurrentView.removeView(mBindPhoneView);
			mCurrentView.addView(mBindPhoneView, new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.FILL_PARENT,
					FrameLayout.LayoutParams.FILL_PARENT));
			CXBindPhoneView.getInstance(mContext).setHomePanel(this);
			break;
		case SHOW_ACCOUNT_INFO:
			mAccountInfoView = CXAccountInfoView.getInstance(mContext)
					.getView();
			if (mAccountInfoView.getParent() != null) {
				((FrameLayout) mAccountInfoView.getParent())
						.removeView(mAccountInfoView);
			}
			mCurrentView.removeView(mAccountInfoView);
			mCurrentView.addView(mAccountInfoView,
					new FrameLayout.LayoutParams(
							FrameLayout.LayoutParams.FILL_PARENT,
							FrameLayout.LayoutParams.FILL_PARENT));
			CXAccountInfoView.getInstance(mContext).setHomePanel(this);
			break;
		case SHOW_CHANGE_PASSWORD:
			mCurrentView.removeView(mChangePasswordView);
			CXChangePasswordView.releaseViews();
			mChangePasswordView = null;
			mChangePasswordView = CXChangePasswordView.getInstance(mContext)
					.getView();
			mCurrentView.addView(mChangePasswordView,
					new FrameLayout.LayoutParams(
							FrameLayout.LayoutParams.FILL_PARENT,
							FrameLayout.LayoutParams.FILL_PARENT));
			CXChangePasswordView.getInstance(mContext).setHomePanel(this);
			break;
		case SHOW_CHANGE_BIND_PHONE:
			mChangeBindPhoneView = CXChangeBindPhoneAgainView.getInstance(
					mContext).getView();
			mCurrentView.removeView(mChangeBindPhoneView);
			mCurrentView.addView(mChangeBindPhoneView,
					new FrameLayout.LayoutParams(
							FrameLayout.LayoutParams.FILL_PARENT,
							FrameLayout.LayoutParams.FILL_PARENT));
			CXChangeBindPhoneAgainView.getInstance(mContext).setHomePanel(this);
			break;

		}
	}

	private void findLoginView() {
		mHomeAccount = (TextView) mLoginView
				.findViewById(CXResources.id.bx_home_account);

		ll_text = (LinearLayout) mLoginView
				.findViewById(CXResources.id.ll_text);
		mHomeAccount_text = (EditText) mLoginView
				.findViewById(CXResources.id.bx_home_account_text);
		mHomePassword = (TextView) mLoginView
				.findViewById(CXResources.id.bx_home_password);
		mHomePassword_text = (EditText) mLoginView
				.findViewById(CXResources.id.bx_home_password_text);
		mHomeFindPassword = (TextView) mLoginView
				.findViewById(CXResources.id.bx_home_find_password);
		mHomeLogin = (Button) mLoginView
				.findViewById(CXResources.id.bx_home_login);
		mHomeRegister = (Button) mLoginView
				.findViewById(CXResources.id.bx_home_register);
		mHomeRegister1 = (Button) mLoginView
				.findViewById(CXResources.id.cx_home_register);
		mHomeSelectAccount = (ImageView) mLoginView
				.findViewById(CXResources.id.bx_home_select_account);
		rl_layout = (RelativeLayout) mLoginView
				.findViewById(CXResources.id.rl_layout);
		wv_back = (Button) mLoginView.findViewById(CXResources.id.wv_back);
		reload = (Button) mLoginView.findViewById(CXResources.id.reload);
		wv_xinlang = (WebView) mLoginView
				.findViewById(CXResources.id.wv_xinlang);
		mXinLangLoginBtn = (Button) mLoginView
				.findViewById(CXResources.id.bx_home_other_xinlang);

		if ("1".equals(CXGameConfig.ORIGIN) || "2".equals(CXGameConfig.ORIGIN)) {
			mHomeFindPassword.setTextColor(Color.GRAY);
		} else {
			mHomeFindPassword.setTextColor(Color.WHITE);
		}
		mHomeFindPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}

	private void findQuickView() {
		mQuickAccount = (TextView) mQuickPlayView
				.findViewById(CXResources.id.bx_quick_random_account);
		mQuickPassword = (TextView) mQuickPlayView
				.findViewById(CXResources.id.bx_quick_random_password);
		mQuickSaveRandomAccount = (ImageView) mQuickPlayView
				.findViewById(CXResources.id.bx_quick_save_random_account);
		mQuickStartPlay = (Button) mQuickPlayView
				.findViewById(CXResources.id.bx_quick_start_play);
		mQuickRegister = (Button) mQuickPlayView
				.findViewById(CXResources.id.bx_quick_register_account);
		mQuickSwitchAccount = (Button) mQuickPlayView
				.findViewById(CXResources.id.bx_quick_switch_account);

		// HashMap<String, String> randomAccount = generateRandomAccount();
		// if (mQuickAccount != null && mQuickPassword != null) {
		// mQuickAccount.setText(randomAccount.get("name"));
		// mQuickPassword.setText(randomAccount.get("password"));
		// }
	}

	private void findRegisterView() {
		mRegisterAccount = (EditText) mRegisterView
				.findViewById(CXResources.id.bx_register_account);
		mRegisterPassword = (EditText) mRegisterView
				.findViewById(CXResources.id.bx_register_password);
		mRegisterPassword_sec = (EditText) mRegisterView
				.findViewById(CXResources.id.bx_register_sec_password);
		mRegisterNowRegister = (Button) mRegisterView
				.findViewById(CXResources.id.bx_register_now_register);
		mRegisterBack = (Button) mRegisterView
				.findViewById(CXResources.id.bx_back);
	}

	private void findFindPasswordView() {
		mFindPasswordBack = (Button) mFindPasswordView
				.findViewById(CXResources.id.bx_back);
		mFindPWDSelectAccount = (ImageView) mFindPasswordView
				.findViewById(CXResources.id.bx_home_select_account);
		mFindPasswordAccount = (EditText) mFindPasswordView
				.findViewById(CXResources.id.bx_find_psw_account);
		mFindPasswordType = (TextView) mFindPasswordView
				.findViewById(CXResources.id.bx_find_password_type);
		mFindPasswordSendButton = (Button) mFindPasswordView
				.findViewById(CXResources.id.bx_find_psw_next);
	}

	private void setSubListener(int type) {
		switch (type) {
		case SHOW_HOME_PANEL:
			setHomeListener();
			break;
		case SHOW_QUICK_PALY:
			setQuickLisener();
			break;
		case SHOW_FIND_PASSWORD:
			setFindListener();
			break;
		case SHOW_ACCOUNT_REGISTER:
			setRegisterListener();
			break;
		}
	}

	private WebSettings settings;
	
	private void setHomeListener() {

		mHomeLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// BFGameConfig.ACCOUNT =
				// mHomeAccount.getText().toString().trim();
				// BFGameConfig.PASSWORD = mHomePassword.getText().toString()
				// .trim();

				// 是否初始化
				if (CXGameConfig.GAMEPARAM != null) {
					if (mHomeAccount_text.getVisibility() == View.VISIBLE) {
						CXGameConfig.ACCOUNT = mHomeAccount_text.getText()
								.toString();
						CXGameConfig.PASSWORD = mHomePassword_text.getText()
								.toString();
						CXGameConfig.USER_ID = "";
						CXGameConfig.ORIGIN = "0";
						if (TextUtils.isEmpty(CXGameConfig.ACCOUNT)
								|| TextUtils.isEmpty(CXGameConfig.PASSWORD)) {
							showToast("请输入账户和密码");
							return;
						}

						if (CXGameConfig.ACCOUNT.length() < 4
								|| CXGameConfig.ACCOUNT.length() > 16) {
							showToast("请输入4-16位账户");
							return;
						}

						if (CXGameConfig.PASSWORD.length() < 6
								|| CXGameConfig.PASSWORD.length() > 20) {
							showToast("请输入6-20位密码");
							return;
						}
						if (CXGameConfig.ACCOUNT.indexOf(" ") != -1) {
							showToast("用户名不能有空格");
						}
						if (CXGameConfig.PASSWORD.indexOf(" ") != -1) {
							showToast("密码不能有空格");
						}
					}

					accountLogin(CXGameConfig.USER_ID,
							CXGameConfig.GAMEPARAM.getApp_id(),
							CXGameConfig.ACCOUNT, CXGameConfig.PASSWORD,
							CXGameConfig.GAMEPARAM.getServerId(),
							REQUEST_LOGIN_KEY);
				} else {

					mContext.loginResult(OperateType.NO_INIT,
							OperateType.NO_INIT);

				}
			}
		});
		mHomeRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showViewType(SHOW_QUICK_PALY);
			}
		});

		mHomeRegister1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showViewType(SHOW_ACCOUNT_REGISTER);
			}
		});
		mHomeSelectAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (loggedAccounts != null && loggedAccounts.size() > 0)
					showAccountSelectDialog();
				else {
					mHomeAccount.setVisibility(View.INVISIBLE);
					mHomePassword.setVisibility(View.INVISIBLE);
					mHomeAccount_text.setVisibility(View.VISIBLE);
					mHomePassword_text.setVisibility(View.VISIBLE);

					showToast("没有已记录的登录账户");
				}
			}

		});

		mHomeFindPassword.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if ("1".equals(CXGameConfig.ORIGIN)
						|| "2".equals(CXGameConfig.ORIGIN)) {
					mHomeFindPassword.setTextColor(Color.GRAY);
					return;
				} else {
					showViewType(SHOW_FIND_PASSWORD);
				}

			}
		});
		wv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wv_xinlang.setVisibility(View.GONE);
				rl_layout.setVisibility(View.GONE);
				showViewType(SHOW_HOME_PANEL);
			}
		});
		reload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wv_xinlang.reload();
			}
		});
		
		mXinLangLoginBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// authorize(new SinaWeibo(mContext));
				wv_xinlang.setVisibility(View.VISIBLE);
				rl_layout.setVisibility(View.VISIBLE);
				String str = CXGameConfig.XINLANG_LOGIN_URL + "&app_id="
						+ CXGameConfig.GAMEPARAM.getApp_id() + "&server_id="
						+ CXGameConfig.GAMEPARAM.getServerId()
						+ "&device_name="
						+ (DeviceUtil.getDeviceModel()).replace(" ", "")
						+ "&imei=" + CXGameConfig.PHONE_IMEI
						+ "&system=android&os_version="
						+ DeviceUtil.getSDKVersion();
				settings = wv_xinlang.getSettings();
				settings.setJavaScriptEnabled(true);
				settings.setSavePassword(false);
				settings.setSaveFormData(false);

				wv_xinlang.loadUrl(str);
				System.out.println("xinlang_URL=="+str);
			
				wv_xinlang.addJavascriptInterface(new Object() {
					public void login(String user_id, String username,
							String password, String origin) {

						if (CXGameConfig.GAMEPARAM != null) {
							threeLogin(user_id,
									CXGameConfig.GAMEPARAM.getApp_id(),
									username, password,
									CXGameConfig.GAMEPARAM.getServerId(),
									origin);
						}

					}
				}, "login");

				wv_xinlang.setWebChromeClient(new WebChromeClient() {
					public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
						super.onProgressChanged(view, progress);
						if (progress == 100) {
							if (customLoadingDialog != null
									&& customLoadingDialog.isShowing()) {
								customLoadingDialog.dismiss();
							}
						} else {
							if (customLoadingDialog != null
									&& !customLoadingDialog.isShowing()) {
								customLoadingDialog.show();
							}
						}
					}
				});
				wv_xinlang.setWebViewClient(new WebViewClient() {
				});
			}
		});

	}

	private void setQuickLisener() {
		mQuickStartPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CXGameConfig.ACCOUNT = mQuickAccount.getText().toString();
				CXGameConfig.PASSWORD = mQuickPassword.getText().toString();

				CXGameConfig.USERTYPE = CXGameConfig.userTypeQuick;
				// 注册接口
				if (CXGameConfig.GAMEPARAM != null) {
					accountQuickRegister(CXGameConfig.GAMEPARAM.getApp_id(),
							CXGameConfig.ACCOUNT, CXGameConfig.PASSWORD,
							CXGameConfig.GAMEPARAM.getServerId());
				} else {
					showToast("未初始化完成，无法注册，请重新注册");
				}
			}
		});
		mQuickRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showViewType(SHOW_ACCOUNT_REGISTER);
			}
		});
		mQuickSwitchAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showViewType(SHOW_HOME_PANEL);
			}
		});
		mQuickSaveRandomAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StringBuffer msgText = new StringBuffer();
				msgText.append("欢迎进入畅想游戏。您的账号：");
				msgText.append(mQuickAccount.getText().toString().trim());
				msgText.append("，密码为：");
				msgText.append(mQuickPassword.getText().toString().trim());
				msgText.append("请保存好账号以便下次进行游戏登录。祝您游戏愉快。");

				Uri smsToUri = Uri.parse("smsto:");// 联系人地址
				Intent mIntent = new Intent(
						android.content.Intent.ACTION_SENDTO, smsToUri);
				mIntent.putExtra("sms_body", msgText.toString());// 短信的内容
				mContext.startActivity(mIntent);
			}
		});
	}

	private void setRegisterListener() {

		mRegisterNowRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stringRegisterAccount = mRegisterAccount.getText().toString()
						.trim();
				stringRegisterPassword = mRegisterPassword.getText().toString()
						.trim();
				stringRegisterPassword_sec = mRegisterPassword_sec.getText()
						.toString().trim();
				if (TextUtils.isEmpty(stringRegisterAccount)
						|| TextUtils.isEmpty(stringRegisterPassword)) {
					showToast("请输入注册的账号和密码");
					return;
				} else if (stringRegisterAccount.length() < 4
						|| stringRegisterAccount.length() > 16) {
					showToast("请输入4-16位的账号");
					return;
				} else if ("123456".equals(stringRegisterAccount)) {
					showToast("用户已存在");
					return;
				} else if (stringRegisterPassword.length() < 6
						|| stringRegisterPassword.length() > 20) {
					showToast("请输入6-20位的密码");
					return;
				} else if (stringRegisterAccount.indexOf(" ") != -1) {
					showToast("用户名不能有空格");
					return;
				} else if (stringRegisterPassword.indexOf(" ") != -1) {
					showToast("密码不能有空格");
					return;
				} else if (TextUtils.isEmpty(stringRegisterPassword_sec)) {
					showToast("请输入确认密码");
					return;
				} else if (!stringRegisterPassword_sec
						.equals(stringRegisterPassword)) {
					showToast("两次输入密码不一致");
					return;
				}
				// 注册接口
				if (CXGameConfig.GAMEPARAM != null) {

					accountRegister(CXGameConfig.GAMEPARAM.getApp_id(),
							stringRegisterAccount, stringRegisterPassword,
							CXGameConfig.GAMEPARAM.getServerId());
				} else {
					showToast("游戏未初始化,无法注册,请重试");
				}

			}
		});

		mRegisterBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if (!TextUtils.isEmpty(stringRegisterAccount)
				// && !TextUtils.isEmpty(stringRegisterPassword)) {
				// CXGameConfig.ACCOUNT = stringRegisterAccount;
				// CXGameConfig.PASSWORD = stringRegisterPassword;
				// // BXGameConfig.USERTYPE =
				// // mRegisterTypeBaofengCheck.isChecked() ? "1" : "0";
				// }
				showViewType(SHOW_HOME_PANEL);
			}
		});
	}

	private void setFindListener() {

		mFindPasswordBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showViewType(SHOW_HOME_PANEL);

				mFindPasswordType.setText("");
			}
		});
		mFindPWDSelectAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (loggedAccounts != null && loggedAccounts.size() > 0)
					showFindPasswordDialog();
				else {
					showToast("没有已记录的登录账户");
				}
			}
		});
		mFindPasswordSendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String account = mFindPasswordAccount.getText()
						.toString();
				if (StringUtil.isEmpty(account)) {
					showToast("请输入账号");
					return;
				}
				bindCheckPhoneNumber(CXGameConfig.USER_ID, account);

			}
		});
	}

	private void setInitValue() {

		// 如果之前有用户登录过，把上次的用户account,password填入输入框中
		if (loggedAccounts != null && loggedAccounts.size() > 0) {

			mHomeAccount.setVisibility(View.VISIBLE);
			mHomePassword.setVisibility(View.VISIBLE);
			mHomeAccount_text.setVisibility(View.INVISIBLE);
			mHomePassword_text.setVisibility(View.INVISIBLE);
			if ("1".equals(CXGameConfig.ORIGIN)
					|| "2".equals(CXGameConfig.ORIGIN)) {
				mHomeAccount.setTextColor(Color.GREEN);
				mHomeFindPassword.setTextColor(Color.GRAY);
			} else {
				mHomeAccount.setTextColor(Color.WHITE);
				mHomeFindPassword.setTextColor(Color.WHITE);
			}
			mHomeAccount.setText(CXGameConfig.NICK_NAME);
			mHomePassword.setText(CXGameConfig.PASSWORD);
			HashMap<String, String> account = loggedAccounts.get(0);
			if (mHomeAccount != null && mHomePassword != null) {
				CXGameConfig.ACCOUNT = account.get("name");
				CXGameConfig.PASSWORD = account.get("password");
				CXGameConfig.USERTYPE = account.get("userType");
				CXGameConfig.TICKET = account.get("ticket");
				CXGameConfig.NICK_NAME = account.get("nickname");
				CXGameConfig.ORIGIN = account.get("origin");
				// if (StringUtil.isNotEmpty(account.get("user_id"))
				// && StringUtil.isNumber(account.get("user_id")))
				CXGameConfig.USER_ID = account.get("user_id");

				showViewType(SHOW_ACCOUNT_INFO);
				if (StringUtil.isNotEmpty(CXGameConfig.PASSWORD)) {

					if ("1".equals(CXGameConfig.ORIGIN)
							|| "2".equals(CXGameConfig.ORIGIN)) {
						mHomeAccount.setTextColor(Color.GREEN);
						mHomeFindPassword.setTextColor(Color.GRAY);
					} else {
						mHomeAccount.setTextColor(Color.WHITE);
						mHomeFindPassword.setTextColor(Color.WHITE);
					}
					mHomeAccount.setText(CXGameConfig.NICK_NAME);
					mHomePassword.setText(CXGameConfig.PASSWORD);
					// mHomeAccount.setSelection(BFGameConfig.ACCOUNT.trim()
					// .length());
					// mHomePassword.setSelection(BFGameConfig.PASSWORD.trim()
					// .length());
				}
			}
		} else {
			mHomeAccount.setVisibility(View.INVISIBLE);
			mHomePassword.setVisibility(View.INVISIBLE);
			mHomeAccount_text.setVisibility(View.VISIBLE);
			mHomePassword_text.setVisibility(View.VISIBLE);
			mHomeAccount_text.setText("");
			mHomePassword_text.setText("");
		}

	}

	private void setFindPasswordValue() {

		// 如果之前有用户登录过，把上次的用户account,password填入输入框中
		if (loggedAccounts != null && loggedAccounts.size() > 0) {
			HashMap<String, String> account = loggedAccounts.get(0);
			if (mFindPasswordAccount != null) {
				// Config.ACCOUNT = account.get("name");

				if (mHomeAccount_text.getVisibility() == View.VISIBLE) {
					mFindPasswordAccount.setText(mHomeAccount_text.getText()
							.toString());
				} else {

					mFindPasswordAccount.setText(CXGameConfig.ACCOUNT);
					mFindPasswordAccount.setSelection(CXGameConfig.ACCOUNT
							.trim().length());
				}
			}
		}

	}

	@Override
	public void onCallBackFromThread(String resultJson, int resultCode) {
		// TODO Auto-generated method stub
		super.onCallBackFromThread(resultJson, resultCode);
		if (!TextUtils.isEmpty(resultJson)) {
			try {
				switch (resultCode) {
				case REQUEST_LOGIN_KEY: {
					HashMap<String, Object> loginResult = ParseUtil
							.getLoginUserData(resultJson);
					if (loginResult != null) {
						int code = (Integer) loginResult.get("code");
						if (code == CXSDKStatusCode.SUCCESS) {
							CXUser user = (CXUser) loginResult.get("data");
							if (user != null) {
								if (user.getQuick_game() == QUICK_GAME) {
									showChangePasswordDialgo();
								} else {
									CXGameConfig.TICKET = user.getTicket();
									CXGameConfig.USER_ID = user.getUser_id();
									CXGameConfig.USER_NAME = user.getUsername();
									CXGameConfig.PASSWORD = user.getPassword();
									CXGameConfig.ACCOUNT = user.getUsername();
									CXGameConfig.NICK_NAME = user
											.getNick_name();
									CXGameConfig.ORIGIN = user.getOrigin();

									rememberAccount();
									mContext.loginResult(OperateType.LOGIN,
											code);
								}

							}
						} else {
							mContext.loginResult(OperateType.LOGIN, code);
						}

					} else {
						mContext.loginResult(OperateType.LOGIN,
								CXSDKStatusCode.SYSTEM_ERROR);
					}
				}
					break;

				case THREE_LOGIN_KEY: {
					HashMap<String, Object> loginResult = ParseUtil
							.getLoginUserData(resultJson);
					if (loginResult != null) {
						int code = (Integer) loginResult.get("code");
						if (code == CXSDKStatusCode.SUCCESS) {
							CXUser user = (CXUser) loginResult.get("data");
							if (user != null) {
								CXGameConfig.TICKET = user.getTicket();
								CXGameConfig.USER_ID = user.getUser_id();
								CXGameConfig.USER_NAME = user.getUsername();
								CXGameConfig.PASSWORD = user.getPassword();
								CXGameConfig.ACCOUNT = user.getUsername();
								CXGameConfig.NICK_NAME = user.getNick_name();
								CXGameConfig.ORIGIN = user.getOrigin();

								rememberAccount();
								mContext.loginResult(OperateType.LOGIN, code);

							}
						} else {
							mContext.loginResult(OperateType.LOGIN, code);
						}

					} else {
						mContext.loginResult(OperateType.LOGIN,
								CXSDKStatusCode.SYSTEM_ERROR);
					}
				}
					break;
				case OTHER_LOGIN_KEY: {
					HashMap<String, Object> loginResult = ParseUtil
							.getLoginUserData(resultJson);
					if (loginResult != null) {
						int code = (Integer) loginResult.get("code");
						if (code == CXSDKStatusCode.SUCCESS) {
							CXUser user = (CXUser) loginResult.get("data");

							CXGameConfig.isQQLogin = true;
							if (user != null) {

								CXGameConfig.TICKET = user.getTicket();
								CXGameConfig.USER_ID = user.getUser_id();
								CXGameConfig.USER_NAME = user.getUsername();
								CXGameConfig.NICK_NAME = user.getNick_name();
								CXGameConfig.ACCOUNT = user.getUsername();
								CXGameConfig.PASSWORD = user.getPassword();
								CXGameConfig.ORIGIN = user.getOrigin();

								rememberAccount();
								mContext.loginResult(OperateType.LOGIN, code);
							}
						} else {
							mContext.loginResult(OperateType.LOGIN, code);
						}

					} else {
						mContext.loginResult(OperateType.LOGIN,
								CXSDKStatusCode.SYSTEM_ERROR);
					}
				}
					break;
				case REQUEST_REGISTER_KEY: {
					HashMap<String, Object> registerResult = ParseUtil
							.getLoginUserData(resultJson);
					if (registerResult != null) {
						int code = (Integer) registerResult.get("code");

						if (code == CXSDKStatusCode.SUCCESS) {
							CXUser user = (CXUser) registerResult.get("data");
							if (user != null) {
								// 注册成功
								showToast("注册成功");
								CXGameConfig.TICKET = user.getTicket();
								CXGameConfig.USER_ID = user.getUser_id();
								CXGameConfig.ACCOUNT = user.getUsername();
								CXGameConfig.USER_NAME = user.getUsername();
								CXGameConfig.PASSWORD = user.getPassword();
								CXGameConfig.NICK_NAME = user.getNick_name();
								CXGameConfig.ORIGIN = user.getOrigin();
								rememberAccount();
								//登录
//								registerLogin(registerResult);
								// 隐藏软键盘
								InputMethodManager imm = (InputMethodManager) mContext
										.getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.toggleSoftInput(0,
										InputMethodManager.HIDE_NOT_ALWAYS);
//								// 显示绑定手机页面
								showViewType(SHOW_BIND_PHONE_AGAIN);
							}
						} else {
							showToast(code);
						}
					} else {
						showToast("注册失败 ");
					}

				}
					break;
				case REQUEST_PHONENUMBER_BIND_CHECK_KEY: {
					resultJson = resultJson.replace("\"data\":[]",
							"\"data\":{}");
					JSONObject resultObject = new JSONObject(resultJson);
					int code = 0;
					if (!resultObject.isNull("code")) {
						code = resultObject.getInt("code");
					}
					if (code == CXSDKStatusCode.SUCCESS) {
						showViewType(SHOW_RESET_PASSWORD);
					} else {
						 showToast("未绑定手机，请联系客服");
						 //盗梦英雄客服QQ1489595180
						mFindPasswordType
								.setText("未绑定手机号，通过联系客服QQ找回密码\n客服QQ:1489595180");
					}

				}
					break;
				case REQUEST_RESET_PASSWORD_KEY: {
					JSONObject resultObject = new JSONObject(resultJson);
					int code = 0;
					if (!resultObject.isNull("code")) {
						code = resultObject.getInt("code");
					}
					if (code == CXSDKStatusCode.SUCCESS) {
						// 静态变量中设置当前密码
						CXGameConfig.PASSWORD = stringResetPassword;
						// 将新的密码写入账户文件中
						changeLocationAccountFile();
						// 是否初始化
						if (CXGameConfig.GAMEPARAM != null) {
							accountLogin(CXGameConfig.USER_ID,
									CXGameConfig.GAMEPARAM.getApp_id(),
									CXGameConfig.ACCOUNT,
									CXGameConfig.PASSWORD,
									CXGameConfig.GAMEPARAM.getServerId(),
									REQUEST_LOGIN_KEY);
						} else {

							mContext.loginResult(OperateType.NO_INIT,
									OperateType.NO_INIT);

						}

						// showViewType(SHOW_HOME_PANEL);

					} else {
						showToast(code);
					}
				}
					break;
				case REQUEST_QUICK_PLAY_REGISTER_KEY: {
					HashMap<String, Object> registerResult = ParseUtil
							.getLoginUserData(resultJson);
					if (registerResult != null) {
						int code = (Integer) registerResult.get("code");

						if (code == CXSDKStatusCode.SUCCESS) {
							CXUser user = (CXUser) registerResult.get("data");
							CXGameConfig.USER_ID = user.getUser_id();
							quickRegisterLogin(registerResult);
						} else if (code == CXSDKStatusCode.USER_EXIST) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									mContext);
							builder.setMessage("游客已存在,请点击确定重新生成");
							builder.setPositiveButton("重新生成",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											generateRandomAccount();
										}
									});
							builder.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									});
							builder.show();
						} else {
							showToast(code);
						}

					}
				}
					break;
				case REQUEST_QUICK_PLAY_LOGIN_KEY: {
					HashMap<String, Object> loginResult = ParseUtil
							.getLoginUserData(resultJson);
					if (loginResult != null) {
						int code = (Integer) loginResult.get("code");

						if (code == CXSDKStatusCode.SUCCESS) {
							CXUser user = (CXUser) loginResult.get("data");
							if (user != null) {
								CXGameConfig.TICKET = user.getTicket();
								CXGameConfig.USER_ID = user.getUser_id();
								CXGameConfig.ACCOUNT = user.getUsername();
								CXGameConfig.USER_NAME = user.getUsername();
								CXGameConfig.PASSWORD = user.getPassword();
								CXGameConfig.NICK_NAME = user.getNick_name();
								CXGameConfig.ORIGIN = user.getOrigin();
								// addAccount();
								rememberAccount();
							}
						}
						mContext.loginResult(OperateType.QUICK_REGISTER, code);
					} else {
						mContext.loginResult(OperateType.QUICK_REGISTER,
								CXSDKStatusCode.SYSTEM_ERROR);
					}
				}
					break;
				case REQUEST_PLAY_LOGIN_KEY: {
					HashMap<String, Object> loginResult = ParseUtil
							.getLoginUserData(resultJson);
					if (loginResult != null) {
						int code = (Integer) loginResult.get("code");
						
						if (code == CXSDKStatusCode.SUCCESS) {
							showToast("注册成功");
							// 显示绑定手机页面
							showViewType(SHOW_BIND_PHONE_AGAIN);
						}
					} else {
						mContext.loginResult(OperateType.QUICK_REGISTER,
								CXSDKStatusCode.SYSTEM_ERROR);
					}
				}
				break;
				case REQUEST_RANDOM_ACCOUNT_CHANGE_PASSWORD_KEY: {

					JSONObject resultObject = new JSONObject(resultJson);
					int code = 0;
					if (!resultObject.isNull("code")) {
						code = resultObject.getInt("code");
					}
					if (code == CXSDKStatusCode.SUCCESS) {
						showToast("修改密码成功");
						if (mForceChangePasswordDialog != null)
							mForceChangePasswordDialog.dismiss();
						// 静态变量中设置当前密码
						CXGameConfig.PASSWORD = stringForceResetPassword;
						// 将新的密码写入账户文件中
						changeLocationAccountFile();

						mHomePassword.setText(stringForceResetPassword);
					} else {
						showToast(code);
					}

				}
					break;
				case REQUEST_GET_GAME_VERSION:
					HashMap<String, Object> gameVersionResult = ParseUtil
							.getGameVersionData(resultJson);

					if (gameVersionResult != null) {
						int code = (Integer) gameVersionResult.get("code");

						if (code == CXSDKStatusCode.SUCCESS) {
							String downloadUrl = (String) gameVersionResult
									.get("downloadurl");
							String version = (String) gameVersionResult
									.get("version");
						}

					}
					break;
				case REQUEST_SEND_PHONENUMBER_CODE:
					JSONObject resultObject = new JSONObject(resultJson);
					int code = 0;
					if (!resultObject.isNull("code")) {
						code = resultObject.getInt("code");
					}
					if (code == CXSDKStatusCode.SUCCESS) {
						JSONObject data = resultObject.optJSONObject("data");
						yanZhengMa = data.optString("validatecode");
					}
					break;
				case REQUEST_CHECK_PHONENUMBER_CODE:
					JSONObject checkObject = new JSONObject(resultJson);
					int check_code = 0;
					if (!checkObject.isNull("code")) {
						check_code = checkObject.getInt("code");
					}
					if (check_code == CXSDKStatusCode.SUCCESS) {
						showViewType(SHOW_RESET_PASSWORD);
					} else {
						showToast("验证码输入错误，请重新输入");
					}
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 用户登录
	 * 
	 * @param app_id
	 *            app唯一编号,由畅想游戏 网游戏生成并提供
	 * @param account
	 *            畅想游戏 账号
	 * @param password
	 *            畅想游戏 密码
	 * @param server_id
	 *            联运服务器编号:用户验证后需要跳转的服务器编号没有为0
	 */
	private void threeLogin(String user_id, int app_id, String username,
			String password, int server_id, String origin) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("username", username));
		parameter.add(new RequestParameter("password", password));
		parameter.add(new RequestParameter("user_id", String.valueOf(user_id)));
		parameter.add(new RequestParameter("origin", origin));
		parameter.add(new RequestParameter("app_id", String.valueOf(app_id)));
		parameter.add(new RequestParameter("server_id", String
				.valueOf(server_id)));

		startHttpRequst(CXGameConfig.HTTP_GET,
				CXGameConfig.BX_SERVICE_URL_ACCOUNT_LOGIN, parameter, true, "",
				false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, THREE_LOGIN_KEY);
	}

	public void vali(int app_id, int server_id, String ticket) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("app_id", String.valueOf(app_id)));
		parameter.add(new RequestParameter("server_id", String
				.valueOf(server_id)));
		parameter.add(new RequestParameter("ticket", String.valueOf(ticket)));
		parameter.add(new RequestParameter("format", "json"));

		startHttpRequst(CXGameConfig.HTTP_GET,
				CXGameConfig.BX_SERVICE_URL_ACCOUNT_LOGIN, parameter, true, "",
				false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, THREE_LOGIN_KEY);
	}

	/**
	 * 随机生成的账户，修改密码
	 * 
	 * @param account
	 *            冰雪SDK平台账号
	 * @param app_id
	 *            运营商id
	 * @param server_id
	 *            服务器id
	 * @param newPassword
	 *            用户新密码
	 */
	protected void randomAccountChangePassword(String account, int app_id,
			int server_id, String newPassword) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("account", account));
		parameter.add(new RequestParameter("app_id", String.valueOf(app_id)));
		parameter.add(new RequestParameter("server_id", String
				.valueOf(server_id)));
		parameter.add(new RequestParameter("password", newPassword));

		startHttpRequst(CXGameConfig.HTTP_POST,
				CXGameConfig.BX_QUICK_RESET_PASSWORD, parameter, true, "",
				false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT,
				REQUEST_RANDOM_ACCOUNT_CHANGE_PASSWORD_KEY);
	}

	/**
	 * 游客登录
	 * @param registerResult
	 */
	private void quickRegisterLogin(HashMap<String, Object> registerResult) {
		if (registerResult == null)
			return;
		// 是否初始化
		if (CXGameConfig.GAMEPARAM != null) {
			quickAccountLogin(CXGameConfig.USER_ID,
					CXGameConfig.GAMEPARAM.getApp_id(), CXGameConfig.ACCOUNT,
					CXGameConfig.PASSWORD,
					CXGameConfig.GAMEPARAM.getServerId(),
					REQUEST_QUICK_PLAY_LOGIN_KEY);
		}
	}
	/**
	 * 注册成功后登录
	 * @param registerResult
	 */
	private void registerLogin(HashMap<String, Object> registerResult) {
		if (registerResult == null)
			return;
		// 是否初始化
		if (CXGameConfig.GAMEPARAM != null) {
			accountLogin(CXGameConfig.USER_ID,
					CXGameConfig.GAMEPARAM.getApp_id(), CXGameConfig.ACCOUNT,
					CXGameConfig.PASSWORD,
					CXGameConfig.GAMEPARAM.getServerId(),
					REQUEST_PLAY_LOGIN_KEY);
		}
	}
	/**
	 * 因为不改动isRememdAccount的值 所以写一个为快速游戏成功后单独调用保存账户的方法
	 */
	private void addAccount() {
		HashMap<String, String> account = new HashMap<String, String>();
		account.put("name", CXGameConfig.ACCOUNT);
		account.put("password", CXGameConfig.PASSWORD);
		account.put("userType", CXGameConfig.USERTYPE);
		if (loggedAccounts == null) {
			loggedAccounts = new ArrayList<HashMap<String, String>>();
		}
		boolean isExist = false;
		HashMap<String, String> existAccount = null;
		for (int i = 0; i < loggedAccounts.size(); i++) {
			existAccount = loggedAccounts.get(i);
			if (CXGameConfig.ACCOUNT.equals(existAccount.get("name"))) {
				// 如果之前已经保存过这个账户，则替换
				existAccount.put("name", CXGameConfig.ACCOUNT);
				existAccount.put("password", CXGameConfig.PASSWORD);
				existAccount.put("userType", CXGameConfig.USERTYPE);
				isExist = true;
				break;
			}
		}
		if (!isExist) {
			// 之前没有保存过这个账户，添加进去
			loggedAccounts.add(0, account);
		} else {
			loggedAccounts.remove(existAccount);
			loggedAccounts.add(0, existAccount);
		}
		AccountPersistenceUtil.writerAccountToFile(accountFileSavePath
				+ File.separator + accountFileName, loggedAccounts);
	}

	public void rememberAccount() {
		// 记住登录状态
		if (isRememdAccount) {
			HashMap<String, String> account = new HashMap<String, String>();
			account.put("name", CXGameConfig.ACCOUNT);
			account.put("password", CXGameConfig.PASSWORD);
			account.put("nickname", CXGameConfig.NICK_NAME);
			account.put("token", CXGameConfig.TOKEN);
			account.put("origin", CXGameConfig.ORIGIN);
			account.put("ticket", CXGameConfig.TICKET);
			account.put("user_id", CXGameConfig.USER_ID);

			if (loggedAccounts == null) {
				loggedAccounts = new ArrayList<HashMap<String, String>>();
			}
			boolean isExist = false;
			HashMap<String, String> existAccount = null;
			for (int i = 0; i < loggedAccounts.size(); i++) {
				existAccount = loggedAccounts.get(i);
				if (CXGameConfig.ACCOUNT.equals(existAccount.get("name"))) {
					// 如果之前已经保存过这个账户，则替换
					existAccount.put("name", CXGameConfig.ACCOUNT);
					existAccount.put("password", CXGameConfig.PASSWORD);
					existAccount.put("token", CXGameConfig.TOKEN);
					existAccount.put("nickname", CXGameConfig.NICK_NAME);
					existAccount.put("user_id", CXGameConfig.USER_ID);
					existAccount.put("ticket", CXGameConfig.TICKET);
					existAccount.put("origin", CXGameConfig.ORIGIN);
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				// 之前没有保存过这个账户，添加进去
				loggedAccounts.add(0, account);
			} else {
				loggedAccounts.remove(existAccount);
				loggedAccounts.add(0, existAccount);
			}
			AccountPersistenceUtil.writerAccountToFile(accountFileSavePath
					+ File.separator + accountFileName, loggedAccounts);
		} else {
			if (loggedAccounts == null) {
				return;
			}
			// 如果是从列表中选择的之前账号
			HashMap<String, String> account = new HashMap<String, String>();
			account.put("name", CXGameConfig.ACCOUNT);
			account.put("password", CXGameConfig.PASSWORD);
			account.put("user_id", String.valueOf(CXGameConfig.USER_ID));
			boolean isExist = false;
			HashMap<String, String> existAccount = null;
			for (int i = 0; i < loggedAccounts.size(); i++) {
				existAccount = loggedAccounts.get(i);
				if (CXGameConfig.ACCOUNT.equals(existAccount.get("name"))) {
					// 如果之前已经保存过这个账户，则替换
					existAccount.put("name", CXGameConfig.ACCOUNT);
					existAccount.put("password", CXGameConfig.PASSWORD);
					existAccount.put("user_id", CXGameConfig.USER_ID);
					existAccount.put("nickname", CXGameConfig.NICK_NAME);
					existAccount.put("origin", CXGameConfig.ORIGIN);
					existAccount.put("ticket", CXGameConfig.TICKET);
					existAccount.put("token", CXGameConfig.TOKEN);
					isExist = true;
					break;
				}
			}
			if (isExist) {
				loggedAccounts.remove(existAccount);
				loggedAccounts.add(0, existAccount);
			}
			AccountPersistenceUtil.writerAccountToFile(accountFileSavePath
					+ File.separator + accountFileName, loggedAccounts);
		}

	}

	/**
	 * 用户登录
	 * 
	 * @param app_id
	 *            app唯一编号,由畅想游戏 网游戏生成并提供
	 * @param account
	 *            畅想游戏 账号
	 * @param password
	 *            畅想游戏 密码
	 * @param server_id
	 *            联运服务器编号:用户验证后需要跳转的服务器编号没有为0
	 */
	private void accountLogin(String user_id, int app_id, String username,
			String password, int server_id, int key) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("username", username));
		parameter.add(new RequestParameter("password", password));
		parameter.add(new RequestParameter("user_id", String.valueOf(user_id)));
		parameter.add(new RequestParameter("origin", String
				.valueOf(CXGameConfig.ORIGIN)));
		parameter.add(new RequestParameter("app_id", String.valueOf(app_id)));
		parameter.add(new RequestParameter("server_id", String
				.valueOf(server_id)));

		startHttpRequst(CXGameConfig.HTTP_GET,
				CXGameConfig.BX_SERVICE_URL_ACCOUNT_LOGIN, parameter, true, "",
				false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, key);
	}

	/**
	 * 用户登录
	 * 
	 * @param app_id
	 *            app唯一编号,由畅想游戏 网游戏生成并提供
	 * @param account
	 *            畅想游戏 账号
	 * @param password
	 *            畅想游戏 密码
	 * @param server_id
	 *            联运服务器编号:用户验证后需要跳转的服务器编号没有为0
	 */
	private void quickAccountLogin(String user_id, int app_id, String username,
			String password, int server_id, int key) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("username", username));
		parameter.add(new RequestParameter("password", password));
		parameter.add(new RequestParameter("user_id", String.valueOf(user_id)));
		parameter.add(new RequestParameter("origin", String.valueOf(3)));
		parameter.add(new RequestParameter("app_id", String.valueOf(app_id)));
		parameter.add(new RequestParameter("server_id", String
				.valueOf(server_id)));

		startHttpRequst(CXGameConfig.HTTP_GET,
				CXGameConfig.BX_SERVICE_URL_ACCOUNT_LOGIN, parameter, true, "",
				false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, key);
	}

//	private void otherLogin(int app_id, String nick_name, int server_id,
//			String open_id, String client) {
//		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
//		parameter.add(new RequestParameter("channel", "android"));
//		parameter.add(new RequestParameter("client", client));
//		parameter.add(new RequestParameter("app_id", String.valueOf(app_id)));
//		parameter.add(new RequestParameter("nick_name", nick_name));
//		parameter.add(new RequestParameter("u_id", String.valueOf(open_id)));
//		parameter.add(new RequestParameter("server_id", String
//				.valueOf(server_id)));
//		parameter.add(new RequestParameter("device_name", String
//				.valueOf(DeviceUtil.getDeviceModel())));
//		parameter.add(new RequestParameter("os_version", DeviceUtil
//				.getSDKVersion()));
//		startHttpRequst(CXGameConfig.HTTP_GET, CXGameConfig.OTHER_LOGIN,
//				parameter, true, "", false,
//				CXGameConfig.CONNECTION_SHORT_TIMEOUT,
//				CXGameConfig.READ_MIDDLE_TIMEOUT, OTHER_LOGIN_KEY);
//	}

	/**
	 * 用户注册
	 */
	private void accountRegister(int app_id, String username, String password,
			int server_id) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("username", username));
		parameter.add(new RequestParameter("password", password));
		parameter.add(new RequestParameter("app_id", String.valueOf(app_id)));
		parameter.add(new RequestParameter("server_id", String
				.valueOf(server_id)));

		startHttpRequst(CXGameConfig.HTTP_POST,
				CXGameConfig.BX_SERVICE_URL_ACCOUNT_REGISTER, parameter, true,
				"", false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, REQUEST_REGISTER_KEY);// FIXME
		// 注册后直接登录
	}

	/**
	 * 快速注册
	 */
	private void accountQuickRegister(int app_id, String username,
			String password, int server_id) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("username", username));
		parameter.add(new RequestParameter("password", password));
		parameter.add(new RequestParameter("isGuset", "1"));
		parameter.add(new RequestParameter("app_id", String.valueOf(app_id)));
		parameter.add(new RequestParameter("server_id", String
				.valueOf(server_id)));
		startHttpRequst(CXGameConfig.HTTP_POST,
				CXGameConfig.BX_SERVICE_URL_ACCOUNT_REGISTER, parameter, true,
				"", false, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT,
				REQUEST_QUICK_PLAY_REGISTER_KEY);
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
				true, "", true, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT,
				REQUEST_PHONENUMBER_BIND_CHECK_KEY);
	}

	public void showViewType(int type) {
		switch (type) {
		case SHOW_HOME_PANEL:
			if (mLoginView == null)
				initSubView(SHOW_HOME_PANEL);
			if (mLoginView != null)
				mLoginView.setVisibility(View.VISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.INVISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.INVISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.INVISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.INVISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);

			if (mHomeAccount != null && mHomePassword != null) {

				mHomeAccount.setText(CXGameConfig.NICK_NAME);
				mHomePassword.setText(CXGameConfig.PASSWORD);
				// mHomeAccount.setSelection(BFGameConfig.ACCOUNT.length());
				// mHomePassword.setSelection(BFGameConfig.PASSWORD.length());
			}
			break;
		case SHOW_QUICK_PALY:
			if (mQuickPlayView == null)
				initSubView(SHOW_QUICK_PALY);
			if (mLoginView != null)
				mLoginView.setVisibility(View.INVISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.VISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.INVISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.INVISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.INVISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);

			generateRandomAccount();
			// HashMap<String, String> randomAccount = generateRandomAccount();
			// if (mQuickAccount != null && mQuickPassword != null) {
			// mQuickAccount.setText(randomAccount.get("name"));
			// mQuickPassword.setText(randomAccount.get("password"));
			// // mQuickAccount.setSelection(mQuickAccount.getText().toString()
			// // .trim().length());
			// //
			// mQuickPassword.setSelection(mQuickPassword.getText().toString()
			// // .trim().length());
			// }
			break;
		case SHOW_ACCOUNT_REGISTER:
			if (mRegisterView == null)
				initSubView(SHOW_ACCOUNT_REGISTER);
			if (mLoginView != null)
				mLoginView.setVisibility(View.INVISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.INVISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.VISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.INVISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.INVISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			stringRegisterAccount = "";
			stringRegisterPassword = "";
			stringRegisterPassword_sec = "";
			if (mRegisterAccount != null && mRegisterPassword != null) {
				mRegisterAccount.setText(stringRegisterAccount);
				mRegisterPassword.setText(stringRegisterPassword);
				mRegisterPassword_sec.setText(stringRegisterPassword_sec);
			}
			break;
		case SHOW_FIND_PASSWORD:
			if (mFindPasswordView == null)
				initSubView(SHOW_FIND_PASSWORD);
			if (mLoginView != null)
				mLoginView.setVisibility(View.INVISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.INVISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.INVISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.VISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.INVISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			stringFindPasswordAccount = "";
			if (mFindPasswordAccount != null) {
				if ("1".equals(CXGameConfig.ORIGIN)
						|| "2".equals(CXGameConfig.ORIGIN)) {
					mFindPasswordAccount.setText("");
				} else if (mHomeAccount_text.getVisibility() == View.VISIBLE) {
					mFindPasswordAccount.setText(mHomeAccount_text.getText()
							.toString());
				} else {
					mFindPasswordAccount.setText(CXGameConfig.ACCOUNT);
					mFindPasswordAccount.setSelection(CXGameConfig.ACCOUNT
							.length());
				}
			}
			initAccountData();
			setFindPasswordValue();
			// bindCheckPhoneNumber(BXGameConfig.ACCOUNT);
			// mFindPasswordInput.setText(stringFindPasswordAccount);
			break;
		case SHOW_RESET_PASSWORD:
			if (mResetPasswordView == null)
				initSubView(SHOW_RESET_PASSWORD);
			if (mLoginView != null)
				mLoginView.setVisibility(View.INVISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.INVISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.INVISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.INVISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.VISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			break;
		case SHOW_XINLANG_WEBVIEW:
			if (mResetPasswordView == null)
				initSubView(SHOW_XINLANG_WEBVIEW);
			if (mLoginView != null)
				mLoginView.setVisibility(View.INVISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.INVISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.INVISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.INVISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.INVISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			break;
		case SHOW_BIND_PHONE_AGAIN:
			if (mResetPasswordView == null)
				initSubView(SHOW_BIND_PHONE_AGAIN);
			if (mLoginView != null)
				mLoginView.setVisibility(View.INVISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.INVISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.INVISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.INVISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.INVISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.VISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			break;
		case SHOW_BIND_PHONE:
			if (mResetPasswordView == null)
				initSubView(SHOW_BIND_PHONE);
			if (mLoginView != null)
				mLoginView.setVisibility(View.INVISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.INVISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.INVISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.INVISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.INVISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.VISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).initValue();
			break;
		case SHOW_ACCOUNT_INFO:
			if (mResetPasswordView == null)
				initSubView(SHOW_ACCOUNT_INFO);
			if (mLoginView != null)
				mLoginView.setVisibility(View.INVISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.INVISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.INVISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.INVISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.INVISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.VISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			// BFAccountInfoView.getInstance(mContext).initValue();

			break;
		case SHOW_CHANGE_PASSWORD:
			if (mResetPasswordView == null)
				initSubView(SHOW_CHANGE_PASSWORD);
			if (mLoginView != null)
				mLoginView.setVisibility(View.INVISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.INVISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.INVISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.INVISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.INVISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.VISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			break;
		case SHOW_CHANGE_BIND_PHONE:
			if (mChangeBindPhoneView == null)
				initSubView(SHOW_CHANGE_BIND_PHONE);
			if (mLoginView != null)
				mLoginView.setVisibility(View.INVISIBLE);
			if (mQuickPlayView != null)
				mQuickPlayView.setVisibility(View.INVISIBLE);
			if (mRegisterView != null)
				mRegisterView.setVisibility(View.INVISIBLE);
			if (mFindPasswordView != null)
				mFindPasswordView.setVisibility(View.INVISIBLE);
			if (mResetPasswordView != null)
				mResetPasswordView.setVisibility(View.INVISIBLE);
			CXAccountInfoView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangePasswordView.getInstance(mContext).getView()
					.setVisibility(View.INVISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).getView()
					.setVisibility(View.VISIBLE);
			CXChangeBindPhoneAgainView.getInstance(mContext).initValue();
			break;
		}
	}

	// /**
	// * 生成随机账号
	// *
	// * @return
	// */
	// private HashMap<String, String> generateRandomAccount() {
	// if (TextUtils.isEmpty(stringRandomAccount)
	// || TextUtils.isEmpty(stringRandomPassword)) {
	// DecimalFormat format = new DecimalFormat("######");
	// format.setMinimumIntegerDigits(4);
	// format.setMaximumIntegerDigits(4);
	// Random random = new Random();
	// String timestamp = String.valueOf(System.currentTimeMillis());
	// stringRandomAccount = timestamp.substring(timestamp.length() - 8,
	// timestamp.length() - 4)
	//
	//
	// + format.format(random.nextInt(10000));
	// stringRandomPassword = timestamp.substring(timestamp.length() - 8,
	// timestamp.length() - 4)
	// + format.format(random.nextInt(10000));
	//
	// System.out.println("****"+timestamp+"："+stringRandomAccount);
	// }
	//
	// HashMap<String, String> account = new HashMap<String, String>();
	// account.put("name", stringRandomAccount);
	// account.put("password", stringRandomPassword);
	// return account;
	// }

	/**
	 * 生成随机账号
	 * 
	 * @return
	 */
	private void generateRandomAccount() {
		stringRandomAccount = "";
		stringRandomPassword = "";
		DecimalFormat format = new DecimalFormat("######");
		format.setMinimumIntegerDigits(4);
		format.setMaximumIntegerDigits(4);
		Random random = new Random();
		String timestamp = String.valueOf(System.currentTimeMillis());
		stringRandomAccount = timestamp.substring(timestamp.length() - 8,
				timestamp.length() - 4)

		+ format.format(random.nextInt(10000));
		stringRandomPassword = timestamp.substring(timestamp.length() - 8,
				timestamp.length() - 4) + format.format(random.nextInt(10000));
		mQuickAccount.setText(stringRandomAccount);
		mQuickPassword.setText(stringRandomPassword);
	}

	private ListView listView;
	private TextView textView;
	private TextView  text;
	private PopupWindow popupWindow;

	/**
	 * 显示选择登录过的账户对话框
	 */
	private void showAccountSelectDialog() {
		final List<String> tokenList = new ArrayList<String>();
		for (int i = 0; i < loggedAccounts.size(); i++) {
			HashMap<String, String> account = loggedAccounts.get(i);
			if (StringUtil.isNotEmpty(account.get("token"))) {
				tokenList.add(account.get("token"));
			}
		}

		final String[] names = new String[loggedAccounts.size()
				- tokenList.size()];
		final String[] passwords = new String[loggedAccounts.size()
				- tokenList.size()];
		final String[] nicknames = new String[loggedAccounts.size()
				- tokenList.size()];
		final String[] userids = new String[loggedAccounts.size()
				- tokenList.size()];
		final String[] origin = new String[loggedAccounts.size()
				- tokenList.size()];
		final String[] tokens = new String[loggedAccounts.size()
				- tokenList.size()];
		final String[] ticket = new String[loggedAccounts.size()
				- tokenList.size()];
		for (int i = 0, j = 0; i < loggedAccounts.size(); i++) {
			HashMap<String, String> account = loggedAccounts.get(i);
			if (StringUtil.isEmpty(account.get("token"))) {
				names[j] = account.get("name");
				passwords[j] = account.get("password");
				nicknames[j] = account.get("nickname");
				origin[j] = account.get("origin");
				userids[j] = account.get("user_id");
				tokens[j] = account.get("token");
				ticket[j] = account.get("ticket");
				j++;
			}
		}

		
		
		// 准备listView
		listView = new ListView(mContext);

		// 给listView设置背景
		listView.setBackgroundResource(CXResources.drawable.bx_rect_bottom_pressed);
		listView.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (position == names.length) {
					text = new TextView(mContext);
					text.setText("其他用户");
					text.setTextColor(Color.BLACK);
					text.setHeight(DeviceUtil.dip2px(mContext, 30));
					text.setGravity(Gravity.CENTER);
					return text;
				}else {
					textView = new TextView(mContext);
					textView.setGravity(Gravity.CENTER);
					textView.setTextColor(Color.BLACK);
					textView.setHeight(DeviceUtil.dip2px(mContext, 30));
					// 对内容进行设置
					textView.setText(nicknames[position]);
					return textView;
				}
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return names.length + 1;
			}
		});

		
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				if (position == names.length) {
					mHomeAccount.setVisibility(View.INVISIBLE);
					mHomePassword.setVisibility(View.INVISIBLE);
					mHomeAccount_text.setVisibility(View.VISIBLE);
					mHomePassword_text.setVisibility(View.VISIBLE);
					mHomeAccount_text.setText("");
					mHomePassword_text.setText("");
					mHomeFindPassword.setClickable(true);
					mHomeFindPassword.setTextColor(Color.WHITE);
					CXGameConfig.ORIGIN = "0";
					popupWindow.dismiss();
				}else {
				mHomeAccount.setVisibility(View.VISIBLE);
				mHomePassword.setVisibility(View.VISIBLE);
				mHomeAccount_text.setVisibility(View.INVISIBLE);
				mHomePassword_text.setVisibility(View.INVISIBLE);

				CXGameConfig.ACCOUNT = names[position];
				CXGameConfig.NICK_NAME = nicknames[position];
				CXGameConfig.ORIGIN = origin[position];
				CXGameConfig.PASSWORD = passwords[position];
				CXGameConfig.USER_ID = userids[position];
				CXGameConfig.TICKET = ticket[position];
				if ("1".equals(CXGameConfig.ORIGIN)
						|| "2".equals(CXGameConfig.ORIGIN)) {
					mHomeAccount.setTextColor(Color.GREEN);
					mHomeFindPassword.setTextColor(Color.GRAY);
				} else {
					mHomeAccount.setTextColor(Color.WHITE);
					mHomeFindPassword.setTextColor(Color.WHITE);
				}
				mHomeAccount.setText(CXGameConfig.NICK_NAME);
				mHomePassword.setText(CXGameConfig.PASSWORD);
				popupWindow.dismiss();
				}
			}
		});

		// 本例中只有downArrow图片有点击事件，
		if (popupWindow == null) {
			popupWindow = new PopupWindow(mContext);
			// 设置弹出窗体的大小
			popupWindow.setWidth(ll_text.getWidth());
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			// 设置弹出窗体的内容
			popupWindow.setContentView(listView);
			// 设置popupwindow以外的区域是否能触摸，设为true,会自动收起popupWindow
			popupWindow.setOutsideTouchable(true);

			// popupWindow默认为不接收焦点
			popupWindow.setFocusable(true);
			popupWindow.setAnimationStyle(CXResources.style.animationPreview);

		}

		popupWindow.showAsDropDown(ll_text, 0, 0);

		// // 对话框
		// Builder builder = new Builder(mContext);
		// builder.setTitle("选择已登录的账户");
		// builder.setSingleChoiceItems(names, 0,
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// BFGameConfig.ACCOUNT = names[which];
		// BFGameConfig.PASSWORD = passwords[which];
		// BFGameConfig.USERTYPE = userTypes[which];
		// // homeAccount.setText(Config.ACCOUNT);
		// // homePassword.setText(Config.PASSWORD);
		// }
		//
		// });
		//
		// // 添加一个确定按钮
		// builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		// {
		// public void onClick(DialogInterface dialog, int which) {
		// // Config.ACCOUNT = names[which];
		// // Config.PASSWORD = passwords[which];
		// mHomeAccount.setText(BFGameConfig.ACCOUNT);
		// mHomePassword.setText(BFGameConfig.PASSWORD);
		//
		// }
		// });
		// builder.create().show();
		// // 显示对话框是，默认选中第一项
		// // Config.ACCOUNT = names[0];
		// // Config.PASSWORD = passwords[0];
	}

	/**
	 * 显示找回密码处的dialog
	 */
	private void showFindPasswordDialog() {
		final String[] names = new String[loggedAccounts.size()];
		// final String[] passwords = new String[loggedAccounts.size()];
		for (int i = 0; i < loggedAccounts.size(); i++) {
			HashMap<String, String> account = loggedAccounts.get(i);
			names[i] = account.get("name");
			// passwords[i] = account.get("password");
		}

		// 对话框
		Builder builder = new Builder(mContext);
		builder.setTitle("选择已登录的账户");
		builder.setSingleChoiceItems(names, 0,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						CXGameConfig.ACCOUNT = names[which];
						// Config.PASSWORD = passwords[which];
						// homeAccount.setText(Config.ACCOUNT);
						// homePassword.setText(Config.PASSWORD);
					}

				});

		// 添加一个确定按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Config.ACCOUNT = names[which];
				// Config.PASSWORD = passwords[which];
				mFindPasswordAccount.setText(CXGameConfig.ACCOUNT);
				// mHomePassword.setText(Config.PASSWORD);
				// bindCheckPhoneNumber(BXGameConfig.ACCOUNT);
			}
		});
		builder.create().show();
		// 显示对话框是，默认选中第一项
		// Config.ACCOUNT = names[0];
		// Config.PASSWORD = passwords[0];
	}

	/**
	 * 显示强制修改密码的dialog 只有在快速注册账号第二次登陆的时候强制修改密码
	 */
	public void showChangePasswordDialgo() {
		if (mForceChangePasswordDialog == null) {
			mForceChangePasswordDialog = new CXCustomDialog(mContext);
			mForceChangePasswordDialog
					.setConfirmClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							stringForceResetPassword = mForceChangePasswordDialog
									.getPasswordContent();
							if (TextUtils.isEmpty(stringForceResetPassword)) {
								showToast("请输入密码");
								return;
							}

							if (!isAllCharacter(stringForceResetPassword)
									|| stringForceResetPassword.length() < 6) {
								showToast("输入密码格式有误，请重新输入！");
								return;
							}

							randomAccountChangePassword(CXGameConfig.ACCOUNT,
									CXGameConfig.GAMEPARAM.getApp_id(),
									CXGameConfig.GAMEPARAM.getServerId(),
									stringForceResetPassword);
						}
					});
		}

		mForceChangePasswordDialog.show();

	}

	/**
	 * 修改sd卡中保存的账户记录文件
	 */
	public void changeLocationAccountFile() {
		final int READ_ACCOUNT_SUCCESS = 100;
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == READ_ACCOUNT_SUCCESS) {
					ArrayList<HashMap<String, String>> accounts = (ArrayList<HashMap<String, String>>) msg.obj;
					if (accounts != null) {
						// 读取保存的账号记录
						boolean isExist = false;
						HashMap<String, String> currentAccount = null;
						for (int i = 0; i < accounts.size(); i++) {
							HashMap<String, String> existAccount = accounts
									.get(i);
							if (CXGameConfig.ACCOUNT.equals(existAccount
									.get("name"))) {
								currentAccount = existAccount;
								// // 如果之前已经保存过这个账户，则替换删除原位置的对象，添加到最后位置
								isExist = true;
								break;
							}
						}
						HashMap<String, String> account = new HashMap<String, String>();
						account.put("name", CXGameConfig.ACCOUNT);
						account.put("password", CXGameConfig.PASSWORD);
						if (!isExist) {
							// 之前没有保存过这个账户，添加进去
							accounts.add(account);
						} else {
							// 删除之前保存的账号，添加更改密码后的账号进去
							accounts.remove(currentAccount);
							accounts.add(account);
						}
						// 写入文件
						AccountPersistenceUtil.writerAccountToFile(
								accountFileSavePath + File.separator
										+ accountFileName, accounts);
					}

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
						msg.what = READ_ACCOUNT_SUCCESS;
						msg.obj = accounts;
						msg.sendToTarget();
					}
				});
	}

	private String isBfUser() {
		return "0";
	}

	// private void authorize(Platform plat) {
	// if (plat.isValid()) {
	// plat.removeAccount();
	// String userId = plat.getDb().getUserId();
	// if (!TextUtils.isEmpty(userId)) {
	// UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
	// login(plat.getName(), userId, null);
	// return;
	// }
	// }
	// plat.setPlatformActionListener(this);
	// plat.SSOSetting(true);
	// plat.showUser(null);
	// }
	//
	// public void login(String plat, String userId,
	// HashMap<String, Object> userInfo) {
	// Message msg = new Message();
	// msg.what = MSG_LOGIN;
	// msg.obj = plat;
	// UIHandler.sendMessage(msg, this);
	// }
	//
	// @Override
	// public void onCancel(Platform platform, int action) {
	// if (action == Platform.ACTION_USER_INFOR) {
	// UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
	// }
	// }
	//
	// @Override
	// public void onComplete(Platform platform, int action,
	// HashMap<String, Object> res) {
	// if (action == Platform.ACTION_USER_INFOR) {
	// UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
	// CXGameConfig.NICK_NAME = platform.getDb().getUserName();
	// CXGameConfig.OPEN_ID = platform.getDb().getUserId();
	// login(platform.getName(), platform.getDb().getUserId(), res);
	// }
	// }
	//
	// @Override
	// public void onError(Platform platform, int action, Throwable t) {
	// // TODO Auto-generated method stub
	// if (action == Platform.ACTION_USER_INFOR) {
	// UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
	// }
	// t.printStackTrace();
	// }
	//
	// @Override
	// public boolean handleMessage(Message msg) {
	// switch (msg.what) {
	// case MSG_USERID_FOUND: {
	// Toast.makeText(mContext, R.string.userid_found, Toast.LENGTH_SHORT)
	// .show();
	// }
	// break;
	// case MSG_LOGIN: {
	//
	// String text = mContext.getString(R.string.logining, msg.obj);
	// Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	// // 是否初始化
	// if (CXGameConfig.GAMEPARAM != null) {
	//
	// otherLogin(CXGameConfig.GAMEPARAM.getApp_id(),
	// CXGameConfig.NICK_NAME,
	// CXGameConfig.GAMEPARAM.getServerId(),
	// CXGameConfig.OPEN_ID, (String) msg.obj);
	//
	// } else {
	//
	// mContext.loginResult(OperateType.NO_INIT, OperateType.NO_INIT);
	//
	// }
	// }
	// break;
	// case MSG_AUTH_CANCEL: {
	// Toast.makeText(mContext, R.string.auth_cancel, Toast.LENGTH_SHORT)
	// .show();
	// }
	// break;
	// case MSG_AUTH_ERROR: {
	// Toast.makeText(mContext, R.string.auth_error, Toast.LENGTH_SHORT)
	// .show();
	// }
	// break;
	// case MSG_AUTH_COMPLETE: {
	// Toast.makeText(mContext, R.string.auth_complete, Toast.LENGTH_SHORT)
	// .show();
	// }
	// break;
	// }
	// return false;
	// }
}
