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
 * �Զ�������ˢ��ListView
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
	 * ��ʼ��ͷ����
	 */
	private void initHeadView(){
		//ˢ��״̬��
		mHeadView = View.inflate(getContext(), R.layout.pull_to_refresh_head, null);
		this.addHeaderView(mHeadView);
		
		//��ʼ���ؼ�
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
	 * ��ʼ��β����
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
				//��ֹ�����б�listview�������¼�������ͷ��viewpager���գ�����listview��startYû�и�ֵ���˴���һ�����´���
				startY = (int) ev.getY();
			}
			int endY = (int) ev.getY();
			int dy = endY -	startY;
			//��ǰ��һ��item��λ��
			int firstItemPosition = getFirstVisiblePosition();
			if (dy > 0 && firstItemPosition == 0) {
				int padding = dy - height;
				mHeadView.setPadding(0, padding, 0, 0);
				
				//ˢ��״̬���Ѿ�ȫ�����ֲ��һ��ڼ���������padding == 0��ʱ����ȫ���֣�
				if (padding > 0 && mRefreeshState != START_RELEASE_TO_REFRESH) {
					//��Ϊ�ɿ�ˢ��״̬
					mRefreeshState = START_RELEASE_TO_REFRESH;
					refreshState();	
					
				}
				//ˢ��״̬�����ڼ�����ʾ���һ��ڼ�������
				else if (padding < 0 && mRefreeshState != START_PULL_TO_REFRESH) {
					//��Ϊ����ˢ��״̬
					mRefreeshState = START_PULL_TO_REFRESH;
					refreshState();		
				}
				
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1;
			if (mRefreeshState == START_PULL_TO_REFRESH) {
				//����ˢ��״̬����ȫ����
				mHeadView.setPadding(0, -height, 0, 0);
			} else if(mRefreeshState == START_RELEASE_TO_REFRESH){
				mRefreeshState = START_REFRESHING;
				refreshState();
				//����ˢ��״̬����ȫ��ʾ
				mHeadView.setPadding(0, 0, 0, 0);
				//�ص�ˢ�·������˷���ʵ���������ݵĲ���
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
	
	//��ʼ������Ч��
	private void  initAnimation(){
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);
		
		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(200);
		animUp.setFillAfter(true);
		
	}

	/**
	 * ���ݵ�ǰ״̬ˢ��״̬��
	 */
	private void refreshState() {
		
		switch (mRefreeshState) {
		case START_PULL_TO_REFRESH:
			tv_nofify.setText("����ˢ��");
			pr_circle.setVisibility(View.INVISIBLE);
			iv_arrows.setVisibility(View.VISIBLE);
			iv_arrows.startAnimation(animUp);
			
			break;
		case START_RELEASE_TO_REFRESH:
			tv_nofify.setText("�ɿ�ˢ��");
			iv_arrows.setVisibility(View.INVISIBLE);
			pr_circle.setVisibility(View.VISIBLE);
			iv_arrows.startAnimation(animDown);
			
			break;
		case START_REFRESHING:
			tv_nofify.setText("����ˢ�¡�����");
			pr_circle.setVisibility(View.VISIBLE);
			iv_arrows.clearAnimation();//�����ͷ�����������޷����ؼ�ͷ
			iv_arrows.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * ˢ����ɺ����ʱ��
	 */
	public void updateNewTime(){
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newTime = timeFormat.format(new Date());
		tv_time.setText(newTime);
	}
	/**
	 * ˢ����ɺ���ָˢ��״̬����
	 * @param state
	 */
	public void onRefreshComplete(boolean state){
		if (RefreshState) {
			mFootView.setPadding(0, -footHeight, 0, 0);//�����ϻ�������
			RefreshState = false;
		}else{
			mHeadView.setPadding(0, -height, 0, 0);
			mRefreeshState = START_PULL_TO_REFRESH;
			tv_nofify.setText("����ˢ��");
			pr_circle.setVisibility(View.INVISIBLE);
			iv_arrows.setVisibility(View.VISIBLE);
			if (true) {
				updateNewTime();
			}
	
		}
	}
	
	/**
	 * �ص����﷨�Ͻ��ǣ�ͨ��һ�� ���ڲ��ӿڡ�����ʵ���˸��ڲ��ӿڵ�����������ķ���
	 */
	
	/**
	 * 3.����һ���ص��ӿ����ã����ڽ��ռ�������
	 */
	public OnRefreshData onrefreshlocal;
	private View mFootView;
	private int footHeight;
	/**
	 * 2.����һ�������ӿڣ���¶�ӿ�
	 * @param onrefresh
	 */
	public void setOnPullDownRefreshListener(OnRefreshData onrefresh){
		onrefreshlocal = onrefresh;
	}
	
	/**
	 *1. ����һ���ص��ӿ�
	 *
	 */
	public interface OnRefreshData {
		public void onRefresh();
		public void onLoadMore();
		
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == SCROLL_STATE_IDLE){//listview���ڿ���״̬
			int lastVisibleItemPosition = getLastVisiblePosition();
			if(lastVisibleItemPosition == getCount() -1){//������listview�ĵײ���
				RefreshState = true; //�»����ײ���˵���»�ˢ��״̬Ϊ��
				mFootView.setPadding(0, 0, 0, 0);//��ʾβ��ˢ��״̬��
				setSelection(getCount() - 1);//listview ��ʾ�����һ��item�ϣ��Ӷ����ظ������ݣ������ֶ�����
				//���ظ����б����ݣ�֪ͨ�����������һҳ����
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
