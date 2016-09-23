package com.szxx.googleplay.ui.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.szxx.googleplay.domain.AppInfos;
import com.szxx.googleplay.http.protocol.HomeProtocol;
import com.szxx.googleplay.ui.activity.HomeDetailActivity;
import com.szxx.googleplay.ui.adpter.MyBaseAdapter;
import com.szxx.googleplay.ui.holder.BaseHolder;
import com.szxx.googleplay.ui.holder.HomeHeaderHolder;
import com.szxx.googleplay.ui.holder.HomeHolder;
import com.szxx.googleplay.ui.view.LoadingLayout.ResultState;
import com.szxx.googleplay.ui.view.MyListView;
import com.szxx.googleplay.uiutils.UIUtils;

public class HomeFragment extends BaseFragment {

//	private ArrayList<String> data;
	private ArrayList<AppInfos> data;
	private ArrayList<String> pictureList;

	//如果加载数据成功，回调该方法，创建成功后的布局界面
	@Override
	public View createSuccessView() {
//		TextView view = new TextView(UIUtils.getUIContext());
//		view.setText(getClass().getSimpleName());
		MyListView listView = new MyListView(UIUtils.getUIContext());
		HomeHeaderHolder header = new HomeHeaderHolder();
		if (pictureList != null) {
			header.setData(pictureList);
		}
		listView.addHeaderView(header.getRootView());
		listView.setAdapter(new HomeAdapter(data));

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppInfos appInfo = data.get(position-1);//去除自动播放头条
				if (appInfo != null) {
					String packageName = appInfo.packageName;
					Intent intent = new Intent(UIUtils.getUIContext(), HomeDetailActivity.class);
					intent.putExtra("packagename", packageName);
					startActivity(intent);
				}
			}
		});
		return listView;
	}

	//传递实现该方法，返回加载数据后的状态
	@Override
	public ResultState onLoadDataResult() {
//		data = new ArrayList<String>();
//		for (int i = 0; i < 20; i++) {
//			data.add("测试数据" + i);
//		}
		HomeProtocol protocol = new HomeProtocol();
		data = protocol.getData(0);
		
		pictureList = protocol.getPictureList();
		return checkId(data);
	}
	
	
	
	class HomeAdapter extends MyBaseAdapter<AppInfos>{
		
		
		public HomeAdapter(ArrayList<AppInfos> data) {
			super(data);
		}

		@Override
		public BaseHolder<AppInfos> getHolder(int position) {
			return new HomeHolder();
		}

		//运行在子线程中,可执行耗时操作
		@Override
		public ArrayList<AppInfos> onLoadMore() {
//			ArrayList<String> moreData = new ArrayList<>();
//			for (int i = 0; i < 10; i++) {
//				moreData.add("更多数据测试 " + i);
//			}
//			SystemClock.sleep(1000);
			
			HomeProtocol protocol2 = new HomeProtocol();
			ArrayList<AppInfos> moreData = protocol2.getData(getListSize());
			return moreData;
		}

//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder;
//			if (convertView == null) {
//				//1.加载布局，
//				convertView = UIUtils.inflateView(R.layout.list_page_home);
//				holder = new ViewHolder();
//				//2.加载组件
//				holder.tv_test = (TextView) convertView.findViewById(R.id.tv_test);
//				//3.设置标记
//				convertView.setTag(holder);
//			}else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			//4.填充数据
//			String content = getItem(position);
//			holder.tv_test.setText(content);
//			return convertView;
//		}
		
		

	
	
	
	
	
	
	
	
	
	
	
	}
	
//	static class ViewHolder{
//		public TextView tv_test;
//	}

}
