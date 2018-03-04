package com.messi.languagehelper.bean;

/**
 * Created by luli on 01/03/2018.
 */

public class JuhaiBean {

    private String sentence;
    private String meanning;

    public JuhaiBean(String sentence,String meanning){
        this.sentence = sentence;
        this.meanning = meanning;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getMeanning() {
        return meanning;
    }

    public void setMeanning(String meanning) {
        this.meanning = meanning;
    }
}
