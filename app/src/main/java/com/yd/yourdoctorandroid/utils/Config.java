package com.yd.yourdoctorandroid.utils;

public class Config {

    // Global topic.
    public static final String TOPIC_GLOBAL = "global";

    // Intent filter cho broadcast receiver.
    public static final String REGISTRATION_COMPLETE = "registration_complete";
    public static final String PUSH_NOTIFICATION = "push_notification";

    // ID để xử lý notification, phân theo từng loại một.
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    // Key cho SharedPreferences.
    public static final String SHARED_PREF = "pref_firebase";
}
