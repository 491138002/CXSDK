package com.changxiang.game.sdk.vo;

public class PayBean {
	private String payName;
	private int isShow;
	private int index;
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public PayBean(String payName, int isShow, int index) {
		super();
		this.payName = payName;
		this.isShow = isShow;
		this.index = index;
	}
	 
}
