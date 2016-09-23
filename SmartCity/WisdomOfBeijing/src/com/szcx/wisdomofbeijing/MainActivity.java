package com.szcx.wisdomofbeijing;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import fragment.ContentFragment;
import fragment.LeftFragment;

public class MainActivity extends SlidingFragmentActivity {
	private static final String TAG_MAIN_CCONTENT = "TAG_MAIN_CCONTENT";
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//设置侧边栏
		setBehindContentView(R.layout.slidingmenu_slide_left);
		//SlidingMenu右边栏
		SlidingMenu slidingMenu = getSlidingMenu();
//		//设置右侧边栏
//		slidingMenu.setSecondaryMenu(R.layout.activity_slide_right);
//		//设置左右都有侧边栏
//		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
//		//设置全屏触摸可以触发侧边栏
//		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//设置主页面的留余空间
//		slidingMenu.setBehindOffset(630);
		
		int width = getWindowManager().getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(width*2/3);
		initFragment();
	}
	
	private void initFragment(){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transcation = fm.beginTransaction();
		//使用Fragment替换贞布局（贞布局id,即将替换新的Fragment,Fragment的标记）
		transcation.replace(R.id.fl_left, new LeftFragment(), TAG_LEFT_MENU);
		transcation.replace(R.id.fl_main, new ContentFragment(), TAG_MAIN_CCONTENT);
		transcation.commit();
		
//		Fragment fragment = fm.findFragmentByTag(TAG_LEFT_MENU);//根据标记找到Fragment
		
	}
	
	//获取侧边栏fragment对象
	public LeftFragment findLeftFragment(){
		FragmentManager fm = getSupportFragmentManager();
		LeftFragment fragment = (LeftFragment) fm.findFragmentByTag(TAG_LEFT_MENU);//根据标记找到Fragment
		return fragment;
		
	}
	
	//获取内容区fragment对象
	public ContentFragment findContentFragment(){
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment2 = (ContentFragment) fm.findFragmentByTag(TAG_MAIN_CCONTENT);//根据标记找到Fragment
		return fragment2;
	}
	
	
}
