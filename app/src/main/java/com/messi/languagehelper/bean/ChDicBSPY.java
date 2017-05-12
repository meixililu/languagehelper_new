package com.messi.languagehelper.bean;

import java.util.List;

public class ChDicBSPY {
	
	private String reason;

	private List<ChDicBushouPinyin> result;

	private int error_code;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<ChDicBushouPinyin> getResult() {
		return result;
	}

	public void setResult(List<ChDicBushouPinyin> result) {
		this.result = result;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
}