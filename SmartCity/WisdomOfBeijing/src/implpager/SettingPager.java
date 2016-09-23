package implpager;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import base.BasePager;

public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initDatePager() {
		super.initDatePager();
		//Ϊframelayout��䲼�ֶ���
		TextView view = new TextView(mActivity);
		view.setText("��������");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);
		fl_content.addView(view);
		//�޸�ҳ�����
		tTitle.setText("����");
		//����ImageButton ����
		btn_menu.setVisibility(View.INVISIBLE);
	}
}
