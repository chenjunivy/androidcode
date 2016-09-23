package com.szxx.googleplay.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView extends ListView {

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public MyListView(Context context) {
		super(context);
		initView();
	}
	//为listview设置一系列的属性
	private void initView(){
		this.setSelector(new ColorDrawable());  //设置默认状态显示器为全透明(按下listview选项时没有边框颜色变化)
		this.setDivider(null);  //去掉分隔线(即listvie选项边框 隐隐可见的分隔线，使颜色更加纯粹)
		this.setCacheColorHint(Color.TRANSPARENT);  //有时滑动listview会变成黑色，将背景变为全透明
	}

}
