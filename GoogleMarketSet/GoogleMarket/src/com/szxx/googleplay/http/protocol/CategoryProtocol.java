package com.szxx.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.szxx.googleplay.domain.CategoryInfo;


public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryInfo>> {

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public String getKey() {
		return "category";
	}

	@Override
	public ArrayList<CategoryInfo> parseData(String result) {
		ArrayList<CategoryInfo> categoryList = new ArrayList<CategoryInfo>();
		
		try {
			JSONArray ja = new JSONArray(result);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject json = ja.getJSONObject(i);
			
				if (json.has("title")) {
					CategoryInfo info = new CategoryInfo();
					info.title = json.getString("title");
					info.isTitle = true;
					categoryList.add(info);
				}
				
				if (json.has("infos")) {
					JSONArray ja2 = json.getJSONArray("infos");
					for (int j = 0; j < ja2.length(); j++) {
						JSONObject json2 = ja2.getJSONObject(j);
						CategoryInfo info2 = new CategoryInfo();
						info2.name1 = json2.getString("name1");
						info2.name2 = json2.getString("name2");
						info2.name3 = json2.getString("name3");
						info2.url1 = json2.getString("url1");
						info2.url2 = json2.getString("url2");
						info2.url3 = json2.getString("url3");
						info2.isTitle = false;
						categoryList.add(info2);
					}
				}
			}
			return categoryList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
