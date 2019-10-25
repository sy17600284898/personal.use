package com.personal.use.execption;

/**
 * 异常编码枚举
 * 
 * @author mifuxing
 *
 */
public enum ExceptionCode {

	SECCESS(200, "请求成功!"),
    ERROR_400(400,"未登录"),
	ERROR_500(500,"未知错误!");
	

	// 异常编码值 
	private int value = 200;
	// 异常编码描述 
	private String description = null;

	 //构造函数
	private ExceptionCode(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}


}