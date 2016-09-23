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
 * �˵�����ҳ������
 * @author SystemIvy
 *
 */
public class NewsMenuDetialPager extends BaseMenuDetialPager implements OnPageChangeListener {
	private ViewPager detailMenuPager;
	private ArrayList<NewsTabData> mTabDate;//��ǩ��������
	private ArrayList<TabDetailPager> mTabPagers;//��ǩҳ�漯��
	private TabPageIndicator mIndicator;
	private ImageButton imgbtn;
	
	public NewsMenuDetialPager(Activity activity, ArrayList<NewsTabData> children) {
		super(activity);
		mTabDate = children;
	}

	@Override
	public View initView() {
		//���ViewPager�໬���
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
		//��Indicator��viewpager����һ�𣬱�����viewpager����adapter֮��
		//��Ҫ��дdispatchTouchEvent������֪ͨ���и��ؼ��Լ���ؼ���Ҫ���ش�����Ӧ����Ϣ�¼�
		mIndicator.setViewPager(detailMenuPager);
		//��ViewPager�ı��Ǵ���������������֮ǰ��Indicator�󶨣��˴���������ViewPager��ע�����������������Indicator��Ϣ��ͬ��
//		detailMenuPager.setOnPageChangeListener(this);
		//Ӧ����Indicator��ע�������������ʵ����Ϣͬ��
		mIndicator.setOnPageChangeListener(this);
		//����Indicator�ұߵļ�ͷ��ť
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
		//��ȡָʾ���ı���
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
	 * ��������������ViewPager����Ϣ�����¼�
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
    
    ////��������ò����
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
