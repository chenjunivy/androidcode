package com.szxx.googleplay.ui.fragment;

import java.util.ArrayList;

import com.szxx.googleplay.ui.view.LoadingLayout;
import com.szxx.googleplay.ui.view.LoadingLayout.ResultState;
import com.szxx.googleplay.uiutils.UIUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment {
	private LoadingLayout mLoadingLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			mLoadingLayout = new LoadingLayout(UIUtils.getUIContext()){

			@Override
			public View createSuccessView() {
				return BaseFragment.this.createSuccessView();
			}

			@Override
			public ResultState onLoadResult() {
				return onLoadDataResult();
			}
			
		};
			return mLoadingLayout;
		
	}
	
	//加载成功的布局，必须由子类实现
	public abstract View createSuccessView();
	//加载网络数据，必须由子类实现
	public abstract  ResultState onLoadDataResult();
	//加载数据
	public void onLoad(){
		if (mLoadingLayout != null) {
			mLoadingLayout.onLoadData();
		}
	}
	
	//对网络数据的返回值合法性进行校验
	public ResultState checkId(Object obj){
		if (obj != null) {
			if (obj instanceof ArrayList) {
				ArrayList list = (ArrayList) obj;
				if (list.isEmpty()) {
					return ResultState.STATE_EMPTY;
				}else {
					return ResultState.STATE_SUCCESS;
				}
			}
		}
		return ResultState.STATE_ERROR;
	}
}
