package implpager;

import fragment.LeftFragment;
import global.GlobalContextSource;
import implpager.menu.InteractMenuDetialPager;
import implpager.menu.NewsMenuDetialPager;
import implpager.menu.PhotosMenuDetialPager;
import implpager.menu.TopicMenuDetialPager;

import java.util.ArrayList;

import utils.CacheUtils;
import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import base.BaseMenuDetialPager;
import base.BasePager;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.szcx.wisdomofbeijing.MainActivity;

import domain.NewsMenu;

public class NewsCenterPager extends BasePager {
	private ArrayList<BaseMenuDetialPager> mBaseMenuDetial;
	private NewsMenu mNewsData;//分类信息解析数据对象

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initDatePager() {
		super.initDatePager();
//		//为framelayout填充布局对象
//		TextView view = new TextView(mActivity);
//		view.setText("新闻内容");
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
//		fl_content.addView(view);
		//修改页面标题
		tTitle.setText("新闻");
		//设置ImageButton 显示
		btn_menu.setVisibility(View.VISIBLE);
		
		//检测是否有缓存，有则读取，无则请求
		String cache = CacheUtils.getCacheContent(GlobalContextSource.CATEGORY_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			System.out.println("发现缓存，准备解析");
			processData(cache);
		} else {
			//请求服务器,获取json数据
			//使用XUtils开源框架
			getDataFromServer();
		}
	}

	/**
	 * 从服务器获取数据
	 * 需要添加权限INTERNET, WRITE_EXTERNAL_STORAGE
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalContextSource.CATEGORY_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//请求成功
				String rec = responseInfo.result;
				processData(rec);
				System.out.println(rec);
				
				//设置缓存
				CacheUtils.setCacheContent(GlobalContextSource.CATEGORY_URL, rec, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//请求失败
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
			}

		});
	}

	/**
	 * 解析json数据
	 */
	protected void processData(String result) {
		//Gson google json
		Gson gs = new Gson();
		mNewsData = gs.fromJson(result, NewsMenu.class);
		System.out.println("解析结果"+mNewsData);
		
		//将新闻主页面导航菜单附给侧边栏，要得到LeftFragment的Id，先获得MainActivity的Id
		MainActivity mainUi = (MainActivity) mActivity;
		LeftFragment leftFrag = mainUi.findLeftFragment();
		
		//给侧边栏设置数据
		leftFrag.setMenuData(mNewsData.data);
		
		//初始化4个菜单详情页
		mBaseMenuDetial = new ArrayList<BaseMenuDetialPager>();
		mBaseMenuDetial.add(new NewsMenuDetialPager(mActivity, mNewsData.data.get(0).children));
		mBaseMenuDetial.add(new TopicMenuDetialPager(mActivity));
		mBaseMenuDetial.add(new PhotosMenuDetialPager(mActivity, btn_photo));
		mBaseMenuDetial.add(new InteractMenuDetialPager(mActivity));
		
		//新闻菜单详情页设置为默认值
		resetNewsContentPager(0);
		
	}
	
	//从新设置新闻中心的FrameLayout的内容
	public void resetNewsContentPager(int position){
		BaseMenuDetialPager pager = mBaseMenuDetial.get(position);
		View view = pager.mRootView;//当前页面的布局
		fl_content.removeAllViews();//清除之前的所有贞布局
		pager.initData();//初始化数据
		fl_content.addView(view);
		tTitle.setText(mNewsData.data.get(position).title);
		
		//是否是组图页面，显示标题栏上的adapterview切换按钮
		if (pager instanceof PhotosMenuDetialPager) {
			btn_photo.setVisibility(View.VISIBLE);
		}else {
			btn_photo.setVisibility(View.INVISIBLE);
		}
		
	}
	
	
}
