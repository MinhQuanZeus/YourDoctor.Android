package com.yd.yourdoctorandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yd.yourdoctorandroid.networks.getListNotification.SenderId;

public class Notification {

    @SerializedName("_id")
    @Expose
    private String id;
    private String nameSender;
    private String receiverId;
    private int type;
    private String storageId;
    private String message;
    private long createdAt;
    private SenderId senderId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SenderId getSenderId() {
        return senderId;
    }

    public void setSenderId(SenderId senderId) {
        this.senderId = senderId;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
