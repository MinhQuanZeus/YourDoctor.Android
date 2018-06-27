package com.yd.yourdoctorandroid.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yd.yourdoctorandroid.fragments.DoctorRankFragment;
import com.yd.yourdoctorandroid.fragments.HomeFragment;
import com.yd.yourdoctorandroid.fragments.MenuFragment;
import com.yd.yourdoctorandroid.fragments.NotifyFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numOfTabs = numberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NotifyFragment();
            case 1:
                return new HomeFragment();
            case 2:
                return new DoctorRankFragment();
            case 3:
                return new MenuFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

