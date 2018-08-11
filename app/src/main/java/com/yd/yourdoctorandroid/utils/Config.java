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

    public static final int TIME_OUT_CHAT_CONVERSATION = 10800;

    //192.168.124.113
    //http://103.221.220.186:3000/api/
    public static final String URL_SERVER = "http://103.221.220.186:3000/api/";
    //http://103.221.220.186:3000
    public static final String URL_SOCKET = "http://103.221.220.186:3000";
}
