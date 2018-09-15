package com.nt.log_analyzer.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.nt.log_analyzer.model.FileModel;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileDaoTest {
	
	@Autowired 
	private FileDao fileDao;
	
	@Test
	public void testInsert() {
		FileModel fileModel = new FileModel();
		fileModel.setFileAbsolutePath("testPath");
		fileModel.setLastLine(12);
		fileModel.setLastModified(1111);
		fileDao.insertFileModel(fileModel);
		
		System.out.println(fileModel);
	}
	
	@Test
	public void testUpdate() {
		FileModel fileModel = new FileModel();
		fileModel.setFileId(2);
		fileModel.setLastLine(10);
		
		
		int i = fileDao.updateById(fileModel);
		System.out.println("===================="+i);
		
		
	}
	
	
	
	
	
	
}
