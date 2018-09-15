package com.nt.log_analyzer.model.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.nt.log_analyzer.utils.JsonUtil;


@Component
@PropertySource(value="classpath:config/myconfig.properties")
@ConfigurationProperties("log1")
public class MyConfig {

	private String indexPath;
	private String logFilePath;
	private String logRegex;  
	private String logParameter; //需转为数组
	private String datePattern;
	private String filterWords; //转为数组
	
	
	public String getIndexPath() {
		return indexPath;
	}
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	public String getLogFilePath() {
		return logFilePath;
	}
	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}
	public String getLogRegex() {
		return logRegex;
	}
	public void setLogRegex(String logRegex) {
		this.logRegex = logRegex;
	}
	public String getLogParameter() {
		return logParameter;
	}
	public void setLogParameter(String logParameter) {
		this.logParameter = logParameter;
	}
	public String getDatePattern() {
		return datePattern;
	}
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}
	public String getFilterWords() {
		return filterWords;
	}
	public void setFilterWords(String filterWords) {
		this.filterWords = filterWords;
	}
	
	
	
	
}
