package com.szxx.googleplay.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.szxx.googelmarket.R;
import com.szxx.googleplay.domain.AppInfos;
import com.szxx.googleplay.http.protocol.HomeDetailProtocol;
import com.szxx.googleplay.ui.holder.AppDetailHolder;
import com.szxx.googleplay.ui.holder.DetialDesHolder;
import com.szxx.googleplay.ui.holder.DetialPicsHolder;
import com.szxx.googleplay.ui.holder.DetialSafeHoolder;
import com.szxx.googleplay.ui.holder.DownloadApkHolder;
import com.szxx.googleplay.ui.view.LoadingLayout;
import com.szxx.googleplay.ui.view.LoadingLayout.ResultState;
import com.szxx.googleplay.uiutils.UIUtils;

public class HomeDetailActivity extends Activity {
//	private LoadingLayout loadingLayout;
	private String packageName;
	private AppInfos appData;
	private LoadingLayout loadingLayout;
	
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		loadingLayout = new LoadingLayout(UIUtils.getUIContext()) {
			
			@Override
			public ResultState onLoadResult() {
				return HomeDetailActivity.this.onLoad();
			}
			
			@Override
			public View createSuccessView() {
				return HomeDetailActivity.this.createSuccessView();
			}
		};
		setContentView(loadingLayout);
		packageName = getIntent().getStringExtra("packagename");
		loadingLayout.onLoadData();//开始加载数据
		
		onActionBarInit();
	}
	 
	 private void onActionBarInit(){
			ActionBar actionbar = getActionBar();
			actionbar.setHomeButtonEnabled(true);
			actionbar.setDisplayHomeAsUpEnabled(true);
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;

			default:
				break;
			}
			return super.onOptionsItemSelected(item);
		}
	 //加载网络数据
	 public ResultState onLoad(){
		 HomeDetailProtocol protocol = new HomeDetailProtocol(packageName);
		 appData = protocol.getData(0);
		 if (appData != null) {
			return ResultState.STATE_SUCCESS;
		}else {
			return ResultState.STATE_ERROR;
		}
	 }
	 
	 //网络数据加载成功后,加载布局
	 public View createSuccessView(){
		 View viewdetail = UIUtils.inflateView(R.layout.pager_home_detali);
		 FrameLayout flDetailInfo = (FrameLayout) viewdetail.findViewById(R.id.fl_appinfo_detail);//应用简单描述帧布局
		 //应用简答描述模块
		 AppDetailHolder holder = new AppDetailHolder();
		 holder.setData(appData);
		 View appView = holder.getRootView();
		 //动态给帧布局填充页面
		 flDetailInfo.addView(appView);
		 
		 //安全描述模块
		 FrameLayout flSafeDes = (FrameLayout) viewdetail.findViewById(R.id.fl_appinfo_safe);
		 DetialSafeHoolder holder1 = new DetialSafeHoolder();
		 holder1.setData(appData);
		 View appSafe = holder1.getRootView();
		 flSafeDes.addView(appSafe);
		 
		 //截图展示模块
		 HorizontalScrollView hsvPicDes = (HorizontalScrollView) viewdetail.findViewById(R.id.hsv_appinfo_pic);
		 DetialPicsHolder holder2 = new DetialPicsHolder();
		 holder2.setData(appData);
		 View appPics = holder2.getRootView();
		 hsvPicDes.addView(appPics);
		 
		 //详细描述模块
		 FrameLayout flDes = (FrameLayout) viewdetail.findViewById(R.id.fl_appinfo_des);
		 DetialDesHolder holder3 = new DetialDesHolder();
		 holder3.setData(appData);
		 View appDes = holder3.getRootView();
		 flDes.addView(appDes);
			 
		 //应用下载模块
		 FrameLayout fl_detail_download = (FrameLayout) viewdetail.findViewById(R.id.fl_detail_download);
		 DownloadApkHolder holder4 = new DownloadApkHolder();
		 holder4.setData(appData);
		 View appDownload = holder4.getRootView();
		 fl_detail_download.addView(appDownload);
		 
		 return viewdetail;
	 }
}
