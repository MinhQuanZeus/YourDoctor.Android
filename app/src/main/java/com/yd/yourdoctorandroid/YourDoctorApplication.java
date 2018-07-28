package com.yd.yourdoctorandroid;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import java.net.URISyntaxException;


public class YourDoctorApplication extends android.app.Application{
    private static YourDoctorApplication mSelf;
    private Gson mGSon;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.VIDEO_CALL_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public static YourDoctorApplication self() {
        return mSelf;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSelf = this;
        mGSon = new Gson();
    }

    public Gson getGSon() {
        return mGSon;
    }

    public Socket getSocket() {
        return mSocket;
    }
}
