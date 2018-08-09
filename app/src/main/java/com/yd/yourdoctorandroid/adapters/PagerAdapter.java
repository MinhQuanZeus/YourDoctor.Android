package com.yd.yourdoctorandroid.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yd.yourdoctorandroid.fragments.HistoryTransactionFragment;
import com.yd.yourdoctorandroid.fragments.HomeFragment;
import com.yd.yourdoctorandroid.fragments.NotifyFragment;
import com.yd.yourdoctorandroid.fragments.SpecialistInfoFragment;

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
            case 2:{
                return new SpecialistInfoFragment();
            }
            case 3:
                return new HistoryTransactionFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

