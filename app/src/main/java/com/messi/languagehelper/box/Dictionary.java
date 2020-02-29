package com.messi.languagehelper.box;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Dictionary {

    @Id
    private Long id;
    private String word_name;
    private String result;  /**for item display**/
    private String to_lan;
    private String from_lan;
    private String ph_am;
    private String ph_en;
    private String ph_zh;
    private String type;
    private String questionVoiceId;
    private String questionAudioPath;
    private String resultVoiceId;
    private String resultAudioPath;
    private String iscollected;
    private Integer visit_times;
    private Integer speak_speed;
    private String backup1;/**for media play **/
    private String backup2;/**playing pause stop sign  XFUtil PlayOnline PlayOffline**/
    private String backup3;/**for split words**/
    private String backup4;
    private String backup5;

    public Dictionary() {
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTo_lan() {
        return to_lan;
    }

    public void setTo_lan(String to_lan) {
        this.to_lan = to_lan;
    }

    public void setTo(String to_lan) {
        this.to_lan = to_lan;
    }

    public String getFrom_lan() {
        return from_lan;
    }

    public void setFrom_lan(String from_lan) {
        this.from_lan = from_lan;
    }

    public String getFrom() {
        return from_lan;
    }

    public void setFrom(String from) {
        this.from_lan = from;
    }

    public String getPh_am() {
        return ph_am;
    }

    public void setPh_am(String ph_am) {
        this.ph_am = ph_am;
    }

    public String getPh_en() {
        return ph_en;
    }

    public void setPh_en(String ph_en) {
        this.ph_en = ph_en;
    }

    public String getPh_zh() {
        return ph_zh;
    }

    public void setPh_zh(String ph_zh) {
        this.ph_zh = ph_zh;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestionVoiceId() {
        return questionVoiceId;
    }

    public void setQuestionVoiceId(String questionVoiceId) {
        this.questionVoiceId = questionVoiceId;
    }

    public String getQuestionAudioPath() {
        return questionAudioPath;
    }

    public void setQuestionAudioPath(String questionAudioPath) {
        this.questionAudioPath = questionAudioPath;
    }

    public String getResultVoiceId() {
        return resultVoiceId;
    }

    public void setResultVoiceId(String resultVoiceId) {
        this.resultVoiceId = resultVoiceId;
    }

    public String getResultAudioPath() {
        return resultAudioPath;
    }

    public void setResultAudioPath(String resultAudioPath) {
        this.resultAudioPath = resultAudioPath;
    }

    public String getIscollected() {
        return iscollected;
    }

    public void setIscollected(String iscollected) {
        this.iscollected = iscollected;
    }

    public Integer getVisit_times() {
        return visit_times;
    }

    public void setVisit_times(Integer visit_times) {
        this.visit_times = visit_times;
    }

    public Integer getSpeak_speed() {
        return speak_speed;
    }

    public void setSpeak_speed(Integer speak_speed) {
        this.speak_speed = speak_speed;
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

    public String getBackup4() {
        return backup4;
    }

    public void setBackup4(String backup4) {
        this.backup4 = backup4;
    }

    public String getBackup5() {
        return backup5;
    }

    public void setBackup5(String backup5) {
        this.backup5 = backup5;
    }

}
