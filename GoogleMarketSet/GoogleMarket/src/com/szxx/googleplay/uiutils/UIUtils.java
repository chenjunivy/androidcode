package com.szxx.googleplay.uiutils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.szxx.googleplay.golbal.GooglePlayApplication;

public class UIUtils {
	
	/**********************基本初始化**************************/
	
	public static Context getUIContext(){
		return GooglePlayApplication.getContext();
	}
	
	public static Handler getUIHandler(){
		return GooglePlayApplication.getHandler();
	}
	
	public static int getUIMainThreadId(){
		return GooglePlayApplication.getMainThreadId();
	}
	
	/************************获取资源****************************/
	
	//获取字符串
	public static String getString(int id){
		return getUIContext().getResources().getString(id);
	}
	
	//获取字符数组
	public static String[] getStringArray(int id){
		return getUIContext().getResources().getStringArray(id);
	}
	
	//获取颜色数组
	public static int getColor(int id){
		return getUIContext().getResources().getColor(id);
	}
	
	//获取磁村
	public static int getDimen(int id){
		return getUIContext().getResources().getDimensionPixelSize(id);//返回具体像素值
	}
	
	//获取图片
	public static Drawable getDrawable(int id){
		return getUIContext().getResources().getDrawable(id);
	}
	
	//根据id获取颜色状态选择器
	public static ColorStateList getColorStateList(int id) {
		return getUIContext().getResources().getColorStateList(id);
	}
	/************************UI操作****************************/
	
	//dip px转换
	//像素dp 转换为 px
	public static int dipToPx(float dip){
		float density = getUIContext().getResources().getDisplayMetrics().density;
		int px = (int) (dip*density + 0.5f);
		return px;
	}
	
	//像素px转换为dp
	public static float pxToDip(int px){
		float density = getUIContext().getResources().getDisplayMetrics().density;
		float dp = px/density;
		return dp;
	}
	
	//加载布局文件
	public static View inflateView(int id){
		return View.inflate(getUIContext(), id, null);
	}
	
	//判断是否运行在主线程
	public static boolean isRunOnUIThread(){
		//获取当前线程id,如果线程id和主线程id相同,
		int currentId = android.os.Process.myTid();
		if (currentId == getUIMainThreadId()) {
			return true;
		}
		
		return false;
	}
		
	//运行在主线程
	public static void runOnUIThread(Runnable r){
		if (isRunOnUIThread()) {
			r.run();//如果是主线程，直接运行
		}else {
			getUIHandler().post(r);//如果是子线程，借助handler使其运行在主线程
		}
	}


		
	}
