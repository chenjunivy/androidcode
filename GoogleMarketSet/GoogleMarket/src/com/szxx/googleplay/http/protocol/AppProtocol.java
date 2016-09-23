package com.szxx.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.szxx.googleplay.domain.AppInfos;

/**
 * 应用网络请求
 * @author SystemIvy
 *
 */

public class AppProtocol extends BaseProtocol<ArrayList<AppInfos>> {

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public String getKey() {
		return "app";
	}

	@Override
	public ArrayList<AppInfos> parseData(String result) {
		//框架Gson 解析，本例子中使用自定义解析方式
		
		//列表项解析数据
		ArrayList<AppInfos> appList = new ArrayList<AppInfos>();
	try {
		JSONArray jo = new JSONArray(result);
		for (int i = 0; i < jo.length(); i++) {
			JSONObject json = jo.getJSONObject(i);
			
			AppInfos info = new AppInfos();
			info.des = json.getString("des");
			info.downloadUrl = json.getString("downloadUrl");
			info.iconUrl = json.getString("iconUrl");
			info.id = json.getString("id");
			info.name = json.getString("name");
			info.packageName = json.getString("packageName");
			info.size = json.getLong("size");
			info.stars = (float) json.getDouble("stars");
			appList.add(info);
			
		}
		
		return appList;
	} catch (JSONException e) {
		e.printStackTrace();
	}
	return null;
	}

}
