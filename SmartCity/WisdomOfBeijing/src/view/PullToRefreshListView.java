package view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.szcx.wisdomofbeijing.R;

/**
 * 自定义下拉刷新ListView
 * @author SystemIvy
 *
 */
public class PullToRefreshListView extends ListView implements OnScrollListener{
	private View mHeadView;
	private int height;
	private int startY = -1;
	private static final int START_PULL_TO_REFRESH = 1;
	private static final int START_RELEASE_TO_REFRESH = 2;
	private static final int START_REFRESHING = 3;
	private int mRefreeshState = START_PULL_TO_REFRESH;
	private ImageView iv_arrows;
	private ProgressBar pr_circle;
	private TextView tv_nofify;
	private TextView tv_time;
	private RotateAnimation animUp;
	private RotateAnimation animDown;
	private boolean RefreshState;
	
	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initHeadView();
		initFootView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeadView();
		initFootView();
	}

	public PullToRefreshListView(Context context) {
		super(context);
		initHeadView();
		initFootView();
	}
	
	/**
	 * 初始化头布局
	 */
	private void initHeadView(){
		//刷新状态栏
		mHeadView = View.inflate(getContext(), R.layout.pull_to_refresh_head, null);
		this.addHeaderView(mHeadView);
		
		//初始化控件
		iv_arrows = (ImageView) mHeadView.findViewById(R.id.iv_pull);
		pr_circle = (ProgressBar) mHeadView.findViewById(R.id.probar_refresh);
		tv_nofify = (TextView) mHeadView.findViewById(R.id.tv_notify);
		tv_time = (TextView) mHeadView.findViewById(R.id.tv_newtime);
		
		mHeadView.measure(0, 0);
		height = mHeadView.getMeasuredHeight();
		mHeadView.setPadding(0, -height, 0, 0);
		
		initAnimation();
		updateNewTime();
	}
	
	/**
	 * 初始化尾布局
	 */
	private void initFootView(){
		mFootView = View.inflate(getContext(), R.layout.pull_to_refresh_foot, null);
		this.addFooterView(mFootView);
		mFootView.measure(0, 0);
		footHeight = mFootView.getMeasuredHeight();
		mFootView.setPadding(0, -footHeight, 0, 0);
		this.setOnScrollListener(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {
				//防止新闻列表listview的下拉事件被新闻头条viewpager接收，导致listview的startY没有赋值，此处做一个重新处理
				startY = (int) ev.getY();
			}
			int endY = (int) ev.getY();
			int dy = endY -	startY;
			//当前第一个item的位置
			int firstItemPosition = getFirstVisiblePosition();
			if (dy > 0 && firstItemPosition == 0) {
				int padding = dy - height;
				mHeadView.setPadding(0, padding, 0, 0);
				
				//刷新状态栏已经全部呈现并且还在继续下拉（padding == 0的时候完全显现）
				if (padding > 0 && mRefreeshState != START_RELEASE_TO_REFRESH) {
					//改为松开刷新状态
					mRefreeshState = START_RELEASE_TO_REFRESH;
					refreshState();	
					
				}
				//刷新状态栏还在继续显示并且还在继续下拉
				else if (padding < 0 && mRefreeshState != START_PULL_TO_REFRESH) {
					//改为下拉刷新状态
					mRefreeshState = START_PULL_TO_REFRESH;
					refreshState();		
				}
				
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;
			if (mRefreeshState == START_PULL_TO_REFRESH) {
				//设置刷新状态栏完全隐藏
				mHeadView.setPadding(0, -height, 0, 0);
			} else if(mRefreeshState == START_RELEASE_TO_REFRESH){
				mRefreeshState = START_REFRESHING;
				refreshState();
				//设置刷新状态栏完全显示
				mHeadView.setPadding(0, 0, 0, 0);
				//回调刷新方法，此方法实现请求数据的操作
				if (onrefreshlocal != null) {
					onrefreshlocal.onRefresh();
				}
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
		
	}
	
	//初始化动画效果
	private void  initAnimation(){
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);
		
		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);
		
	}

	/**
	 * 根据当前状态刷新状态栏
	 */
	private void refreshState() {
		
		switch (mRefreeshState) {
		case START_PULL_TO_REFRESH:
			tv_nofify.setText("下拉刷新");
			pr_circle.setVisibility(View.INVISIBLE);
			iv_arrows.setVisibility(View.VISIBLE);
			iv_arrows.startAnimation(animUp);
			
			break;
		case START_RELEASE_TO_REFRESH:
			tv_nofify.setText("松开刷新");
			iv_arrows.setVisibility(View.INVISIBLE);
			pr_circle.setVisibility(View.VISIBLE);
			iv_arrows.startAnimation(animDown);
			
			break;
		case START_REFRESHING:
			tv_nofify.setText("正在刷新。。。");
			pr_circle.setVisibility(View.VISIBLE);
			iv_arrows.clearAnimation();//清除箭头动画，否则无法隐藏箭头
			iv_arrows.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 刷新完成后更新时间
	 */
	public void updateNewTime(){
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newTime = timeFormat.format(new Date());
		tv_time.setText(newTime);
	}
	/**
	 * 刷新完成后是指刷新状态栏标
	 * @param state
	 */
	public void onRefreshComplete(boolean state){
		if (RefreshState) {
			mFootView.setPadding(0, -footHeight, 0, 0);//隐藏上滑加载栏
			RefreshState = false;
		}else{
			mHeadView.setPadding(0, -height, 0, 0);
			mRefreeshState = START_PULL_TO_REFRESH;
			tv_nofify.setText("下拉刷新");
			pr_circle.setVisibility(View.INVISIBLE);
			iv_arrows.setVisibility(View.VISIBLE);
			if (true) {
				updateNewTime();
			}
	
		}
	}
	
	/**
	 * 回调从语法上讲是：通过一个 “内部接口”调用实现了该内部接口的其他类或对象的方法
	 */
	
	/**
	 * 3.设置一个回调接口引用，用于接收监听对象
	 */
	public OnRefreshData onrefreshlocal;
	private View mFootView;
	private int footHeight;
	/**
	 * 2.设置一个监听接口，暴露接口
	 * @param onrefresh
	 */
	public void setOnPullDownRefreshListener(OnRefreshData onrefresh){
		onrefreshlocal = onrefresh;
	}
	
	/**
	 *1. 设置一个回调接口
	 *
	 */
	public interface OnRefreshData {
		public void onRefresh();
		public void onLoadMore();
		
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_IDLE){//listview处于空闲状态
			int lastVisibleItemPosition = getLastVisiblePosition();
			if(lastVisibleItemPosition == getCount() -1){//滑动到listview的底部了
				RefreshState = true; //下滑到底部，说明下滑刷新状态为真
				mFootView.setPadding(0, 0, 0, 0);//显示尾部刷新状态栏
				setSelection(getCount() - 1);//listview 显示在最后一个item上，从而加载更多数据，无需手动滑动
				//加载更多列表数据，通知主界面加载下一页数据
				if(onrefreshlocal != null){
					onrefreshlocal.onLoadMore();
					
				}
				
				
			}
			
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
	
	
	
	

}
