package com.messi.languagehelper.bean;

/**
 * Created by luli on 28/08/2017.
 */

public class PopMenuItem {

    private int resource_id;
    private String code;

    public PopMenuItem(int resource_id,String code){
        this.resource_id = resource_id;
        this.code = code;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
