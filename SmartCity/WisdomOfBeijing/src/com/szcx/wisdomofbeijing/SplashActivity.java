package com.szcx.wisdomofbeijing;

import utils.PrefUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {
	RelativeLayout rlRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		
		// ��ת����
		RotateAnimation aniRotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		aniRotate.setDuration(1000);
		aniRotate.setFillAfter(true);
		
		// ���䶯��
		 AlphaAnimation aniAlpha = new AlphaAnimation(0, 1);
		 aniAlpha.setDuration(1000);
		 aniAlpha.setFillAfter(true);
		 
		
		//���Ŷ���
		ScaleAnimation aniScale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5F);
		aniScale.setDuration(1000);
		aniScale.setFillAfter(true);
		
		AnimationSet aniSet = new AnimationSet(true);
		aniSet.addAnimation(aniAlpha);
		aniSet.addAnimation(aniScale);
		aniSet.addAnimation(aniRotate);
		
		rlRoot.startAnimation(aniSet);
		
		aniSet.setAnimationListener(new AnimationListener() {
			
			private Intent intent;

			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//����������תҳ��
				//����ǵ�һ�δ򿪣���ת������������
				//����ǵڶ��μ��Ժ���룬ֱ����ʾ������
				boolean firstLoad = PrefUtils.getBoolean(SplashActivity.this, "is_first_load", true);
				if (firstLoad) {
					//��������
					intent = new Intent(getApplicationContext(), GuideActivity.class);
				} else {
					//��ҳ��
					intent = new Intent(getApplicationContext(), MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});
	}
}
