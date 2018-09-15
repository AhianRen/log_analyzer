package com.nt.log_analyzer.service;

import java.io.IOException;
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
	
	void creatIndex(List<LogModel> logModels,FileModel fileModel) throws Exception;
	
	Map<String, Object> getTopDocs(int resultCount, String key) throws Exception;
	
	List<Integer> getIdsByIndex(int resultCount, String threadName, String className, String message,String relatedType)
			throws Exception;
	
}
