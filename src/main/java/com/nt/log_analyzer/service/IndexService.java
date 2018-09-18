package com.nt.log_analyzer.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;


import com.nt.log_analyzer.model.FileModel;
import com.nt.log_analyzer.model.LogModel;

public interface IndexService {
	/**
	 * 轮询日志文件
	 * @throws IOException
	 * @throws Exception 
	 */
	void poll() throws Exception;
	
	

	Map<String, Object> selectByIndex(String indexPath, int pageIndex, int pageSize, String fileName,
			Date timeStamp_from, Date timeStamp_to, String priority, String threadName, String className,
			String message, String relatedType) throws Exception;



	void creatIndex(List<LogModel> logModels, String indexPath);







	
	


	
	
}
