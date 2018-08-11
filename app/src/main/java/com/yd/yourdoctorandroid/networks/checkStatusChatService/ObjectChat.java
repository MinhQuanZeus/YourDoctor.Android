package com.yd.yourdoctorandroid.networks.checkStatusChatService;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjectChat {
    @SerializedName("ChatsHistoryID")
    @Expose
    private String chatsHistoryID;

    private boolean statusDone;

    public String getChatsHistoryID() {
        return chatsHistoryID;
    }

    public void setChatsHistoryID(String chatsHistoryID) {
        this.chatsHistoryID = chatsHistoryID;
    }

    public boolean isStatusDone() {
        return statusDone;
    }

    public void setStatusDone(boolean statusDone) {
        this.statusDone = statusDone;
    }
}
