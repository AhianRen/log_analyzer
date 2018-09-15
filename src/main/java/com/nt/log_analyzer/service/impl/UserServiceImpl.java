package com.nt.log_analyzer.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.log_analyzer.dao.UserMapper;
import com.nt.log_analyzer.model.User;
import com.nt.log_analyzer.service.UserService;
@WebService(name="UserService",targetNamespace="http://service.log_analyzer.nt.com/")
@Component                                 
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User getUser(int userId) {
		if (1==userId) {
			return new User(1, "1号", 10);
		}else {
			return new User(0, "默认", 0);
		}
		
	}

	@Override
	public List<User> getUserList() {
		List<User> users = new ArrayList<>();
		User user0 = new User(0, "0号", 0);
		User user1 = new User(1,"1号",1);
		
		users.add(user0);
		users.add(user1);
		return users;
	}

	@Override
	@Transactional
	public int addUser(User user) {
		int i = userMapper.addUser(user);
		return i;
	}

	@Override
	public List<User> getAllUser() {
		return userMapper.getAllUser();
	}

}
