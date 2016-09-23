package implpager.menu;

import global.GlobalContextSource;

import java.util.ArrayList;

import utils.CacheUtils;
import utils.PrefUtils;
import view.PullToRefreshListView;
import view.PullToRefreshListView.OnRefreshData;
import view.TopNewsViewPager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import base.BaseMenuDetialPager;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.szcx.wisdomofbeijing.NewDetailActivity;
import com.szcx.wisdomofbeijing.R;
import com.viewpagerindicator.CirclePageIndicator;

import domain.NewsMenu.NewsTabData;
import domain.NewsTabBean;
import domain.NewsTabBean.NewsData;
import domain.NewsTabBean.TopNews;
/**
 * ��������ҳ��ǩ������BaseMenuDetialPager�ޱ�Ȼ��ϵ��Ϊ���������ѡ��̳�
 * @author SystemIvy
 *
 */
public class TabDetailPager extends BaseMenuDetialPager {
	private NewsTabData newsTab;//����ҳǩ��������
//	private TextView view;
	private view.TopNewsViewPager vp_tab;
	private ListView lv_tab;
	private String TABDET_URL;
	private ArrayList<TopNews> newsTop;
	private TextView tv_title;
	private CirclePageIndicator indicator_tb;
	private PullToRefreshListView lv_news;
	private NewsAdapter newsAdapter;
	private ArrayList<NewsData> newsItem;
	private String moreUrl;
	private Handler mHander;
	
	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		newsTab = newsTabData;
		TABDET_URL = GlobalContextSource.SERVER_HOST + newsTabData.url;
	}

	@Override
	public View initView() {
//		view = new TextView(m2Activity);
//		//view.setText(newsTab.title);//���ڵ��ø��෽��������ʱ����,newsTab����û�д��ݣ�����ֿ�ָ���쳣
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
		View view = View.inflate(m2Activity, R.layout.pager_tab_menu_detali, null);
		lv_news = (PullToRefreshListView) view.findViewById(R.id.lv_news_item);
		//listview��ͷ�����֣�Ϊһ�����ViewPager��ͷ�����ŵĲ���
		View mHeaderView = View.inflate(m2Activity, R.layout.list_item_header, null);
		
		vp_tab = (TopNewsViewPager) mHeaderView.findViewById(R.id.vp_tab_menudetail);
		tv_title = (TextView) mHeaderView.findViewById(R.id.tv_tab_title);
		indicator_tb = (CirclePageIndicator) mHeaderView.findViewById(R.id.indicator);
		
		//lv_tab = (ListView) view.inflate(m2Activity, R.id.lv_tab_menudetail, null);
		
		lv_news.addHeaderView(mHeaderView);
		lv_news.setOnPullDownRefreshListener(new OnRefreshData(){

			@Override
			public void onRefresh() {
				//�������������������
				getTabDataFromServer();
			}

			@Override
			public void onLoadMore() {
				//���ظ����б�����
				if (moreUrl != null) {
					getMoreDataFromServer();
				}else {
					//���û�и������ݣ�ͬ�������ϻ�����״̬��
					lv_news.onRefreshComplete(true);
				}
			}
			
		});
		
		lv_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int headcount = lv_news.getHeaderViewsCount(); //��ȡlistviewͷ��view����
				int startPosition = position -headcount; //��Ҫ��ȥͷ���ֵ�ռλ
				System.out.println("���ڵ�λ���ǣ�"+startPosition);
				
				NewsData news = newsItem.get(startPosition);
				String mUrl = news.url;
				
				String readId = PrefUtils.getString(m2Activity, "read_id", "");
				if (!readId.contains(news.id + "")) {//�����ǰ������ַ��������������������id,��ȥ׷������ID
					readId = readId + news.id + ",";
					PrefUtils.setString(m2Activity, "read_id",readId);
				}
				
				//����Ѷ���Ŀ���⣬Ϊ��ɫ
				TextView tv_color = (TextView) view.findViewById(R.id.tv_title);
				tv_color.setTextColor(Color.GRAY);
				
				//newsAdapter.notifyDataSetChanged();
				//ˢ��ȫ�֣������˷����ܣ�ֻ�Ǳ���Ѷ�����Ϊ��ɫ��û�б�Ҫˢ��ȫ��
				Intent intent = new Intent(m2Activity, NewDetailActivity.class);
				intent.putExtra("murl", mUrl);
				m2Activity.startActivity(intent);
				
			}
		});
		
		return view;
	}
	


	@Override
	public void initData() {
//		view.setText(newsTab.title);
		//�ж��Ƿ���ڻ���
		String cacheContent = CacheUtils.getCacheContent(TABDET_URL, m2Activity);
		if (!TextUtils.isEmpty(cacheContent)) {
			processTabData(cacheContent,true);
		} else {
			getTabDataFromServer();
		}
		
		
	}
	//�ӷ�������ȡ����
	/**
	 * �ӷ�������ȡ����
	 * ��Ҫ���Ȩ��INTERNET, WRITE_EXTERNAL_STORAGE
	 */
	private void getTabDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, TABDET_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//����ɹ�
				String respose = responseInfo.result;
				processTabData(respose, true);
				System.out.println(respose);
				//���û���
				CacheUtils.setCacheContent(TABDET_URL, respose, m2Activity);
				//��������ˢ�¿ؼ�
				lv_news.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//����ʧ��
				error.printStackTrace();
				Toast.makeText(m2Activity, msg, Toast.LENGTH_LONG).show();
				//��������ˢ�¿ؼ�
				lv_news.onRefreshComplete(false);
			}

		});
	}
	
	/**
	 * �ϻ�ʱ�ӷ��������ظ����б�����
	 */
	protected void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//����ɹ�
				String respose = responseInfo.result;
				processTabData(respose,false);
				System.out.println(respose);
				//�����ϻ�ˢ�¿ؼ�
				lv_news.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//����ʧ��
				error.printStackTrace();
				Toast.makeText(m2Activity, msg, Toast.LENGTH_LONG).show();
				//�����ϻ�ˢ�¿ؼ�
				lv_news.onRefreshComplete(false);
			}

		});
	}

	//������ǩҳjson����
	protected void processTabData(String respose, boolean upadatemore) {
		Gson tabjson = new Gson();
		NewsTabBean mTabBean = tabjson.fromJson(respose, NewsTabBean.class);
		System.out.println("�����˵�����ҳ����" + mTabBean);
		//�ж��Ƿ�����һҳ������
		moreUrl = mTabBean.data.more;
		if (TextUtils.isEmpty(moreUrl)) {
			moreUrl = null;
		}else {
			moreUrl = GlobalContextSource.SERVER_HOST + moreUrl;
		}
		
		if (upadatemore) {//������ˢ�����ݻ����ϻ��������ݣ�trueΪ������falseΪ�ϻ�
			//ͷ�������������
			newsTop = mTabBean.data.topnews;
				if (newsTop != null) {
			TabDetailViewPagerAdapter tabDetailViewPagerAdapter = new TabDetailViewPagerAdapter();
			vp_tab.setAdapter(tabDetailViewPagerAdapter);
			//��ViewPager�� CirclePageIndicator��
			indicator_tb.setViewPager(vp_tab);
			indicator_tb.setSnap(true);//���շ�ʽ
			//�¼��������ø�Indicator
			indicator_tb.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
		        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
					
		        }

		        @Override
		        public void onPageSelected(int position) {
		        	TopNews mTopNews = newsTop.get(position);
		        	tv_title.setText(mTopNews.title);
		        }

		        @Override
		        public void onPageScrollStateChanged(int state) {
		        	
		        }
			});
			//���µ�һ��ͷ������
			tv_title.setText(newsTop.get(0).title);
			//���»ص����ű�ǩҳ��ʱ��Indicator���¸�λ
			indicator_tb.onPageSelected(0);
			
		}
		//�б�����
		newsItem = mTabBean.data.news;
		if (newsItem != null) {
			newsAdapter = new NewsAdapter();
			lv_news.setAdapter(newsAdapter);
		}
		//�Զ�ѭ���л�ͷ��ViewPager
		autoTakeTurns();
		
			}else {//����ԭ��listview����׷�����б�
				 ArrayList<NewsData> newsMore = mTabBean.data.news;
				 newsItem.addAll(newsMore);
				 newsAdapter.notifyDataSetChanged();
			}
	}
	
	//�Զ���ͷ��
	private void autoTakeTurns() {
		//����viewpager�Զ���Ъ����
		if (mHander == null) {
			mHander = new Handler(){

				@Override
				public void handleMessage(android.os.Message msg) {
					int currentItem = vp_tab.getCurrentItem();
					currentItem ++;
					if (currentItem > newsTop.size() -1) {
						currentItem = 0;
					}
					vp_tab.setCurrentItem(currentItem);
										
					mHander.sendEmptyMessageDelayed(0, 3000);//ѭ������Ϣʵ���ڲ�ѭ����Ъ����
				}
				
			};
			//��֤�����Զ�ѭ�������߼�ִֻ��һ�Σ�������Ϣʹ������Ϣ������ѭ����viewpagerѭ������
			mHander.sendEmptyMessageDelayed(0, 3000);
			
			vp_tab.setOnTouchListener(new OnTouchListener(
					) {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						//ֹͣͷ������ѭ������,ɾ��handler��������Ϣ
						mHander.removeCallbacksAndMessages(null);
						
						break;
					case MotionEvent.ACTION_CANCEL://ȡ���¼���������viewpager��ֱ�ӻ���listview,����̧���¼�����Ӧ
						System.out.println("ACTION_CANCEL");
						//��������ѭ������
						mHander.sendEmptyMessageDelayed(0, 3000);
						
						break;
					case MotionEvent.ACTION_MOVE:
						
						break;
					case MotionEvent.ACTION_UP:
						//��������ѭ������
						mHander.sendEmptyMessageDelayed(0, 3000);
						break;
						
					default:
						break;
					}
					return false;
				}
			});
		}
	}

	//ͷ����������������
	class TabDetailViewPagerAdapter extends PagerAdapter{
		private BitmapUtils mBitmapUtils;

		public TabDetailViewPagerAdapter() {
			mBitmapUtils = new BitmapUtils(m2Activity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);//���ó�ʼ���ص�ͼƬ
		}

		@Override
		public int getCount() {
			return newsTop.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView iv_tab = new ImageView(m2Activity);
//			iv_tab.setImageResource(R.drawable.topnews_item_default);
			iv_tab.setScaleType(ScaleType.FIT_XY);//ͼƬ�������ViewPager���
			//����ͼƬ����ͼƬ���ø�imageview,����oom,���洦��
			//ʹ�ÿ�Դ���BitmapUtils (XUtils)
			//���Ȼ��ͼƬ��URL
			String imageUrl = newsTop.get(position).topimage;
			//���ڱ�������ʹ��genymotion,��ʶ���ڲ�IP 10.0.2.2,�����滻Ϊ����IP 192.168.56.1
			String imageUrlCovert = imageUrl.replace("10.0.2.2", "192.168.56.1");
			
			mBitmapUtils.display(iv_tab, imageUrlCovert);
			
			container.addView(iv_tab);
			return iv_tab;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}
	
	//�����б�������
	class NewsAdapter extends BaseAdapter{
		private BitmapUtils mBitmapUtilsBase;
		public NewsAdapter() {
			mBitmapUtilsBase = new BitmapUtils(m2Activity);
			mBitmapUtilsBase.configDefaultLoadingImage(R.drawable.topnews_item_default);//��ʼ�������б��ʱ��Ĭ��ͼƬ
		}

		@Override
		public int getCount() {
			return newsItem.size();
		}

		@Override
		public Object getItem(int position) {
			return newsItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(m2Activity, R.layout.list_tab_news_item, null);
				holder = new ViewHolder();
				holder.imag_news = (ImageView) convertView.findViewById(R.id.img_photo);
				holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			NewsData news = (NewsData) getItem(position);
			holder.tv_title.setText(news.title);
			holder.tv_date.setText(news.pubdate);
			
			String readId = PrefUtils.getString(m2Activity, "read_id", "");
			
			if (readId.contains(news.id + "")) {
				//����Ѷ���Ŀ���⣬Ϊ��ɫ
				holder.tv_title.setTextColor(Color.GRAY);
			}else {
				//���δ����Ŀ����
				holder.tv_title.setTextColor(Color.BLACK);
			}
	
			String newsImg = news.listimage;
			String newsImg2 = newsImg.replace("10.0.2.2", "192.168.56.1");
			
			mBitmapUtilsBase.display(holder.imag_news, newsImg2);
			
			return convertView;
		}
		
	}

	static class ViewHolder{
		public ImageView imag_news;
		public TextView tv_title;
		public TextView tv_date;
	}
}
