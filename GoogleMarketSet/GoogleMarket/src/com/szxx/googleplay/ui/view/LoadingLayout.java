package com.szxx.googleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.szxx.googelmarket.R;
import com.szxx.googleplay.manager.ThreadManager;
import com.szxx.googleplay.uiutils.UIUtils;

/**
 * 使用自定义帧布局存放加载的五种状态
 * 
 * 未加载状态
 * 正在加载状态
 * 加载失败状态
 * 加载为空状态
 * 加载成功状态
 *
 */
public abstract class LoadingLayout extends FrameLayout {

	/**加载状态标示**/
	private static final int STATE_LOAD_UNDO = 1;
	private static final int STATE_LOAD_LOADING = 2;
	private static final int STATE_LOAD_FAILED = 3;
	private static final int STATE_LOAD_EMPTY = 4;
	private static final int STATE_LOAD_SUCCESS = 5;
	
	//当前状态
	private int mCurrentState = STATE_LOAD_UNDO;
	private View mLoadingPage;
	private View mLoadingError;
	private View mLoadingEmpty;
	private View mLoadingSuccess;
	
	public LoadingLayout(Context context) {
		super(context);
		initView();
	}

	public LoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {
		if (mLoadingPage == null) {
			//初始化加载中的布局
			mLoadingPage = UIUtils.inflateView(R.layout.pager_loading);
			addView(mLoadingPage);
		}
		
		if (mLoadingError == null) {
			//加载出错时的布局
			mLoadingError = UIUtils.inflateView(R.layout.pager_error);
			//加载失败后点击重新加载
			Button btn_retry = (Button) mLoadingError.findViewById(R.id.btn_retry);
			btn_retry.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onLoadData();
				}
			});
			
			addView(mLoadingError);
		}
		
		if (mLoadingEmpty == null) {
			mLoadingEmpty = UIUtils.inflateView(R.layout.pager_empty);
			addView(mLoadingEmpty);
		}
		
		showRightPage();
	}

	//根据状态显示相应的布局界面
	private void showRightPage() {
		mLoadingPage.setVisibility((mCurrentState == STATE_LOAD_UNDO || mCurrentState == STATE_LOAD_LOADING)?View.VISIBLE:View.GONE);
		mLoadingError.setVisibility((mCurrentState == STATE_LOAD_FAILED)?View.VISIBLE:View.GONE);
		mLoadingEmpty.setVisibility((mCurrentState == STATE_LOAD_EMPTY)?View.VISIBLE:View.GONE);
		
		if (mCurrentState == STATE_LOAD_SUCCESS && mLoadingSuccess == null) {
			mLoadingSuccess = createSuccessView();
			if (mLoadingSuccess != null) {
				addView(mLoadingSuccess);
			}
		}
		
		if (mLoadingSuccess != null) {
			mLoadingSuccess.setVisibility((mCurrentState == STATE_LOAD_SUCCESS)?View.VISIBLE:View.GONE);
		}
		
	}
	
	//加载数据，异步加载
	public void onLoadData(){
	
		if (mCurrentState != STATE_LOAD_LOADING) {//如果没有开始加载，就开始加载数据
			mCurrentState = STATE_LOAD_LOADING;
			//单独创建线程处理界面
/*			new Thread(){
				@Override
				public void run() {
					final ResultState  resultState = onLoadResult();
					//运行在主线程,以更新UI
					UIUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							if (resultState != null) {
								mCurrentState = resultState.getRsultState(); //网络加载结束后，更新网络状态
								//根据最新的状态更新主界面 
								showRightPage();
							}
						}
					});

				}
			}.start();*/	
			
			//使用线程池管理线程
			ThreadManager.getThreadPool().executeThread(new Runnable() {
				
				@Override
				public void run() {
					final ResultState  resultState = onLoadResult();
					//运行在主线程,以更新UI
					UIUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							if (resultState != null) {
								mCurrentState = resultState.getRsultState(); //网络加载结束后，更新网络状态
								//根据最新的状态更新主界面 
								showRightPage();
							}
						}
					});
				}
			});
		}

	}
	
	//创建了正确的View,可以正确加载布局
	public abstract View createSuccessView();
	
	//加载网络数据，返回值表示请求网络后的状态
	public abstract ResultState onLoadResult();
	
	//结果状态枚举类
	public enum ResultState {
		STATE_SUCCESS(STATE_LOAD_SUCCESS),STATE_EMPTY(STATE_LOAD_EMPTY),STATE_ERROR(STATE_LOAD_FAILED);
		
		private int state;
		
		private ResultState(int state){
			this.state = state;
		}
		
		private int getRsultState(){
			return state;
		} 
	}
}
