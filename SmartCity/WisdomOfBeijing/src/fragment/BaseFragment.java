package fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	//Fragment����ʱ����
	public  Activity attachActivity;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//��ȡ��ǰactivity
		attachActivity = getActivity();
	}
	//Fragment����viewʱ����
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = initView();
		return view;
	}
	//Fragemnt������activity ��onCreate() ������������ø÷���
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	
	//��ʼ����ͼ���,������ʵ��
	public abstract View initView();
	//��ʼ������,������ʵ��
	public abstract void initData();
}
