package com.yd.yourdoctorandroid.fragments;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.PagerAdapter;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorRankFragment extends Fragment {

    @BindView(R.id.tabSpecialists)
    TabLayout tabSpecialists;

    @BindView(R.id.vpDoctorRanking)
    ViewPager vpDoctorRanking;

    @BindView(R.id.tbLogoSpecialist)
    Toolbar tbLogoSpecialist;

    @BindView(R.id.progessBar)
    ProgressBar progessBar;


    Unbinder butterKnife;
    private List<Specialist> specialists = new ArrayList<>();

    private ViewPagerAdapter adapter;

    public DoctorRankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_rank, container, false);
        butterKnife = ButterKnife.bind(this, view);
        setUpSpecialists();


        tbLogoSpecialist.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tbLogoSpecialist.setTitle(getResources().getString(R.string.ranking_doctor));
        tbLogoSpecialist.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tbLogoSpecialist.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScreenManager.backFragment(getFragmentManager());
            }
        });


        return view;
    }

    private void setUpSpecialists() {
        specialists = LoadDefaultModel.getInstance().getSpecialists();

        if (specialists == null) {
            GetSpecialistService getSpecialistService = RetrofitFactory.getInstance().createService(GetSpecialistService.class);
            getSpecialistService.getMainObjectSpecialist().enqueue(new Callback<MainObjectSpecialist>() {
                @Override
                public void onResponse(Call<MainObjectSpecialist> call, Response<MainObjectSpecialist> response) {

                    if (response.code() == 200) {
                        MainObjectSpecialist mainObjectSpecialist = response.body();
                        specialists = mainObjectSpecialist.getListSpecialist();
                        LoadDefaultModel.getInstance().setSpecialists(specialists);
                        setupViewPager();
                    } else if (response.code() == 401) {
                        Utils.backToLogin(getActivity().getApplicationContext());
                    }
                    if(progessBar != null){
                        progessBar.setVisibility(View.GONE);
                    }
                    //progessBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                    Log.e("loi mang", t.toString());
                    Toast.makeText(getContext(), "Kết nối mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                    if(progessBar != null){
                        progessBar.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            if(progessBar != null){
                progessBar.setVisibility(View.GONE);
            }
            setupViewPager();

        }

    }

    private void setupViewPager() {
        for (Specialist specialist : specialists) {
            tabSpecialists.addTab(tabSpecialists.newTab().setText(specialist.getName()));
        }

        adapter = new ViewPagerAdapter(this.getChildFragmentManager(), specialists);
        vpDoctorRanking.setAdapter(adapter);
        vpDoctorRanking.setOffscreenPageLimit(1);
        //vpDoctorRanking.notify();
        vpDoctorRanking.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabSpecialists));

        tabSpecialists.getTabAt(0).select();
        vpDoctorRanking.setCurrentItem(0);

        tabSpecialists.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpDoctorRanking.setCurrentItem(tab.getPosition());
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //butterKnife.unbind();
    }


    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private List specialists;

        public ViewPagerAdapter(FragmentManager fm, List<Specialist> specialists) {
            super(fm);
            this.specialists = specialists;
        }


        @Override
        public Fragment getItem(int position) {
            return new ListDoctorRankingSpecialistFragment().setSpecialistId(((Specialist) specialists.get(position)).getId(), getContext());

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

