package utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCacheUtils {
//	private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<String, SoftReference<Bitmap>>();
	
	//由于从 Android 2.3 (API Level 9)开始，垃圾回收器会更倾向于回收持有软引用或弱引用的对象，这让软引用和弱引用变得不再可靠。
	//推荐使用LruCache,最近最少使用的对象在缓存值达到预设定值之前从内存中移，least recent used
	private LruCache<String, Bitmap> mMemoryCache ;
	
	public MemoryCacheUtils(){
		long maxMemory = Runtime.getRuntime().maxMemory();
		System.out.println("maxMemory is :" + maxMemory);
		
		mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory/8)){
			//返回每个对象的大小
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int byteCount = value.getByteCount(); //计算图片大小
				return byteCount;
			}
		};
	}
	
	//写内存缓存
	public void setMemoryCache(String url, Bitmap bitmap){
		/*SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap); //将bitmap封装成软引用
		mMemoryCache.put(url, soft);*/
		mMemoryCache.put(url, bitmap);
		
	}
	
	//读内存缓存
	public Bitmap getMemoryCache(String url){
		/*SoftReference<Bitmap> softBitmap = mMemoryCache.get(url);
		if (softBitmap != null) {
			Bitmap bitmap = softBitmap.get();
			return bitmap;
		}
		return null;*/
		
		return mMemoryCache.get(url);
	}
}
