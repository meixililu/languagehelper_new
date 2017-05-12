package com.messi.languagehelper.bean;

public class TwistaItem {

	private String id;

	private String quest;

	private String title;

	private String content;

	private String result;

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
