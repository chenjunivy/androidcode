
package com.szxx.googleplay.ui.holder;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.szxx.googelmarket.R;
import com.szxx.googleplay.domain.AppInfos;
import com.szxx.googleplay.uiutils.UIUtils;

public class DetialDesHolder extends BaseHolder<AppInfos> {

	private View view;
	private TextView tv_des;
	private RelativeLayout rl_toggle;
	private TextView tv_author;
	private ImageView iv_arrow;

	@Override
	public View initView() {
		view = UIUtils.inflateView(R.layout.layout_detial_desinfo);
		tv_des = (TextView) view.findViewById(R.id.tv_detail_des);
		rl_toggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
		tv_author = (TextView) view.findViewById(R.id.tv_detail_author);
		iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
		rl_toggle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggle();
			}
		});
		return view;
	}

	private boolean isOpen = false;
	private LinearLayout.LayoutParams mParsms;
	//展开或隐藏的触发器
	protected void toggle() {
		ValueAnimator animator = null;
		int shortHeight = getShortHeight();
		int longHeight = getLongHeight();
		if (isOpen) {
			isOpen = false;
			if (shortHeight < longHeight) {//如果描述数据超过7行，才有动画效果
				animator = ValueAnimator.ofInt(longHeight, shortHeight);
			}
		}else {
			isOpen = true;
			if (shortHeight < longHeight) {
				animator = ValueAnimator.ofInt(shortHeight, longHeight);
			}
		}
		
		if (animator != null) {
			
			animator.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					Integer height = (Integer) animation.getAnimatedValue();
					
					mParsms.height = height;
					tv_des.setLayoutParams(mParsms);
					
				}
			});
			
			animator.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					final ScrollView scrollView = getScrollView();
					//为了保证运行的安全和稳定，可以将滑动到底部的方法存放到消息中执行
					scrollView.post(new Runnable() {
						
						@Override
						public void run() {
							scrollView.fullScroll(ScrollView.FOCUS_DOWN);//祖控件ScrollView自动上滑以显示数据
						}
					});
					
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
			animator.setDuration(200);
			animator.start();
		}
	}

	@Override
	public void refreshView(AppInfos data) {
		tv_des.setText(data.des);
		tv_author.setText(data.author);
		
		//放在消息队列中执行，消除不足7行的描叙内容仍然按照7行布局大小展示  这个bug
		tv_des.post(new Runnable() {
			
			@Override
			public void run() {
				//设置初始描述文字最多显示7行
				int shortHeight = getShortHeight();
				mParsms = (LinearLayout.LayoutParams) tv_des.getLayoutParams();
				mParsms.height = shortHeight;
				tv_des.setLayoutParams(mParsms);
				
			}
		});
		
	}
	
	//获取7行文字的高度
	private int getShortHeight(){
		//虚拟一个textview,设置相同的文字内容和字体大小，最大显示7行文字，从而计算tv_des的7行文字有多高
		//由于TextView没有提供方法计来算指定行数的高度，所以才需要“曹冲称象”策略
		int width = tv_des.getMeasuredWidth();
		
		TextView tv_test = new TextView(UIUtils.getUIContext());
		tv_test.setText(getData().des);
		tv_test.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		tv_test.setMaxLines(7);

		//设置虚拟TextView的布局宽高模式和宽高值，
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);  //对应于TextView组件的layout_width="match_parent"
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);  //对应于TextView组件的layout_height="wrap_content" //第一个参数表示控件 宽或高的最大值
		//注意，如果组件在xml布局中定义了宽高模式，调用measure(0,0)表示组件宽高由底层控制，即xml中子组件与父组件的综合作用结果
		tv_test.measure(widthMeasureSpec, heightMeasureSpec);
		
		return tv_test.getMeasuredHeight();
	}
	
	//获取全篇文字的高度
	private int getLongHeight(){
		//虚拟一个textview,设置相同的文字内容和字体大小，最大显示7行文字，从而计算tv_des的7行文字有多高
		//由于TextView没有提供方法计来算指定行数的高度，所以才需要“曹冲称象”策略
		int width = tv_des.getMeasuredWidth();
		
		TextView tv_test = new TextView(UIUtils.getUIContext());
		tv_test.setText(getData().des);
		tv_test.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//		tv_test.setMaxLines(7);
		
		//设置虚拟TextView的布局宽高模式和宽高值，
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);  //对应于TextView组件的layout_width="match_parent"
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(2000, MeasureSpec.AT_MOST);  //对应于TextView组件的layout_height="wrap_content" //第一个参数表示控件 宽或高的最大值
		//注意，如果组件在xml布局中定义了宽高模式，调用measure(0,0)表示组件宽高由底层控制，即xml中子组件与父组件的综合作用结果
		tv_test.measure(widthMeasureSpec, heightMeasureSpec);
		
		return tv_test.getMeasuredHeight();
	}
	
	//获取祖控件,一层层向上找到需要的ScrollView控件，(必须确保有祖控件存在ScrollView,)
	private ScrollView getScrollView(){
		ViewParent parent = tv_des.getParent();
		
		while(!(parent instanceof ScrollView)){
			parent = parent.getParent();
		}
		return (ScrollView) parent;
	}

}
