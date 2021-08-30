package com.demo.exception;

public class CustomException extends Exception {

	private int code;
	private String phrase;
	
	public CustomException(int code, String phrase) {
		super();
		this.code = code;
		this.phrase = phrase;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return code + ": " + phrase;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	
}
