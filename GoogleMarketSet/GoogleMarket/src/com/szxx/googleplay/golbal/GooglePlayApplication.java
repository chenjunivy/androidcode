package com.szxx.googleplay.golbal;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 *自定义application,进行全局初始化 
 */
public class GooglePlayApplication extends Application {

	public static Context context;
	public static Handler handler;
	public static int mainThreadId;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		handler = new Handler();
		mainThreadId = android.os.Process.myTid();//获取线程标记
	}

	public static Context getContext() {
		return context;
	}

	public static Handler getHandler() {
		return handler;
	}

	public static int getMainThreadId() {
		return mainThreadId;
	}
	
}
