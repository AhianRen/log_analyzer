package com.nt.log_analyzer.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nt.log_analyzer.dao.LogModelDao;
import com.nt.log_analyzer.model.LogModel;
import com.nt.log_analyzer.service.IndexService;
import com.nt.log_analyzer.service.LogModelService;
import com.nt.log_analyzer.utils.ExcelUtil;
import com.nt.log_analyzer.utils.PdfUtil;

@Controller
public class LogModelController {
	
	@Autowired
	private LogModelService logModelService;
	
	@RequestMapping("/log/home")
	@ResponseBody
	public Map<String, Object> home(@RequestParam("rows") int rows,@RequestParam("page") int page,
			String timeStamp_from,String timeStamp_to,String threadName,String priority,String className,String message,String fileName,String relatedType,String queryType) throws Exception {
		System.out.println(rows);
	
		if (!"and".equals(relatedType)&&!"or".equals(relatedType)) {
			relatedType = "and";
		}
		
		
		if ("yyyy-MM-dd HH:mm:ss".equals(timeStamp_from)) {
			timeStamp_from = null;
		}
		if ("yyyy-MM-dd HH:mm:ss".equals(timeStamp_to)) {
			timeStamp_to = null;
		}
		//int pageNum = Integer.parseInt(page);
		//int size = Integer.parseInt(rows);
		//int startRow = size*(pageNum-1);
		

		Map<String, Object> map = logModelService.getResultByCondition(fileName, timeStamp_from, timeStamp_to, threadName, className, priority, message, page, rows, relatedType, queryType);
	
		return map;
	}
	
	@RequestMapping("/queryCount")
	@ResponseBody
	public int getQueryCount(String timeStamp_from,String timeStamp_to,String threadName,String priority,String className,String message,String fileName,String relatedType,String queryType) throws Exception{
		if (!"and".equals(relatedType)&&!"or".equals(relatedType)) {
			relatedType = "and";
		}
		if ("yyyy-MM-dd HH:mm:ss".equals(timeStamp_from)) {
			timeStamp_from = null;
		}
		if ("yyyy-MM-dd HH:mm:ss".equals(timeStamp_to)) {
			timeStamp_to = null;
		}
		
		Map<String, Object> map = logModelService.getResultByCondition(fileName, timeStamp_from, timeStamp_to, threadName, className, priority, message, 1, 1, relatedType, queryType);
		Integer integer = (Integer) map.get("total");
		return integer.intValue();
	}
	
	@RequestMapping(value="/download/excel")
	public void resultToExcel(String timeStamp_from,String timeStamp_to,String threadName,String priority,String className,String message,String fileName,String relatedType,String queryType,int outCount,HttpServletResponse response){
		if (!"and".equals(relatedType)&&!"or".equals(relatedType)) {
			relatedType = "and";
		}
		if ("yyyy-MM-dd HH:mm:ss".equals(timeStamp_from)) {
			timeStamp_from = null;
		}
		if ("yyyy-MM-dd HH:mm:ss".equals(timeStamp_to)) {
			timeStamp_to = null;
		}
		//生成文件名
		String caeatefileName = String.valueOf(System.currentTimeMillis())+".xls";
		ServletOutputStream sos = null;
	
		try {
			Map<String, Object> map = logModelService.getResultByCondition(fileName, timeStamp_from, timeStamp_to, threadName, className, priority, message, 1, outCount, relatedType, queryType);
			
			if (((Integer) map.get("total")).intValue()==0) {
				return;
			}
			List<LogModel> logModels = (List<LogModel>) map.get("rows");
		
			
			HSSFWorkbook excel = ExcelUtil.createExcel(caeatefileName,logModels , new LogModel(),"yyyy-MM-dd HH:mm:ss");
			
			caeatefileName = URLEncoder.encode(caeatefileName,"UTF-8");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition","attachment;filename="+caeatefileName);
			sos = response.getOutputStream();
			excel.write(sos);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if (sos !=null) {
				try {
					sos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					sos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
	
	@RequestMapping("/download/pdf")
	public void resultToPdf(String timeStamp_from,String timeStamp_to,String threadName,String priority,String className,String message,String fileName,String relatedType,String queryType,int outCount,HttpServletResponse response) throws Exception {
		
		if ("yyyy-MM-dd HH:mm:ss".equals(timeStamp_from)) {
			timeStamp_from = null;
		}
		if ("yyyy-MM-dd HH:mm:ss".equals(timeStamp_to)) {
			timeStamp_to = null;
		}
		String createfileName = String.valueOf(System.currentTimeMillis())+".pdf";
		createfileName = URLEncoder.encode(createfileName,"UTF-8");
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition","attachment;filename="+createfileName);
	
		Map<String, Object> map = logModelService.getResultByCondition(fileName, timeStamp_from, timeStamp_to, threadName, className, priority, message, 1, outCount, relatedType, queryType);
		if (((Integer) map.get("total")).intValue()==0) {
			return;
		}
		List<LogModel>	logModels = (List<LogModel>) map.get("rows");
		

		PdfUtil.createPdf(logModels,response.getOutputStream());
		
	}
	
}
