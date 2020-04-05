package com.messi.languagehelper.bean;

public class RespoADData {

	private int code;
	private int pos;

	public RespoADData(int code){
		this.code = code;
	}

	public RespoADData(int code, int pos){
		this.code = code;
		this.pos = pos;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}
}