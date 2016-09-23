package utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.PublicKey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NetCacheUtils {

	private HttpURLConnection conn;
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;
	
	public NetCacheUtils(LocalCacheUtils mLocalCacheUtils, MemoryCacheUtils mMemoryCacheUtils) {
		this.mLocalCacheUtils = mLocalCacheUtils;
		this.mMemoryCacheUtils = mMemoryCacheUtils;
	}
	

	public void getBitmapFromNet(ImageView iv, String url) {
		// AsyncTask 异步封装的工具，可以实现异步请求及更新主界面（对线程池+handler的封装）
		new BitmapTask().execute(iv, url);
	}

	/**
	 * 三个泛型参数意义，第一个泛型，doInBackground 的参数类型 第二个泛型：onProgressUpdate 的参数类型
	 * 第三个泛型：doInBackground 的返回值类型，onPostExecute参数类型
	 * 
	 * @author System_ivy
	 * 
	 */
	class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {

		private ImageView imageView;
		private String url;

		// 预加载，运行在主线程
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			System.out.println("onPreExecute");
		}

		// 正在加载，运行在子线程，可直接异步请求
		@Override
		protected Bitmap doInBackground(Object... params) {
			System.out.println("doInBackground");
			imageView = (ImageView) params[0];
			url = (String) params[1];
			imageView.setTag(url);
			
			Bitmap bitmap = downloadBitmap(url);
//			publishProgress(); //会回调onProgressUpdate
			return bitmap;
			
		}

		// 更新进度的方法，运行在主线程
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		// 加载结束，运行在主线程
		@Override
		protected void onPostExecute(Bitmap result) {
			System.out.println("onPostExecute");
			if (result != null) {
				//由于listvview 的重用重用机制，导致imageview对象可能被多个item重用，从而导致将错误的图片设置给了imageview对象
				
				//检测图片是否是所需的图片
				String url = (String) imageView.getTag();
				if (url.equals(this.url)) {
					imageView.setImageBitmap(result);
					System.out.println("从网络下载图片");
					//设置本地缓存
					mLocalCacheUtils.setLocalCache(url, result);
					//设置内存缓存
					mMemoryCacheUtils.setMemoryCache(url, result);
				}
			}
			super.onPostExecute(result);
		}

	}

	public Bitmap downloadBitmap(String url) {
		
		//获取urlconnection
		URL mUrl;
		try {
			mUrl = new URL(url);
			conn = (HttpURLConnection) mUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			conn.setConnectTimeout(5000);
			conn.connect();
			
			int respondCode = conn.getResponseCode();
			if (respondCode == 200) {
				InputStream input = conn.getInputStream();
				
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				return bitmap;
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		return null;
	}

}
