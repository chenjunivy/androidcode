package utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.szcx.wisdomofbeijing.R;

/**
 * 自定义三级缓存图片加载工具
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
		//优先从内存中加载图片，速度快，无流量消耗
		Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(url);
		if (bitmap != null) {
			iv.setImageBitmap(bitmap);
			System.out.println("从内存中加载图片");
			return;
		}
		
		//其次从本地（sdcard），速度快，缓存量大
		bitmap = mLocalCacheUtils.getLocalCache(url);
		if (bitmap != null) {
			iv.setImageBitmap(bitmap);
			System.out.println("从本地缓存加载图片");
			
			//设置内存缓存
			mMemoryCacheUtils.setMemoryCache(url, bitmap);
			return;
		}
		
		//从网络下载，速度慢，耗流量
		iv.setImageResource(R.drawable.pic_item_list_default);
		mNetCacheUtils.getBitmapFromNet(iv, url);
	}
}
