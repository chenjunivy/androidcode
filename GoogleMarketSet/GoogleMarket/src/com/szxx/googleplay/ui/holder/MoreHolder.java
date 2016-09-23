package com.szxx.googleplay.ui.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szxx.googelmarket.R;
import com.szxx.googleplay.uiutils.UIUtils;

public class MoreHolder extends BaseHolder<Integer> {

	public View moreView;
	private View tv_more;
	private View prg_more;
	private LinearLayout ll_loading;
	private TextView tv_fail;

	public final static int STATE_MORE_MORE = 0; //加载更多布局
	public final static int STATE_MORE_NONE = 1; //没有更多的布局可以加载
	public final static int STATE_MORE_ERROR = 2; //记载更多的布局失败
	
	public MoreHolder(boolean hasMore) {
		setData(hasMore ? STATE_MORE_MORE : STATE_MORE_NONE);
	}
	@Override
	public View initView() {
		moreView = UIUtils.inflateView(R.layout.list_item_more);
		ll_loading = (LinearLayout) moreView.findViewById(R.id.ll_loading);
		tv_fail = (TextView) moreView.findViewById(R.id.tv_fail);
		tv_more = moreView.findViewById(R.id.tv_more);
		prg_more = moreView.findViewById(R.id.prg_more);
		return moreView;
	}

	@Override
	public void refreshView(Integer data) {
		switch (data) {
		case STATE_MORE_MORE:
			//显示加载更多
			ll_loading.setVisibility(View.VISIBLE);
			tv_fail.setVisibility(View.GONE);
			break;
		case STATE_MORE_NONE:
			//隐藏加载更多
			ll_loading.setVisibility(View.GONE);
			tv_fail.setVisibility(View.GONE);
			break;
		case STATE_MORE_ERROR:
			//显示加载失败的布局
			ll_loading.setVisibility(View.GONE);
			tv_fail.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

}
