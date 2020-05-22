package com.messi.languagehelper.bean;

import java.util.List;

public class TranResultRoot<T> {

    private int code;
    private String error;
    private List<T> result;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
    public List<T> getResult() {
        return result;
    }

}
