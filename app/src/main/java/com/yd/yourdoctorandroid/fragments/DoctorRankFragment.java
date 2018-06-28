package com.yd.yourdoctorandroid.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.network.RetrofitFactory;
import com.yd.yourdoctorandroid.network.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.network.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.network.getSpecialistService.Specialist;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorRankFragment extends Fragment {

    @BindView(R.id.tab_specialists)
    TabLayout tab_specialists;

    @BindView(R.id.vp_doctorRanking)
    ViewPager vp_doctorRanking;

    @BindView(R.id.tb_logo_specialist)
    Toolbar tb_logo_specialist;

    Unbinder butterKnife;
    private List<Specialist> specialists = new ArrayList<>();

    public DoctorRankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_rank, container, false);

        setUpSpecialists();

        butterKnife = ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(tb_logo_specialist);
        final ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle(R.string.ranking_doctor);

        tb_logo_specialist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScreenManager.backFragment(getFragmentManager());

            }
        });

        tab_specialists.setupWithViewPager(vp_doctorRanking);
        tab_specialists.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                vp_doctorRanking.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }


    private void setUpSpecialists() {

        GetSpecialistService getSpecialistService = RetrofitFactory.getInstence().createService(GetSpecialistService.class);
        getSpecialistService.getMainObjectSpecialist().enqueue(new Callback<MainObjectSpecialist>() {
            @Override
            public synchronized void onResponse(Call<MainObjectSpecialist> call, Response<MainObjectSpecialist> response) {
                Log.e("AnhLe", "success: " + response.body());
                MainObjectSpecialist mainObjectSpecialist = response.body();
                List<Specialist> specialist = mainObjectSpecialist.getSpecialist();

                specialists = specialist;
                setupViewPager(vp_doctorRanking);
                vp_doctorRanking.setCurrentItem(0);

            }

            @Override
            public synchronized void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                Log.e("AnhLe", "Fail: " + t.getMessage());
            }
        });

    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager(), specialists);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter

    {

        private List specialists;

        public ViewPagerAdapter(FragmentManager fm, List<Specialist> specialists) {
            super(fm);
            this.specialists = specialists;
        }


        @Override
        public Fragment getItem(int position) {
            return new ListDoctorRankingSpecialistFragment().setSpecialistId(((Specialist) specialists.get(position)).get_id(), getContext());

        }

        @Override
        public int getCount() {
            return specialists.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Specialist specialist = (Specialist) specialists.get(position);
            return specialist.getName();
        }
    }

}

