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
 * 新闻详情页标签栏，与BaseMenuDetialPager无必然联系，为方便起见，选择继承
 * @author SystemIvy
 *
 */
public class TabDetailPager extends BaseMenuDetialPager {
	private NewsTabData newsTab;//单个页签网络数据
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
//		//view.setText(newsTab.title);//由于调用父类方法，初次时调用,newsTab数据没有传递，会出现空指针异常
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
		View view = View.inflate(m2Activity, R.layout.pager_tab_menu_detali, null);
		lv_news = (PullToRefreshListView) view.findViewById(R.id.lv_news_item);
		//listview的头部布局，为一个填充ViewPager的头条新闻的布局
		View mHeaderView = View.inflate(m2Activity, R.layout.list_item_header, null);
		
		vp_tab = (TopNewsViewPager) mHeaderView.findViewById(R.id.vp_tab_menudetail);
		tv_title = (TextView) mHeaderView.findViewById(R.id.tv_tab_title);
		indicator_tb = (CirclePageIndicator) mHeaderView.findViewById(R.id.indicator);
		
		//lv_tab = (ListView) view.inflate(m2Activity, R.id.lv_tab_menudetail, null);
		
		lv_news.addHeaderView(mHeaderView);
		lv_news.setOnPullDownRefreshListener(new OnRefreshData(){

			@Override
			public void onRefresh() {
				//重新向服务器请求数据
				getTabDataFromServer();
			}

			@Override
			public void onLoadMore() {
				//加载更多列表数据
				if (moreUrl != null) {
					getMoreDataFromServer();
				}else {
					//如果没有跟多数据，同样收起上滑加载状态栏
					lv_news.onRefreshComplete(true);
				}
			}
			
		});
		
		lv_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int headcount = lv_news.getHeaderViewsCount(); //获取listview头部view数量
				int startPosition = position -headcount; //需要减去头布局的占位
				System.out.println("现在的位置是："+startPosition);
				
				NewsData news = newsItem.get(startPosition);
				String mUrl = news.url;
				
				String readId = PrefUtils.getString(m2Activity, "read_id", "");
				if (!readId.contains(news.id + "")) {//如果先前保存的字符串不包含点击过的新闻id,才去追加新闻ID
					readId = readId + news.id + ",";
					PrefUtils.setString(m2Activity, "read_id",readId);
				}
				
				//标记已读条目标题，为灰色
				TextView tv_color = (TextView) view.findViewById(R.id.tv_title);
				tv_color.setTextColor(Color.GRAY);
				
				//newsAdapter.notifyDataSetChanged();
				//刷新全局，但是浪费性能，只是标记已读新闻为灰色，没有必要刷新全局
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
		//判断是否存在缓存
		String cacheContent = CacheUtils.getCacheContent(TABDET_URL, m2Activity);
		if (!TextUtils.isEmpty(cacheContent)) {
			processTabData(cacheContent,true);
		} else {
			getTabDataFromServer();
		}
		
		
	}
	//从服务器获取数据
	/**
	 * 从服务器获取数据
	 * 需要添加权限INTERNET, WRITE_EXTERNAL_STORAGE
	 */
	private void getTabDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, TABDET_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//请求成功
				String respose = responseInfo.result;
				processTabData(respose, true);
				System.out.println(respose);
				//设置缓存
				CacheUtils.setCacheContent(TABDET_URL, respose, m2Activity);
				//收起下拉刷新控件
				lv_news.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//请求失败
				error.printStackTrace();
				Toast.makeText(m2Activity, msg, Toast.LENGTH_LONG).show();
				//收起下拉刷新控件
				lv_news.onRefreshComplete(false);
			}

		});
	}
	
	/**
	 * 上滑时从服务器加载更多列表数据
	 */
	protected void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, moreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//请求成功
				String respose = responseInfo.result;
				processTabData(respose,false);
				System.out.println(respose);
				//收起上滑刷新控件
				lv_news.onRefreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//请求失败
				error.printStackTrace();
				Toast.makeText(m2Activity, msg, Toast.LENGTH_LONG).show();
				//收起上滑刷新控件
				lv_news.onRefreshComplete(false);
			}

		});
	}

	//解析标签页json数据
	protected void processTabData(String respose, boolean upadatemore) {
		Gson tabjson = new Gson();
		NewsTabBean mTabBean = tabjson.fromJson(respose, NewsTabBean.class);
		System.out.println("解析菜单详情页数据" + mTabBean);
		//判断是否有下一页的数据
		moreUrl = mTabBean.data.more;
		if (TextUtils.isEmpty(moreUrl)) {
			moreUrl = null;
		}else {
			moreUrl = GlobalContextSource.SERVER_HOST + moreUrl;
		}
		
		if (upadatemore) {//是下拉刷新数据还是上滑加载数据，true为下拉，false为上滑
			//头条新闻填充数据
			newsTop = mTabBean.data.topnews;
				if (newsTop != null) {
			TabDetailViewPagerAdapter tabDetailViewPagerAdapter = new TabDetailViewPagerAdapter();
			vp_tab.setAdapter(tabDetailViewPagerAdapter);
			//将ViewPager与 CirclePageIndicator绑定
			indicator_tb.setViewPager(vp_tab);
			indicator_tb.setSnap(true);//快照方式
			//事件监听设置给Indicator
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
			//更新第一个头条新闻
			tv_title.setText(newsTop.get(0).title);
			//重新回到新闻标签页面时，Indicator重新复位
			indicator_tb.onPageSelected(0);
			
		}
		//列表新闻
		newsItem = mTabBean.data.news;
		if (newsItem != null) {
			newsAdapter = new NewsAdapter();
			lv_news.setAdapter(newsAdapter);
		}
		//自动循环切换头条ViewPager
		autoTakeTurns();
		
			}else {//在由原来listview后面追加列列表
				 ArrayList<NewsData> newsMore = mTabBean.data.news;
				 newsItem.addAll(newsMore);
				 newsAdapter.notifyDataSetChanged();
			}
	}
	
	//自动求换头条
	private void autoTakeTurns() {
		//设置viewpager自动间歇滑动
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
										
					mHander.sendEmptyMessageDelayed(0, 3000);//循环发消息实现内部循环间歇播放
				}
				
			};
			//保证启动自动循环播放逻辑只执行一次，发送消息使处理消息进入内循环，viewpager循环滚动
			mHander.sendEmptyMessageDelayed(0, 3000);
			
			vp_tab.setOnTouchListener(new OnTouchListener(
					) {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						//停止头条新闻循环播放,删除handler的所有消息
						mHander.removeCallbacksAndMessages(null);
						
						break;
					case MotionEvent.ACTION_CANCEL://取消事件，当安下viewpager后，直接滑动listview,导致抬起事件无响应
						System.out.println("ACTION_CANCEL");
						//启动新闻循环播放
						mHander.sendEmptyMessageDelayed(0, 3000);
						
						break;
					case MotionEvent.ACTION_MOVE:
						
						break;
					case MotionEvent.ACTION_UP:
						//启动新闻循环播放
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

	//头条新闻数据适配器
	class TabDetailViewPagerAdapter extends PagerAdapter{
		private BitmapUtils mBitmapUtils;

		public TabDetailViewPagerAdapter() {
			mBitmapUtils = new BitmapUtils(m2Activity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);//设置初始加载的图片
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
			iv_tab.setScaleType(ScaleType.FIT_XY);//图片缩放填充ViewPager组件
			//下载图片，将图片设置给imageview,避免oom,缓存处理
			//使用开源框架BitmapUtils (XUtils)
			//首先获得图片的URL
			String imageUrl = newsTop.get(position).topimage;
			//由于本机测试使用genymotion,不识别内部IP 10.0.2.2,所以替换为本机IP 192.168.56.1
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
	
	//新闻列表适配器
	class NewsAdapter extends BaseAdapter{
		private BitmapUtils mBitmapUtilsBase;
		public NewsAdapter() {
			mBitmapUtilsBase = new BitmapUtils(m2Activity);
			mBitmapUtilsBase.configDefaultLoadingImage(R.drawable.topnews_item_default);//初始化加载列表的时候默认图片
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
				//标记已读条目标题，为灰色
				holder.tv_title.setTextColor(Color.GRAY);
			}else {
				//标记未读条目标题
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
