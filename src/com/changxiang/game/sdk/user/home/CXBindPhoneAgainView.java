package com.changxiang.game.sdk.user.home;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.user.CXBasePanel;
import com.changxiang.game.sdk.user.HomePanel;

public class CXBindPhoneAgainView extends CXBasePanel {
	private static CXBindPhoneAgainView mBXBindPhoneAgainView;
	
	private HomePanel mHomePanel;
	private View mBaseView;
	
	private Button mBindPhoneNowBtn;
	private Button mBindPhoneLaterBtn;
	
	public synchronized static CXBindPhoneAgainView getInstance(Context context){
		if(mBXBindPhoneAgainView == null){
			mBXBindPhoneAgainView = new CXBindPhoneAgainView(context);
		}
		return mBXBindPhoneAgainView;
	}
	
	private CXBindPhoneAgainView(Context context) {
		super(context);
		
		init();
		initView();
		initListener();
		initValue();
	}
	
	public void init() {
		mBaseView = mInflater.inflate(CXResources.layout.bx_bind_phone_again, null);
	}

	public void initView() {
		mBindPhoneNowBtn = (Button) mBaseView.findViewById(CXResources.id.bx_bind_phone_way_now);
		mBindPhoneLaterBtn = (Button) mBaseView.findViewById(CXResources.id.bx_bind_phone_later);
	}

	public void initListener() {
		mBindPhoneNowBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				mHomePanel.showViewType(HomePanel.SHOW_BIND_PHONE);
			}
		});
		mBindPhoneLaterBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (CXGameConfig.CALLBACK_LISTENER != null)
					CXGameConfig.CALLBACK_LISTENER.callback(
							CXSDKStatusCode.SUCCESS,
							CXGameConfig.USER_ID);
				((Activity)mContext).finish();
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
	
	public static void releaseViews(){
		mBXBindPhoneAgainView = null;
	}
	
	
}
