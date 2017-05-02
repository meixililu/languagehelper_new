package com.messi.languagehelper.dao;

import java.util.List;

import com.alibaba.fastjson.JSON;

import android.text.TextUtils;


public class WordListType {

    private Long id;
    private String type_id;
    private String course_num;
    private String title;
    private String word_num;
    private String listJson;
    private String img_url;
    private String backup1;
    private String backup2;
    private String backup3;

    private List<WordListItem> itemList;

    public List<WordListItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<WordListItem> itemList) {
		this.itemList = itemList;
	}

	public void setList(){
		if(!TextUtils.isEmpty(listJson)){
			itemList = JSON.parseArray(listJson, WordListItem.class);
		}
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getCourse_num() {
        return course_num;
    }

    public void setCourse_num(String course_num) {
        this.course_num = course_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWord_num() {
        return word_num;
    }

    public void setWord_num(String word_num) {
        this.word_num = word_num;
    }

    public String getListJson() {
        return listJson;
    }

    public void setListJson(String listJson) {
        this.listJson = listJson;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBackup1() {
        return backup1;
    }

    public void setBackup1(String backup1) {
        this.backup1 = backup1;
    }

    public String getBackup2() {
        return backup2;
    }

    public void setBackup2(String backup2) {
        this.backup2 = backup2;
    }

    public String getBackup3() {
        return backup3;
    }

    public void setBackup3(String backup3) {
        this.backup3 = backup3;
    }

}
