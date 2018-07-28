package com.yd.yourdoctorandroid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.fragments.LoginFragment;
import com.yd.yourdoctorandroid.fragments.VideoCallFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;

public class VideoCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        ScreenManager.openFragment(getSupportFragmentManager(), new VideoCallFragment(), R.id.fl_video_call, false, true);
    }
}
