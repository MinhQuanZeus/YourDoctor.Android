package com.yd.yourdoctorandroid.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.yd.yourdoctorandroid.utils.Config;

public class YDFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = YDFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Log.e("RR", "Regresh");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Lưu token vào SharedPreferences để sử dụng về sau.
        storeRegIdInPref(refreshedToken);
        Log.e("haha" + TAG, refreshedToken);
        // Gửi token lên server.
        sendRegistrationToServer(refreshedToken);

        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // Hàm gửi token lên server, cần xử lý bằng AsyncTask hoặc thread riêng.
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
    }
}
