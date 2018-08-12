package com.yd.yourdoctorandroid;

import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.yd.yourdoctorandroid.utils.RxScheduler;

import org.json.JSONObject;

import java.net.URISyntaxException;


public class YourDoctorApplication extends android.app.Application{
    private static YourDoctorApplication mSelf;
    private Gson mGSon;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.VIDEO_CALL_SERVER_URL);
            if (mSocket != null){
                mSocket.on("connect", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        RxScheduler.runOnUi(o -> {
                            try {
                                Log.d("YourDoctorApplication", "On Connect");
                            } catch (Exception e) {

                            }
                        });
                    }
                });
            }
        } catch (URISyntaxException e) {
            Log.d("YourDoctorApplication", e.getMessage());
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

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            RxScheduler.runOnUi(o -> {
                try {
                    Log.d("YourDoctorApplication", "On Connect");
                } catch (Exception e) {

                }
            });
        }
    };
}
