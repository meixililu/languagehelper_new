package com.messi.languagehelper.bean;

public class QQTranAILabData {

    private int type;
    private String org_text;
    private String trans_text;
    private String source_text;
    private String target_text;

    public String getSource_text() {
        return source_text;
    }

    public void setSource_text(String source_text) {
        this.source_text = source_text;
    }

    public String getTarget_text() {
        return target_text;
    }

    public void setTarget_text(String target_text) {
        this.target_text = target_text;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setOrg_text(String org_text) {
        this.org_text = org_text;
    }
    public String getOrg_text() {
        return org_text;
    }

    public void setTrans_text(String trans_text) {
        this.trans_text = trans_text;
    }
    public String getTrans_text() {
        return trans_text;
    }
    
}
