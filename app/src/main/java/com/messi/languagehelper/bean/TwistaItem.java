package com.messi.languagehelper.bean;

public class TwistaItem {

	private String id;
	private String quest;
	private String title;
	private String content;
	private String result;
	private String reason;
	private String answer;
	private String en;
	private String zh;



	private String front;
	private String behind;

	private String question;
	private String abbr;
	private String pinyin;
	private String source;
	private String study;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBehind() {
		return behind;
	}

	public void setBehind(String behind) {
		this.behind = behind;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getZh() {
		return zh;
	}

	public void setZh(String zh) {
		this.zh = zh;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private boolean isShowResult;

	public boolean isShowResult() {
		return isShowResult;
	}

	public void setShowResult(boolean showResult) {
		isShowResult = showResult;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuest() {
		return quest;
	}

	public String getReason() {
		return reason;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setQuest(String quest) {
		this.quest = quest;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
