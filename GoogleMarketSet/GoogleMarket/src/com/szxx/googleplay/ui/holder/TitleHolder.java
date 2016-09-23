package com.szxx.googleplay.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.szxx.googelmarket.R;
import com.szxx.googleplay.domain.CategoryInfo;
import com.szxx.googleplay.uiutils.UIUtils;

public class TitleHolder extends BaseHolder<CategoryInfo> {

	private View titleView;
	private TextView tv_title;

	@Override
	public View initView() {
		titleView = UIUtils.inflateView(R.layout.list_item_title);
		tv_title = (TextView) titleView.findViewById(R.id.tv_cate_title);
		return titleView;
	}

	@Override
	public void refreshView(CategoryInfo data) {
		if (data.isTitle) {
			tv_title.setText(data.title);
		}
	}

}
