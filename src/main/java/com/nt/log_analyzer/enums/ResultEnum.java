package com.nt.log_analyzer.enums;

public enum ResultEnum {
	
	
	SUCCESS(0,"成功"),SELECT_ERROR(1,"查询失败");
	
	private int code;
	
	private String msg;

	private ResultEnum() {
	}

	private ResultEnum(int state, String msg) {
		this.code = state;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
