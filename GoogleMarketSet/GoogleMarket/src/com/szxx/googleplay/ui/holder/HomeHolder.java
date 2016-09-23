package com.szxx.googleplay.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szxx.googelmarket.R;
import com.szxx.googleplay.domain.AppInfos;
import com.szxx.googleplay.http.HttpHelper;
import com.szxx.googleplay.uiutils.BitmapHelper;
import com.szxx.googleplay.uiutils.UIUtils;

public class HomeHolder extends BaseHolder<AppInfos> {

	private View homeView;
	private TextView tv;
	private ImageView iv_icon;
	private TextView tv_name;
	private TextView tv_size;
	private RatingBar rb_star;
	private TextView tv_desc;
	private BitmapUtils mBitmapUtils;

	@Override
	public View initView() {
		homeView = UIUtils.inflateView(R.layout.list_page_home);
		iv_icon = (ImageView) homeView.findViewById(R.id.iv_home_logo);
		tv_name = (TextView) homeView.findViewById(R.id.tv_home_name);
		tv_size = (TextView) homeView.findViewById(R.id.tv_contain);
		rb_star = (RatingBar) homeView.findViewById(R.id.rb_star);
		tv_desc = (TextView) homeView.findViewById(R.id.tv_home_desc);
		
//		mBitmapUtils = new BitmapUtils(UIUtils.getUIContext());
		mBitmapUtils = BitmapHelper.getBitmapUtils();  //为提高性能使用单例模式，只创建一次以后复用
		
		
		//tv = (TextView) homeView.findViewById(R.id.tv_test);
		return homeView;
	}

	@Override
	public void refreshView(AppInfos data) {
		//tv.setText(data.name);
		tv_name.setText(data.name);
		tv_size.setText(Formatter.formatFileSize(UIUtils.getUIContext(), data.size));
		tv_desc.setText(data.des);
		rb_star.setRating(data.stars);
		
		mBitmapUtils.display(iv_icon, HttpHelper.URL + "image?name=" + data.iconUrl);
	}

}
