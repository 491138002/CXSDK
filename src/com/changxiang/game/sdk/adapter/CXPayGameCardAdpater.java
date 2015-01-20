package com.changxiang.game.sdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.changxiang.game.sdk.CXResources;
import com.changxiang.game.sdk.activity.CXPayActivity;

public class CXPayGameCardAdpater extends BaseAdapter{

	private LayoutInflater mInflater;
	private Context context;
	private int selectIndex;
	private int[] rIds = {
			CXResources.drawable.bx_btn_jw_selector, CXResources.drawable.bx_btn_qq_selector,
			CXResources.drawable.bx_btn_sd_selector, CXResources.drawable.bx_btn_wm_selector,
			CXResources.drawable.bx_btn_wy_selector
			
	};
	
	public CXPayGameCardAdpater(Context context){
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		selectIndex = 0;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return rIds.length;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mInflater.inflate(CXResources.layout.bx_pay_center_game_card_item, null);
		}
		
		ImageView iconIv = (ImageView) convertView.findViewById(CXResources.id.bx_game_card_icon_iv);
		ImageView selectIv = (ImageView) convertView.findViewById(CXResources.id.bx_game_card_selected_iv);
		
		iconIv.setBackgroundResource(rIds[position]);
		if(selectIndex == position){
			selectIv.setVisibility(View.VISIBLE);
		}else{
			selectIv.setVisibility(View.INVISIBLE);
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				selectIndex = position;
				switch(position){
				case 0:
					if(context instanceof CXPayActivity){
						((CXPayActivity)context).setJunWangTipList();
					}
					break;
				case 1:
					if(context instanceof CXPayActivity){
						((CXPayActivity)context).setQBiTipList();
					}
					break;
				case 2:
					if(context instanceof CXPayActivity){
						((CXPayActivity)context).setShengDaTipList();
					}
					break;
				case 3:
					if(context instanceof CXPayActivity){
						((CXPayActivity)context).setWanMeiTipList();
					}
					break;
				case 4:
					if(context instanceof CXPayActivity){
						((CXPayActivity)context).setWangYiTipList();
					}
					break;
				}
				notifyDataSetChanged();
			}
		});
		
		return convertView;
	}

	public int getSelectIndex() {
		return selectIndex;
	}

	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
	}

}
