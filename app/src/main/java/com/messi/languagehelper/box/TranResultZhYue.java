package com.messi.languagehelper.box;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;


@Entity
public class TranResultZhYue {

    @Id
    private Long id;
    private String english;
    private String chinese;
    private String resultAudioPath;
    private String questionAudioPath;
    private String questionVoiceId;
    private String resultVoiceId;
    private String iscollected;
    private Integer visit_times;
    private Integer speak_speed;
    private String backup1;
    private String backup2;/**playing pause stop sign  XFUtil PlayOnline PlayOffline**/
    private String backup3;

    public TranResultZhYue(){}

    public TranResultZhYue(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    public TranResultZhYue(String cantonese, String chinese, String toLan) {
        this.english = cantonese;
        this.chinese = chinese;
        this.backup3 = toLan;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEnglish() {
        return this.english;
    }
    public void setEnglish(String english) {
        this.english = english;
    }
    public String getChinese() {
        return this.chinese;
    }
    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
    public String getResultAudioPath() {
        return this.resultAudioPath;
    }
    public void setResultAudioPath(String resultAudioPath) {
        this.resultAudioPath = resultAudioPath;
    }
    public String getQuestionAudioPath() {
        return this.questionAudioPath;
    }
    public void setQuestionAudioPath(String questionAudioPath) {
        this.questionAudioPath = questionAudioPath;
    }
    public String getQuestionVoiceId() {
        return this.questionVoiceId;
    }
    public void setQuestionVoiceId(String questionVoiceId) {
        this.questionVoiceId = questionVoiceId;
    }
    public String getResultVoiceId() {
        return this.resultVoiceId;
    }
    public void setResultVoiceId(String resultVoiceId) {
        this.resultVoiceId = resultVoiceId;
    }
    public String getIscollected() {
        return this.iscollected;
    }
    public void setIscollected(String iscollected) {
        this.iscollected = iscollected;
    }
    public Integer getVisit_times() {
        return this.visit_times;
    }
    public void setVisit_times(Integer visit_times) {
        this.visit_times = visit_times;
    }
    public Integer getSpeak_speed() {
        return this.speak_speed;
    }
    public void setSpeak_speed(Integer speak_speed) {
        this.speak_speed = speak_speed;
    }
    public String getBackup1() {
        return this.backup1;
    }
    public void setBackup1(String backup1) {
        this.backup1 = backup1;
    }
    public String getBackup2() {
        return this.backup2;
    }
    public void setBackup2(String backup2) {
        this.backup2 = backup2;
    }
    public String getBackup3() {
        return this.backup3;
    }
    public void setBackup3(String backup3) {
        this.backup3 = backup3;
    }
}
