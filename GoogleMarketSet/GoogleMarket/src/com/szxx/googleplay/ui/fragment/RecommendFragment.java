package com.szxx.googleplay.ui.fragment;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.szxx.googleplay.http.protocol.RecommendProtocol;
import com.szxx.googleplay.ui.view.LoadingLayout.ResultState;
import com.szxx.googleplay.ui.view.fly.ShakeListener;
import com.szxx.googleplay.ui.view.fly.ShakeListener.OnShakeListener;
import com.szxx.googleplay.ui.view.fly.StellarMap;
import com.szxx.googleplay.uiutils.UIUtils;

public class RecommendFragment extends BaseFragment {

	private ArrayList<String> data;
	
	@Override
	public View createSuccessView() {
		final StellarMap stellar = new StellarMap(UIUtils.getUIContext());
		stellar.setAdapter(new RecommendAdapter());
		
		//设置9行6列的随机网格显示
		stellar.setRegularity(6, 9);
		//设置内边距10dp
		int padding = UIUtils.dipToPx(10);
		stellar.setPadding(padding, padding, padding, padding);
		
		
		//设置初始化界面可见(即在初始不滑动情况下仍然有数据)
		stellar.setGroup(0, true);
		
		//摇动模块
		ShakeListener listener = new ShakeListener(UIUtils.getUIContext());
		listener.setOnShakeListener(new OnShakeListener() {
			
			@Override
			public void onShake() {
				stellar.zoomIn();
			}
		});
		return stellar;
	}
	
	@Override
	public ResultState onLoadDataResult() {
		RecommendProtocol protocol = new RecommendProtocol();
		data = protocol.getData(0);
		return checkId(data);
	}
	
	class RecommendAdapter implements StellarMap.Adapter{

		//获取数组数
		@Override
		public int getGroupCount() {
			
			return 2;
		}

		//获取每个数组所能容纳的最大值
		@Override
		public int getCount(int group) {
			int count = data.size()/getGroupCount();
			if (group == getGroupCount()-1) {
				//如果加载到最后一个布局，将不够一组的数据加载到最后的布局界面，以防数据丢失
				count += data.size()%getGroupCount();
			}
			return count;
		}

		//加载布局
		@Override
		public View getView(int group, int position, View convertView) {
			//由于每次加载时总是从初始位置加载数据，导致position 的值总是从0开始加载，因此需要根据祖的id记录position的每次初始位置
			position  += group*getCount(group -1);
			System.out.println("pos = " + position);
			//关键词显示
			String keyword = data.get(position);
			TextView view = new TextView(UIUtils.getUIContext());
			view.setText(keyword);
			
			Random random = new Random();
			//设置字体的大小，15~25
			int size = 15 + random.nextInt(10);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
			//设置字体颜色 R G B (30 ~ 230)，防止字体颜色过暗或过亮
			int red = 30 + random.nextInt(200);
			int green = 30 + random.nextInt(200);
			int blue = 30 + random.nextInt(200);
			view.setTextColor(Color.rgb(red, green, blue));
			return view;
		}

		//获取下一个条目的id
		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			System.out.println("group" + group);
			if (isZoomIn) {
				//下滑加载上一个布局
				if (group>0) {//如果没有滑动到第一个布局
					group--;
				}else {
					group = getGroupCount() - 1;
				}
			}else {
				//上滑加载下一个布局
				if (group<getGroupCount()-1) {//如果没有滑动到最后一个布局
					group++;
				}else {
					group = 0;
				}
				
			}
			return group;
		}
		
	}
	

}
