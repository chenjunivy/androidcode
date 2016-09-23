package com.szxx.googleplay.uiutils;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelper {
	private static BitmapUtils mBitmapUtils = null;
	public static BitmapUtils getBitmapUtils(){
		if (mBitmapUtils == null) {
			synchronized (BitmapHelper.class) {
				if (mBitmapUtils == null) {
					mBitmapUtils = new BitmapUtils(UIUtils.getUIContext());
				}
			}
		}
		return mBitmapUtils;
	}
}
