package com.yd.yourdoctorandroid.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.fragments.LoginFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.services.CheckNetWordChangeService;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.SocketUtils;

import java.net.Socket;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        if(SharedPrefs.getInstance().get("USER_INFO", Patient.class) != null){
            FirebaseMessaging.getInstance().subscribeToTopic(SharedPrefs.getInstance().get("USER_INFO", Patient.class).getId());
            LoadDefaultModel.getInstance().registerServiceCheckNetwork(getApplicationContext());
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            ScreenManager.openFragment(getSupportFragmentManager(), new LoginFragment(), R.id.fl_auth, false, false);
        }
    }
}
