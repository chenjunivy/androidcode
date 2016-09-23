package com.szxx.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.szxx.googleplay.domain.AppInfos;
import com.szxx.googleplay.domain.AppInfos.SafeInfo;

public class HomeDetailProtocol extends BaseProtocol<AppInfos> {
	public String packageName;
	public HomeDetailProtocol(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String getParams() {
		return "&packageName=" + packageName;
	}

	@Override
	public String getKey() {
		return "detail";
	}

	@Override
	public AppInfos parseData(String result) {
		try {
			JSONObject jo = new JSONObject(result);
			AppInfos info = new AppInfos();
			info.des = jo.getString("des");
			info.downloadUrl = jo.getString("downloadUrl");
			info.iconUrl = jo.getString("iconUrl");
			info.id = jo.getString("id");
			info.name = jo.getString("name");
			info.packageName = jo.getString("packageName");
			info.size = jo.getLong("size");
			info.stars = (float) jo.getDouble("stars");
			
			info.author = jo.getString("author");
			info.date = jo.getString("date");
			info.downloadNum = jo.getString("downloadNum");
			info.version = jo.getString("version");
			
			//解析安全信息
			ArrayList<SafeInfo> safeList = new ArrayList<AppInfos.SafeInfo>();
			JSONArray ja = jo.getJSONArray("safe");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject json1 = ja.getJSONObject(i);
				SafeInfo safe = new AppInfos.SafeInfo();
				safe.safeDes = json1.getString("safeDes");
				safe.safeDesColor = json1.getString("safeDesColor");
				safe.safeDesUrl = json1.getString("safeDesUrl");
				safe.safeUrl = json1.getString("safeUrl");
				safeList.add(safe);
			}
			info.safe = safeList;
			
			//解析截图信息
			ArrayList<String> screenList = new ArrayList<String>();
			JSONArray ja1 = jo.getJSONArray("screen");
			for (int j = 0; j < ja1.length(); j++) {
				String pic = ja1.getString(j);
				screenList.add(pic);
			}
			info.screen = screenList;
			
			return info;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
