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
		// AsyncTask �첽��װ�Ĺ��ߣ�����ʵ���첽���󼰸��������棨���̳߳�+handler�ķ�װ��
		new BitmapTask().execute(iv, url);
	}

	/**
	 * �������Ͳ������壬��һ�����ͣ�doInBackground �Ĳ������� �ڶ������ͣ�onProgressUpdate �Ĳ�������
	 * ���������ͣ�doInBackground �ķ���ֵ���ͣ�onPostExecute��������
	 * 
	 * @author System_ivy
	 * 
	 */
	class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {

		private ImageView imageView;
		private String url;

		// Ԥ���أ����������߳�
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			System.out.println("onPreExecute");
		}

		// ���ڼ��أ����������̣߳���ֱ���첽����
		@Override
		protected Bitmap doInBackground(Object... params) {
			System.out.println("doInBackground");
			imageView = (ImageView) params[0];
			url = (String) params[1];
			imageView.setTag(url);
			
			Bitmap bitmap = downloadBitmap(url);
//			publishProgress(); //��ص�onProgressUpdate
			return bitmap;
			
		}

		// ���½��ȵķ��������������߳�
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		// ���ؽ��������������߳�
		@Override
		protected void onPostExecute(Bitmap result) {
			System.out.println("onPostExecute");
			if (result != null) {
				//����listvview ���������û��ƣ�����imageview������ܱ����item���ã��Ӷ����½������ͼƬ���ø���imageview����
				
				//���ͼƬ�Ƿ��������ͼƬ
				String url = (String) imageView.getTag();
				if (url.equals(this.url)) {
					imageView.setImageBitmap(result);
					System.out.println("����������ͼƬ");
					//���ñ��ػ���
					mLocalCacheUtils.setLocalCache(url, result);
					//�����ڴ滺��
					mMemoryCacheUtils.setMemoryCache(url, result);
				}
			}
			super.onPostExecute(result);
		}

	}

	public Bitmap downloadBitmap(String url) {
		
		//��ȡurlconnection
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
