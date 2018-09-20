package com.nt.log_analyzer.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nt.log_analyzer.dao.FileDao;
import com.nt.log_analyzer.model.FileModel;
import com.nt.log_analyzer.model.config.Config;
import com.nt.log_analyzer.utils.FileUtil;
import com.nt.log_analyzer.utils.IndexUtils;

@Component
public class Timer {
	private Logger logger = LoggerFactory.getLogger(Timer.class);
	
	@Autowired
	private IndexService indexService;
	
	@Autowired
	private FileDao fileDao;
	
	@Autowired
	private Config config;
	
	@Scheduled(cron="*/30 * * * * ?")
	public void poll() {
		try {
			//indexService.poll();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//@Scheduled(cron="0 0 0 * * *")
	public void clear() {
		String indexPath = config.getIndexpath();
		List<FileModel> fileModels = fileDao.selectAllFile();
		List<Integer> ids = FileUtil.getBeDeletedFileIds(fileModels);
		if (ids.size()==0) {
			logger.info("没有文件被删除");
		}
		for (Integer i : ids) {
			try {
				
				fileDao.deleteFileAndLog(i);
				IndexUtils.deleteByFieldAndTerm(indexPath, "fileId", String.valueOf(i));
				logger.info("删除了id为"+i+"的文件的相关数据");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
