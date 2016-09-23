package com.szxx.googleplay.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szxx.googelmarket.R;
import com.szxx.googleplay.domain.SubjectInfo;
import com.szxx.googleplay.http.HttpHelper;
import com.szxx.googleplay.uiutils.BitmapHelper;
import com.szxx.googleplay.uiutils.UIUtils;

public class SubjectHolder extends BaseHolder<SubjectInfo> {

	private View subView;
	private TextView tv_sub;
	private ImageView iv_pic;

	private BitmapUtils mBitmapUtils;

	@Override
	public View initView() {
		subView = UIUtils.inflateView(R.layout.list_item_subject);
		iv_pic = (ImageView) subView.findViewById(R.id.iv_subiect);
		tv_sub = (TextView) subView.findViewById(R.id.tv_subject);
				
//		mBitmapUtils = new BitmapUtils(UIUtils.getUIContext());
		mBitmapUtils = BitmapHelper.getBitmapUtils();  //为提高性能使用单例模式，只创建一次以后复用
		
		
		//tv = (TextView) homeView.findViewById(R.id.tv_test);
		return subView;
	}

	@Override
	public void refreshView(SubjectInfo data) {
		//tv.setText(data.name);
		tv_sub.setText(data.des);
		
		mBitmapUtils.display(iv_pic, HttpHelper.URL + "image?name=" + data.url);
	}

}
