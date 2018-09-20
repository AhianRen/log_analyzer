package com.nt.log_analyzer.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;



import com.nt.log_analyzer.model.FileModel;
import com.nt.log_analyzer.model.LogModel;

public class FileUtil {
	
	
	/**
	 * 根据logModelFieldNameArray给logModel属性赋值
	 * @param logModel
	 * @param matcher
	 * @param logModelFieldNameArray
	 * @param dateFormat
	 * @throws Exception
	 */
	public static void setLogModel(LogModel logModel,Matcher matcher, String[] logModelFieldNameArray, String dateFormat) throws Exception{
		Class clazz = logModel.getClass();
		
		for(int j = 1;j<=matcher.groupCount();j++ ) {
			String fieldValue = matcher.group(j);
			
			Field field = clazz.getDeclaredField(logModelFieldNameArray[j-1]);
			field.setAccessible(true);
			String typeName = field.getType().getSimpleName();
			
			if ("Date".equals(typeName)) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
				Date date = simpleDateFormat.parse(fieldValue);
				
				field.set(logModel, date);
				continue;
			}
			
			if ("long".equals(typeName) || "Long".equals(typeName)) {
				long l = Long.parseLong(fieldValue);
				field.set(logModel, l);
				continue;
			}
			if ("int".equals(typeName) || "Integer".equals(typeName)) {
				int z = Integer.parseInt(fieldValue);
				field.set(logModel, z);
				continue;
			}
			field.set(logModel, fieldValue);
		}
	}
	
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
	 * 获取fileModels中所有不存在的文件的id
	 * @param fileModels
	 * @return
	 */
	public static List<Integer> getBeDeletedFileIds(List<FileModel> fileModels){
		List<Integer> ids = new ArrayList<>();
		for (FileModel fileModel : fileModels) {
			File file = new File(fileModel.getFileAbsolutePath());
			if (!file.exists()) {
				ids.add(fileModel.getFileId());
			}
		}
		return ids;
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
