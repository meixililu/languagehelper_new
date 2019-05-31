/**
  * Copyright 2019 bejson.com 
  */
package com.messi.languagehelper.bean;
import java.util.List;

/**
 * Auto-generated: 2019-05-31 11:10:31
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class YoudaoApiBean {

    private String type;
    private int errorCode;
    private int elapsedTime;
    private List<List<YoudaoApiResult>> translateResult;
    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setErrorCode(int errorCode) {
         this.errorCode = errorCode;
     }
     public int getErrorCode() {
         return errorCode;
     }

    public void setElapsedTime(int elapsedTime) {
         this.elapsedTime = elapsedTime;
     }
     public int getElapsedTime() {
         return elapsedTime;
     }

    public void setTranslateResult(List<List<YoudaoApiResult>> translateResult) {
         this.translateResult = translateResult;
     }
     public List<List<YoudaoApiResult>> getTranslateResult() {
         return translateResult;
     }

}