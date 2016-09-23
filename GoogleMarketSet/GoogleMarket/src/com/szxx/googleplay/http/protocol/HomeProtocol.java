package com.szxx.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.szxx.googleplay.domain.AppInfos;

/**
 * 首页面解析json数据
 * @author SystemIvy
 *
 */

public class HomeProtocol extends BaseProtocol<ArrayList<AppInfos>> {

	private ArrayList<String> imgname;

	@Override
	public String getParams() {
		
		return "";//如果没有参数，就传空串，不传null
	}

	@Override
	public String getKey() {
		
		return "home"; 
	}

	@Override
	public ArrayList<AppInfos> parseData(String result) {
		//框架Gson 解析，本例子中使用自定义解析方式
		
			//列表项解析数据
			ArrayList<AppInfos> home_appList = new ArrayList<AppInfos>();
		try {
			JSONObject jo = new JSONObject(result);
			JSONArray ja = jo.getJSONArray("list");
			for (int i = 0; i < ja.length(); i++) {
				JSONObject json = ja.getJSONObject(i);
				
				AppInfos info = new AppInfos();
				info.des = json.getString("des");
				info.downloadUrl = json.getString("downloadUrl");
				info.iconUrl = json.getString("iconUrl");
				info.id = json.getString("id");
				info.name = json.getString("name");
				info.packageName = json.getString("packageName");
				info.size = json.getLong("size");
				info.stars = (float) json.getDouble("stars");
				home_appList.add(info);
				
			}
			imgname = new ArrayList<String>();
			JSONArray ja1 = jo.getJSONArray("picture");
			for (int i = 0; i < ja1.length(); i++) {
				String pic = ja1.getString(i);
				imgname.add(pic);
			}
			
			return home_appList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> getPictureList(){
		return imgname;
	}
 
}
