package com.changxiang.game.sdk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.changxiang.game.sdk.config.CXGameConfig;
import com.changxiang.game.sdk.exception.CXCallbackListenerNullException;
import com.changxiang.game.sdk.listener.CXGameCallbackListener;
import com.changxiang.game.sdk.listener.CXPayCallbackListener;
import com.changxiang.game.sdk.type.CXOrientation;
import com.changxiang.game.sdk.type.CXSDKStatusCode;
import com.changxiang.game.sdk.util.StringUtil;
import com.changxiang.game.sdk.vo.CXGameParamInfo;
import com.changxiang.game.sdk.vo.CXGamePayParamInfo;

public class StartActivity extends Activity {

	private LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	private Context context = this;

	private double money = 0.01;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		params.leftMargin = 20;
		params.rightMargin = 20;
		params.topMargin = 20;
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		
		Button mainBtn = new Button(this);
		Button pay2Btn = new Button(this);

		mainBtn.setText("启动首页");
		mainBtn.setGravity(Gravity.CENTER);
		mainBtn.setPadding(20, 20, 20, 20);
		layout.addView(mainBtn, params);

		pay2Btn.setText("启动支付页");
		pay2Btn.setGravity(Gravity.CENTER);
		pay2Btn.setPadding(20, 20, 20, 20);
		layout.addView(pay2Btn, params);
		ColorDrawable dw = new ColorDrawable(Color.WHITE);
		layout.setBackgroundDrawable(dw);
		setContentView(layout);

		// 测试用
		CXGameParamInfo param = new CXGameParamInfo();
		param.setApp_id(10017);
		param.setCpKey("QWERT3456ERTY1");
		param.setServerId(1);
		param.setScreenOrientation(CXOrientation.VERTICAL);

		try {
			CXGameSDK.defaultSDK().initSDK(StartActivity.this, param,
					new CXGameCallbackListener() {
						@Override
						public void callback(int resultCode, Object resultData) {
							// 返回的消息
//							System.out.println("msg:" + resultData);
							switch (resultCode) {
							case CXSDKStatusCode.SUCCESS:
								
//								// 初始化成功，可以执行后续的登录充值操作
//								Toast.makeText(StartActivity.this,
//										"初始化成功，可以执行后续的登录充值操作",
//										Toast.LENGTH_LONG).show();
								break;
							case CXSDKStatusCode.FAIL:
								// 初始化失败,不能执行后续操作
//								Toast.makeText(StartActivity.this,
//										"初始化失败,不能执行后续操作", Toast.LENGTH_LONG)
//										.show();
								break;

							default:
								break;
							}
						}
					});
		} catch (CXCallbackListenerNullException e) {

		}

		mainBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				try {
					
					CXGameSDK.defaultSDK().login(StartActivity.this,
							new CXGameCallbackListener() {
								@Override
								public void callback(int resultCode, Object arg1) {
									if (resultCode == CXSDKStatusCode.SUCCESS) {
										
										Toast.makeText(
												StartActivity.this,
												"login success" + (String) arg1,
												Toast.LENGTH_LONG).show();
										// Toast.makeText(StartActivity.this,"用户票据="+CXGameSDK.defaultSDK().getTicket(),Toast.LENGTH_LONG).show();

									} else if (resultCode == CXSDKStatusCode.FAIL) {
										Toast.makeText(StartActivity.this,
												"login faile",
												Toast.LENGTH_LONG).show();
									} else if (resultCode == CXSDKStatusCode.LOGIN_EXIT) {
										Toast.makeText(StartActivity.this,
												"login exit", Toast.LENGTH_LONG)
												.show();
									}

								}
							});
				} catch (CXCallbackListenerNullException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		pay2Btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				try {
					if (StringUtil.isEmpty(CXGameConfig.ACCOUNT)) {
						Toast.makeText(StartActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
						return;
					}
					
					// 测试设置支付订单号
					CXGamePayParamInfo payparam = new CXGamePayParamInfo();
					payparam.setCpBillNo("123456");// CP订单号
					payparam.setExtra("abc2013-05-24"); // 扩展参数 支付成功服务器通知接口将原样返回
					if (money > 0) {
						payparam.setCpBillMoney(money);// 设置订单金额 可选项
					}
					payparam.setSubject("1000元宝");
					//短信支付商品ID
					payparam.setGood_id("47");

					// 如果不设置 请提供同一通知地址给我们 ，通知将统一发送到所提供的地址
					payparam.setNotifyUrl("http://sdkapi.test.ak.cc/notify.php"); // 本单据支付成功后的通知返回地址
					// 用于多服务器不同通知地址

					CXGameSDK.defaultSDK().pay(StartActivity.this,
							new CXPayCallbackListener<String>() {
								public void callback(int statuscode,
										String data, int type) {
									if (statuscode == CXSDKStatusCode.SUCCESS) {
										Toast.makeText(
												StartActivity.this,
												"pay success"
														+ String.valueOf(statuscode),
												Toast.LENGTH_LONG).show();
									} else if (statuscode == CXSDKStatusCode.CHARGE_USER_EXIT) {
										Toast.makeText(StartActivity.this,
												"pay exit", Toast.LENGTH_LONG)
												.show();
									}
								}

							}, payparam);
				} catch (CXCallbackListenerNullException e) {

				}
			}
		});
	}
}
