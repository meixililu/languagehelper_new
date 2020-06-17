package com.messi.languagehelper.event;

import com.jeremyliao.liveeventbus.core.LiveEvent;

/**
 * Created by luli on 24/02/2018.
 */

public class SubjectSubscribeEvent implements LiveEvent {

    private String objectID;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }
}
