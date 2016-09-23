package com.szcx.wisdomofbeijing;

import java.util.ArrayList;
import java.util.List;

import utils.DensityUttils;
import utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GuideActivity extends Activity {
	private ViewPager vp;
	private List<ImageView> al_images;
	private LinearLayout ll_container;
	private ImageView iv_point;
	private Button btn_welcome;

	private int[] imgIds = new int[] { R.drawable.guide_1, R.drawable.guide_2,
			R.drawable.guide_3 };
	private int distance_tween;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		vp = (ViewPager) findViewById(R.id.hello_freshman);
		ll_container = (LinearLayout) findViewById(R.id.ll_point);
		iv_point = (ImageView) findViewById(R.id.iv_point_red);
		btn_welcome = (Button) findViewById(R.id.btn_start);
		init();
		vp.setAdapter(new GuidPageAdapter());
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				//某个页面被选中
				if (position == imgIds.length -1) {
					btn_welcome.setVisibility(View.VISIBLE);
				} else {
					btn_welcome.setVisibility(View.INVISIBLE);
				}
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// 当前页面滑动过程中的回调
				System.out.println("当前位置：" + position + "移动偏移的百分比：" + positionOffset);
				//更新小红点距离, 计算当前小红点的左边距
				  int leftMargin_distance = (int) (distance_tween*positionOffset + position*distance_tween);
				  RelativeLayout.LayoutParams param1 = (LayoutParams) iv_point.getLayoutParams();
				  param1.leftMargin = leftMargin_distance;
				  iv_point.setLayoutParams(param1);
				  	
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// 页面状态发生变化时的回调
				
			}
		});
		//计算两圆点的距离
		//移动距离 = 第二个圆点left值 - 第一个圆点left值
		//控件的创建需要在onCreate()之后 , 然后执行流程  measure --> layout --> draw
//		distance_tween = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
//		System.out.println("圆点距离：" + distance_tween);
		
		//监听layout方法结束后的事件，位置确定后再获取圆点间距
		iv_point.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// layout 方法结束后回调该方法
				distance_tween = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
				System.out.println("圆点距离：" + distance_tween);
				//移除监听，避免重复回调
				iv_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				
			}
		});
		
		btn_welcome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//更新SharePerference ,第一次登陆已经记录
				PrefUtils.setBoolean(getApplicationContext(), "is_first_load", false);
				
				//跳转主activity
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	// 初始化数据
	private void init() {
		al_images = new ArrayList<ImageView>();
		for (int i = 0; i < imgIds.length; i++) {
			ImageView img = new ImageView(this);
			img.setBackgroundResource(imgIds[i]);
			// img.setImageResource(resId);//等效于上行代码，但图片受分辨率影响，不一定全部填充父ImageView组件
			al_images.add(img);

			// 初始化小圆点，底色灰色点
			ImageView point = new ImageView(this);
			point.setImageResource(R.drawable.guide_point_gray);
			// 设置点间距之前需要先获得布局参数
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i > 0) {
//				param.leftMargin = 10;
				param.leftMargin = DensityUttils.dipToPx(10, this);
			}
			point.setLayoutParams(param);
			ll_container.addView(point);
		}
	}

	class GuidPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return al_images.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		// 初始化item布局
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = al_images.get(position);
			container.addView(view);
			return view;
		}

		// 销毁item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
