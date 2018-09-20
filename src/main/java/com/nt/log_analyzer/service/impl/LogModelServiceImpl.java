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
import com.nt.log_analyzer.model.config.Config;
import com.nt.log_analyzer.service.IndexService;
import com.nt.log_analyzer.service.LogModelService;
@Service
public class LogModelServiceImpl implements LogModelService{
	
	@Autowired
	private LogModelDao logModelDao;
	
	@Autowired
	private IndexService indexService;
	
	@Autowired
	private Config config;
	
	/*@Override
	public List<LogModel> getLogModelsByDateRange(Date fromTimeStamp,Date toTimeStamp){
		return logModelDao.selectLogModelsByDateRange(fromTimeStamp, toTimeStamp);
	}
*/
	

/*
	@Override
	public int getResultCount(List<Integer> ids, Date timeStamp_from, Date timeStamp_to, String priority) {
		return logModelDao.selectResultCountByCondition(ids, timeStamp_from, timeStamp_to, priority);
	}
*/
	@Override
	public Map<String, Object> getResultByCondition(String fileName,String timeStamp_from, String timeStamp_to,
			String threadName,String className,String priority,String message,int startRow, int size,String relatedType,String queryType) throws Exception {
		
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date timeStamp_from1 = null;
		Date timeStamp_to1 = null;
		if (StringUtils.isNotBlank(timeStamp_from)) {
			timeStamp_from1 = simpleDateFormat.parse(timeStamp_from);
		}
		if (StringUtils.isNotBlank(timeStamp_to)) {
			timeStamp_to1 = simpleDateFormat.parse(timeStamp_to);
		}
		
		Map<String, Object> map = indexService.selectByIndex(config.getIndexpath(),startRow, size, fileName, timeStamp_from1, timeStamp_to1, priority, threadName, className, message, relatedType);
		List<Integer> ids = (List<Integer>) map.get("ids");
		if (ids==null||ids.size()==0) {
			map.put("rows", "");
		}else {
			List<LogModel> logModels = logModelDao.selectByIds(ids);
			
			map.put("rows", logModels);
		}
		map.remove("ids");
		return map;
		/*
		Map<String, Object> map = new HashMap<>();
		//TODO 从lucene中查
		long count = logModelDao.selectAllCount();
		
		List<Integer> ids = null;
		List<LogModel> logModels = null;
		
		if ("lucene".equals(queryType)) {
			if (StringUtils.isNotBlank(threadName)||StringUtils.isNotBlank(className)||StringUtils.isNotBlank(message)) {
				ids = indexService.getIdsByIndex((int)count, threadName, className, message,relatedType);
				if (ids.size() == 0) {
					map.put("total", 0);
					map.put("rows", "");
					return map;
				}
			}
			
			count = logModelDao.selectResultCountByCondition(ids, fileName, timeStamp_from1, timeStamp_to1, priority, relatedType, null, null, null);
			logModels = logModelDao.selectLogModelsByCondition(ids, fileName, timeStamp_from1, timeStamp_to1, priority, startRow, size, relatedType, null, null, null);
			map.put("total", count);
			map.put("rows", logModels);
			
		}else if ("mysql".equals(queryType)) {
			count = logModelDao.selectResultCountByCondition(null, fileName, timeStamp_from1, timeStamp_to1, priority, relatedType, threadName, className, message);
			logModels = logModelDao.selectLogModelsByCondition(null, fileName, timeStamp_from1, timeStamp_to1, priority, startRow, size, relatedType, threadName, className, message);
			map.put("total", count);
			map.put("rows", logModels);
			
		}
		*/
	}
	/*
	 * 根据fileID删除fileModel和LogModel
	 */
	public void deleteFileAndLog(int fileId) {
		
		
	}
	
}
