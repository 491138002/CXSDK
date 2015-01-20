package com.changxiang.game.sdk.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.vo.PayBean;

public class CXPayLeftBarAdapter extends BaseAdapter{

//	private String[] items = {
//			"信用卡快捷", 
//			"支付宝",
//			"储蓄卡",
//			"充值卡", 
//			"点卡", 
//			"银联",
//			"短信支付"
//	};
	/** 
	 * 选中下表
	 */
	private int selectIndex = 0;
	/**
	 * 内容tag
	 */
	private String tag_text = "content";
	/**
	 * 上下文
	 */
	private Context context;
	
	private int defaultBg = 0;
	private int pressedBg = 0;
	
	private CXOnItemClickListener onItemClickListener;
	private List<PayBean> list;
	
	public CXOnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(CXOnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public CXPayLeftBarAdapter(Context context, List<PayBean> list){
		this.list=list;
		this.context = context;
		selectIndex = 0;
		defaultBg = CXResources.drawable.bx_right_btn_default;
		pressedBg  = CXResources.drawable.bx_right_btn_pressed;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
			return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parentView) {
		
		if(convertView == null){
			LinearLayout layout = new LinearLayout(context);
			layout.setGravity(Gravity.CENTER);
			
			TextView textView = new TextView(context);
			textView.setText(list.get(position).getPayName());
			textView.setTextColor(Color.WHITE);
			textView.setTag(tag_text);
			layout.addView(textView);
			convertView = layout;
		}else{
			TextView textView = (TextView) convertView.findViewWithTag(tag_text);
			textView.setText(list.get(position).getPayName());
		}
		
		if(position == selectIndex){
			convertView.setBackgroundResource(pressedBg);
		}else{
			convertView.setBackgroundResource(defaultBg);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(onItemClickListener != null){
//					if(items.length-1 == position)
//						onItemClickListener.onItemClick(position+1);
//					else
						onItemClickListener.onItemClick(list.get(position).getIndex());
				}
				
				selectIndex = position;
				notifyDataSetChanged();
			}
		});
		
		return convertView;
	}

	
	public interface CXOnItemClickListener{
		public void onItemClick(int position);
	}
}
