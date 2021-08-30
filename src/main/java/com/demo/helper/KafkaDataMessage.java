package com.demo.helper;

public class KafkaDataMessage {

	private String domain;
	private String id;
	private String message;
	
	public KafkaDataMessage() {
		// TODO Auto-generated constructor stub
	}

	public KafkaDataMessage(String domain, String id, String message) {
		super();
		this.domain = domain;
		this.id = id;
		this.message = message;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
