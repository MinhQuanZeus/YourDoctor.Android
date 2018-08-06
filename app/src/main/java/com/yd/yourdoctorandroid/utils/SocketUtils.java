package com.yd.yourdoctorandroid.utils;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.Patient;

import java.net.URISyntaxException;

public class SocketUtils {
    //http://192.168.124.109:3000
    //https://your-doctor-test2.herokuapp.com
    private final static String URL_SERVER = "http://192.168.124.104:3000";
    private Socket mSocket;
    private static SocketUtils socketUtils;

    public SocketUtils() {
        try {
            mSocket = IO.socket(URL_SERVER);

        } catch (URISyntaxException e) {

        }
    }

    public static SocketUtils getInstance(){
        if(socketUtils == null){
            socketUtils = new SocketUtils();
        }
        return socketUtils;
    }

    public Socket getSocket(){
        return mSocket;
    }

    public void reConnect(){
        if(SharedPrefs.getInstance().get("USER_INFO", Patient.class) != null){
            getInstance().getSocket().connect();
            SocketUtils.getInstance().getSocket().emit("addUser",SharedPrefs.getInstance().get("USER_INFO", Patient.class).getId());
        }

    }

    public void disconnectConnect(){
        if(SharedPrefs.getInstance().get("USER_INFO", Patient.class) != null){
            getInstance().getSocket().disconnect();
        }

    }
}
