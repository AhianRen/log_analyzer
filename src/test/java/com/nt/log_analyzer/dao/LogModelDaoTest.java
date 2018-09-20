package com.nt.log_analyzer.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nt.log_analyzer.model.LogModel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogModelDaoTest {
	
	@Autowired
	private LogModelDao logModelDao;
	
	
	
	@Test
	public void test1() {
		
		List<Integer> ids = new ArrayList<>();
		for(int i=150030;i<150039;i++) {
			ids.add(i);
		}
		List<LogModel> logModels = logModelDao.selectByIds(ids);
		
		System.out.println(logModels);
	}
	
	@Test
	public void test2() throws Exception {
		
		
	}
	@Test
	public void test3() throws Exception {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date timeStamp_from = simpleDateFormat.parse("2017-09-29 11:12:44");
		Date timeStamp_to = simpleDateFormat.parse("2017-09-29 11:12:46");
		
		List<Integer> ids = new ArrayList<>();
		
		for(int i = 60;i<65;i++) {
			ids.add(new Integer(i));
		}
		String priority = "WARN";
		
		
		/*List<LogModel> logModels = logModelDao.selectLogModelsByCondition(null, timeStamp_from, timeStamp_to, null, 0, 0);
		for (LogModel logModel : logModels) {
			System.out.println(logModel);
		}*/
		
	}
}
