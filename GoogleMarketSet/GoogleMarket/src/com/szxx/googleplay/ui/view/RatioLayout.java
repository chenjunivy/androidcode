package com.szxx.googleplay.ui.view;

import com.szxx.googelmarket.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
/**
 * 自定义控件，按照比例来决定布局高度
 * @author SystemIvy
 *
 */
public class RatioLayout extends FrameLayout {

	private float ratio;

	public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获取属性
//		attrs.getAttributeFloatValue("", "ratio", -1);
		//自定义属性时，系统会自动生成属性相关id,此id通过R。styleable来引用 
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
		//id=属性名_具体属性字段名称(此id自动生成)
		ratio = array.getFloat(R.styleable.RatioLayout_ratio, -1); //获取宽高比
		array.recycle();//回收数组，提高性能
		System.out.println("ratio = " + ratio);
		
	}

	public RatioLayout(Context context) {
		super(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//1.获取宽度
		//2.根据宽度和图片的宽高比例计算出控件宽高
		//3.重新绘制控件
		
		int width = MeasureSpec.getSize(widthMeasureSpec);//获取宽度值
		int withMode = MeasureSpec.getMode(widthMeasureSpec);//获取宽度模式
		int height = MeasureSpec.getSize(heightMeasureSpec);//获取高度值
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);//获取高度模式
		
		//宽度确定，高度不确定，才开始计算高度值
		if (withMode==MeasureSpec.EXACTLY && heightMode!=MeasureSpec.EXACTLY && ratio>0) {
			//获取子控件的宽度(除去内边距)
			int sub_width = width - getPaddingLeft() - getPaddingRight(); 
			//获取子控件的高度
			int sub_height = (int) (sub_width/ratio + 0.5f);
			//获取父控件高度(父控件指的是自身RatioLayout)
			int main_height = sub_height + getPaddingBottom() + getPaddingTop();
			//给父控件高度重新赋值
			height = main_height;
			//从新定义父控件高度和模式
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
		}
		
//		MeasureSpec.AT_MOST  //至多模式，控件有多大显示多大wrap_content
//		MeasureSpec.EXACTLY  //确定模式，控件宽高写死  类似于match_parent
//		MeasureSpec.UNSPECIFIED  //未指定模式
		
		//按照新的模式和高度重新测量控件
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
