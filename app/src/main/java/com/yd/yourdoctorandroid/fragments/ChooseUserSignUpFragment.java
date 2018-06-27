package com.yd.yourdoctorandroid.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.ScreenManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseUserSignUpFragment extends Fragment {

    private TextView btnPatientLogin;
    private TextView btnDoctorLogin;

    public ChooseUserSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_user_sign_up, container, false);
        setUp(view);
        return view;
    }

    private void setUp(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tb_main);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle(R.string.sign_up);
        toolbar.setTitleTextColor(getResources().getColor(R.color.primary_text));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnPatientLogin = (TextView) view.findViewById(R.id.btn_patient);
        btnDoctorLogin = (TextView) view.findViewById(R.id.btn_doctor);

        btnDoctorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.openFragment(getActivity().getSupportFragmentManager(),new InputPhoneNumberFragment(),R.id.fl_auth, true, true);
            }
        });

        btnPatientLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.openFragment(getActivity().getSupportFragmentManager(),new InputPhoneNumberFragment(),R.id.fl_auth, true, true);
            }
        });
    }

}
