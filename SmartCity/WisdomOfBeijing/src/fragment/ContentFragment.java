package fragment;

import implpager.GovAffairsPager;
import implpager.HomePager;
import implpager.NewsCenterPager;
import implpager.SettingPager;
import implpager.SmartServicePager;

import java.util.ArrayList;

import view.NoScrollViewPager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import base.BasePager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.szcx.wisdomofbeijing.MainActivity;
import com.szcx.wisdomofbeijing.R;

public class ContentFragment extends BaseFragment {

	private NoScrollViewPager vp_content;
	private PagerAdapter pa_content;
	private BasePager bp_pager;
	private ArrayList<BasePager> al_pagers;
	private RadioGroup rg_btn;

	@Override
	public View initView() {
		View view = View.inflate(attachActivity, R.layout.fragment_content, null);
		vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		rg_btn = (RadioGroup) view.findViewById(R.id.rg_select);
		return view;
	}

	@Override
	public void initData() {
		al_pagers = new ArrayList<BasePager>();
		al_pagers.add(new HomePager(attachActivity));
		al_pagers.add(new NewsCenterPager(attachActivity));
		al_pagers.add(new SmartServicePager(attachActivity));
		al_pagers.add(new GovAffairsPager(attachActivity));
		al_pagers.add(new SettingPager(attachActivity));
		vp_content.setAdapter(new ContentPagerAdapter());
		rg_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				
				case R.id.rb_home:
					vp_content.setCurrentItem(0, false);
					
					break;
				case R.id.rb_news:
					vp_content.setCurrentItem(1, false);
					
					break;
				case R.id.rb_service:
					vp_content.setCurrentItem(2, false);
					
					break;
				case R.id.rb_gover:
					vp_content.setCurrentItem(3, false); //����2��ʾ�Ƿ��ж�����ʾ
					
					break;
				case R.id.rb_set:
					vp_content.setCurrentItem(4, false);
					
					break;
				default:
					break;
				}
			}
		});
		vp_content.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				BasePager pager2 = al_pagers.get(position);
				pager2.initDatePager();
				if (position == 0 || position == al_pagers.size() - 1) {
					setSlidingMenuVisible(false);
				} else {
					setSlidingMenuVisible(true);

				}
			}
			

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		al_pagers.get(0).initDatePager();//�ֶ��ӵ�һҳ����
		setSlidingMenuVisible(false);
	}
	
	//��������ò����
	private void setSlidingMenuVisible(boolean b) {
		MainActivity mActivity = (MainActivity) attachActivity;
		SlidingMenu slidMenu = mActivity.getSlidingMenu();
		if (b) {
			slidMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
	
	class ContentPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return al_pagers.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = al_pagers.get(position);
			View view = pager.rootView; //��ȡ��ǰҳ��Ĳ���
//			pager.initDatePager(); //��ʼ��pager��framelayout������,ViewPagerĬ�ϼ�����һ��pager�����ݣ�Ϊ�˽�ʡ�������������ܣ����ڴ˴���ʼ������
			container.addView(view);
			return view;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}
	//��ȡ��������ҳ��
	public NewsCenterPager  getNewsContentPager(){
		return (NewsCenterPager) al_pagers.get(1);
	}
	
}
