
package com.szxx.googleplay.ui.adpter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.szxx.googleplay.manager.ThreadManager;
import com.szxx.googleplay.ui.holder.BaseHolder;
import com.szxx.googleplay.ui.holder.MoreHolder;
import com.szxx.googleplay.uiutils.UIUtils;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	ArrayList<T> data;
	//注意：getItemViewType返回数组元素下标，因此状态必须使用下标的命名规则。从"0"开始
	public final static int TYPE_NORMAL = 1;
	public final static int TYPE_MORE = 0;
	
	public MyBaseAdapter(ArrayList<T> data) {
		this.data = data;
	}
	
	@Override
	public int getCount() {
		return data.size() + 1; //底部添加一种新的下拉选项类型
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	//下拉列表项的类型种类
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	//使用哪一种下拉列表的类型
	@Override
	public int getItemViewType(int position) {
		if (position == getCount() -1) {
			return TYPE_MORE;
		}
		return getInnerPype(position);
	}
	
	//可以被子类重写，以便根据实际情况更改列表项的类型
	public int getInnerPype(int position) {
		return TYPE_NORMAL;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder holder;
		if (convertView == null) {
			if (getItemViewType(position) == TYPE_MORE) {
				holder = new MoreHolder(hasMore());
			}else {
				//加载布局，组件，标记
				holder = getHolder(position);
			}
		
		}else {
			holder = (BaseHolder) convertView.getTag();
		}
		
		if (getItemViewType(position) == TYPE_MORE) {
			//加载更多布局
			//一旦加载更多布局项显示出来，就加载更多界面
			MoreHolder moreholder = (MoreHolder) holder;
			if (moreholder.getData() == MoreHolder.STATE_MORE_MORE) {
				loadMore(moreholder);
			}
		} else {
			//设置数据
			holder.setData(getItem(position));
		}
		
		
		return holder.getRootView();
	}
	
	//被被子类选择重写，判断是否可以加载更多
	public boolean hasMore() {
		return true; //默认可以加载更多
	}

	private boolean isLoadMore = false; //标记是否正在加载更多
	//加载更多数据
	public void loadMore(final MoreHolder moreholder){
		if (!isLoadMore) {
			isLoadMore = true;
			
			/*new Thread(){
				@Override
				public void run() {
//					final ArrayList<T> moreData = onLoadMore();
					UIUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							if (moreData != null) {
								//有更多数据可供加载，每页可以容纳20条列表项，如果返回的项目数小于，则认为是加载到最后了
								if (moreData.size() < 20) {
									moreholder.setData(MoreHolder.STATE_MORE_NONE);
									Toast.makeText(UIUtils.getUIContext(), "数据已加载完毕，没有更多的la...", Toast.LENGTH_LONG).show();
								} else {
									//超过20条列表项，加载新一页数据
									moreholder.setData(MoreHolder.STATE_MORE_MORE);
								}
								//将更多数据加载到当前列表项中
								data.addAll(moreData);
								//刷新界面
								MyBaseAdapter.this.notifyDataSetChanged();
								
							}else {
								//加载更多数据失败
								moreholder.setData(MoreHolder.STATE_MORE_ERROR);
							}
							isLoadMore = false;
						}
					});
					
				}
			}.start();*/
			
			ThreadManager.getThreadPool().executeThread(new Runnable() {
				
				@Override
				public void run() {
					final ArrayList<T> moreData = onLoadMore();
					UIUtils.runOnUIThread(new Runnable() {
						
						@Override
						public void run() {
							if (moreData != null) {
								//有更多数据可供加载，每页可以容纳20条列表项，如果返回的项目数小于，则认为是加载到最后了
								if (moreData.size() < 20) {
									moreholder.setData(MoreHolder.STATE_MORE_NONE);
									Toast.makeText(UIUtils.getUIContext(), "数据已加载完毕，没有更多的la...", Toast.LENGTH_LONG).show();
								} else {
									//超过20条列表项，加载新一页数据
									moreholder.setData(MoreHolder.STATE_MORE_MORE);
								}
								//将更多数据加载到当前列表项中
								data.addAll(moreData);
								//刷新界面
								MyBaseAdapter.this.notifyDataSetChanged();
								
							}else {
								//加载更多数据失败
								moreholder.setData(MoreHolder.STATE_MORE_ERROR);
							}
							isLoadMore = false;
						}
					});
				}
			});
		}
		
	}
	//1.获取相应的布局及其组件，必须由子类负责实现
	public abstract BaseHolder<T> getHolder(int position);
	
	//2.加载更多的列表项布局，由子类去实现
	public abstract ArrayList<T> onLoadMore();
	
	//3.获取当前集合大小
	public int getListSize(){
		return data.size();
	}

}
