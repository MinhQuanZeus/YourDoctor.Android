package com.yd.yourdoctorandroid.networks.getChatHistory;

public class MainRecord {
    private String _id;
    private String recorderID;
    private int type;
    private String value;
    private long createdAt;;

    public MainRecord(String _id, String recorderID, int type, String value, long createdAt) {
        this._id = _id;
        this.recorderID = recorderID;
        this.type = type;
        this.value = value;
        this.createdAt = createdAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRecorderID() {
        return recorderID;
    }

    public void setRecorderID(String recorderID) {
        this.recorderID = recorderID;
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

    public long getCreated() {
        return createdAt;
    }

    public void setCreated(long createdAt) {
        this.createdAt = createdAt;
    }
}
