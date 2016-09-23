package utils;

import android.content.Context;

public class DensityUttils {
	//像素转换为dp
	public static float pxToDip(float px, Context context){
		//像素密度
		float density = context.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
		
	}
	//dp转换为像素
	public static int dipToPx(float dp, Context context){
		float density = context.getResources().getDisplayMetrics().density;
		int px = (int) (dp * density + 0.5f);
		return px;
	}
}
