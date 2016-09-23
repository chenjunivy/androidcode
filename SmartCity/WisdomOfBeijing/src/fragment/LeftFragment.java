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
	private int mMenuIndex;//���˵���ѡ�е�ʱ����±�
	@Override
	public View initView() {
		View view = View.inflate(attachActivity, R.layout.fragment_leftmenu, null);
		lv_left = (ListView) view.findViewById(R.id.lv_leftmenu);
		
		return view;
	}

	@Override
	public void initData() {
	}

	//�������������
	public void setMenuData(ArrayList<NewsMenuData> data){
		mMenuIndex = 0;//����ҳ���лص�ʱ����������Ĭ�ϲ˵�����ҳ������
		//����ҳ��
		mNewsMenuData = data;
		lfragadpter	= new LeftFragMenuAdapter();
		lv_left.setAdapter(lfragadpter);
		lv_left.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mMenuIndex = position;
				lfragadpter.notifyDataSetChanged();
				
				//���ͬʱ����������
				pickUp();
				//���������󣬸�������������ص����ݵ���
				setCurrentDetialPager(position);
			}
		});
		
	}
	//�򿪻�رղ����
	protected  void pickUp(){
		MainActivity mainUi = (MainActivity) attachActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//�����ǰ״̬ʱ�رգ�����֮���Ǵ򿪣���֮��Ȼ
	}
	/**
	 * ���²˵�������
	 * @param position
	 */
	private void setCurrentDetialPager(int position){
		//���������Ķ���
		MainActivity mainUi = (MainActivity) attachActivity;
		//��ȡContentFragment
		ContentFragment fragContent = mainUi.findContentFragment();
		//��ȡ��������ҳ��
		NewsCenterPager contNews = fragContent.getNewsContentPager();
		//�޸��������ĵ�FrameLayout����
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
				//��ѡ��
				tv_menu.setEnabled(true);
			} else {
				//δѡ��
				tv_menu.setEnabled(false);
			}
			return view2;
		}
		
	}
}
