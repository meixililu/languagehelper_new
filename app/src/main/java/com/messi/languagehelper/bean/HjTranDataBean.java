package com.messi.languagehelper.bean;

import java.util.List;

public class HjTranDataBean {
	
	private String content;
	private String fromLang;
	private String key;
	private String original_text;
	private Pronounce pronounce;
	private String toLang;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFromLang() {
		return fromLang;
	}

	public void setFromLang(String fromLang) {
		this.fromLang = fromLang;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOriginal_text() {
		return original_text;
	}

	public void setOriginal_text(String original_text) {
		this.original_text = original_text;
	}

	public Pronounce getPronounce() {
		return pronounce;
	}

	public void setPronounce(Pronounce pronounce) {
		this.pronounce = pronounce;
	}

	public String getToLang() {
		return toLang;
	}

	public void setToLang(String toLang) {
		this.toLang = toLang;
	}
}

class Pronounce {
	private List<Audio> audio;
	private int audioOriginStatus;
	private int audioStatus;
	public void setAudio(List<Audio> audio) {
		this.audio = audio;
	}
	public List<Audio> getAudio() {
		return audio;
	}

	public void setAudioOriginStatus(int audioOriginStatus) {
		this.audioOriginStatus = audioOriginStatus;
	}
	public int getAudioOriginStatus() {
		return audioOriginStatus;
	}

	public void setAudioStatus(int audioStatus) {
		this.audioStatus = audioStatus;
	}
	public int getAudioStatus() {
		return audioStatus;
	}
}

class Audio {

	private int length;
	private String originUrl;
	private int start;
	private String url;
	public void setLength(int length) {
		this.length = length;
	}
	public int getLength() {
		return length;
	}

	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
	}
	public String getOriginUrl() {
		return originUrl;
	}

	public void setStart(int start) {
		this.start = start;
	}
	public int getStart() {
		return start;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}

}