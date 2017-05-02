package com.messi.languagehelper.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class EveryDaySentence {

    @Id(autoincrement = true)
    private Long id;
    private Long cid;
    private String sid;
    private String tts;
    private String tts_local_position;
    private String content;
    private String note;
    private String love;
    private String translation;
    private String picture;
    private String picture2;
    private String caption;
    private String dateline;
    private String s_pv;
    private String sp_pv;
    private String fenxiang_img;
    private String fenxiang_img_local_position;
    private String backup1;
    private String backup2;
    private String backup3;
    @Transient
    private boolean isPlaying;

    @Generated(hash = 603994386)
    public EveryDaySentence(Long id, Long cid, String sid, String tts,
            String tts_local_position, String content, String note, String love,
            String translation, String picture, String picture2, String caption,
            String dateline, String s_pv, String sp_pv, String fenxiang_img,
            String fenxiang_img_local_position, String backup1, String backup2,
            String backup3) {
        this.id = id;
        this.cid = cid;
        this.sid = sid;
        this.tts = tts;
        this.tts_local_position = tts_local_position;
        this.content = content;
        this.note = note;
        this.love = love;
        this.translation = translation;
        this.picture = picture;
        this.picture2 = picture2;
        this.caption = caption;
        this.dateline = dateline;
        this.s_pv = s_pv;
        this.sp_pv = sp_pv;
        this.fenxiang_img = fenxiang_img;
        this.fenxiang_img_local_position = fenxiang_img_local_position;
        this.backup1 = backup1;
        this.backup2 = backup2;
        this.backup3 = backup3;
    }

    @Generated(hash = 2087339357)
    public EveryDaySentence() {
    }

    public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}

   

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public String getTts_local_position() {
        return tts_local_position;
    }

    public void setTts_local_position(String tts_local_position) {
        this.tts_local_position = tts_local_position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getS_pv() {
        return s_pv;
    }

    public void setS_pv(String s_pv) {
        this.s_pv = s_pv;
    }

    public String getSp_pv() {
        return sp_pv;
    }

    public void setSp_pv(String sp_pv) {
        this.sp_pv = sp_pv;
    }

    public String getFenxiang_img() {
        return fenxiang_img;
    }

    public void setFenxiang_img(String fenxiang_img) {
        this.fenxiang_img = fenxiang_img;
    }

    public String getFenxiang_img_local_position() {
        return fenxiang_img_local_position;
    }

    public void setFenxiang_img_local_position(String fenxiang_img_local_position) {
        this.fenxiang_img_local_position = fenxiang_img_local_position;
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

    public boolean getIsPlaying() {
        return this.isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}
