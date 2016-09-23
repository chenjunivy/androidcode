package com.szxx.googleplay.ui.holder;

import java.util.ArrayList;

import android.view.View;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.szxx.googelmarket.R;
import com.szxx.googleplay.domain.AppInfos;
import com.szxx.googleplay.http.HttpHelper;
import com.szxx.googleplay.uiutils.BitmapHelper;
import com.szxx.googleplay.uiutils.UIUtils;

public class DetialPicsHolder extends BaseHolder<AppInfos> {
	private ImageView[] mDesPic;
	private View view;
	private BitmapUtils bitmapUtils;
	
	@Override
	public View initView() {
		mDesPic =  new ImageView[5];
		view = UIUtils.inflateView(R.layout.layout_detial_despic);
		mDesPic[0] = (ImageView) view.findViewById(R.id.iv_pic1);
		mDesPic[1] = (ImageView) view.findViewById(R.id.iv_pic2);
		mDesPic[2] = (ImageView) view.findViewById(R.id.iv_pic3);
		mDesPic[3] = (ImageView) view.findViewById(R.id.iv_pic4);
		mDesPic[4] = (ImageView) view.findViewById(R.id.iv_pic5);
		
		bitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}

	@Override
	public void refreshView(AppInfos data) {
		ArrayList<String> picDes = data.screen;
		for (int i = 0; i < 5; i++) {
			if (i<picDes.size()) {
				bitmapUtils.display(mDesPic[i], HttpHelper.URL + "image?name=" + picDes.get(i));
			}else {
				mDesPic[i].setVisibility(View.GONE);
			}
		}
	}

}
