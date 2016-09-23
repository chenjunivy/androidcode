package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class LocalCacheUtils {
	public static final String LOACL_CACCHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhbj_cache";
	
	
	//写本地缓存
	public void setLocalCache(String url, Bitmap bitmap){
		File dir = new File(LOACL_CACCHE_PATH);
		if (!dir.exists() || dir.isDirectory() ) {
			dir.mkdirs(); //创建文件夹
		}
		String md5Url;
		try {
			//MD5加密url
			md5Url = MD5Encoder.encoder(url);
			//创建缓存文件
			File cacheFile = new File(dir, md5Url);
			//压缩图片（图片格式，压缩比， 输出流）
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(cacheFile));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//读本地缓存
	public Bitmap getLocalCache(String url){
		try {
			File cacheFile = new File(LOACL_CACCHE_PATH, MD5Encoder.encoder(url));
			if (cacheFile.exists()) {
				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
				return bitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
