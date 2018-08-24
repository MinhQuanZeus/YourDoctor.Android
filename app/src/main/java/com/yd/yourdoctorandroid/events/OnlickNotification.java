package com.yd.yourdoctorandroid.events;

import com.yd.yourdoctorandroid.models.Notification;

public class OnlickNotification {
    private Notification notification;

    public OnlickNotification(Notification notification) {
        this.notification = notification;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
