package com.yd.yourdoctorandroid.networks.getListPendingChatService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IDPending {
    @SerializedName("_id")
    @Expose
    private String id;

    private long createdAt;

    private long timeRemain;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getTimeRemain() {
        return timeRemain;
    }

    public void setTimeRemain(long timeRemain) {
        this.timeRemain = timeRemain;
    }
}
