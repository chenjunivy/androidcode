package com.szxx.googleplay.ui.fragment;

import java.util.ArrayList;

import android.view.View;

import com.szxx.googleplay.domain.CategoryInfo;
import com.szxx.googleplay.http.protocol.CategoryProtocol;
import com.szxx.googleplay.ui.adpter.MyBaseAdapter;
import com.szxx.googleplay.ui.holder.BaseHolder;
import com.szxx.googleplay.ui.holder.CategoryHolder;
import com.szxx.googleplay.ui.holder.TitleHolder;
import com.szxx.googleplay.ui.view.LoadingLayout.ResultState;
import com.szxx.googleplay.ui.view.MyListView;
import com.szxx.googleplay.uiutils.UIUtils;

public class CategoryFragment extends BaseFragment {

	private ArrayList<CategoryInfo> data;
	@Override
	public View createSuccessView() {
		MyListView list = new MyListView(UIUtils.getUIContext());
		list.setAdapter(new CategroyAdapter(data) );
		return list;
	}

	@Override
	public ResultState onLoadDataResult() {
		CategoryProtocol protocol = new CategoryProtocol();
		data = protocol.getData(0);
		return checkId(data);
	}
	
	/**
	 * 由于布局跟前期home等布局不太相同，加之新增了listView布局(三张图片并排放)，去除了加载更多的布局
	 * 1.关闭加载更多的布局    重写hasMore()，不加载更多
	 * 2.在原来的基础上增加一种基础类型 重写getViewTypeCount()
	 * 3。新增列表项的类型  重写getInnerPype()
	 * @author SystemIvy
	 *
	 */
	class CategroyAdapter extends MyBaseAdapter<CategoryInfo>{

		public CategroyAdapter(ArrayList<CategoryInfo> data) {
			super(data);
		}
		
		//1.取消加载更多下拉布局
		@Override
		public boolean hasMore() {
			return false;
		}
		
		//2.在原来的基础上增加一种基础类型
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount() + 1;  //在原来的基础上增加一种标题类型
		}
		//3.新增列表类型
		@Override
		public int getInnerPype(int position) {
			//判断使用何种类型
			CategoryInfo info = data.get(position);
			if (info.isTitle) {
				return super.getInnerPype(position) + 1; //原有类型基础上加1，注意 TYPE_NORMAL == 1
			}
			return super.getInnerPype(position);
		}
		
		@Override
		public BaseHolder<CategoryInfo> getHolder(int position) {
			CategoryInfo info = data.get(position);
			if (info.isTitle) {
				return new TitleHolder();
			}else {
				return new CategoryHolder();
			}
			
		}

		@Override
		public ArrayList<CategoryInfo> onLoadMore() {
			return null;
		}
		
	}
	
}
