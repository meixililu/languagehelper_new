/**
  * Copyright 2018 bejson.com 
  */
package com.messi.languagehelper.bean;

/**
 * Auto-generated: 2018-10-16 21:52:17
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TTParseBean {

    private int retCode;
    private String retDesc;
    private TTParseDataBean data;
    private boolean succ;
    public void setRetCode(int retCode) {
         this.retCode = retCode;
     }
     public int getRetCode() {
         return retCode;
     }

    public void setRetDesc(String retDesc) {
         this.retDesc = retDesc;
     }
     public String getRetDesc() {
         return retDesc;
     }

    public void setData(TTParseDataBean data) {
         this.data = data;
     }
     public TTParseDataBean getData() {
         return data;
     }

    public void setSucc(boolean succ) {
         this.succ = succ;
     }
     public boolean getSucc() {
         return succ;
     }

}