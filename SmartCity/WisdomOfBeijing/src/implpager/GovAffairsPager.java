package implpager;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import base.BasePager;

public class GovAffairsPager extends BasePager {

	public GovAffairsPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initDatePager() {
		super.initDatePager();
		//为framelayout填充布局对象
		TextView view = new TextView(mActivity);
		view.setText("时政要闻");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		fl_content.addView(view);
		//修改页面标题
		tTitle.setText("时政");
		//设置ImageButton 显示
		btn_menu.setVisibility(View.VISIBLE);
	}
}
