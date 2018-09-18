package com.nt.log_analyzer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {
	
	
	/**
	 * 字符串中是否包含过滤词
	 * @param str 要过滤的字符串
	 * @param filteWorlds 过滤词数组
	 * @return
	 */
	public static boolean logFilter(String str,String[] filteWorlds) {
		for (String s : filteWorlds) {
			if(str.contains(s)) {
				return true;
			}
		}
		return false;
	}
	
	
	
	/**
	 * 获取文件总行数
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Integer getFileRowNumber(File file){
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			int i = 0;
			while(br.readLine()!=null) {
				i++;
			}
			return new Integer(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				isr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
}
