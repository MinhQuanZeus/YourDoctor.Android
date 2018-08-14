package com.yd.yourdoctorandroid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessaging;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.fragments.LoginFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.SocketUtils;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
            ScreenManager.openFragment(getSupportFragmentManager(), new LoginFragment(), R.id.fl_auth, false, false);
    }
}
