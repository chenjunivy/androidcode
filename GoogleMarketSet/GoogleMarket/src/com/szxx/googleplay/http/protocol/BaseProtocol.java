package com.szxx.googleplay.http.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.szxx.googleplay.http.HttpHelper;
import com.szxx.googleplay.http.HttpHelper.HttpResult;
import com.szxx.googleplay.uiutils.IOUtils;
import com.szxx.googleplay.uiutils.MD5Encoder;
import com.szxx.googleplay.uiutils.StringUtils;
import com.szxx.googleplay.uiutils.UIUtils;

/**
 * 访问网络的基本类
 * @author SystemIvy
 *
 */
public abstract class BaseProtocol<T> {
	
	//index表示从哪个位置开始返回20条数据，用于分页
	public T getData(int index){
		//先判断是否有缓存，优先从缓存中加载数据
		String result = getCache(index);
		
		if (StringUtils.isEmpty(result)) {//如果缓存超时或者过期，从服务器加载数据
			result = getDataFromServer(index);
		}
		
		if (result != null) {
			T data = parseData(result);
			return data;
		}
		return null;
	}

	//从网络中加载数据
	//index表示从哪个位置返回20条数据，用于分页
	private String getDataFromServer(int index) {
		HttpResult httpResult = HttpHelper.get(HttpHelper.URL + getKey() + "?index=" + index + getParams());
		
		if (httpResult != null) {
			String result = httpResult.getString();
			System.out.println("访问结果是  : " + result);
			
			//写缓存
			if (!StringUtils.isEmpty(result)) {
				setCache(index, result);
			}
			return result;
		}
		return null;
	}
	
	//设置缓存
	//url 为文件名，json为文件内容
	private void setCache(int index, String json){
		File cacheDir = UIUtils.getUIContext().getCacheDir(); //获取应用的缓存文件夹
		FileWriter writer = null;
		try {
			File cacheFile = new File(cacheDir, MD5Encoder.encoder(HttpHelper.URL + getKey() + "?index=" + index + getParams()));
			writer = new FileWriter(cacheFile);
			
			//设置缓存失效时间
			long cacheTime = System.currentTimeMillis() + 30*60*1000; //有效时间设置为半小时
			writer.write(cacheTime + "\n");  //第一行写入缓存时间，换行
			
			writer.write(json); //写入json数据
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			IOUtils.close(writer);
		}
	}
	
	//获取缓存
	private String getCache(int index){
		File cacheDir = UIUtils.getUIContext().getCacheDir(); //获取应用的缓存文件夹
		BufferedReader reader = null;
		try {
			File cacheFile = new File(cacheDir, MD5Encoder.encoder(HttpHelper.URL + getKey() + "?index=" + index + getParams()));
			if (cacheFile.exists()) {//判断缓存是否存在
				reader = new BufferedReader(new FileReader(cacheFile));
				
				String headline = reader.readLine();
				long overtime = Long.parseLong(headline);
				if (System.currentTimeMillis() < overtime) {//如果尚在缓存有效时间内
					StringBuffer buffer = new StringBuffer();
					String line;
					while((line = reader.readLine()) != null){
						buffer.append(line);
					}
					return buffer.toString();
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			IOUtils.close(reader);
		}
		return null;
	}

	//获取网络连接关键词，子类必须实现
	public abstract String getParams();

	//获取网络连接参数，子类必须实现
	public abstract String getKey();
	
	//解析json数据
	public abstract T parseData(String result);
}
