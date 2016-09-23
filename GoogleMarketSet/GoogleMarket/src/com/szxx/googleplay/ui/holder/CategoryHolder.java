package com.szxx.googleplay.ui.holder;

import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.szxx.googelmarket.R;
import com.szxx.googleplay.domain.CategoryInfo;
import com.szxx.googleplay.http.HttpHelper;
import com.szxx.googleplay.uiutils.BitmapHelper;
import com.szxx.googleplay.uiutils.UIUtils;

public class CategoryHolder extends BaseHolder<CategoryInfo> implements OnClickListener {

	
	private View view;
	private TextView tvdesc1, tvdesc2, tvdesc3;
	private ImageView iv_icon1, iv_icon2, iv_icon3;
	private LinearLayout logo1, logo2, logo3;
	private BitmapUtils bitmapUtils;

	@Override
	public View initView() {
		view = UIUtils.inflateView(R.layout.list_item_category);
		tvdesc1 = (TextView) view.findViewById(R.id.tv_desc1);
		tvdesc2 = (TextView) view.findViewById(R.id.tv_desc2);
		tvdesc3 = (TextView) view.findViewById(R.id.tv_desc3);
		
		iv_icon1 = (ImageView) view.findViewById(R.id.iv_cate_icon1);
		iv_icon2 = (ImageView) view.findViewById(R.id.iv_cate_icon2);
		iv_icon3 = (ImageView) view.findViewById(R.id.iv_cate_icon3);
		
		logo1 = (LinearLayout) view.findViewById(R.id.ll_logo1);
		logo2 = (LinearLayout) view.findViewById(R.id.ll_logo2);
		logo3 = (LinearLayout) view.findViewById(R.id.ll_logo3);
		
		logo1.setOnClickListener(this);
		logo2.setOnClickListener(this);
		logo3.setOnClickListener(this);
		
		bitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshView(CategoryInfo data) {
		tvdesc1.setText(data.name1);
		tvdesc2.setText(data.name2);
		tvdesc3.setText(data.name3);
		
		bitmapUtils.display(iv_icon1, HttpHelper.URL + "image?name=" + data.url1);
		bitmapUtils.display(iv_icon2, HttpHelper.URL + "image?name=" + data.url2);
		bitmapUtils.display(iv_icon3, HttpHelper.URL + "image?name=" + data.url3);
		
	}

	@Override
	public void onClick(View v) {
		CategoryInfo info = getData();
		switch (v.getId()) {
		case R.id.ll_logo1:
			Toast.makeText(UIUtils.getUIContext(), info.name1, Toast.LENGTH_SHORT).show();
			break;
		case R.id.ll_logo2:
			Toast.makeText(UIUtils.getUIContext(), info.name2, Toast.LENGTH_SHORT).show();
			break;
		case R.id.ll_logo3:
			Toast.makeText(UIUtils.getUIContext(), info.name3, Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

}
