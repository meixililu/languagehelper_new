package com.messi.languagehelper.bean;


import com.iflytek.voiceads.conn.NativeDataRef;

public class WechatJXResult {

	private String ctime;

	private String title;

	private String description;

	private String picUrl;

	private String url;

	private NativeDataRef mNativeADDataRef;

	private boolean isHasShowAD;

	public boolean isHasShowAD() {
		return isHasShowAD;
	}

	public void setHasShowAD(boolean hasShowAD) {
		isHasShowAD = hasShowAD;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public NativeDataRef getmNativeADDataRef() {
		return mNativeADDataRef;
	}

	public void setmNativeADDataRef(NativeDataRef mNativeADDataRef) {
		this.mNativeADDataRef = mNativeADDataRef;
	}


}
