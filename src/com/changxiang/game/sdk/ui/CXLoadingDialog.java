package com.changxiang.game.sdk.ui;

import java.util.concurrent.TimeUnit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.net.DefaultThreadPool;

public class CXLoadingDialog extends ProgressDialog implements OnDismissListener{
	TextView loading_text;
	TextView loading_del_textview;
	String content = "";
	boolean isHideCloseBtn = false;
	private Context context;
	public CXLoadingDialog(Context context,String content,boolean isHideCloseBtn) {
		super(context);
	    this.context = context;
	    this.isHideCloseBtn = isHideCloseBtn;
	    setMessage(content);
	    setOnDismissListener(this);
	}
	
	protected void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
	 }
	 //called when this dialog is dismissed
	 protected void onStop() {
		 super.onStop();
	 }
	 @Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		 CXGameConfig.IS_STOP_REQUEST = false;
		 
	}
	 @Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		 try{
			 DefaultThreadPool.pool.awaitTermination(1, TimeUnit.MICROSECONDS);
		 }catch (Exception e) {
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
//			 if(isHideCloseBtn){
//				 CXGameConfig.IS_STOP_REQUEST = true;
//				 dismiss();
//				 return super.onKeyDown(keyCode, event);
//			 }else{
//				 CXGameConfig.IS_STOP_REQUEST = true;
//				 return super.onKeyDown(keyCode, event);
//			 }
			 
			 return true;
			 	
		 }
		 
		return super.onKeyDown(keyCode, event);
		
	}
	 @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		 if (event.getAction()==MotionEvent.ACTION_DOWN) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
//		CXGameConfig.IS_STOP_REQUEST = true;
	}
	 

	 
}
