package com.yd.yourdoctorandroid.events;

public class EventSend {
    // 1: thay doi thong tin nguoi dung hien tai
    private int type;

    public EventSend(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
