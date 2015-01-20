package com.changxiang.game.sdk.listener;

public abstract interface CXPayCallbackListener<T> {

	public abstract void callback(int statuscode, T data, int type);
	
}
