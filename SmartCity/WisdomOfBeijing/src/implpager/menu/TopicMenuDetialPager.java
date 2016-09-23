package implpager.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import base.BaseMenuDetialPager;
/**
 * 菜单详情页，主题
 * @author SystemIvy
 *
 */
public class TopicMenuDetialPager extends BaseMenuDetialPager {

	public TopicMenuDetialPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		//为framelayout填充布局对象
		TextView view = new TextView(m2Activity);
		view.setText("菜单详情页，主题");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		return view;
	}

}
