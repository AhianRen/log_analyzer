package com.nt.log_analyzer.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.nt.log_analyzer.model.User;

@WebService
public interface UserService {
	
	@WebMethod
	User getUser(int userId);
	
	@WebMethod
	List<User> getUserList();
	
	int addUser(User user);
	List<User> getAllUser();
	
	
}
