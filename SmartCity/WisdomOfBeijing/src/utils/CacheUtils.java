package utils;

import android.content.Context;

/**
 * 缓存工具类
 * @author SystemIvy
 *
 */
public class CacheUtils {
	/**
	 * 
	 * @param url  以url为key,
	 * @param json  以json 为value,保存在本地
	 * @param context
	 */
	public static void setCacheContent(String url, String json, Context context){
		PrefUtils.setString(context, url, json);
		//同理可以使用文件缓存，url为文件名，json为文件内容
	}
	
	/**
	 * 获取本地缓存的json数据
	 * @param url
	 * @param json
	 * @param context
	 * @return
	 */
	public static String getCacheContent(String url, Context context){
		return PrefUtils.getString(context, url, null);
		//同理可以使用文件缓存，url为文件名，json为文件内容
	}
}
