/**
  * Copyright 2018 bejson.com 
  */
package com.messi.languagehelper.bean;
import java.util.List;

/**
 * Auto-generated: 2018-10-16 21:52:17
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TTParseDataBean {

    private String cover;
    private String text;
    private List<TTparseVideoBean> video;
    public void setCover(String cover) {
         this.cover = cover;
     }
     public String getCover() {
         return cover;
     }

    public void setText(String text) {
         this.text = text;
     }
     public String getText() {
         return text;
     }

    public void setVideo(List<TTparseVideoBean> video) {
         this.video = video;
     }
     public List<TTparseVideoBean> getVideo() {
         return video;
     }

}