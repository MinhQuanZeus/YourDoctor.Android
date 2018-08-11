package com.yd.yourdoctorandroid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nhancv.npermission.NPermission;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.fragments.LoginFragment;
import com.yd.yourdoctorandroid.fragments.VideoCallFragment;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Specialist;

public class VideoCallActivity extends AppCompatActivity implements NPermission.OnPermissionResult{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);
        Specialist specialist = new Specialist();
        ScreenManager.openFragment(getSupportFragmentManager(), new VideoCallFragment().setSpecialist(null), R.id.fl_video_call, false, true);
    }

    @Override
    public void onPermissionResult(String s, boolean b) {
        
    }
}
