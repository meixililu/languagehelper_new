package com.messi.languagehelper.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Means {

    @Id(autoincrement = true)
    private Long id;
    private String mean;
    private String resultVoiceId;
    private String resultAudioPath;
    private Long partsId;

    @Generated(hash = 2113365818)
    public Means(Long id, String mean, String resultVoiceId, String resultAudioPath,
            Long partsId) {
        this.id = id;
        this.mean = mean;
        this.resultVoiceId = resultVoiceId;
        this.resultAudioPath = resultAudioPath;
        this.partsId = partsId;
    }

    @Generated(hash = 2028550332)
    public Means() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
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

    public Long getPartsId() {
        return partsId;
    }

    public void setPartsId(Long partsId) {
        this.partsId = partsId;
    }

}
