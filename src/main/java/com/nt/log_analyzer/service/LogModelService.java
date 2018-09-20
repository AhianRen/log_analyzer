package com.nt.log_analyzer.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.nt.log_analyzer.model.LogModel;

public interface LogModelService {

	//List<LogModel> getLogModelsByDateRange(Date fromTimeStamp, Date toTimeStamp);

	Map<String, Object> getResultByCondition(String fileName, String timeStamp_from, String timeStamp_to,
			String threadName, String className, String priority, String message, int startRow, int size,
			String relatedType, String queryType) throws Exception;
	
	
	
}
