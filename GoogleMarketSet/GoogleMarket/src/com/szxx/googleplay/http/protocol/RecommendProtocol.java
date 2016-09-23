package com.szxx.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.szxx.googleplay.domain.AppInfos;

/**
 * 推荐网络请求
 * @author SystemIvy
 *
 */

public class RecommendProtocol extends BaseProtocol<ArrayList<String>> {

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public String getKey() {
		return "recommend";
	}

	@Override
	public ArrayList<String> parseData(String result) {
		//框架Gson 解析，本例子中使用自定义解析方式
		
		//列表项解析数据
		ArrayList<String> recomList = new ArrayList<String>();
	try {
		JSONArray jo = new JSONArray(result);
		for (int i = 0; i < jo.length(); i++) {
			String name = jo.getString(i);
			recomList.add(name);
		}
		
		return recomList;
	} catch (JSONException e) {
		e.printStackTrace();
	}
	return null;
	}

}
