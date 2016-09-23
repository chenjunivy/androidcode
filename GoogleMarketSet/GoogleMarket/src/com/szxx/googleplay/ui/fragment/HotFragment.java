package com.szxx.googleplay.ui.fragment;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.szxx.googleplay.http.protocol.HotProtocol;
import com.szxx.googleplay.ui.view.FlowLayout;
import com.szxx.googleplay.ui.view.LoadingLayout.ResultState;
import com.szxx.googleplay.uiutils.DrawableUtils;
import com.szxx.googleplay.uiutils.UIUtils;

public class HotFragment extends BaseFragment {

	private ArrayList<String> data;
	@Override
	public View createSuccessView() {
		ScrollView scrollView = new ScrollView(UIUtils.getUIContext());
		FlowLayout flow = new FlowLayout(UIUtils.getUIContext());
		//设置内边距
		int padding = UIUtils.dipToPx(6);
		flow.setPadding(padding, padding, padding, padding);
		
		flow.setHorizontalSpacing(UIUtils.dipToPx(8));//设置水平间距
		flow.setVerticalSpacing(UIUtils.dipToPx(8));//设置垂直间距
		for (int i = 0; i < data.size(); i++) {
			final String keyword = data.get(i);
			TextView view = new TextView(UIUtils.getUIContext());
			view.setText(keyword);
			
			view.setTextColor(Color.WHITE);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			view.setPadding(padding, padding, padding, padding);
			
			Random random = new Random();
			int red = 30 + random.nextInt(200);
			int green = 30 + random.nextInt(200);
			int blue = 30 + random.nextInt(200);
			
			int color = 0xffcecece;//按下后偏白的背景色
			
			//将如下代码重载到DrawableUtils中去
/*			GradientDrawable bgNormal = DrawableUtils.getGradientDrawable(Color.rgb(red, green, blue), UIUtils.dipToPx(6));
			GradientDrawable bgPress = DrawableUtils.getGradientDrawable(color, UIUtils.dipToPx(6));
			
			StateListDrawable selector = DrawableUtils.getSelector(bgNormal, bgPress);
*/
			StateListDrawable selector = DrawableUtils.getSelector(Color.rgb(red, green, blue), color, UIUtils.dipToPx(6));
			view.setBackground(selector);
			
			flow.addView(view);
			//设置点击监听事件，状态选择器才起作用
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(UIUtils.getUIContext(), keyword, Toast.LENGTH_LONG).show();
				}
			});
		}
		scrollView.addView(flow);
		return scrollView;
	}

	@Override
	public ResultState onLoadDataResult() {
		HotProtocol protocol = new HotProtocol();
		data = protocol.getData(0);
		return checkId(data);
	}

}
