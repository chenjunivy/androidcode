package com.szxx.googleplay.ui.view;

import java.util.ArrayList;

import com.szxx.googleplay.uiutils.UIUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyFlowLayout extends ViewGroup {
	private int mChildWidth; //一行子控件的宽度之和，包含控件水平间距间距
	private Line mLine;  //当前行对象
	
	private ArrayList<Line> mLineList = new ArrayList<Line>();  //维护行对象的集合
	private static final int MAX_LINE = 100;
	private int marginLeft = UIUtils.dipToPx(7);
	private int marginTop = UIUtils.dipToPx(5);

	public MyFlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	public MyFlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	public MyFlowLayout(Context context) {
		super(context);
		
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//获取有效宽度/高度(去除内边距)
		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int height= MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
		
		//获取宽高模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		int childCount  = getChildCount();
		for(int i=0; i<childCount;i++){
			View childView = getChildAt(i);
			//如果父控件的宽度模式是确定的，相当于math_parent 子控件才有可能是warp_content,否者只能是和父控件模式保持一致，才有可能容纳子控件
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, (widthMode==MeasureSpec.EXACTLY)?MeasureSpec.AT_MOST:widthMode);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, (heightMode==MeasureSpec.EXACTLY)?MeasureSpec.AT_MOST:height);
			
			//测量子控件的宽高以及模式
			childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
			
			if (mLine == null) {
				mLine = new Line();
			}
			//获取子控件高度
			int childWidth = childView.getMeasuredWidth();
			mChildWidth += childWidth;  
			if (mChildWidth < width) {
				mLine.addView(childView);//当前行对象添加子控件
				
				mChildWidth += marginLeft;
				if (mChildWidth > width) {
					//增加水平间距后，超出了边界。此时需要执行换行
					if (!newLine()) {//如果换行不成功，行数超标，不再加载子控件填充到新的一行中
						break;
					}
				}
				
			}else {
				if (mLine.getLineCount() == 0) {
					//1.当前行没有任何控件,一旦添加，则超出行的宽度,需要先强行给当前行添加控件然后换行
					mLine.addView(childView);//强行添加到当前行
					if (!newLine()) {
						break;
					}
				}else {
					//2.当前行再添加一个子控件就超出行的宽度，需要先换行，再将子控件添加到下一行
					if (!newLine()) {
						break;
					}
					mLine.addView(childView);
					mChildWidth = childWidth + marginLeft;  //更新已使用宽度
				}
			}
			
		}
		
		//保存最后一行行对象
		if (mLine!=null && mLine.getLineCount()!=0 && !mLineList.contains(mLine)) {
			mLineList.add(mLine);
		}
		
		//添加子控件后的宽高
		int totalHeight = 0;  // 控件整体高度
		int totalWidth = MeasureSpec.getSize(widthMeasureSpec);  //控件整体宽度
		for (int i = 0; i < mLineList.size(); i++) {
			Line line = mLineList.get(i);
			totalHeight += line.mMaxHeight;
		}
		
		totalHeight += (mLineList.size()-1)*marginTop;  //增加竖直间距
		totalHeight += getPaddingTop() + getPaddingBottom();  //增加上下边距
		
		//根据最新的宽高来测量整体布局大小
		setMeasuredDimension(totalWidth, totalHeight);
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	//创建行的类
	class Line {
		
		private int mTotalWidth;  //当前行所含控件的总宽度
		public int mMaxHeight;  //当前行控件的最大高度
		private ArrayList<View> mChildViewList = new ArrayList<View>();
		
		//添加一个子控件
		public void addView(View view){
			mChildViewList.add(view);
			//总宽度增加
			mTotalWidth += view.getMeasuredWidth();
			
			int height = view.getMeasuredHeight();
			mMaxHeight = mMaxHeight<height?height:mMaxHeight;
			
		}
		
		public int getLineCount(){
			return mChildViewList.size();
		}
		
	}
	
	//换行方法
	private boolean newLine(){
		mLineList.add(mLine); //保存上一行数据
		
		if (mLineList.size() < MAX_LINE) {
			mLine = new Line();
			mChildWidth = 0; //当前行控件宽度之和清零
		return true;	
		}
		return false;
	}
	
	
}
