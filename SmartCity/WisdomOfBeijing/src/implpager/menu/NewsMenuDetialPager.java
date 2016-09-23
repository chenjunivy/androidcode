package implpager.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import base.BaseMenuDetialPager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.szcx.wisdomofbeijing.MainActivity;
import com.szcx.wisdomofbeijing.R;
import com.viewpagerindicator.TabPageIndicator;

import domain.NewsMenu.NewsTabData;
/**
 * 菜单详情页，新闻
 * @author SystemIvy
 *
 */
public class NewsMenuDetialPager extends BaseMenuDetialPager implements OnPageChangeListener {
	private ViewPager detailMenuPager;
	private ArrayList<NewsTabData> mTabDate;//标签网络数据
	private ArrayList<TabDetailPager> mTabPagers;//标签页面集合
	private TabPageIndicator mIndicator;
	private ImageButton imgbtn;
	
	public NewsMenuDetialPager(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		mTabDate = children;
	}

	@Override
	public View initView() {
		//添加ViewPager侧滑组件
		View view = View.inflate(m2Activity, R.layout.pager_news_menu_detail, null);
		detailMenuPager = (ViewPager) view.findViewById(R.id.vp_news_menu_detail);
		mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		imgbtn = (ImageButton) view.findViewById(R.id.imgbtn_next);
	
		return view;
	}
	
	@Override
	public void initData() {
		mTabPagers = new ArrayList<TabDetailPager>();
		int j =  mTabDate.size();
		for (int i = 0; i < j; i++) {
			TabDetailPager mTabDetail = new TabDetailPager(m2Activity,mTabDate.get(i));
			mTabPagers.add(mTabDetail);
		}
		
		detailMenuPager.setAdapter(new NewsDetialPager());
		//将Indicator与viewpager绑定在一起，必须在viewpager设置adapter之后
		//需要重写dispatchTouchEvent方法，通知所有父控件以及祖控件不要拦截触屏响应的消息事件
		mIndicator.setViewPager(detailMenuPager);
		//当ViewPager改变是触发监听器，由于之前与Indicator绑定，此处不适宜在ViewPager上注册监听器，否则会出现Indicator信息不同步
//		detailMenuPager.setOnPageChangeListener(this);
		//应该在Indicator上注册监听器，即可实现信息同步
		mIndicator.setOnPageChangeListener(this);
		//设置Indicator右边的箭头按钮
		imgbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
					int currentItem = detailMenuPager.getCurrentItem();
					currentItem++;
					detailMenuPager.setCurrentItem(currentItem);
			}
		});
		
	}
	
	class NewsDetialPager extends PagerAdapter{
		//获取指示器的标题
		@Override
		public CharSequence getPageTitle(int position) {
			NewsTabData newstab =  mTabDate.get(position);
			return newstab.title;
		}
		
		@Override
		public int getCount() {
			return mTabPagers.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager pager = mTabPagers.get(position);
			View view = pager.mRootView;
			container.addView(view);
			pager.initData();
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}

	/**
	 * 以下三个方法是ViewPager的消息处理事件
	 */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // This space for rent
    }

    @Override
    public void onPageSelected(int position) {
    	if (position == 0) {
			setSlidingMenuVisible(true);
		} else {
			setSlidingMenuVisible(false);
		}
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // This space for rent
    }
    
    ////开启或禁用侧边栏
    private void setSlidingMenuVisible(boolean b) {
		MainActivity mActivity = (MainActivity) m2Activity;
		SlidingMenu slidMenu = mActivity.getSlidingMenu();
		if (b) {
			slidMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
    
    
    

}
