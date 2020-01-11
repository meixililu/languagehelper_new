package com.messi.languagehelper.box;

import com.avos.avoscloud.AVObject;
import com.baidu.mobads.AdView;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.iflytek.voiceads.conn.NativeDataRef;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;

@Entity
public class Reading {

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
    private String type_id;
    private String level;
    private String content_type;
    private String img_urls;
    private String status;
    private String vid;
    private String isCollected;
    private long collected_time;
    private String isReadLater;
    private long read_later_time;
    private int like;
    private int unlike;
    private int comments;
    private int readed;
    private String lrc_url;
    private String backup1;
    private String backup2;
    private String backup3;
    private String backup4;
    private String backup5;
    private int img_color;
    @Transient
    private boolean isPlaying;
    @Transient
    private boolean isAd;
    @Transient
    private boolean isAdShow;
    @Transient
    private NativeDataRef mNativeADDataRef;
    @Transient
    private NativeExpressADView mTXADView;
    @Transient
    private TTFeedAd csjTTFeedAd;
    @Transient
    private AdView bdAdView;
    @Transient
    private int bdHeight;
    @Transient
    private List<AVObject> xvideoList;

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
}
