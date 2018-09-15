package com.nt.log_analyzer.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nt.log_analyzer.model.LogModel;

@Service
public class LogFileFormatService {

	
	public void logFileToDB() throws Exception {
		/*String[] logModelFieldNameArray = { "date", "milliSecond", "threadName", "priority", "className", "message" };
		List<LogModel> logList = LogFileFormat.logList("(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}),(\\d+)\\s\\[(\\S*)\\]\\s(\\S*)\\s+(\\S*)\\s-\\s(.*)",
				"D:\\logs\\新建文本文档.txt",
				logModelFieldNameArray,
				"yyyy-MM-dd HH:mm:ss");
		for (LogModel logModel : logList) {
			System.out.println(logModel);
		}
		*/
		
	}
	
}
