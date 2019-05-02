package com.messi.languagehelper.box;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

@Entity
public class MomentLikes {

    @Id
    private long id;
    @Index
    private String moments_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMoments_id() {
        return moments_id;
    }

    public void setMoments_id(String moments_id) {
        this.moments_id = moments_id;
    }
}
