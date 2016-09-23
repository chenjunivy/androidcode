package com.szxx.googleplay.ui.holder;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lidroid.xutils.BitmapUtils;
import com.szxx.googelmarket.R;
import com.szxx.googleplay.http.HttpHelper;
import com.szxx.googleplay.uiutils.BitmapHelper;
import com.szxx.googleplay.uiutils.UIUtils;

public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {
	private ArrayList<String> picName;
	private RelativeLayout rlRoot;
	private ViewPager pager;
	private LinearLayout ll_container;
	private int prepoint_id; //上一个viewpager的position

	@Override
	public View initView() {
		rlRoot = new RelativeLayout(UIUtils.getUIContext());
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, UIUtils.dipToPx(160));
//		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dipToPx(150)); //类型转换异常
		rlRoot.setLayoutParams(params);
		
		pager = new ViewPager(UIUtils.getUIContext());
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//		ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dipToPx(150));//类型转换异常
		pager.setLayoutParams(params2);
		rlRoot.addView(pager);
		
		ll_container = new LinearLayout(UIUtils.getUIContext());
		ll_container.setOrientation(LinearLayout.HORIZONTAL);
		RelativeLayout.LayoutParams rlparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		//设置内边距
		int padding = UIUtils.dipToPx(6);
		ll_container.setPadding(padding, padding, padding, padding);
		//添加规则，设定展示位置
		rlparams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlparams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rlRoot.addView(ll_container, rlparams);
		
		return rlRoot;
	}

	@Override
	public void refreshView(ArrayList<String> data) {
		this.picName = data;
		pager.setAdapter(new HomeHeaderAdapter());
		//设置当前viewpager的位置
		pager.setCurrentItem(picName.size()*10000);
		
		//初始化指示器
		for (int i = 0; i < picName.size(); i++) {
			ImageView point = new ImageView(UIUtils.getUIContext());
			LinearLayout.LayoutParams param_point = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i == 0) {
				point.setImageResource(R.drawable.indicator_selected);
			}else {
				point.setImageResource(R.drawable.indicator_normal);
				
				param_point.leftMargin = UIUtils.dipToPx(5);
			}
			point.setLayoutParams(param_point);
			
			ll_container.addView(point);
		}
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				position = position % picName.size();
				
				ImageView point_current = (ImageView) ll_container.getChildAt(position);
				point_current.setImageResource(R.drawable.indicator_selected);
				
				ImageView point_previous = (ImageView) ll_container.getChildAt(prepoint_id);
				point_previous.setImageResource(R.drawable.indicator_normal);//上一个viewpager指示器设置为未选中
				
				prepoint_id = position; //实时记录当前位置，发生改变即变为先前位置
			}
			
			@Override
			 public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});
		
		//自动播放头条新闻
		HomeHeaderTask task = new HomeHeaderTask();
		task.beginSpread();
		
	}
	
	class HomeHeaderTask implements Runnable{
		private void beginSpread(){
			//移除之前所有的消息，避免消息重复
			UIUtils.getUIHandler().removeCallbacksAndMessages(null);
			UIUtils.getUIHandler().postDelayed(this, 3000);
		}
		
		@Override
		public void run() {
			int currentItem = pager.getCurrentItem();
			currentItem++;
			pager.setCurrentItem(currentItem);
			//循环发送消息实现内循环
			UIUtils.getUIHandler().postDelayed(this, 3000);
		}
		
	}
	
	class HomeHeaderAdapter extends PagerAdapter{
		private BitmapUtils bitmapUtils;
		
		public HomeHeaderAdapter() {
			bitmapUtils = BitmapHelper.getBitmapUtils();
		}
		
		@Override
		public int getCount() {
//			return picName.size();
			//实现循环播放
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = position % picName.size();
			String url = picName.get(position);
			ImageView image = new ImageView(UIUtils.getUIContext());
			image.setScaleType(ScaleType.FIT_XY);
			bitmapUtils.display(image, HttpHelper.URL + "image?name=" + url);
			
			container.addView(image);
			return image;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}
	

}
