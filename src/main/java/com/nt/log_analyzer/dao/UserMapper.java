package com.nt.log_analyzer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.nt.log_analyzer.model.User;

@Mapper
public interface UserMapper {
	public List<User> getAllUser();
	public int addUser(User user);
}
