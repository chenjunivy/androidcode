package com.szxx.googleplay.ui.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.szxx.googelmarket.R;
import com.szxx.googleplay.ui.fragment.BaseFragment;
import com.szxx.googleplay.ui.fragment.FragmentFactory;
import com.szxx.googleplay.ui.view.PagerTab;
import com.szxx.googleplay.uiutils.UIUtils;

/**
 * 当项目和appcompat关联在一起时，必须在清单文件中设置Theme.AppCompat的主题，否则崩溃
 * android:theme="@style/Theme.AppCompat.Light"
 * @author System_ivy
 *
 */
public class MainActivity extends BaseActivity {
	private PagerTab mPagerTab;
	private ViewPager mViewPager;
	private MyAdapter mAdapter;
	private ActionBarDrawerToggle toggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mPagerTab = (PagerTab) findViewById(R.id.pager_tab);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		
		mAdapter = new MyAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		
		mPagerTab.setViewPager(mViewPager);  //将指针和viewpager绑定在一起
		
		mPagerTab.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				BaseFragment fragment2 = FragmentFactory.createFragment(position);
				//调用以加载数据
				fragment2.onLoad();
				
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
		
		onActionBarInit();
	}
	
	//ActionBar初始化
	private void onActionBarInit(){
		android.support.v7.app.ActionBar actionbar = getSupportActionBar();
		actionbar.setHomeButtonEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		toggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer_am, R.string.openbar, R.string.closebar);
		toggle.syncState();//DrawerLayout 与toggle 关联起来
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle.onOptionsItemSelected(item);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 *FragmentPagerAdapter是PagerAdapter的子类 ，如果viewpager是fragment,就继承FragmentPagerAdapter
	 */
	class MyAdapter extends FragmentPagerAdapter{

		private String[] mTbaNames;

		public MyAdapter(FragmentManager fm) {
			super(fm);
			mTbaNames = UIUtils.getStringArray(R.array.tab_names); //加载页面标题数组
		}
		
		//返回页签标题
		@Override
		public CharSequence getPageTitle(int position) {
			return mTbaNames[position];
			
		}
		
		//返回当前页面位置的fragment对象
		@Override
		public Fragment getItem(int position) {
			BaseFragment fragment = FragmentFactory.createFragment(position);
			return fragment;
		}

		//fragment数量
		@Override
		public int getCount() {
			return mTbaNames.length;
		}
		
	}

	
}
