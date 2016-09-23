package utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCacheUtils {
//	private HashMap<String, SoftReference<Bitmap>> mMemoryCache = new HashMap<String, SoftReference<Bitmap>>();
	
	//���ڴ� Android 2.3 (API Level 9)��ʼ��������������������ڻ��ճ��������û������õĶ������������ú������ñ�ò��ٿɿ���
	//�Ƽ�ʹ��LruCache,�������ʹ�õĶ����ڻ���ֵ�ﵽԤ�趨ֵ֮ǰ���ڴ����ƣ�least recent used
	private LruCache<String, Bitmap> mMemoryCache ;
	
	public MemoryCacheUtils(){
		long maxMemory = Runtime.getRuntime().maxMemory();
		System.out.println("maxMemory is :" + maxMemory);
		
		mMemoryCache = new LruCache<String, Bitmap>((int) (maxMemory/8)){
			//����ÿ������Ĵ�С
			@Override
			protected int sizeOf(String key, Bitmap value) {
				int byteCount = value.getByteCount(); //����ͼƬ��С
				return byteCount;
			}
		};
	}
	
	//д�ڴ滺��
	public void setMemoryCache(String url, Bitmap bitmap){
		/*SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap); //��bitmap��װ��������
		mMemoryCache.put(url, soft);*/
		mMemoryCache.put(url, bitmap);
		
	}
	
	//���ڴ滺��
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
