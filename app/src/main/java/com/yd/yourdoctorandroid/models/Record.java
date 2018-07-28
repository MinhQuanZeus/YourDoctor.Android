package com.yd.yourdoctorandroid.models;

public class Record {
    private String recorderID;
    private String name;
    private String avatar;
    private int type;
    private String value;
    private String createdAt;

    public Record(String recorderID, String name, String avatar, int type, String value, String createdAt) {
        this.recorderID = recorderID;
        this.name = name;
        this.avatar = avatar;
        this.type = type;
        this.value = value;
        this.createdAt = createdAt;
    }

    public Record() {
    }

    public String getRecorderID() {
        return recorderID;
    }

    public void setRecorderID(String recorderID) {
        this.recorderID = recorderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}


