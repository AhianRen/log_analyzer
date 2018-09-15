package com.nt.log_analyzer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nt.log_analyzer.model.config.MyConfig;
import com.nt.log_analyzer.utils.JsonUtil;

@Controller
public class ControllerTest {

	@Autowired
	private MyConfig myConfig;
	
	@RequestMapping("/config")
	@ResponseBody
	public String[] config() {
		String logParameter = myConfig.getLogParameter();
		//System.out.println(logParameter);
		String[] array = JsonUtil.jsonStrToStringArray(logParameter);
		
		for (String str : array) {
			System.out.println(str);
		}
		
		
		return array;
	}
	
}
