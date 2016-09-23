package implpager.menu;

import global.GlobalContextSource;

import java.util.ArrayList;

import utils.CacheUtils;
import utils.MyBitmapUtils;
import view.PhotoBean;
import view.PhotoBean.PhotoNews;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import base.BaseMenuDetialPager;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.szcx.wisdomofbeijing.R;
/**
 * 菜单详情页，图片
 * @author SystemIvy
 *
 */
public class PhotosMenuDetialPager extends BaseMenuDetialPager implements OnClickListener{
	@ViewInject(R.id.lv_photo)
	private ListView lv_photo;
	@ViewInject(R.id.gv_photo)
	private GridView gv_photo;
	private ArrayList<PhotoNews> mNewsList;
	private ImageButton btn_photo; 

	public PhotosMenuDetialPager(Activity activity, ImageButton btn_photo) {
		super(activity);
		btn_photo.setOnClickListener(this);
		this.btn_photo = btn_photo;
	}

	@Override
	public View initView() {
		//为framelayout填充布局对象
//		TextView view = new TextView(m2Activity);
//		view.setText("菜单详情页，图片");
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
		View view = View.inflate(m2Activity, R.layout.pager_photos_menu_detail, null);
		ViewUtils.inject(this, view);
		
		
		//lv_photo = (ListView) view.findViewById(R.id.lv_photo);
		//gv_photo = (GridView) view.findViewById(R.id.gv_photo);
		
		return view;
	}

	@Override
	public void initData() {
		String cacheContent = CacheUtils.getCacheContent(GlobalContextSource.PHOTO_URL, m2Activity);
		if (!TextUtils.isEmpty(cacheContent)) {
			processData(cacheContent);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalContextSource.PHOTO_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result);
				CacheUtils.setCacheContent(GlobalContextSource.PHOTO_URL, result, m2Activity);
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//请求失败
				error.printStackTrace();
				Toast.makeText(m2Activity, "请求失败", Toast.LENGTH_LONG).show();
			}
			
		});
	}

	protected void processData(String result) {
		Gson gson = new Gson();
		PhotoBean photoBean = gson.fromJson(result, PhotoBean.class);
		mNewsList = photoBean.data.news;
		lv_photo.setAdapter(new PhotoAdapter());
		gv_photo.setAdapter(new PhotoAdapter());
		
	}
	
	class PhotoAdapter extends BaseAdapter{
		
//		private BitmapUtils mBitmapUtils; //XUtils加载工具
		private MyBitmapUtils mBitmapUtils;  //自定义加载工具

		public PhotoAdapter(){
//			mBitmapUtils = new BitmapUtils(m2Activity);
//			mBitmapUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
			mBitmapUtils = new MyBitmapUtils();
			
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public PhotoNews getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(m2Activity, R.layout.list_item_photos, null);
				holder = new ViewHolder();
				holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
				holder.tv_btitle = (TextView) convertView.findViewById(R.id.tv_bottom_title);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			PhotoNews item = getItem(position);
			holder.tv_btitle.setText(item.title);
			
			//转换
			String convertUrl = item.listimage.replace("10.0.2.2", "192.168.56.1");
			mBitmapUtils.display(holder.iv_pic, convertUrl);
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		public ImageView iv_pic;
		public TextView tv_btitle;
	}

	private boolean isListView = true; //标记当前是否是listview显示
	@Override
	public void onClick(View v) {
		if (isListView) {
			//切换adapterview,显示gridview
			lv_photo.setVisibility(View.GONE);
			gv_photo.setVisibility(View.VISIBLE);
			
			btn_photo.setImageResource(R.drawable.icon_pic_grid_type);
			isListView = false;
		} else {
			lv_photo.setVisibility(View.VISIBLE);
			gv_photo.setVisibility(View.GONE);
			
			btn_photo.setImageResource(R.drawable.icon_pic_list_type);
			isListView = true;
		}
	}
	
	

}
