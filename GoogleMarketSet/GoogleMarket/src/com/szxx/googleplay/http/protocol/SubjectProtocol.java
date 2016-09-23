package com.szxx.googleplay.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.szxx.googleplay.domain.SubjectInfo;

/**
 * 专题网络请求
 * @author SystemIvy
 *
 */

public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectInfo>> {

	@Override
	public String getParams() {
		
		return "";
	}

	@Override
	public String getKey() {
		
		return "subject";
	}

	@Override
	public ArrayList<SubjectInfo> parseData(String result) {
		ArrayList<SubjectInfo> subjectList = new ArrayList<SubjectInfo>();
		try {
			JSONArray ja = new JSONArray(result);
			for (int i = 0; i < ja.length(); i++) {
				JSONObject json = ja.getJSONObject(i);
				
				SubjectInfo info = new SubjectInfo();
				info.url = json.getString("url");
				info.des = json.getString("des");
				
				subjectList.add(info);
			}
			return subjectList;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
