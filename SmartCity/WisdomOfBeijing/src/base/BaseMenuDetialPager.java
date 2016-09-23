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
	
	//��ʼ�����֣�������ȥʵ��
	public abstract View initView();
	//��ʼ������
	public void initData(){
		
	}
}
