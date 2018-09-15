package com.nt.log_analyzer.service;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nt.log_analyzer.service.impl.UserServiceImpl;

@Configuration
public class UserWS {
	
	@Autowired
	private Bus bus;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IndexService indexService;
	
	@Bean
	public Endpoint userEndpoint() {
		Endpoint endpoint = new EndpointImpl(bus, userService);
		endpoint.publish("/user");
		return endpoint;
	}
	
/*	
	@Bean
	public Endpoint logEndpoint() {
		Endpoint endpoint = new EndpointImpl(bus, indexService);
		endpoint.publish("/log");
		return endpoint;
	}
	
	*/
	
	
}
