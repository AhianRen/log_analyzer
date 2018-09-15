package com.nt.log_analyzer.model;

import java.util.Date;



public class LogModel {
	
	private int id;					// 日志id				  存
	private String fileName;				//文件id
	private int rowNumber; 			// 行号
	private Date timeStamp; 		// 时间戳		
	private Integer milliSecond; 	// 毫秒值		
	private Integer proceedingID; 	// 进程ID		
	private String threadName; 		// 线程名        	索引	分词	不存
	private String priority; 		// 日志级别	
	private Integer executeTime;	// 执行时间  	
	private String className;		// 类名   		索引	 分词   不存
	private String message; 		// 详细信息        索引	 分词	 不存
	

	@Override
	public String toString() {
		return  fileName + "  " + rowNumber + "  " + timeStamp
				+ "  " + milliSecond + "  " + "  " + threadName
				+ "  " + priority + "  " + executeTime + "  " + className + "  "
				+ message;
	}

	public LogModel() {
		super();
	}
	
	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public Integer getMilliSecond() {
		return milliSecond;
	}

	public void setMilliSecond(Integer milliSecond) {
		this.milliSecond = milliSecond;
	}

	public Integer getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Integer executeTime) {
		this.executeTime = executeTime;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	
	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Integer getProceedingID() {
		return proceedingID;
	}
	public void setProceedingID(Integer proceedingID) {
		this.proceedingID = proceedingID;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
