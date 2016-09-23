package com.szcx.wisdomofbeijing;

import implpager.menu.TabDetailPager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NewDetailActivity extends Activity implements OnClickListener {

	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.ibtn_item)
	private ImageButton btn_item;
	@ViewInject(R.id.ibtn_back)
	private ImageButton btn_back;
	@ViewInject(R.id.ll_control)
	private LinearLayout ll_ctrl;
	@ViewInject(R.id.ibtn_textsize)
	private ImageButton btn_textsize;
	@ViewInject(R.id.ibtn_share)
	private ImageButton btn_share;
	@ViewInject(R.id.wv_news_detail)
	private WebView wv_news;
	@ViewInject(R.id.probar_refresh)
	private ProgressBar pb_bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		ViewUtils.inject(this);//使用XUtils工具依赖注入,替代findViewById
		
		ll_ctrl.setVisibility(View.VISIBLE);
		btn_back.setVisibility(View.VISIBLE);
		btn_item.setVisibility(View.GONE);
		btn_textsize.setVisibility(View.VISIBLE);
		btn_share.setVisibility(View.VISIBLE);
		
		btn_textsize.setOnClickListener(this);
		btn_share.setOnClickListener(this);
		btn_back.setOnClickListener(this);
				
		String mURL = getIntent().getStringExtra("murl");
		String mURL2 = mURL.replace("10.0.2.2", "192.168.56.1");
		
		//wv_news.loadUrl("http://www.baidu.com");
		//wv_news.loadUrl("http://www.imooc.com");
		//wv_news.loadUrl("http://www.imooc.com");
		wv_news.loadUrl(mURL2);
		
		wv_setting = wv_news.getSettings();
		wv_setting.setBuiltInZoomControls(true); //显示缩放按钮
		wv_setting.setUseWideViewPort(true); //支持双击缩放
		wv_setting.setJavaScriptEnabled(true); //支持javascript功能
		
		wv_news.setWebViewClient(new WebViewClient(){
		
			//开始加载网页
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				System.out.println("开始加载网页");
				pb_bar.setVisibility(View.VISIBLE);
			}
			
			//网页加载结束
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("网页加载结束");
				pb_bar.setVisibility(View.INVISIBLE);
				System.out.println(url);
			}
			
			//所有链接跳转会调用该方法
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("跳转页面时：" + url);
				view.loadUrl(url);//跳转链接时强制在当前页面中加载
				return true;
			}
			
		});
		
//		wv_news.goBack();//跳转上一个页面
//		wv_news.goForward();//跳转下一个页面
		
		wv_news.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				System.out.println("进度：" + newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				System.out.println("新闻标题：" + title);
			}
			
		});
				
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ibtn_back:
			finish();
			break;
		case R.id.ibtn_textsize:
			showChooseDialog();
			break;
		case R.id.ibtn_share:
			
			break;


		default:
			break;
		}
	}
	
	private int mTempItem;//记录临时字体选项大小
	private int mCurrentItem = 2;//当前选中的字体，默认是正常字体
	private WebSettings wv_setting;
	private void showChooseDialog(){
		String[] fontItems = {"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("字体大小设置");
		builder.setSingleChoiceItems(fontItems, mCurrentItem, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTempItem = which;
			}
		});
		
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (mTempItem) {
				case 0:
					//超大字体
//					wv_setting.setTextSize(TextSize.LARGER);
					wv_setting.setTextZoom(180);
					break;
				case 1:
					//大号字体
					wv_setting.setTextZoom(140);
					break;
				case 2:
					//正常字体
					wv_setting.setTextZoom(110);
					break;
				case 3:
					//小号字体
					wv_setting.setTextZoom(80);
					break;
				case 4:
					//超小号字体
					wv_setting.setTextZoom(50);
					break;

				default:
					break;
				}
				mCurrentItem = mTempItem;
			}
		});
		
		builder.setNegativeButton("取消", null);
		builder.show();
	}
}
