package com.szxx.googleplay.uiutils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtils {
	//获取一个shape对象
	public static GradientDrawable getGradientDrawable(int color, int radius){
		//xml中的shape标签对应的是此类
		GradientDrawable shape = new GradientDrawable();
		shape.setShape(GradientDrawable.RECTANGLE);//矩形
		shape.setCornerRadius(radius);//圆角半径
		shape.setColor(color);//颜色
		return shape;
	}
	
	//获取状态选择器
	public static StateListDrawable getSelector(Drawable normal, Drawable press){
		
		StateListDrawable selector = new StateListDrawable();
		selector.addState(new int[]{android.R.attr.state_pressed}, press);//按下时图片
		selector.addState(new int[]{}, normal);//默认时图片
		
		return selector;
	}
	
	//获取状态选择器，方法的重载
	public static StateListDrawable getSelector(int normal, int press, int radius){
		GradientDrawable bgnormal = getGradientDrawable(normal, radius);
		GradientDrawable bgpress = getGradientDrawable(press, radius);
		
		StateListDrawable selector = getSelector(bgnormal, bgpress);
		return selector;
	}
}
