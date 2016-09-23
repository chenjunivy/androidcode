package com.szxx.googleplay.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;

import com.szxx.googelmarket.R;
import com.szxx.googleplay.domain.AppInfos;
import com.szxx.googleplay.domain.DownloadInfo;
import com.szxx.googleplay.manager.DownloadManager;
import com.szxx.googleplay.manager.DownloadManager.DownloadObserver;
import com.szxx.googleplay.ui.view.ProgressHorizontal;
import com.szxx.googleplay.uiutils.UIUtils;

public class DownloadApkHolder extends BaseHolder<AppInfos> implements
		DownloadObserver, OnClickListener {

	private View view;
	private Button btn_download;
	private FrameLayout fl_download;
	private DownloadManager downloadManager;
	private int mCurrentState;
	private float mProgress;
	private ProgressHorizontal progressH;

	@Override
	public View initView() {
		view = UIUtils.inflateView(R.layout.layout_detail_download);
		btn_download = (Button) view.findViewById(R.id.btn_download);
		btn_download.setOnClickListener(this);

		fl_download = (FrameLayout) view.findViewById(R.id.fl_download);
		fl_download.setOnClickListener(this);
		progressH = new ProgressHorizontal(UIUtils.getUIContext());

		progressH.setProgressBackgroundResource(R.drawable.progress_bg); // 进度背景图片
		progressH.setProgressResource(R.drawable.progress_normal); // 进度条图片
		progressH.setProgressTextColor(Color.WHITE); // 设置进度文字颜色
		progressH.setProgressTextSize(UIUtils.dipToPx(16)); // 进度文字大小

		FrameLayout.LayoutParams fl_param = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		// 帧布局中添加进度条
		fl_download.addView(progressH, fl_param);

		return view;
	}

	@Override
	public void refreshView(AppInfos data) {
		
		downloadManager = DownloadManager.getDownloadInstance();
		downloadManager.registerDownloadObserver(this); // 注册监听者，监听状态和进度变化

		DownloadInfo downloadInfo = downloadManager.getDownloadInfo(data);
		if (downloadInfo != null) {
			// 之前下载过
			mCurrentState = downloadInfo.currentState;
			mProgress = downloadInfo.getProgress();
		} else {
			mCurrentState = DownloadManager.STATE_UNDO;
			mProgress = 0;
		}

		refreshUI(mCurrentState, mProgress);
	}

	// 更新UI
	private void refreshUI(int currentstate, float progress) {
		mCurrentState = currentstate;
		mProgress = progress;
		switch (currentstate) {
		case DownloadManager.STATE_UNDO:
			btn_download.setVisibility(View.VISIBLE);
			fl_download.setVisibility(View.GONE);
			btn_download.setText("下载");
			break;
		case DownloadManager.STATE_WAITING:
			btn_download.setVisibility(View.VISIBLE);
			fl_download.setVisibility(View.GONE);
			btn_download.setText("等待下载");
			break;
		case DownloadManager.STATE_DOWNLOADING:
			btn_download.setVisibility(View.GONE);
			fl_download.setVisibility(View.VISIBLE);
			progressH.setProgress(mProgress);
			progressH.setCenterText("");
			break;
		case DownloadManager.STATE_PAUSE:
			btn_download.setVisibility(View.GONE);
			fl_download.setVisibility(View.VISIBLE);
			progressH.setCenterText("暂停下载");
			break;
		case DownloadManager.STATE_SUCCESS:
			btn_download.setVisibility(View.VISIBLE);
			fl_download.setVisibility(View.GONE);
			btn_download.setText("安装");
			break;
		case DownloadManager.STATE_FAIL:
			btn_download.setVisibility(View.VISIBLE);
			fl_download.setVisibility(View.GONE);
			btn_download.setText("下载失败");
			break;
		default:
			break;
		}
	}

	// 主线程中更新UI
//	private void refreshUIOnMainThread(final int currentState,
//			final float progress) {
	private void refreshUIOnMainThread(final DownloadInfo info) {
		UIUtils.runOnUIThread(new Runnable() {

			@Override
			public void run() {
				refreshUI(info.currentState, info.getProgress());
			}
		});
	}

	// 状态发生变化回调
	@Override
	public void onDownloadStateChanged(DownloadInfo downloadInfo) {
		// 判断下载对象是是否是当前应用
		AppInfos appInfo = getData();
		if (appInfo.id.equals(downloadInfo.id)) {
			refreshUIOnMainThread(downloadInfo);
		}

	}

	// 下载进度变化回调
	@Override
	public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
		// 判断下载对象是是否是当前应用
		AppInfos appInfo = getData();
		if (appInfo.id.equals(downloadInfo.id)) {
			refreshUIOnMainThread(downloadInfo);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_download:
		case R.id.fl_download:
			if (mCurrentState == DownloadManager.STATE_UNDO
					|| mCurrentState == DownloadManager.STATE_PAUSE
					|| mCurrentState == DownloadManager.STATE_FAIL) {
				downloadManager.onDownloadApk(getData());
			} else if (mCurrentState == DownloadManager.STATE_DOWNLOADING
					|| mCurrentState == DownloadManager.STATE_WAITING) {
				downloadManager.onPauseDownload(getData());
			} else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
				downloadManager.onInstallApk(getData());
			}
			break;

		default:
			break;
		}
	}

}
