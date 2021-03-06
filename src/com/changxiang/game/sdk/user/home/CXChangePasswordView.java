package com.changxiang.game.sdk.user.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.net.RequestParameter;
import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.user.CXBasePanel;
import com.changxiang.game.sdk.user.HomePanel;

public class CXChangePasswordView extends CXBasePanel {

	private final int CHANGE_PASSWORD_KEY = 1001;
	
	private static CXChangePasswordView mBXChangePasswordView;

	private HomePanel mHomePanel;
	private View mBaseView;
	
	private EditText mChangeOldPswInput;
	private EditText mChangeNewPswInput;
	private EditText mChangeNewPswConfirmInput;
	private Button mChangeBackBtn;
	private Button mChangeCommitBtn;
	
	private String mOldPassword;
	private String mNewPassword;
	private String mNewPasswordConfirm;
	
	public synchronized static CXChangePasswordView getInstance(Context context){
		if(mBXChangePasswordView == null){
			mBXChangePasswordView = new CXChangePasswordView(context);
		}
		return mBXChangePasswordView;
	}
	
	public CXChangePasswordView(Context context) {
		super(context);
		
		init();
		initView();
		initListener();
		initValue();
	}
	
	public void init() {
		mBaseView = mInflater.inflate(CXResources.layout.bx_change_password, null);
	}

	public void initView() {
		mChangeOldPswInput = (EditText) mBaseView.findViewById(CXResources.id.bx_change_password_old_psw);
		mChangeNewPswInput = (EditText) mBaseView.findViewById(CXResources.id.bx_change_password_new_psw);
		mChangeNewPswConfirmInput = (EditText) mBaseView.findViewById(CXResources.id.bx_change_password_new_confirm);
		mChangeBackBtn = (Button) mBaseView.findViewById(CXResources.id.bx_back);
		mChangeCommitBtn = (Button) mBaseView.findViewById(CXResources.id.bx_change_password_commit);
	}

	public void initListener() {
		mChangeBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mHomePanel.showViewType(HomePanel.SHOW_ACCOUNT_INFO);
				CXAccountInfoView.getInstance(mContext).initValue();
			}
		});
		mChangeCommitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mOldPassword = mChangeOldPswInput.getText().toString();
				mNewPassword = mChangeNewPswInput.getText().toString();
				mNewPasswordConfirm = mChangeNewPswConfirmInput.getText()
						.toString();
				if (mOldPassword == null || mNewPassword == null
						|| "".equals(mOldPassword) || "".equals(mNewPassword)
						|| mNewPasswordConfirm == null
						|| "".equals(mNewPasswordConfirm)
						|| !isAllCharacter(mNewPassword)) {
					showToast("输入密码格式有误，请重新输入！");
					return;
				}

				if (mNewPassword.length() < 6) {
					showToast("请输入不少于6位密码");
					return;
				}

				if (!mNewPasswordConfirm.equals(mNewPassword)) {
					showToast("两次输入密码不一致，请重新输入！");
					return;
				}

				if (mOldPassword.equals(mNewPassword)) {
					showToast("新密码与旧密码相同，请重新输入！");
					return;
				}
				changePassword(CXGameConfig.USER_ID, mOldPassword, mNewPassword);

			}
		});
	}

	public void initValue() {
	}

	public View getView(){
		return mBaseView;
	}
	
	public void setHomePanel(HomePanel homePanel){
		this.mHomePanel = homePanel;
	}

	/**
	 * 修改密码 调用的前提是用户已经登录
	 * 
	 * @param user_id
	 *            用户ID
	 * @param oldPassword
	 *            原始密码
	 * @param newPassword
	 *            新密码
	 */
	private void changePassword(String user_id, String original_pwd,
			String newPassword) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("user_id", String.valueOf(user_id)));
		parameter.add(new RequestParameter("original_pwd", original_pwd));
		parameter.add(new RequestParameter("new_pwd", newPassword));
		startHttpRequst(CXGameConfig.HTTP_POST,
				CXGameConfig.BX_SERVICE_URL_CHANGE_PASSWORD, parameter, true,
				"", true, CXGameConfig.CONNECTION_SHORT_TIMEOUT,
				CXGameConfig.READ_MIDDLE_TIMEOUT, CHANGE_PASSWORD_KEY);
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
				case CHANGE_PASSWORD_KEY:
					if (code == CXSDKStatusCode.SUCCESS) {
						showToast("密码修改成功");
						CXGameConfig.PASSWORD = mNewPassword;
						mHomePanel.changeLocationAccountFile();
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
	
	public static void releaseViews(){
		mBXChangePasswordView = null;
	}
}
