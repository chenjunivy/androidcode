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
				//ĳ��ҳ�汻ѡ��
				if (position == imgIds.length -1) {
					btn_welcome.setVisibility(View.VISIBLE);
				} else {
					btn_welcome.setVisibility(View.INVISIBLE);
				}
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// ��ǰҳ�滬�������еĻص�
				System.out.println("��ǰλ�ã�" + position + "�ƶ�ƫ�Ƶİٷֱȣ�" + positionOffset);
				//����С������, ���㵱ǰС������߾�
				  int leftMargin_distance = (int) (distance_tween*positionOffset + position*distance_tween);
				  RelativeLayout.LayoutParams param1 = (LayoutParams) iv_point.getLayoutParams();
				  param1.leftMargin = leftMargin_distance;
				  iv_point.setLayoutParams(param1);
				  	
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// ҳ��״̬�����仯ʱ�Ļص�
				
			}
		});
		//������Բ��ľ���
		//�ƶ����� = �ڶ���Բ��leftֵ - ��һ��Բ��leftֵ
		//�ؼ��Ĵ�����Ҫ��onCreate()֮�� , Ȼ��ִ������  measure --> layout --> draw
//		distance_tween = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
//		System.out.println("Բ����룺" + distance_tween);
		
		//����layout������������¼���λ��ȷ�����ٻ�ȡԲ����
		iv_point.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// layout ����������ص��÷���
				distance_tween = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
				System.out.println("Բ����룺" + distance_tween);
				//�Ƴ������������ظ��ص�
				iv_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				
			}
		});
		
		btn_welcome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//����SharePerference ,��һ�ε�½�Ѿ���¼
				PrefUtils.setBoolean(getApplicationContext(), "is_first_load", false);
				
				//��ת��activity
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	// ��ʼ������
	private void init() {
		al_images = new ArrayList<ImageView>();
		for (int i = 0; i < imgIds.length; i++) {
			ImageView img = new ImageView(this);
			img.setBackgroundResource(imgIds[i]);
			// img.setImageResource(resId);//��Ч�����д��룬��ͼƬ�ֱܷ���Ӱ�죬��һ��ȫ����丸ImageView���
			al_images.add(img);

			// ��ʼ��СԲ�㣬��ɫ��ɫ��
			ImageView point = new ImageView(this);
			point.setImageResource(R.drawable.guide_point_gray);
			// ���õ���֮ǰ��Ҫ�Ȼ�ò��ֲ���
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

		// ��ʼ��item����
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = al_images.get(position);
			container.addView(view);
			return view;
		}

		// ����item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
}
