package implpager.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import base.BaseMenuDetialPager;
/**
 * �˵�����ҳ������
 * @author SystemIvy
 *
 */
public class TopicMenuDetialPager extends BaseMenuDetialPager {

	public TopicMenuDetialPager(Activity activity) {
		super(activity);
	}

	@Override
	public View initView() {
		//Ϊframelayout��䲼�ֶ���
		TextView view = new TextView(m2Activity);
		view.setText("�˵�����ҳ������");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		return view;
	}

}
