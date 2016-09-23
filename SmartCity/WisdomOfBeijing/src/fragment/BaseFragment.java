package fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	//Fragment创建时调用
	public  Activity attachActivity;
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//获取当前activity
		attachActivity = getActivity();
	}
	//Fragment创建view时调用
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = initView();
		return view;
	}
	//Fragemnt依赖的activity 的onCreate() 方法结束后调用该方法
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	
	//初始化视图组件,由子类实现
	public abstract View initView();
	//初始化数据,由子类实现
	public abstract void initData();
}
