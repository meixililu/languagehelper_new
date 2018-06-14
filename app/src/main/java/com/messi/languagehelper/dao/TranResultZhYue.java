package com.messi.languagehelper.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by luli on 2018/6/13.
 */

@Entity
public class TranResultZhYue {

    @Id(autoincrement = true)
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

    @Generated(hash = 1682891612)
    public TranResultZhYue(Long id, String english, String chinese, String resultAudioPath,
            String questionAudioPath, String questionVoiceId, String resultVoiceId,
            String iscollected, Integer visit_times, Integer speak_speed, String backup1,
            String backup2, String backup3) {
        this.id = id;
        this.english = english;
        this.chinese = chinese;
        this.resultAudioPath = resultAudioPath;
        this.questionAudioPath = questionAudioPath;
        this.questionVoiceId = questionVoiceId;
        this.resultVoiceId = resultVoiceId;
        this.iscollected = iscollected;
        this.visit_times = visit_times;
        this.speak_speed = speak_speed;
        this.backup1 = backup1;
        this.backup2 = backup2;
        this.backup3 = backup3;
    }

    public TranResultZhYue(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    public TranResultZhYue(String cantonese, String chinese, String toLan) {
        this.english = cantonese;
        this.chinese = chinese;
        this.backup3 = toLan;
    }

    @Generated(hash = 1932584421)
    public TranResultZhYue() {
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
