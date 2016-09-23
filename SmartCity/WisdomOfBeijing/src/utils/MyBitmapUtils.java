package utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.szcx.wisdomofbeijing.R;

/**
 * �Զ�����������ͼƬ���ع���
 * @author SystemIvy
 *
 */
public class MyBitmapUtils {
	private NetCacheUtils mNetCacheUtils;
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	public MyBitmapUtils() {
		mMemoryCacheUtils = new MemoryCacheUtils();
		mLocalCacheUtils = new LocalCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
	}
	
	public void display(ImageView iv, String url){
		//���ȴ��ڴ��м���ͼƬ���ٶȿ죬����������
		Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(url);
		if (bitmap != null) {
			iv.setImageBitmap(bitmap);
			System.out.println("���ڴ��м���ͼƬ");
			return;
		}
		
		//��δӱ��أ�sdcard�����ٶȿ죬��������
		bitmap = mLocalCacheUtils.getLocalCache(url);
		if (bitmap != null) {
			iv.setImageBitmap(bitmap);
			System.out.println("�ӱ��ػ������ͼƬ");
			
			//�����ڴ滺��
			mMemoryCacheUtils.setMemoryCache(url, bitmap);
			return;
		}
		
		//���������أ��ٶ�����������
		iv.setImageResource(R.drawable.pic_item_list_default);
		mNetCacheUtils.getBitmapFromNet(iv, url);
	}
}
