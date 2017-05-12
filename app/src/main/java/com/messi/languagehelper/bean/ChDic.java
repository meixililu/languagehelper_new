package com.messi.languagehelper.bean;

public class ChDic {
	
	private String reason;

	private ChDicContent result;

	private int error_code;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public ChDicContent getResult() {
		return result;
	}

	public void setResult(ChDicContent result) {
		this.result = result;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
}