package com.szxx.googleplay.ui.holder;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.szxx.googelmarket.R;
import com.szxx.googleplay.domain.AppInfos;
import com.szxx.googleplay.domain.AppInfos.SafeInfo;
import com.szxx.googleplay.http.HttpHelper;
import com.szxx.googleplay.uiutils.BitmapHelper;
import com.szxx.googleplay.uiutils.UIUtils;

public class DetialSafeHoolder extends BaseHolder<AppInfos> {

	private ImageView[] mSafeIcons; //安全标示图片 
	private ImageView[] mDesIcons;  //安全描述图片
	private TextView[] mSafeDes;  //安全描述文字简介
	private LinearLayout[] mSafeDesCol;  //安全图片与文字描述集合
	private View view;
	private BitmapUtils bitmapUtils;
	private RelativeLayout rl_root;  //安全标示图片所在的相对布局
	private LinearLayout ll_root;  //安全描述所在的线性布局
	private int mDesHeight;
	private LinearLayout.LayoutParams layoutParams;  
	private ImageView iv_arrow;
	
	@Override
	public View initView() {
		view = UIUtils.inflateView(R.layout.layout_detail_safeinfo);
		mSafeIcons = new ImageView[4];
		mSafeIcons[0] = (ImageView) view.findViewById(R.id.iv_safe1);
		mSafeIcons[1] = (ImageView) view.findViewById(R.id.iv_safe2);
		mSafeIcons[2] = (ImageView) view.findViewById(R.id.iv_safe3);
		mSafeIcons[3] = (ImageView) view.findViewById(R.id.iv_safe4);
		
		mDesIcons = new ImageView[4];
		mDesIcons[0] = (ImageView) view.findViewById(R.id.iv_des1);
		mDesIcons[1] = (ImageView) view.findViewById(R.id.iv_des2);
		mDesIcons[2] = (ImageView) view.findViewById(R.id.iv_des3);
		mDesIcons[3] = (ImageView) view.findViewById(R.id.iv_des4);
		
		mSafeDes = new TextView[4];
		mSafeDes[0] = (TextView) view.findViewById(R.id.tv_des1);
		mSafeDes[1] = (TextView) view.findViewById(R.id.tv_des2);
		mSafeDes[2] = (TextView) view.findViewById(R.id.tv_des3);
		mSafeDes[3] = (TextView) view.findViewById(R.id.tv_des4);
		
		mSafeDesCol = new LinearLayout[4];
		mSafeDesCol[0] = (LinearLayout) view.findViewById(R.id.ll_des1);
		mSafeDesCol[1] = (LinearLayout) view.findViewById(R.id.ll_des2);
		mSafeDesCol[2] = (LinearLayout) view.findViewById(R.id.ll_des3);
		mSafeDesCol[3] = (LinearLayout) view.findViewById(R.id.ll_des4);
		
		rl_root = (RelativeLayout) view.findViewById(R.id.rl_des_root);
		rl_root.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				toggle(); //打开或关闭安全描述
			}
		});
		
		ll_root = (LinearLayout) view.findViewById(R.id.ll_des_root);
		iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
		
		bitmapUtils = BitmapHelper.getBitmapUtils();
		return view;
	}
	
	private boolean isOpen = false;  //标记安全描述开关，默认为 关
	//打开或关闭安全描述
	protected void toggle() {
		//属性动画
		ValueAnimator animation = null;
		
		if (isOpen) {
			isOpen = false;
			animation = ValueAnimator.ofInt(mDesHeight, 0);
		}else {
			isOpen = true;
			animation = ValueAnimator.ofInt(0, mDesHeight);
			
		}
		//动画更新的监听
		animation.addUpdateListener(new AnimatorUpdateListener() {
			//启动动画后，会不断对调此方法来获取最新的值
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				//实时获取动画中mDesHeight的渐变高度
				Integer height = (Integer) animation.getAnimatedValue();
				
				//重新修改布局的高度
				layoutParams.height = height;
				ll_root.setLayoutParams(layoutParams);
			}
		});
		
		animation.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}
			
			//动画结束后
			@Override
			public void onAnimationEnd(Animator animation) {
				if (isOpen) {
					iv_arrow.setImageResource(R.drawable.arrow_down);
				}else {
					iv_arrow.setImageResource(R.drawable.arrow_up);
				}
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}
		});
		animation.setDuration(200);
		animation.start();
	}

	@Override
	public void refreshView(AppInfos data) {
		ArrayList<SafeInfo> safe = data.safe;
		for (int i = 0; i < 4; i++) {
			if (i < safe.size()) {
				SafeInfo info = safe.get(i);
				bitmapUtils.display(mSafeIcons[i], HttpHelper.URL + "image?name=" + info.safeUrl);
				mSafeDes[i].setText(info.safeDes);
				bitmapUtils.display(mDesIcons[i], HttpHelper.URL + "image?name=" + info.safeDesUrl);
				
			}else {
				mSafeIcons[i].setVisibility(View.GONE);  //如果没有多余的安全标示图标，将初始化剩余没有赋值的图标隐藏
//				mSafeDes[i].setVisibility(View.GONE);  //如果没有多余的安全文字描述，将初始化剩余没有赋值的文字描述隐藏
//				mDesIcons[i].setVisibility(View.GONE);  //如果没有多余的安全描述图片，将初始化剩余没有赋值的图片描述隐藏
				//替代上述两条注释
				mSafeDesCol[i].setVisibility(View.GONE);
			}
			
		}
		//不设置所在线性布局的宽高，表示由底层自动计算扩充
		ll_root.measure(0, 0);
		//安全描述的高度
		mDesHeight = ll_root.getMeasuredHeight();
		
		//修改安全描述布局高度为0
		layoutParams = (android.widget.LinearLayout.LayoutParams) ll_root.getLayoutParams();
		layoutParams.height = 0;
		ll_root.setLayoutParams(layoutParams);
		
	}

}
