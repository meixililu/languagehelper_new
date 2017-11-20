package com.messi.languagehelper.bean;

import java.util.List;

public class BaiduV2ResultBean {
	
	private int type;
	private int status;
	private String from;
	private String en;
	private String all;
	private List<BaiduV2ResultDataBean> data;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getAll() {
		return all;
	}

	public void setAll(String all) {
		this.all = all;
	}

	public List<BaiduV2ResultDataBean> getData() {
		return data;
	}

	public void setData(List<BaiduV2ResultDataBean> data) {
		this.data = data;
	}
}