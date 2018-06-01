package com.messi.languagehelper.bean;

public class HjTranBean {
	
	private int status;
	private String message;
	private HjTranDataBean data;
	private String time;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HjTranDataBean getData() {
		return data;
	}

	public void setData(HjTranDataBean data) {
		this.data = data;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}