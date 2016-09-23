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

public class AppDetailHolder extends BaseHolder<AppInfos> {

	private View view;
	private ImageView ic_icon;
	private TextView tv_name;
	private RatingBar rb_star;
	private TextView tv_count;
	private TextView tv_version;
	private TextView tv_date;
	private TextView tv_size;
	private BitmapUtils bitmapUtils;
	
	@Override
	public View initView() {
		view = UIUtils.inflateView(R.layout.list_page_home_appdetail);
		ic_icon = (ImageView) view.findViewById(R.id.iv_home_icon);
		tv_name = (TextView) view.findViewById(R.id.tv_detailname);
		rb_star = (RatingBar) view.findViewById(R.id.rb_star_detail);
		tv_count = (TextView) view.findViewById(R.id.tv_download);
		tv_version = (TextView) view.findViewById(R.id.tv_version);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_size = (TextView) view.findViewById(R.id.tv_space);
		
		bitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshView(AppInfos data) {
		bitmapUtils.display(ic_icon, HttpHelper.URL + "image?name=" + data.iconUrl);
		tv_name.setText(data.name);
		rb_star.setRating(data.stars);
		tv_count.setText("下载量:" + data.downloadNum);
		tv_version.setText("版本号:"+ data.version);
		tv_date.setText(data.date);
		tv_size.setText(Formatter.formatFileSize(UIUtils.getUIContext(), data.size));
	}

}
