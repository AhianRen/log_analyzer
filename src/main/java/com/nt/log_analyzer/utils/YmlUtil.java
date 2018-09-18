package com.nt.log_analyzer.utils;

public class YmlUtil {
	
	
	public static String ymlArrayToString(String[] array) {
		String str = "";
		for (String s : array) {
			str += (s+","); 
		}
		return str.substring(0, str.length()-1);
	}
	
}
