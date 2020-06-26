package com.messi.languagehelper.box;


import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

@Entity
public class ReadingSubject implements Parcelable {

    @Id
    private long id;
    @Index
    private String objectId;
    private String name;
    private String category;
    private String source_name;
    private String source_url;
    private int imgId;
    private String level;
    private String code;
    private String order;
    private int views;
    private long creat_time;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public long getCreat_time() {
        return creat_time;
    }

    public void setCreat_time(long creat_time) {
        this.creat_time = creat_time;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.objectId);
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeString(this.source_name);
        dest.writeString(this.source_url);
        dest.writeString(this.level);
        dest.writeString(this.code);
        dest.writeString(this.order);
        dest.writeInt(this.views);
        dest.writeInt(this.imgId);
        dest.writeLong(this.creat_time);
    }

    public ReadingSubject() {
    }

    protected ReadingSubject(Parcel in) {
        this.id = in.readLong();
        this.objectId = in.readString();
        this.name = in.readString();
        this.category = in.readString();
        this.source_name = in.readString();
        this.source_url = in.readString();
        this.level = in.readString();
        this.code = in.readString();
        this.order = in.readString();
        this.views = in.readInt();
        this.imgId = in.readInt();
        this.creat_time = in.readLong();
    }

    public static final Creator<ReadingSubject> CREATOR = new Creator<ReadingSubject>() {
        @Override
        public ReadingSubject createFromParcel(Parcel source) {
            return new ReadingSubject(source);
        }

        @Override
        public ReadingSubject[] newArray(int size) {
            return new ReadingSubject[size];
        }
    };
}
