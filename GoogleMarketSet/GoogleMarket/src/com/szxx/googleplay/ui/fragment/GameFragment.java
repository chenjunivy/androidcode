package com.szxx.googleplay.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.szxx.googleplay.ui.view.LoadingLayout.ResultState;
import com.szxx.googleplay.uiutils.UIUtils;

public class GameFragment extends BaseFragment {

	@Override
	public View createSuccessView() {
		TextView view  = new TextView(UIUtils.getUIContext());
		view.setText("GameFragment");
		return view;
	}

	@Override
	public ResultState onLoadDataResult() {
		return ResultState.STATE_SUCCESS;
	}

}
