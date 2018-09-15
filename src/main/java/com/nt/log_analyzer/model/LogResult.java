package com.nt.log_analyzer.model;

import com.nt.log_analyzer.enums.ResultEnum;


public class LogResult {
	//响应状态码
	private int code;
	//响应消息
	private String msg;
	//响应数据
	private Object data;
	
	
	public static LogResult success() {
		return new LogResult(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),null);
	}
	public static LogResult success(Object data) {
		return new LogResult(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),data);
	}
	
	public static LogResult build(int code,String msg) {
		return new LogResult(code,msg,null);
		
	}
	public static LogResult build(int code,String msg,Object data) {
		return new LogResult(code,msg,data);
	}
	
	public LogResult() {
		super();
	}
	public LogResult(int code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
}
