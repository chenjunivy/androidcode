package com.szxx.googleplay.ui.holder;

import android.view.View;

public abstract class BaseHolder<T> {
	public View contentView;
	public T data;
	
	public BaseHolder() {
		contentView = initView();
		contentView.setTag(this);
	}
	
	//返回item的布局对象
	public View getRootView(){
		return contentView;
	}
	
	//1.初始化布局与组件
	public abstract View initView();
	
	//2.根据数据刷新组件
	public abstract void refreshView(T data);
	
	//3.设置item的刷新数据，同时刷新组件
	public void setData(T data){
		this.data = data;
		refreshView(data);
	}
	
	//4.获取刷新数据
	public T getData(){
		return data;
	}
}
