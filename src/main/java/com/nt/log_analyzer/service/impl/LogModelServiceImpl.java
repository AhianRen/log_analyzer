package com.nt.log_analyzer.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.log_analyzer.dao.LogModelDao;
import com.nt.log_analyzer.model.LogModel;
import com.nt.log_analyzer.service.IndexService;
import com.nt.log_analyzer.service.LogModelService;
@Service
public class LogModelServiceImpl implements LogModelService{
	
	@Autowired
	private LogModelDao logModelDao;
	
	@Autowired
	private IndexService indexService;
	
	@Override
	public List<LogModel> getLogModelsByDateRange(Date fromTimeStamp,Date toTimeStamp){
		return logModelDao.selectLogModelsByDateRange(fromTimeStamp, toTimeStamp);
	}

	@Override
	public List<LogModel> getHome(int startRow, int size) {
		return logModelDao.selectAllByIdDesc(startRow, size);
	}
	
	@Override
	public long getAllCount() {
		return logModelDao.selectAllCount();
		
	}
/*
	@Override
	public int getResultCount(List<Integer> ids, Date timeStamp_from, Date timeStamp_to, String priority) {
		return logModelDao.selectResultCountByCondition(ids, timeStamp_from, timeStamp_to, priority);
	}
*/
	@Override
	public Map<String, Object> getResultByCondition(String fileName,String timeStamp_from, String timeStamp_to,
			String threadName,String className,String priority,String message,int startRow, int size,String relatedType) throws Exception {
		
		Map<String, Object> map = new HashMap<>();
		
		long count = logModelDao.selectAllCount();
		
		List<Integer> ids = null;
		
		if (StringUtils.isNotBlank(threadName)||StringUtils.isNotBlank(className)||StringUtils.isNotBlank(message)) {
			ids = indexService.getIdsByIndex((int)count, threadName, className, message,relatedType);
			if (ids.size() == 0) {
				map.put("total", 0);
				map.put("rows", null);
				return map;
			}
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date timeStamp_from1 = null;
		Date timeStamp_to1 = null;
		if (StringUtils.isNotBlank(timeStamp_from)) {
			timeStamp_from1 = simpleDateFormat.parse(timeStamp_from);
		}
		if (StringUtils.isNotBlank(timeStamp_to)) {
			timeStamp_to1 = simpleDateFormat.parse(timeStamp_to);
		}
		
		count = logModelDao.selectResultCountByCondition(ids, fileName,timeStamp_from1, timeStamp_to1, priority,relatedType);
	
		List<LogModel> logModels = logModelDao.selectLogModelsByCondition(ids,fileName, timeStamp_from1, timeStamp_to1, priority, startRow, size,relatedType);
		
		map.put("total", count);
		map.put("rows", logModels);
		return map;
	}
	
	public int selectResultCountByCondition(List<Integer>ids,String fileName,Date timeStamp_from, Date timeStamp_to,String priority,String relatedType) {
		
		return logModelDao.selectResultCountByCondition(ids, fileName,timeStamp_from, timeStamp_to, priority,relatedType);
		
	}

	
	
}
