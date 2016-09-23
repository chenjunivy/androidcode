package com.szxx.googleplay.ui.fragment;

import java.util.ArrayList;

import android.view.View;

import com.szxx.googleplay.domain.AppInfos;
import com.szxx.googleplay.http.protocol.AppProtocol;
import com.szxx.googleplay.ui.adpter.MyBaseAdapter;
import com.szxx.googleplay.ui.holder.AppHolder;
import com.szxx.googleplay.ui.holder.BaseHolder;
import com.szxx.googleplay.ui.view.LoadingLayout.ResultState;
import com.szxx.googleplay.ui.view.MyListView;
import com.szxx.googleplay.uiutils.UIUtils;

public class AppFragment extends BaseFragment {

	private ArrayList<AppInfos> data;
	
	@Override
	public View createSuccessView() {
		MyListView listView = new MyListView(UIUtils.getUIContext());
		listView.setAdapter(new AppAdapter(data));

		return listView;
	}

	@Override
	public ResultState onLoadDataResult() {
		AppProtocol app = new AppProtocol();
		data = app.getData(0);
				
		return checkId(data);
	}
	
	class AppAdapter extends MyBaseAdapter<AppInfos>{

		public AppAdapter(ArrayList<AppInfos> data) {
			super(data);
		}

		@Override
		public BaseHolder<AppInfos> getHolder(int position) {
			return new AppHolder();
		}

		@Override
		public ArrayList<AppInfos> onLoadMore() {
			AppProtocol moreapp = new AppProtocol();
			ArrayList<AppInfos> moredata = moreapp.getData(getListSize());
			return moredata;
		}
		
	}

}
