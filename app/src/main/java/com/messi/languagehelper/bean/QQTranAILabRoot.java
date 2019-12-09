package com.messi.languagehelper.bean;

public class QQTranAILabRoot {

    private int ret;
    private String msg;
    private QQTranAILabData data;
    public void setRet(int ret) {
        this.ret = ret;
    }
    public int getRet() {
        return ret;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setData(QQTranAILabData data) {
        this.data = data;
    }
    public QQTranAILabData getData() {
        return data;
    }

}
