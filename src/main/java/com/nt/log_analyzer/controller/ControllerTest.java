package com.nt.log_analyzer.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nt.log_analyzer.model.config.Config;
import com.nt.log_analyzer.model.config.MyConfig;
import com.nt.log_analyzer.service.IndexService;
import com.nt.log_analyzer.utils.YmlUtil;

@Controller
public class ControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ControllerTest.class);
	
	@Autowired
	private MyConfig myConfig;
	
	@Autowired
	private Config config;
	
	@Autowired
	private IndexService indexService;
	
	
	@RequestMapping("/config")
	@ResponseBody
	public String config() {
		//String logParameter = myConfig.getLogParameter();
		//System.out.println(logParameter);
		//String[] array = JsonUtil.jsonStrToStringArray(logParameter);
		
		/*for (String str : array) {
			System.out.println(str);
		}*/

		List<Map<String, String[]>> configs = config.getConfigs();
		for (Map<String, String[]> map : configs) {
			//String[] strings = map.get("logregex");
			//System.out.println(YmlUtil.ymlArrayToString(strings));
			
			String string = map.get("logfilePath")[0];
			System.out.println(string);
			
			/*for (String str : strings) {
				System.out.println(str);
			}
			String[] strings2 = map.get("filterwords");
			for (String string : strings2) {
				System.out.println(string);
			}*/
			
		}
		
		
		
		
		return config.toString();
	}
	
	
	
}
