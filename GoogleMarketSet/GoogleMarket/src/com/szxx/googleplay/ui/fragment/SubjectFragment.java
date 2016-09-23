package com.szxx.googleplay.ui.fragment;

import java.util.ArrayList;

import android.view.View;

import com.szxx.googleplay.domain.SubjectInfo;
import com.szxx.googleplay.http.protocol.SubjectProtocol;
import com.szxx.googleplay.ui.adpter.MyBaseAdapter;
import com.szxx.googleplay.ui.holder.BaseHolder;
import com.szxx.googleplay.ui.holder.SubjectHolder;
import com.szxx.googleplay.ui.view.LoadingLayout.ResultState;
import com.szxx.googleplay.ui.view.MyListView;
import com.szxx.googleplay.uiutils.UIUtils;

public class SubjectFragment extends BaseFragment {

	ArrayList<SubjectInfo> data;
	@Override
	public View createSuccessView() {
		MyListView subjectlist = new MyListView(UIUtils.getUIContext());
		subjectlist.setAdapter(new SubjectAdapter(data));
		return subjectlist;
	}

	@Override
	public ResultState onLoadDataResult() {
		SubjectProtocol protocol = new SubjectProtocol();
		data = protocol.getData(0);
		return checkId(data);
	}
	
	class SubjectAdapter extends MyBaseAdapter<SubjectInfo>{

		public SubjectAdapter(ArrayList<SubjectInfo> data) {
			super(data);
		}

		@Override
		public BaseHolder<SubjectInfo> getHolder(int position) {
			return new SubjectHolder();
		}

		@Override
		public ArrayList<SubjectInfo> onLoadMore() {
			SubjectProtocol protocol = new SubjectProtocol();
			ArrayList<SubjectInfo> moredata = protocol.getData(getListSize());
			return moredata;
		}
		
	}

}
