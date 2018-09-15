package com.nt.log_analyzer.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nt.log_analyzer.model.FileModel;
import com.nt.log_analyzer.model.LogModel;
import com.nt.log_analyzer.service.IndexService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexServiceTest {
	
	@Autowired
	private IndexService indexService;
	
	@Test
	public void insertLogModelTest() {
		
		LogModel logModel = new LogModel();
		//logModel.setFileId(1);
		logModel.setRowNumber(1);
		logModel.setTimeStamp(new Date());
		logModel.setMilliSecond(new Integer(654));

		logModel.setThreadName("main");
		logModel.setPriority("INFO");

		logModel.setClassName("o.e.m.c.i.p.ProjectConfigurationManager");
		logModel.setMessage("测试啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦");
		//int i = indexService.insertLogModel(logModel);
		//System.out.println("插入了" + i + "行");
		System.out.println(logModel);
	}
	
	@Test
	public void testGetLogModelByIndex() throws Exception {
		//List<LogModel> logModels = indexService.getLogModelByIndex(20, "sources");
		//for(LogModel logModel : logModels) {
		//	System.out.println(logModel);
		//}
		
	}
	
	@Test
	public void testPoll() throws Exception {
		indexService.poll();
		
	}
	
	@Test
	public void testGetTopDocs() throws Exception {
		//List<FileModel> result = indexService.getTopDocs(10, "jndi");
		//System.out.println(result);
	}
	
	@Test
	public void test() throws Exception {
		
	}
}
