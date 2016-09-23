package base;

import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetialPager {
	public Activity m2Activity;
	public View mRootView;
	public BaseMenuDetialPager(Activity activity) {
		m2Activity = activity;
		mRootView = initView();
	}
	
	//初始化布局，由子类去实现
	public abstract View initView();
	//初始化数据
	public void initData(){
		
	}
}
