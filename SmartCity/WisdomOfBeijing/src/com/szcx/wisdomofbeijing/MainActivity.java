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
		//���ò����
		setBehindContentView(R.layout.slidingmenu_slide_left);
		//SlidingMenu�ұ���
		SlidingMenu slidingMenu = getSlidingMenu();
//		//�����Ҳ����
//		slidingMenu.setSecondaryMenu(R.layout.activity_slide_right);
//		//�������Ҷ��в����
//		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
//		//����ȫ���������Դ��������
//		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//������ҳ�������ռ�
//		slidingMenu.setBehindOffset(630);
		
		int width = getWindowManager().getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(width*2/3);
		initFragment();
	}
	
	private void initFragment(){
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transcation = fm.beginTransaction();
		//ʹ��Fragment�滻�겼�֣��겼��id,�����滻�µ�Fragment,Fragment�ı�ǣ�
		transcation.replace(R.id.fl_left, new LeftFragment(), TAG_LEFT_MENU);
		transcation.replace(R.id.fl_main, new ContentFragment(), TAG_MAIN_CCONTENT);
		transcation.commit();
		
//		Fragment fragment = fm.findFragmentByTag(TAG_LEFT_MENU);//���ݱ���ҵ�Fragment
		
	}
	
	//��ȡ�����fragment����
	public LeftFragment findLeftFragment(){
		FragmentManager fm = getSupportFragmentManager();
		LeftFragment fragment = (LeftFragment) fm.findFragmentByTag(TAG_LEFT_MENU);//���ݱ���ҵ�Fragment
		return fragment;
		
	}
	
	//��ȡ������fragment����
	public ContentFragment findContentFragment(){
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment2 = (ContentFragment) fm.findFragmentByTag(TAG_MAIN_CCONTENT);//���ݱ���ҵ�Fragment
		return fragment2;
	}
	
	
}
