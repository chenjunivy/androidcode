package implpager.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import base.BaseMenuDetialPager;
/**
 * 菜单详情页，互动
 * @author SystemIvy
 *
 */
public class InteractMenuDetialPager extends BaseMenuDetialPager {

	public InteractMenuDetialPager(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		//为framelayout填充布局对象
		TextView view = new TextView(m2Activity);
		view.setText("菜单详情页，互动");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		return view;
	}

}
