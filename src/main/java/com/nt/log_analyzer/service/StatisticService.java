package com.nt.log_analyzer.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.log_analyzer.dao.LogModelDao;

@Service
public class StatisticService {

	@Autowired
	private LogModelDao logModelDao;

	public Map<String, Integer> count() {
		Map<String, Integer> map = new LinkedHashMap<>();

		List<String> allPriority = logModelDao.selectAllPriority();
		for (String priority : allPriority) {
			int i = logModelDao.selectCountByPriority(priority);
			map.put(priority, i);
		}

		List<String> threadNames = logModelDao.selectAllThreadName();
		for (String threadName : threadNames) {
			int i = logModelDao.selectCountByThreadName(threadName);
			map.put(threadName, i);
		}

		return map;
	}

}
