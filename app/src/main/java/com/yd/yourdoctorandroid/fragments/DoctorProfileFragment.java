package com.yd.yourdoctorandroid.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.DoctorCertificationAdapter;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Certification;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.CropTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorProfileFragment extends Fragment {

    @BindView(R.id.iv_ava_doctor)
    ImageView iv_ava_doctor;

    @BindView(R.id.tv_name_doctor)
    TextView tv_name_doctor;

    @BindView(R.id.rb_doctorranking)
    RatingBar rb_doctorranking;

    @BindView(R.id.tb_back_from_profile_doctor)
    Toolbar tb_back_from_profile_doctor;

    @BindView(R.id.rl_certification_doctor)
    RecyclerView rl_certification_doctor;
    Unbinder butterKnife;

    public DoctorProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        setupUI(view);

        return view;
    }

    private void setupUI(View view){

        butterKnife = ButterKnife.bind(DoctorProfileFragment.this, view);
        tv_name_doctor.setText("Lê Thế Anh");
        rb_doctorranking.setRating((float) 3);
        rb_doctorranking.setMax(5);

        ((AppCompatActivity)getActivity()).setSupportActionBar(tb_back_from_profile_doctor);
        final ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle("Trang cá nhân bác sĩ");

        tb_back_from_profile_doctor.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getFragmentManager());
            }
        });

        //TEST
        Picasso.with(getContext()).load("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg").transform(new CropCircleTransformation()).into(iv_ava_doctor);
        Resources res = getResources();
        String text = String.format(res.getString(R.string.introduce_doctor_text), "27/03/1990", "123 lê thanh nghị hà nội", "Da liễu , Chấn thương ngoại", "Đại học Y Hà Nội","2013", "Bệnh viện bạch mai hà nội");
        TextView textView = view.findViewById(R.id.tv_introduce_doctor);
        textView.setText(text);

        rl_certification_doctor.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rl_certification_doctor.setFocusable(false);
        List<Certification> certificationList = new ArrayList<>();

        //Test
        for(int i= 0 ; i < 5 ; i++){
            certificationList.add(new Certification("0", "Chứng chỉ xuất sắc", "http://bacsilanda.com.vn/wp-content/uploads/2016/11/Giay-chung-nhan-y-hoc-va-vat-ly-tri-lieu.jpg"));
        }
        DoctorCertificationAdapter doctorCertificationAdapter = new DoctorCertificationAdapter(certificationList,getContext());
        rl_certification_doctor.setAdapter(doctorCertificationAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        butterKnife.unbind();
    }
}
