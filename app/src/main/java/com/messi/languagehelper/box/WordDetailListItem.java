package com.messi.languagehelper.box;


import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;

@Entity
public class WordDetailListItem implements Parcelable {

    @Id
    private Long id;
    @Index
    private String item_id;
    @Index
    private String class_id;
    @Index
    private Integer course;
    private String class_title;
    private String desc;
    @Index
    private String name;
    private String sound;
    private String symbol;
    private String examples;
    private String mp3_sdpath;
    private String img_url;
    private String new_words;
    private String is_study;
    private String level;

    private String paraphrase;
    private String en_paraphrase;
    private String au_paraphrase;
    private String dicts;
    private String examinations;
    private String root;
    private String tense;
    private String type;
    private boolean is_know;

    private String backup1;
    private String backup2;
    private String backup3;
    private String backup4;
    private String backup5;
    private String backup6;
    private String backup7;
    @Transient
    private int select_time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIs_know() {
        return is_know;
    }

    public void setIs_know(boolean is_know) {
        this.is_know = is_know;
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

    public String getBackup6() {
        return backup6;
    }

    public void setBackup6(String backup6) {
        this.backup6 = backup6;
    }

    public String getBackup7() {
        return backup7;
    }

    public void setBackup7(String backup7) {
        this.backup7 = backup7;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setSelect_Time(){
        select_time++;
    }

    public int getSelect_time() {
        return select_time;
    }

    public void setSelect_time(int select_time) {
        this.select_time = select_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public String getClass_title() {
        return class_title;
    }

    public void setClass_title(String class_title) {
        this.class_title = class_title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getMp3_sdpath() {
        return mp3_sdpath;
    }

    public void setMp3_sdpath(String mp3_sdpath) {
        this.mp3_sdpath = mp3_sdpath;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getNew_words() {
        return new_words;
    }

    public void setNew_words(String new_words) {
        this.new_words = new_words;
    }

    public String getIs_study() {
        return is_study;
    }

    public void setIs_study(String is_study) {
        this.is_study = is_study;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.item_id);
        dest.writeString(this.class_id);
        dest.writeValue(this.course);
        dest.writeString(this.class_title);
        dest.writeString(this.desc);
        dest.writeString(this.name);
        dest.writeString(this.sound);
        dest.writeString(this.symbol);
        dest.writeString(this.examples);
        dest.writeString(this.mp3_sdpath);
        dest.writeString(this.img_url);
        dest.writeString(this.new_words);
        dest.writeString(this.is_study);
        dest.writeString(this.level);
        dest.writeString(this.paraphrase);
        dest.writeString(this.en_paraphrase);
        dest.writeString(this.au_paraphrase);
        dest.writeString(this.dicts);
        dest.writeString(this.examinations);
        dest.writeString(this.root);
        dest.writeString(this.tense);
        dest.writeString(this.type);
        dest.writeByte(this.is_know ? (byte) 1 : (byte) 0);
        dest.writeString(this.backup1);
        dest.writeString(this.backup2);
        dest.writeString(this.backup3);
        dest.writeString(this.backup4);
        dest.writeString(this.backup5);
        dest.writeString(this.backup6);
        dest.writeString(this.backup7);
        dest.writeInt(this.select_time);
    }

    public WordDetailListItem() {
    }

    protected WordDetailListItem(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.item_id = in.readString();
        this.class_id = in.readString();
        this.course = (Integer) in.readValue(Integer.class.getClassLoader());
        this.class_title = in.readString();
        this.desc = in.readString();
        this.name = in.readString();
        this.sound = in.readString();
        this.symbol = in.readString();
        this.examples = in.readString();
        this.mp3_sdpath = in.readString();
        this.img_url = in.readString();
        this.new_words = in.readString();
        this.is_study = in.readString();
        this.level = in.readString();
        this.paraphrase = in.readString();
        this.en_paraphrase = in.readString();
        this.au_paraphrase = in.readString();
        this.dicts = in.readString();
        this.examinations = in.readString();
        this.root = in.readString();
        this.tense = in.readString();
        this.type = in.readString();
        this.is_know = in.readByte() != 0;
        this.backup1 = in.readString();
        this.backup2 = in.readString();
        this.backup3 = in.readString();
        this.backup4 = in.readString();
        this.backup5 = in.readString();
        this.backup6 = in.readString();
        this.backup7 = in.readString();
        this.select_time = in.readInt();
    }

    public static final Parcelable.Creator<WordDetailListItem> CREATOR = new Parcelable.Creator<WordDetailListItem>() {
        @Override
        public WordDetailListItem createFromParcel(Parcel source) {
            return new WordDetailListItem(source);
        }

        @Override
        public WordDetailListItem[] newArray(int size) {
            return new WordDetailListItem[size];
        }
    };
}
