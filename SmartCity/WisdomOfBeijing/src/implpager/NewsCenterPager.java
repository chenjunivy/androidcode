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
	private NewsMenu mNewsData;//������Ϣ�������ݶ���

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initDatePager() {
		super.initDatePager();
//		//Ϊframelayout��䲼�ֶ���
//		TextView view = new TextView(mActivity);
//		view.setText("��������");
//		view.setTextColor(Color.RED);
//		view.setTextSize(22);
//		view.setGravity(Gravity.CENTER);
//		fl_content.addView(view);
		//�޸�ҳ�����
		tTitle.setText("����");
		//����ImageButton ��ʾ
		btn_menu.setVisibility(View.VISIBLE);
		
		//����Ƿ��л��棬�����ȡ����������
		String cache = CacheUtils.getCacheContent(GlobalContextSource.CATEGORY_URL, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			System.out.println("���ֻ��棬׼������");
			processData(cache);
		} else {
			//���������,��ȡjson����
			//ʹ��XUtils��Դ���
			getDataFromServer();
		}
	}

	/**
	 * �ӷ�������ȡ����
	 * ��Ҫ���Ȩ��INTERNET, WRITE_EXTERNAL_STORAGE
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, GlobalContextSource.CATEGORY_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//����ɹ�
				String rec = responseInfo.result;
				processData(rec);
				System.out.println(rec);
				
				//���û���
				CacheUtils.setCacheContent(GlobalContextSource.CATEGORY_URL, rec, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//����ʧ��
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
			}

		});
	}

	/**
	 * ����json����
	 */
	protected void processData(String result) {
		//Gson google json
		Gson gs = new Gson();
		mNewsData = gs.fromJson(result, NewsMenu.class);
		System.out.println("�������"+mNewsData);
		
		//��������ҳ�浼���˵������������Ҫ�õ�LeftFragment��Id���Ȼ��MainActivity��Id
		MainActivity mainUi = (MainActivity) mActivity;
		LeftFragment leftFrag = mainUi.findLeftFragment();
		
		//���������������
		leftFrag.setMenuData(mNewsData.data);
		
		//��ʼ��4���˵�����ҳ
		mBaseMenuDetial = new ArrayList<BaseMenuDetialPager>();
		mBaseMenuDetial.add(new NewsMenuDetialPager(mActivity, mNewsData.data.get(0).children));
		mBaseMenuDetial.add(new TopicMenuDetialPager(mActivity));
		mBaseMenuDetial.add(new PhotosMenuDetialPager(mActivity, btn_photo));
		mBaseMenuDetial.add(new InteractMenuDetialPager(mActivity));
		
		//���Ų˵�����ҳ����ΪĬ��ֵ
		resetNewsContentPager(0);
		
	}
	
	//���������������ĵ�FrameLayout������
	public void resetNewsContentPager(int position){
		BaseMenuDetialPager pager = mBaseMenuDetial.get(position);
		View view = pager.mRootView;//��ǰҳ��Ĳ���
		fl_content.removeAllViews();//���֮ǰ�������겼��
		pager.initData();//��ʼ������
		fl_content.addView(view);
		tTitle.setText(mNewsData.data.get(position).title);
		
		//�Ƿ�����ͼҳ�棬��ʾ�������ϵ�adapterview�л���ť
		if (pager instanceof PhotosMenuDetialPager) {
			btn_photo.setVisibility(View.VISIBLE);
		}else {
			btn_photo.setVisibility(View.INVISIBLE);
		}
		
	}
	
	
}
