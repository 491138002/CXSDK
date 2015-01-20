/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.changxiang.game.sdk.alipay;
//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {


	//麦克莱斯
//	//合作身份者id，以2088开头的16位纯数字
//	public static final String DEFAULT_PARTNER = "2088411705925145";
//
//	//收款支付宝账号
//	public static final String DEFAULT_SELLER = "2088411705925145";
//
//	//商户私钥，自助生成
//	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANfNNxwJ3ba6mrFNMS0gs9SmIKDpJI4ky/mXVFoNCPJUNdBAJ2GqwA7T3CIeOLF0ZGacPv3kxAvIHSBKhwsgtYO1T/sab6O+2xA94Zwzganft+pe6SjdyXMJIH5ZgpEu0OmX9E5v2srrhfuibnHDjZXCKbP5jL9cQc1kmopVucMbAgMBAAECgYEA11Ikja5ucbepSAV8bsm1hRUpc0SNO+MNPwG2oY9mANzzQNHyOWo07daIS+aZrL40u4lk9NIAprzKCwEx03GUiXwK9yxzjod/Wf2HxQYF30z5kdVzSdpRclujSnrEWlSoksCQ1g2kbc7DYUx9qSlasO6QZq1x+Ut4bgEDTHFqbOECQQDvl+PYtkTT9I0aFZjdXPPiCoP9lg4CrKyGUzEmujnEagSH4Vi3cq9w7eZOWnzqFF/81cIpt0iZgQQwbrW5L05RAkEA5pRAvc3rk4Qi7mwFaYbLONsMbVrL6Mo5E5ur+GdxbyCq3/ohCXaJM4ynyypy7+ALu0Lo6/O9KzA219Q5iP2DqwJAZIOOPM5KlbkUsQq6dLOYRQ4wTWR0QD78qeWgyyR5M6NefzrUozFj1LaZVem0WeduVX2/1QxlUrDDdyYa6rOj4QJBAI6wN9A9WgcTwjohBshi7eflRi91/LG2UtPbhfRWr0/Bu3yXYVJl1EneRQfo4O+oihn8Mf+x+eJ8F7RAVMp9d7kCQCN3h8M/yoMoX5J8o7dcu7bH+SfhsrfllsXsHln2HcyuuwVivWRueMTWPAmmXh0oxLi1I/6YEyCVuZ+LLZUKPP8=";
//
//	//支付宝公钥
//	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	
	
	//畅想
	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088711263088594";
	
	//收款支付宝账号
	public static final String DEFAULT_SELLER = "2088711263088594";
	
	//商户私钥，自助生成
	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJYYMbBLFoaN2rTBgQjgta1DmXY6yj14VLqIhjt6wAI15qEhGzTf4x78sqkXCRdurEkpLCwBaMxsuU4XoKGBJofj99nt8PSaKu/kYKwZwpaGzKHbz5QTInxIc3rFZNCc8zYU+vA4VwL3Ckdj3iuPFVU1mxV4k2JUAAIkqmWxiGf5AgMBAAECgYBMEUrta8CoxK+4t/DrTOcGPqJB1x2z9Y4LUzGkZ1t0Q1j1BFBDhcwXYj4xj+kdpQtPsLwgOT6hi+CGAVd5QnkB0wHHj2EhJwN7v1EBBu/9ZvBGHPtZ2UIepQ3Ovlamv50nrxKxQuXOmtTXhGLarhG2+x6/BSHStlc5MtbBQHM3GQJBAMdURYtJKc21qOwcCXEhAoLLz1SKkHfZhR5GZdYDTGsyQIAqdlSMo62KUktVaYC9agf8fF1GpxhG8d8ZyOAMqL8CQQDAxHyXPypLbZg3ko53HBhwuqEBUNTFQsTyzR+xmf0/bYHD56SAI3XdjTJE7VV60Sqa8jV+oPIcLxX1n0Gc3iVHAkEAhtgEn+Bjzky5NNkWrhhlqXQVEx0V9G4Ldtqq46ehl9cL+WhAWpw10h2D5ICoebYpt7Nfsn4sZekAkSvRT3hg4wJBAKH39p+2yTjbexymneHiz35YsdPDMSQV+Bny1ICL3MggoPoUdpncMbrYWrajnEE34s6SWPRvEz8vKQpap+zAkx0CQF+PBK6MK59NA9F396o4da2qyGc2q/onMnVAMXqtKHcnWW37ICiuV3Es8j3LbcLOK1wl/vAbFovF5szbdgu1GTw=";
	
	//支付宝公钥
	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	
	
}
