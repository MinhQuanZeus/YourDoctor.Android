package com.yd.yourdoctorandroid.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yd.yourdoctorandroid.utils.Config;
import com.yd.yourdoctorandroid.utils.SharedPrefs;

public class YDFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = YDFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if(refreshedToken != null){
            SharedPrefs.getInstance().put("regId",refreshedToken);
        }
    }

}
