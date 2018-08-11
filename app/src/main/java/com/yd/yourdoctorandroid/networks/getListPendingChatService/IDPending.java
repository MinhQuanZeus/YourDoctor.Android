package com.yd.yourdoctorandroid.networks.getListPendingChatService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IDPending {
    @SerializedName("_id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
