package com.yd.yourdoctorandroid.events;

public class EventSend {
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
