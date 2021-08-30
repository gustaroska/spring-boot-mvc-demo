package com.demo;

import java.time.LocalDateTime;

public class ResponseWrapper {
	
	private String status = "success";
	
	private int code = 200;
	
	private LocalDateTime timestamp = LocalDateTime.now();
	
	private Object data;
	
	public ResponseWrapper() {
		// TODO Auto-generated constructor stub
	}

	public ResponseWrapper(String status, int code) {
		super();
		this.status = status;
		this.code = code;
	}

	public ResponseWrapper(Object data) {
		// TODO Auto-generated constructor stub
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
