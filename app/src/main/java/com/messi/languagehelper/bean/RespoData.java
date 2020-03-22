package com.messi.languagehelper.bean;

public class RespoData<T> {
	
	private T data;

	private int code;

	private String errStr;

	public RespoData(T data){
		this.data = data;
	}

	public RespoData(String errStr){
		this.errStr = errStr;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getErrStr() {
		return errStr;
	}

	public void setErrStr(String errStr) {
		this.errStr = errStr;
	}
}