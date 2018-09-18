package com.nt.log_analyzer.model.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="myconfig")
public class Config {
//	private String str;
//	private String array;
//	private List<String> list = new ArrayList<>();
//	private Map<String, String> map = new HashMap<>();
//	private List<Map<String, String>> listmap = new ArrayList<>();
//	private Map<String, List<String>> maplist = new HashMap<>();
	
	private String indexpath;
	private List<Map<String, String[]>> configs = new ArrayList<>();
	
	
	
	
	
	
	public String getIndexpath() {
		return indexpath;
	}
	public void setIndexpath(String indexpath) {
		this.indexpath = indexpath;
	}
	public List<Map<String, String[]>> getConfigs() {
		return configs;
	}
	public void setConfigs(List<Map<String, String[]>> configs) {
		this.configs = configs;
	}
	
	
	
	

	
	
	
}
