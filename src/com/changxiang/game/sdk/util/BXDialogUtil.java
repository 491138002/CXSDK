package com.changxiang.game.sdk.util;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.activity.CXPayActivity;

public class BXDialogUtil {
	/**
	 * 退出窗口
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog getTipDialog(final CXPayActivity context, String msg) {
		final Dialog dialog = new Dialog(context,
				CXResources.style.bx_custom_dialog);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(CXResources.layout.bx_tip_dialog, null);

		Button submit = (Button) view
				.findViewById(CXResources.id.bx_tip_submit);
		TextView content = (TextView) view
				.findViewById(CXResources.id.bx_tip_content);
		content.setText(msg);

		submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		return dialog;
	}

	public interface OnAlertSelectId {
		void onClick(int whichButton, Object o);
	}

	/**
	 * 下载提示
	 * 
	 * @param context
	 * @param selectListener
	 * @return
	 */
	public static Dialog showDownloadDialog(Context context,
			final OnAlertSelectId selectListener,
			final HashMap<String, Object> data) {
		View view = ((Activity) context).getLayoutInflater().inflate(
				CXResources.layout.bx_dialog_download_tip, null);

		final Dialog dialog = new Dialog(context,
				CXResources.style.bx_custom_dialog);
		dialog.setContentView(view);

		Button share_cancel = (Button) view
				.findViewById(CXResources.id.bx_download_cancle);
		Button share_submit = (Button) view
				.findViewById(CXResources.id.bx_download_submit);

		if (((Integer) data.get("forcedown")) == 1) {
			share_cancel.setVisibility(View.GONE);
		} else {
			share_cancel.setVisibility(View.VISIBLE);
		}

		share_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		share_submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				selectListener.onClick(1, data);
				dialog.cancel();
			}
		});

		return dialog;
	}

	/**
	 * 提示窗口
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param submtText
	 * @param cancelText
	 * @param selectListener
	 * @return
	 */
	public static Dialog showDialog(Context context, String title,
			String content, String submtText, String cancelText,
			final OnAlertSelectId selectListener) {
		View view = ((Activity) context).getLayoutInflater().inflate(
				CXResources.layout.bx_pay_center_transaction_dialog, null);

		TextView titleTv = (TextView) view
				.findViewById(CXResources.id.bx_dialog_title_tv);
		TextView contentTv = (TextView) view
				.findViewById(CXResources.id.bx_dialog_content_tv);
		Button submitBtn = (Button) view
				.findViewById(CXResources.id.bx_dialog_submit_btn);
		Button cancleBtn = (Button) view
				.findViewById(CXResources.id.bx_dialog_cancel_btn);

		final Dialog dialog = new Dialog(context,
				CXResources.style.bx_custom_dialog);

		dialog.setContentView(view);
		titleTv.setText(title);
		contentTv.setText(content);

		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK
						|| keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {

					return false;
				}
			}
		});
		if (StringUtil.isNotEmpty(submtText)) {
			submitBtn.setText(submtText);
			submitBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (selectListener != null) {
						selectListener.onClick(0, null);
					}
					dialog.cancel();
				}
			});
		} else {
			submitBtn.setVisibility(View.GONE);
		}

		if (StringUtil.isNotEmpty(cancelText)) {
			cancleBtn.setText(cancelText);
			cancleBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (selectListener != null) {
						selectListener.onClick(1, null);
					}
					dialog.cancel();
				}
			});
		} else {
			cancleBtn.setVisibility(View.GONE);
		}

		return dialog;
	}
}
