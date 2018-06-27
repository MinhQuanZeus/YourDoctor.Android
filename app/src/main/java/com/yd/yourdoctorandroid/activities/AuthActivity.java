package com.yd.yourdoctorandroid.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.fragments.LoginFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ScreenManager.openFragment(getSupportFragmentManager(), new LoginFragment(), R.id.fl_auth, false, false);
    }
}
