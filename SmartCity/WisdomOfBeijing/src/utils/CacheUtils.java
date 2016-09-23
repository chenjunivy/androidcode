package utils;

import android.content.Context;

/**
 * ���湤����
 * @author SystemIvy
 *
 */
public class CacheUtils {
	/**
	 * 
	 * @param url  ��urlΪkey,
	 * @param json  ��json Ϊvalue,�����ڱ���
	 * @param context
	 */
	public static void setCacheContent(String url, String json, Context context){
		PrefUtils.setString(context, url, json);
		//ͬ�����ʹ���ļ����棬urlΪ�ļ�����jsonΪ�ļ�����
	}
	
	/**
	 * ��ȡ���ػ����json����
	 * @param url
	 * @param json
	 * @param context
	 * @return
	 */
	public static String getCacheContent(String url, Context context){
		return PrefUtils.getString(context, url, null);
		//ͬ�����ʹ���ļ����棬urlΪ�ļ�����jsonΪ�ļ�����
	}
}
