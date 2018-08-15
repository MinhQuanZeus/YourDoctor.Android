package com.yd.yourdoctorandroid.fragments;


import android.graphics.PorterDuff;
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
import com.yd.yourdoctorandroid.adapters.PagerAdapter;
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

    private HistoryTransactionFragment.ViewPagerAdapter adapter;

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

        tabHistory.addTab(tabHistory.newTab().setText("Chat"));
        tabHistory.addTab(tabHistory.newTab().setText("Video call"));
        tabHistory.addTab(tabHistory.newTab().setText("Thanh Toán"));
        tabHistory.addTab(tabHistory.newTab().setText("Ngân Hàng"));


        adapter = new HistoryTransactionFragment.ViewPagerAdapter(getFragmentManager(),4);
        vpHistory.setAdapter(adapter);
        vpHistory.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabHistory));
        tabHistory.getTabAt(0).select();
        vpHistory.setCurrentItem(0);

        tabHistory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpHistory.setCurrentItem(tab.getPosition());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        //private List typeHistory;
        private int numberPage;
        public ViewPagerAdapter(FragmentManager fm,int numberPage) {
            super(fm);
            this.numberPage = numberPage;
            //this.typeHistory = typeHistory;
        }


        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:{
                    return new ListChatHistoryFragment();
                }
                case 1:{
                    return new VideoCallHistoryFragment();
                }
                case 2:{
                    return new ListPaymentHistoryFragment();
                }
                case 3:{
                    return new BankingHistoryFragment();
                }
            }
            return null;

        }

        @Override
        public int getCount() {
            return numberPage;
        }

    }

}
