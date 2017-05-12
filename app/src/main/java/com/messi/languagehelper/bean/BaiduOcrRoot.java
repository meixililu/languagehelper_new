package com.messi.languagehelper.bean;

import java.util.List;

public class BaiduOcrRoot {

	private int error_code;

	private String error_msg;

	private int direction;

	private long log_id;

	private List<BDORCItem> words_result;

	private int words_result_num;

	public List<BDORCItem> getWords_result() {
		return words_result;
	}

	public void setWords_result(List<BDORCItem> words_result) {
		this.words_result = words_result;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public long getLog_id() {
		return log_id;
	}

	public void setLog_id(long log_id) {
		this.log_id = log_id;
	}

	public int getWords_result_num() {
		return words_result_num;
	}

	public void setWords_result_num(int words_result_num) {
		this.words_result_num = words_result_num;
	}


}
