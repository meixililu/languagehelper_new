package com.messi.languagehelper.box;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Record {

    @Id
    private Long id;
    private String english;
    private String chinese;
    private String resultAudioPath;
    private String questionAudioPath;
    private String questionVoiceId;
    private String resultVoiceId;
    private String iscollected;
    private String ph_am_mp3;
    private String ph_en_mp3;
    private String ph_tts_mp3;
    private String des;
    private String examples;
    private String paraphrase;
    private String en_paraphrase;
    private String au_paraphrase;
    private String dicts;
    private String examinations;
    private String root;
    private String tense;
    private String type;
    private Integer visit_times;
    private Integer speak_speed;
    private String backup1;
    private String backup2;/**playing pause stop sign  XFUtil PlayOnline PlayOffline**/
    private String backup3;

    public Record() {}

    public Record(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    public String getPh_am_mp3() {
        return ph_am_mp3;
    }

    public void setPh_am_mp3(String ph_am_mp3) {
        this.ph_am_mp3 = ph_am_mp3;
    }

    public String getPh_en_mp3() {
        return ph_en_mp3;
    }

    public void setPh_en_mp3(String ph_en_mp3) {
        this.ph_en_mp3 = ph_en_mp3;
    }

    public String getPh_tts_mp3() {
        return ph_tts_mp3;
    }

    public void setPh_tts_mp3(String ph_tts_mp3) {
        this.ph_tts_mp3 = ph_tts_mp3;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getParaphrase() {
        return paraphrase;
    }

    public void setParaphrase(String paraphrase) {
        this.paraphrase = paraphrase;
    }

    public String getEn_paraphrase() {
        return en_paraphrase;
    }

    public void setEn_paraphrase(String en_paraphrase) {
        this.en_paraphrase = en_paraphrase;
    }

    public String getAu_paraphrase() {
        return au_paraphrase;
    }

    public void setAu_paraphrase(String au_paraphrase) {
        this.au_paraphrase = au_paraphrase;
    }

    public String getDicts() {
        return dicts;
    }

    public void setDicts(String dicts) {
        this.dicts = dicts;
    }

    public String getExaminations() {
        return examinations;
    }

    public void setExaminations(String examinations) {
        this.examinations = examinations;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getTense() {
        return tense;
    }

    public void setTense(String tense) {
        this.tense = tense;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getResultAudioPath() {
        return resultAudioPath;
    }

    public void setResultAudioPath(String resultAudioPath) {
        this.resultAudioPath = resultAudioPath;
    }

    public String getQuestionAudioPath() {
        return questionAudioPath;
    }

    public void setQuestionAudioPath(String questionAudioPath) {
        this.questionAudioPath = questionAudioPath;
    }

    public String getQuestionVoiceId() {
        return questionVoiceId;
    }

    public void setQuestionVoiceId(String questionVoiceId) {
        this.questionVoiceId = questionVoiceId;
    }

    public String getResultVoiceId() {
        return resultVoiceId;
    }

    public void setResultVoiceId(String resultVoiceId) {
        this.resultVoiceId = resultVoiceId;
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

}
