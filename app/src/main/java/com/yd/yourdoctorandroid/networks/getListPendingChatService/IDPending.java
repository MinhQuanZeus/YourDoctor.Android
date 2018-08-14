package com.yd.yourdoctorandroid.networks.getListPendingChatService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IDPending {
    @SerializedName("_id")
    @Expose
    private String id;

    private String createdAt;

    private long timeRemain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getTimeRemain() {
        return timeRemain;
    }

    public void setTimeRemain(long timeRemain) {
        this.timeRemain = timeRemain;
    }
}
