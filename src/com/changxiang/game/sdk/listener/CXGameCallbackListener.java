package com.changxiang.game.sdk.listener;

public abstract interface CXGameCallbackListener<T> {

	public abstract void callback(int statuscode, T data);
}
