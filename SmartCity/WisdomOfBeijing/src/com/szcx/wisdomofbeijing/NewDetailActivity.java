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
		ViewUtils.inject(this);//ʹ��XUtils��������ע��,���findViewById
		
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
		wv_setting.setBuiltInZoomControls(true); //��ʾ���Ű�ť
		wv_setting.setUseWideViewPort(true); //֧��˫������
		wv_setting.setJavaScriptEnabled(true); //֧��javascript����
		
		wv_news.setWebViewClient(new WebViewClient(){
		
			//��ʼ������ҳ
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				System.out.println("��ʼ������ҳ");
				pb_bar.setVisibility(View.VISIBLE);
			}
			
			//��ҳ���ؽ���
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				System.out.println("��ҳ���ؽ���");
				pb_bar.setVisibility(View.INVISIBLE);
				System.out.println(url);
			}
			
			//����������ת����ø÷���
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println("��תҳ��ʱ��" + url);
				view.loadUrl(url);//��ת����ʱǿ���ڵ�ǰҳ���м���
				return true;
			}
			
		});
		
//		wv_news.goBack();//��ת��һ��ҳ��
//		wv_news.goForward();//��ת��һ��ҳ��
		
		wv_news.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				System.out.println("���ȣ�" + newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				System.out.println("���ű��⣺" + title);
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
	
	private int mTempItem;//��¼��ʱ����ѡ���С
	private int mCurrentItem = 2;//��ǰѡ�е����壬Ĭ������������
	private WebSettings wv_setting;
	private void showChooseDialog(){
		String[] fontItems = {"���������", "�������", "��������", "С������", "��С������"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("�����С����");
		builder.setSingleChoiceItems(fontItems, mCurrentItem, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mTempItem = which;
			}
		});
		
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (mTempItem) {
				case 0:
					//��������
//					wv_setting.setTextSize(TextSize.LARGER);
					wv_setting.setTextZoom(180);
					break;
				case 1:
					//�������
					wv_setting.setTextZoom(140);
					break;
				case 2:
					//��������
					wv_setting.setTextZoom(110);
					break;
				case 3:
					//С������
					wv_setting.setTextZoom(80);
					break;
				case 4:
					//��С������
					wv_setting.setTextZoom(50);
					break;

				default:
					break;
				}
				mCurrentItem = mTempItem;
			}
		});
		
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}
}
