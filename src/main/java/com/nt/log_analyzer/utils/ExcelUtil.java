package com.nt.log_analyzer.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

public class ExcelUtil {

	public static <E> HSSFWorkbook createExcel(String sheetName, List<E> data, E e, String datePattern)
			throws Exception {

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);
		
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);

		Class clazz = e.getClass();
		Field[] fields = clazz.getDeclaredFields();
		
		HSSFCell cell = null;
		HSSFRow row = sheet.createRow(0);
		for(int i =0;i<fields.length;i++) {
			fields[i].setAccessible(true);

			cell = row.createCell(i);
			cell.setCellValue(fields[i].getName());
			cell.setCellStyle(style);
		}
		
		
		for (int i = 0; i < data.size(); i++) {
			row = sheet.createRow(i + 1);
			
			for (int j = 0; j < fields.length; j++) {
				Field field = fields[j];
				field.setAccessible(true);
				
				
				E e2 = data.get(i);
				Class clazz2 = e2.getClass();
				Field field2 = clazz2.getDeclaredField(field.getName());
				field2.setAccessible(true);
				
				if (field2.get(e2) == null) {
					continue;
				}
			
				String typeName = field.getType().getSimpleName();
				String value = null;
				
				switch (typeName) {
				case "Date":
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
					value = simpleDateFormat.format(field2.get(e2));
					break;
				case "int":
				case "long":
				case "double":
				case "float":  //**********
					value = field2.get(e2) + "";
					break;
				default:
					value = field2.get(e2).toString();
					break;
				}
				
				row.createCell(j).setCellValue(value);

			}
		}

		return wb;
	}

}
