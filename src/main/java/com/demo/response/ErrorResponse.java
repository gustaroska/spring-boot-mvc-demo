package com.demo.response;

import java.util.Date;

public class ErrorResponse<T> {

	private String message;
    private int code;
    private Date timestamp;
    
    
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
    
	
    

}
