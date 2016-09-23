package fragment;

import implpager.NewsCenterPager;

import java.util.ArrayList;
import base.BasePager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.szcx.wisdomofbeijing.MainActivity;
import com.szcx.wisdomofbeijing.R;

import domain.NewsMenu.NewsMenuData;

public class LeftFragment extends BaseFragment {

	private ListView lv_left;
	private ArrayList<NewsMenuData> mNewsMenuData;
	private LeftFragMenuAdapter lfragadpter;
	private int mMenuIndex;//左侧菜单被选中的时候的下标
	@Override
	public View initView() {
		View view = View.inflate(attachActivity, R.layout.fragment_leftmenu, null);
		lv_left = (ListView) view.findViewById(R.id.lv_leftmenu);
		
		return view;
	}

	@Override
	public void initData() {
	}

	//侧边栏设置数据
	public void setMenuData(ArrayList<NewsMenuData> data){
		mMenuIndex = 0;//其他页面切回的时候，新闻中心默认菜单详情页是新闻
		//更新页面
		mNewsMenuData = data;
		lfragadpter	= new LeftFragMenuAdapter();
		lv_left.setAdapter(lfragadpter);
		lv_left.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mMenuIndex = position;
				lfragadpter.notifyDataSetChanged();
				
				//与此同时，收起侧边栏
				pickUp();
				//点击侧边栏后，更新新闻中心相关的内容导航
				setCurrentDetialPager(position);
			}
		});
		
	}
	//打开或关闭侧边栏
	protected  void pickUp(){
		MainActivity mainUi = (MainActivity) attachActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//如果当前状态时关闭，调用之后是打开，反之亦然
	}
	/**
	 * 更新菜单详情项
	 * @param position
	 */
	private void setCurrentDetialPager(int position){
		//获新闻中心对象
		MainActivity mainUi = (MainActivity) attachActivity;
		//获取ContentFragment
		ContentFragment fragContent = mainUi.findContentFragment();
		//获取新闻中心页面
		NewsCenterPager contNews = fragContent.getNewsContentPager();
		//修改新闻中心的FrameLayout布局
		contNews.resetNewsContentPager(position);
		
	}
	
	class LeftFragMenuAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mNewsMenuData.size();
		}

		@Override
		public Object getItem(int position) {
			return mNewsMenuData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view2 =  View.inflate(attachActivity, R.layout.list_left_menu, null);
			
			TextView tv_menu = (TextView) view2.findViewById(R.id.tv_menu_left);
			NewsMenuData item = (NewsMenuData) getItem(position);
			tv_menu.setText(item.title);
			if (mMenuIndex == position) {
				//被选中
				tv_menu.setEnabled(true);
			} else {
				//未选中
				tv_menu.setEnabled(false);
			}
			return view2;
		}
		
	}
}
