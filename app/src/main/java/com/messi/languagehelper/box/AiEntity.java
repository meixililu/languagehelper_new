package com.messi.languagehelper.box;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class AiEntity {

    @Id
    private Long id;
    private String entity_type;
    private String content_type;
    private String role;
    private String content;
    private String content_video_id;
    private String content_video_path;
    private String img_url;
    private String link;
    private String translate;
    private String ai_type;
    private Long created;
    private String backup1;
    private String backup2;
    private String backup3;
    private String backup4;
    private String backup5;
    private String backup6;
    private String backup7;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEntity_type() {
        return this.entity_type;
    }
    public void setEntity_type(String entity_type) {
        this.entity_type = entity_type;
    }
    public String getContent_type() {
        return this.content_type;
    }
    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }
    public String getRole() {
        return this.role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContent_video_id() {
        return this.content_video_id;
    }
    public void setContent_video_id(String content_video_id) {
        this.content_video_id = content_video_id;
    }
    public String getContent_video_path() {
        return this.content_video_path;
    }
    public void setContent_video_path(String content_video_path) {
        this.content_video_path = content_video_path;
    }
    public String getImg_url() {
        return this.img_url;
    }
    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
    public String getLink() {
        return this.link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public String getTranslate() {
        return this.translate;
    }
    public void setTranslate(String translate) {
        this.translate = translate;
    }
    public String getAi_type() {
        return this.ai_type;
    }
    public void setAi_type(String ai_type) {
        this.ai_type = ai_type;
    }
    public Long getCreated() {
        return this.created;
    }
    public void setCreated(Long created) {
        this.created = created;
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
    public String getBackup6() {
        return this.backup6;
    }
    public void setBackup6(String backup6) {
        this.backup6 = backup6;
    }
    public String getBackup7() {
        return this.backup7;
    }
    public void setBackup7(String backup7) {
        this.backup7 = backup7;
    }


}
