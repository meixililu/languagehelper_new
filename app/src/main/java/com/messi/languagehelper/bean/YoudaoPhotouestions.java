/**
  * Copyright 2019 bejson.com 
  */
package com.messi.languagehelper.bean;

import com.jeremyliao.liveeventbus.core.LiveEvent;

/**
 * Auto-generated: 2019-04-19 22:45:5
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class YoudaoPhotouestions implements LiveEvent {

    private String answer;
    private String id;
    private String analysis;
    private String content;
    private String knowledge;
    public void setAnswer(String answer) {
         this.answer = answer;
     }
     public String getAnswer() {
         return answer;
     }

    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setAnalysis(String analysis) {
         this.analysis = analysis;
     }
     public String getAnalysis() {
         return analysis;
     }

    public void setContent(String content) {
         this.content = content;
     }
     public String getContent() {
         return content;
     }

    public void setKnowledge(String knowledge) {
         this.knowledge = knowledge;
     }
     public String getKnowledge() {
         return knowledge;
     }

}