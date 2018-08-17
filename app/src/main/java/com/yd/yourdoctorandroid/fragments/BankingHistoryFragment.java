package com.yd.yourdoctorandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yd.yourdoctorandroid.R;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BankingHistoryFragment extends Fragment {
//    @BindView(R.id.rvListBankingHistory)
//    RecyclerView rvListBankingHistory;
//    @BindView(R.id.pbListBankingHistory)
//    ProgressBar pbListBankingHistory;

    public BankingHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_banking_history, container, false);
    }

}
