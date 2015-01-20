package com.changxiang.game.sdk.vo;

import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.type.CXOrientation;

public class CXGameParamInfo {

	/** 游戏合作商ID **/
	private int cp_id;
	
	private int app_id;

	/** 游戏合作商秘钥 **/
	private String cp_key;

	/** 游戏ID **/
	private int game_id;

	/** 游戏服务器（游戏分区）ID **/
	private int server_id=1;

	/** 渠道编号 **/
	private int channel_id;

	/** 是否显示短信支付 **/
	private boolean is_hasSMS = false;

	public int getCpId() {
		return cp_id;
	}

	public void setCpId(int cp_id) {
		this.cp_id = cp_id;
	}

	public int getGameId() {
		return game_id;
	}

	public void setGameId(int game_id) {
		this.game_id = game_id;
	}

	public int getServerId() {
		return server_id;
	}

	public void setServerId(int server_id) {
		this.server_id = server_id;
	}

	public int getChannelId() {
		return channel_id;
	}

	public void setChannelId(int channel_id) {
		this.channel_id = channel_id;
	}

	public String getCpKey() {
		return cp_key;
	}

	public void setCpKey(String cp_key) {
		this.cp_key = cp_key;
	}

	public boolean getSMS() {
		return is_hasSMS;
	}

	public void setSMS(boolean is_hasSMS) {
		this.is_hasSMS = is_hasSMS;
	}

	/**
	 * 是否是快速注册
	 * 
	 * @param isFastRegister
	 */
	public void setIsFastRegister(boolean isFastRegister) {
		CXGameConfig.ISFASTREGISTER = isFastRegister;
	}

	/**
	 * 是否是竖屏显示
	 * 
	 * @param orientation
	 */
	public void setScreenOrientation(CXOrientation orientation) {
		CXGameConfig.SCREEN_ORIENTATION = orientation;
	}

	public int getApp_id() {
		return app_id;
	}

	public void setApp_id(int app_id) {
		this.app_id = app_id;
	}
}
