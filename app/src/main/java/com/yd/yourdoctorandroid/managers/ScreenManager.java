package com.yd.yourdoctorandroid.managers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yd.yourdoctorandroid.R;

public class ScreenManager {

    public static void openFragment(FragmentManager fragmentManager, Fragment fragment,
                                    int layoutID, boolean addToBackStack, boolean haveAnimation){
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction()
                .replace(layoutID, fragment);
        if(addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        if(haveAnimation){
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right,
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right);
        }
        fragmentTransaction.commit();
    }

    public static void backFragment(FragmentManager fragmentManager){
        fragmentManager.popBackStack();
    }
}
