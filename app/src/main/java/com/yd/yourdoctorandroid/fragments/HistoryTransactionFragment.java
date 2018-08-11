package com.yd.yourdoctorandroid.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.models.Specialist;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTransactionFragment extends Fragment {

    @BindView(R.id.tabHistory)
    TabLayout tabHistory;

    @BindView(R.id.vpHistory)
    ViewPager vpHistory;


    public HistoryTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_transaction, container, false);
        ButterKnife.bind(this, view);
        setUpData();
        return view;
    }
    private void setUpData(){
        vpHistory.setCurrentItem(0);
        tabHistory.setupWithViewPager(vpHistory);
        tabHistory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpHistory.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        HistoryTransactionFragment.ViewPagerAdapter adapter = new HistoryTransactionFragment.ViewPagerAdapter(getFragmentManager());
        vpHistory.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        //private List typeHistory;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            //this.typeHistory = typeHistory;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:{
                    return new ListChatHistoryFragment();
                }
                case 1:{
                    return new NotifyFragment();
                }
                case 2:{
                    return new ListPaymentHistoryFragment();
                }
                case 3:{
                    return new NotifyFragment();
                }
            }
            return null;

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 :{
                    return "Chat";
                }
                case 1 :{
                    return "Video Call";
                }
                case 2 :{
                    return "Thanh toán";
                }
                case 3 :{
                    return "Ngân Hàng";
                }
            }
            return "Chat";
        }
    }

}
