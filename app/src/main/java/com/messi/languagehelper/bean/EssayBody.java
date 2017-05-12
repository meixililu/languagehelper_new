package com.messi.languagehelper.bean;

import java.util.List;

public class EssayBody {
	private List<EssayData> data;

	private String ret_message;

	private int ret_code;

	public List<EssayData> getData() {
		return data;
	}

	public void setData(List<EssayData> data) {
		this.data = data;
	}

	public String getRet_message() {
		return ret_message;
	}

	public void setRet_message(String ret_message) {
		this.ret_message = ret_message;
	}

	public int getRet_code() {
		return ret_code;
	}

	public void setRet_code(int ret_code) {
		this.ret_code = ret_code;
	}
}