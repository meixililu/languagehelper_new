package com.messi.languagehelper.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;

@Entity
public class Dictionary {

    @Id(autoincrement = true)
    private Long id;
    private String word_name;
    private String result;  /**for item display**/
    private String to;
    private String from;
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

    @ToMany(referencedJoinProperty = "dictionaryId")
    private List<Parts> partList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 965981863)
    private transient DictionaryDao myDao;

    @Generated(hash = 863251322)
    public Dictionary(Long id, String word_name, String result, String to, String from,
            String ph_am, String ph_en, String ph_zh, String type, String questionVoiceId,
            String questionAudioPath, String resultVoiceId, String resultAudioPath,
            String iscollected, Integer visit_times, Integer speak_speed, String backup1,
            String backup2, String backup3, String backup4, String backup5) {
        this.id = id;
        this.word_name = word_name;
        this.result = result;
        this.to = to;
        this.from = from;
        this.ph_am = ph_am;
        this.ph_en = ph_en;
        this.ph_zh = ph_zh;
        this.type = type;
        this.questionVoiceId = questionVoiceId;
        this.questionAudioPath = questionAudioPath;
        this.resultVoiceId = resultVoiceId;
        this.resultAudioPath = resultAudioPath;
        this.iscollected = iscollected;
        this.visit_times = visit_times;
        this.speak_speed = speak_speed;
        this.backup1 = backup1;
        this.backup2 = backup2;
        this.backup3 = backup3;
        this.backup4 = backup4;
        this.backup5 = backup5;
    }

    @Generated(hash = 487998537)
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
        if(from.equals("en")){
        	String[] words = word_name.split(" ");
        	if(words.length > 1){
        		backup3 = word_name;
        	}
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 940927618)
    public List<Parts> getPartList() {
        if (partList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PartsDao targetDao = daoSession.getPartsDao();
            List<Parts> partListNew = targetDao._queryDictionary_PartList(id);
            synchronized (this) {
                if (partList == null) {
                    partList = partListNew;
                }
            }
        }
        return partList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 141659070)
    public synchronized void resetPartList() {
        partList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1544509509)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDictionaryDao() : null;
    }

}
