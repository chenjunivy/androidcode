package utils;

import android.content.Context;

public class DensityUttils {
	//����ת��Ϊdp
	public static float pxToDip(float px, Context context){
		//�����ܶ�
		float density = context.getResources().getDisplayMetrics().density;
		float dp = px / density;
		return dp;
		
	}
	//dpת��Ϊ����
	public static int dipToPx(float dp, Context context){
		float density = context.getResources().getDisplayMetrics().density;
		int px = (int) (dp * density + 0.5f);
		return px;
	}
}
