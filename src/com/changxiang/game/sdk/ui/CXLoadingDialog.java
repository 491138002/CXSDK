package com.changxiang.game.sdk.ui;

import java.util.concurrent.TimeUnit;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.net.DefaultThreadPool;
import com.changxiang.game.sdk.util.StringUtil;

public class CXLoadingDialog extends Dialog{
	TextView loading_text;
	TextView loading_del_textview;
	String content = "";
	boolean isHideCloseBtn = false;
	private Context context;

	public CXLoadingDialog(Context context, String content,
			boolean isHideCloseBtn) {
		super(context);
		this.context = context;
		this.content = content;
		this.isHideCloseBtn = isHideCloseBtn;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//去掉标题
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 //设置view样式
		 setContentView(CXResources.layout.cx_commom_loading);	
		 getWindow().setBackgroundDrawableResource(CXResources.drawable.cx_commom_progressbar_bg);
		 loading_text= (TextView) findViewById(CXResources.id.tv_loading);
		 
		 if(StringUtil.isNotEmpty(content)){
			 	loading_text.setText(content);
			}else{
				loading_text.setText("加载中...");
			}
	}

	protected void onStop() {
		super.onStop();
	}

	@Override
	public void show() {
		super.show();
		CXGameConfig.IS_STOP_REQUEST = false;
	}
	@Override
	public void dismiss() {
		super.dismiss();
		try {
			DefaultThreadPool.pool.awaitTermination(1, TimeUnit.MICROSECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

}
