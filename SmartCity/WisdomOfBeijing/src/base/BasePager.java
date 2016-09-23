package base;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.szcx.wisdomofbeijing.MainActivity;
import com.szcx.wisdomofbeijing.R;

/**
 * 五个标签类的基础类
 * @author System_ivy
 *
 */
public class BasePager {
	
	public Activity mActivity;
	public TextView tTitle;
	public ImageButton btn_menu;
	public FrameLayout fl_content;
	public View rootView;  //当前页面的布局对象
	public ImageButton btn_photo;
	
	public BasePager(Activity activity) {
		mActivity = activity;
		rootView = initViewPager();
	}
	
	public View initViewPager(){
		View view = View.inflate(mActivity, R.layout.fragement_base_pager, null);
		tTitle = (TextView) view.findViewById(R.id.tv_title);
		btn_menu = (ImageButton) view.findViewById(R.id.ibtn_item);
		fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
		btn_photo = (ImageButton) view.findViewById(R.id.ib_switch_lv);
		btn_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pickUp();
			}
		});
		return view;
	}
	
	//打开或关闭侧边栏
	private void pickUp(){
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//如果当前状态时关闭，调用之后是打开，反之亦然
	}	
	
	public void initDatePager(){}
}
