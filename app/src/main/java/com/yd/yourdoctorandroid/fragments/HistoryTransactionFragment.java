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
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.PagerAdapter;
import com.yd.yourdoctorandroid.events.EventSend;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @BindView(R.id.tv_remain_money_history)
    TextView tvRemainMoneyHistory;

    private HistoryTransactionFragment.ViewPagerAdapter adapter;

    Patient patient;

    public HistoryTransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_transaction, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        setUpData();
        return view;
    }


    private void setUpData(){
        patient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        if(patient != null) tvRemainMoneyHistory.setText("Số dư tài khoản : " + Utils.formatStringNumber(patient.getRemainMoney()) +" đ");


        tabHistory.addTab(tabHistory.newTab().setText("Chat"));
        tabHistory.addTab(tabHistory.newTab().setText("Video call"));
        tabHistory.addTab(tabHistory.newTab().setText("Thanh Toán"));

        adapter = new HistoryTransactionFragment.ViewPagerAdapter(getFragmentManager(),3);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventSend eventSend) {
        if(eventSend.getType() == 1){
            patient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
            if(patient != null){
                tvRemainMoneyHistory.setText("Số dư tài khoản : " + Utils.formatStringNumber(patient.getRemainMoney()) +" đ");
            }
        }
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

            }
            return null;

        }

        @Override
        public int getCount() {
            return numberPage;
        }

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
