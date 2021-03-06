package com.messi.languagehelper.bean;

public class RespoData<T> {
	
	private T data;

	private int code;//0 error  1 right

	private String errStr;

	private boolean isHideFooter;

	private int positionStart;

	private int itemCount;

	public RespoData(){
	}

	public RespoData(T data){
		this.data = data;
	}

	public RespoData(String errStr){
		this.errStr = errStr;
	}

	public RespoData(int code,String errStr){
		this.code = code;
		this.errStr = errStr;
	}

	public int getPositionStart() {
		return positionStart;
	}

	public void setPositionStart(int positionStart) {
		this.positionStart = positionStart;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public boolean isHideFooter() {
		return isHideFooter;
	}

	public void setHideFooter(boolean hideFooter) {
		isHideFooter = hideFooter;
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