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
		
		// 旋转动画
		RotateAnimation aniRotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		aniRotate.setDuration(1000);
		aniRotate.setFillAfter(true);
		
		// 渐变动画
		 AlphaAnimation aniAlpha = new AlphaAnimation(0, 1);
		 aniAlpha.setDuration(1000);
		 aniAlpha.setFillAfter(true);
		 
		
		//缩放动画
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
				//动画结束跳转页面
				//如果是第一次打开，跳转新手引导界面
				//如果是第二次及以后进入，直接显示主界面
				boolean firstLoad = PrefUtils.getBoolean(SplashActivity.this, "is_first_load", true);
				if (firstLoad) {
					//引导界面
					intent = new Intent(getApplicationContext(), GuideActivity.class);
				} else {
					//主页面
					intent = new Intent(getApplicationContext(), MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		});
	}
}
