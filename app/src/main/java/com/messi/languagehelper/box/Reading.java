package com.messi.languagehelper.box;

import android.os.Parcel;
import android.os.Parcelable;

import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

import cn.leancloud.AVObject;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;

@Entity
public class Reading implements Parcelable {

    @Id
    private Long id;
    @Index
    private String object_id;
    private String title;
    private String source_url;
    private String content;
    private String item_id;
    private String media_url;
    private String source_name;
    private String publish_time;
    private String type_name;
    private String img_type;
    private String img_url;
    private String type;
    private String category;
    private String category_2;
    private String type_id;
    private String level;
    private String content_type;
    private String img_urls;
    private String status;
    private String vid;
    private String isCollected;
    private String boutique_code;
    private long collected_time;
    private String isReadLater;
    private long read_later_time;
    private int like;
    private int unlike;
    private int comments;
    private int readed;
    private String lrc_url;
    private String backup1;//use for mp3url
    private String backup2;//category_2 play series
    private String backup3;
    private String backup4;
    private String backup5;
    private int img_color;
    @Transient
    private transient boolean isPlaying;
    @Transient
    private transient boolean isAd;
    @Transient
    private transient boolean isAdShow;
    @Transient
    private transient NativeDataRef mNativeADDataRef;
    @Transient
    private transient NativeExpressADView mTXADView;
    @Transient
    private transient TTFeedAd csjTTFeedAd;
    @Transient
    private transient AdView bdAdView;
    @Transient
    private transient int bdHeight;
    @Transient
    private transient List<AVObject> xvideoList;

    public AdView getBdAdView() {
        return bdAdView;
    }

    public void setBdAdView(AdView bdAdView) {
        this.bdAdView = bdAdView;
    }

    public TTFeedAd getCsjTTFeedAd() {
        return csjTTFeedAd;
    }

    public void setCsjTTFeedAd(TTFeedAd csjTTFeedAd) {
        this.csjTTFeedAd = csjTTFeedAd;
    }

    public int getBdHeight() {
        return bdHeight;
    }

    public void setBdHeight(int bdHeight) {
        this.bdHeight = bdHeight;
    }

    public List<AVObject> getXvideoList() {
        return xvideoList;
    }

    public void setXvideoList(List<AVObject> xvideoList) {
        this.xvideoList = xvideoList;
    }

    public NativeExpressADView getmTXADView() {
        return mTXADView;
    }

    public void setmTXADView(NativeExpressADView mTXADView) {
        this.mTXADView = mTXADView;
    }

    public long getRead_later_time() {
        return read_later_time;
    }

    public void setRead_later_time(long read_later_time) {
        this.read_later_time = read_later_time;
    }

    public long getCollected_time() {
        return collected_time;
    }

    public void setCollected_time(long collected_time) {
        this.collected_time = collected_time;
    }

    public int getImg_color() {
        return img_color;
    }

    public void setImg_color(int img_color) {
        this.img_color = img_color;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAdShow() {
        return isAdShow;
    }

    public void setAdShow(boolean adShow) {
        isAdShow = adShow;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean ad) {
        isAd = ad;
    }

    public NativeDataRef getmNativeADDataRef() {
        return mNativeADDataRef;
    }

    public void setmNativeADDataRef(NativeDataRef mNativeADDataRef) {
        this.mNativeADDataRef = mNativeADDataRef;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSource_url() {
        return this.source_url;
    }
    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getMedia_url() {
        return this.media_url;
    }
    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }
    public String getSource_name() {
        return this.source_name;
    }
    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }
    public String getPublish_time() {
        return this.publish_time;
    }
    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }
    public String getType_name() {
        return this.type_name;
    }
    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
    public String getItem_id() {
        return this.item_id;
    }
    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
    public String getImg_type() {
        return this.img_type;
    }
    public void setImg_type(String img_type) {
        this.img_type = img_type;
    }
    public String getImg_url() {
        return this.img_url;
    }
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getCategory() {
        return this.category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getType_id() {
        return this.type_id;
    }
    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
    public String getLevel() {
        return this.level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getContent_type() {
        return this.content_type;
    }
    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }
    public String getImg_urls() {
        return this.img_urls;
    }
    public void setImg_urls(String img_urls) {
        this.img_urls = img_urls;
    }

    public String getBoutique_code() {
        return boutique_code;
    }

    public void setBoutique_code(String boutique_code) {
        this.boutique_code = boutique_code;
    }

    public String getIsCollected() {
        return this.isCollected;
    }

    public void setIsCollected(String isCollected) {
        this.isCollected = isCollected;
    }

    public String getIsReadLater() {
        return this.isReadLater;
    }

    public void setIsReadLater(String isReadLater) {
        this.isReadLater = isReadLater;
    }

    public int getLike() {
        return this.like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getUnlike() {
        return this.unlike;
    }

    public void setUnlike(int unlike) {
        this.unlike = unlike;
    }

    public int getComments() {
        return this.comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getReaded() {
        return this.readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
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

    public String getBackup4() {
        return this.backup4;
    }

    public void setBackup4(String backup4) {
        this.backup4 = backup4;
    }

    public String getBackup5() {
        return this.backup5;
    }

    public void setBackup5(String backup5) {
        this.backup5 = backup5;
    }

    public String getLrc_url() {
        return this.lrc_url;
    }

    public void setLrc_url(String lrc_url) {
        this.lrc_url = lrc_url;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getCategory_2() {
        return category_2;
    }

    public void setCategory_2(String category_2) {
        this.category_2 = category_2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.object_id);
        dest.writeString(this.title);
        dest.writeString(this.source_url);
        dest.writeString(this.content);
        dest.writeString(this.item_id);
        dest.writeString(this.media_url);
        dest.writeString(this.source_name);
        dest.writeString(this.publish_time);
        dest.writeString(this.type_name);
        dest.writeString(this.img_type);
        dest.writeString(this.img_url);
        dest.writeString(this.type);
        dest.writeString(this.category);
        dest.writeString(this.category_2);
        dest.writeString(this.type_id);
        dest.writeString(this.level);
        dest.writeString(this.content_type);
        dest.writeString(this.img_urls);
        dest.writeString(this.status);
        dest.writeString(this.vid);
        dest.writeString(this.isCollected);
        dest.writeString(this.boutique_code);
        dest.writeLong(this.collected_time);
        dest.writeString(this.isReadLater);
        dest.writeLong(this.read_later_time);
        dest.writeInt(this.like);
        dest.writeInt(this.unlike);
        dest.writeInt(this.comments);
        dest.writeInt(this.readed);
        dest.writeString(this.lrc_url);
        dest.writeString(this.backup1);
        dest.writeString(this.backup2);
        dest.writeString(this.backup3);
        dest.writeString(this.backup4);
        dest.writeString(this.backup5);
        dest.writeInt(this.img_color);
        dest.writeByte(this.isPlaying ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAd ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isAdShow ? (byte) 1 : (byte) 0);
        dest.writeInt(this.bdHeight);
    }

    public Reading() {
    }

    protected Reading(Parcel in) {
        this.id = in.readLong();
        this.object_id = in.readString();
        this.title = in.readString();
        this.source_url = in.readString();
        this.content = in.readString();
        this.item_id = in.readString();
        this.media_url = in.readString();
        this.source_name = in.readString();
        this.publish_time = in.readString();
        this.type_name = in.readString();
        this.img_type = in.readString();
        this.img_url = in.readString();
        this.type = in.readString();
        this.category = in.readString();
        this.category_2 = in.readString();
        this.type_id = in.readString();
        this.level = in.readString();
        this.content_type = in.readString();
        this.img_urls = in.readString();
        this.status = in.readString();
        this.vid = in.readString();
        this.isCollected = in.readString();
        this.boutique_code = in.readString();
        this.collected_time = in.readLong();
        this.isReadLater = in.readString();
        this.read_later_time = in.readLong();
        this.like = in.readInt();
        this.unlike = in.readInt();
        this.comments = in.readInt();
        this.readed = in.readInt();
        this.lrc_url = in.readString();
        this.backup1 = in.readString();
        this.backup2 = in.readString();
        this.backup3 = in.readString();
        this.backup4 = in.readString();
        this.backup5 = in.readString();
        this.img_color = in.readInt();
        this.isPlaying = in.readByte() != 0;
        this.isAd = in.readByte() != 0;
        this.isAdShow = in.readByte() != 0;
        this.bdHeight = in.readInt();
    }

    public static final Parcelable.Creator<Reading> CREATOR = new Parcelable.Creator<Reading>() {
        @Override
        public Reading createFromParcel(Parcel source) {
            return new Reading(source);
        }

        @Override
        public Reading[] newArray(int size) {
            return new Reading[size];
        }
    };
}
