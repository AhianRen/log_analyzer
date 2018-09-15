package com.nt.log_analyzer.utils;


import com.alibaba.fastjson.JSONArray;

public class JsonUtil {
	
	public static String[] jsonStrToStringArray(String str){
		
		JSONArray parseArray = JSONArray.parseArray(str);
		String[] array = new String[parseArray.size()];
		for(int i = 0;i<parseArray.size();i++) {
			array[i] = parseArray.get(i).toString();
		}
		
		return array;
		
	}
	
	
}
