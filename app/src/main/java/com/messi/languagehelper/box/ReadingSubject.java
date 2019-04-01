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
    private String name;
    private String category;
    private String level;
    private String code;
    private String order;
    private long creat_time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeString(this.level);
        dest.writeString(this.code);
        dest.writeString(this.order);
        dest.writeLong(this.creat_time);
    }

    public ReadingSubject() {
    }

    protected ReadingSubject(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.category = in.readString();
        this.level = in.readString();
        this.code = in.readString();
        this.order = in.readString();
        this.creat_time = in.readLong();
    }

    public static final Parcelable.Creator<ReadingSubject> CREATOR = new Parcelable.Creator<ReadingSubject>() {
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
