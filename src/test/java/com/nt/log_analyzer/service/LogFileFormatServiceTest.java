package com.nt.log_analyzer.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.nt.log_analyzer.dao.UserMapper;
import com.nt.log_analyzer.model.User;
import com.nt.log_analyzer.service.impl.LogFileFormatService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogFileFormatServiceTest {
	
	@Autowired
	private LogFileFormatService logFileFormatService;
	
	@Autowired
	private UserService userService;
	
	
	@Test
	public void Test() throws Exception {
		logFileFormatService.logFileToDB();
	}
	
	@Test
	public void select(){
		List<User> allUser = userService.getAllUser();
		System.out.println(allUser);
		
	}
	@Test
	public void insert() {
		User user = new User();
		user.setName("黄盖");
		user.setAge(60);
		user.setContent("疼疼疼疼疼疼疼疼疼疼疼");
		int i = userService.addUser(user);
		System.out.println(i);
		System.out.println(user);
		
	}
	
	
	
	
	
	
}
