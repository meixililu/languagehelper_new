package com.messi.languagehelper.bean;

public class ChDicIdiom {

    private String reason;

    private ChDicIdiomContent result;

    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ChDicIdiomContent getResult() {
        return result;
    }

    public void setResult(ChDicIdiomContent result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}