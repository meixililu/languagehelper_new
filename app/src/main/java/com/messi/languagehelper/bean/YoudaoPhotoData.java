/**
  * Copyright 2019 bejson.com 
  */
package com.messi.languagehelper.bean;

import com.jeremyliao.liveeventbus.core.LiveEvent;

import java.util.List;

/**
 * Auto-generated: 2019-04-19 22:45:5
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class YoudaoPhotoData implements LiveEvent {

    private List<YoudaoPhotouestions> questions;
    private String text;
    public void setQuestions(List<YoudaoPhotouestions> questions) {
         this.questions = questions;
     }
     public List<YoudaoPhotouestions> getQuestions() {
         return questions;
     }

    public void setText(String text) {
         this.text = text;
     }
     public String getText() {
         return text;
     }

}