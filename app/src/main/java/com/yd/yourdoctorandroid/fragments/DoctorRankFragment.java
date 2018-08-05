package com.yd.yourdoctorandroid.fragments;


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
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
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


        tabSpecialists.setupWithViewPager(vpDoctorRanking);
        tabSpecialists.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                vpDoctorRanking.setCurrentItem(tab.getPosition());

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
        specialists = (ArrayList<Specialist>) LoadDefaultModel.getInstance().getSpecialists();

        if(specialists == null){
            GetSpecialistService getSpecialistService = RetrofitFactory.getInstance().createService(GetSpecialistService.class);
            getSpecialistService.getMainObjectSpecialist().enqueue(new Callback<MainObjectSpecialist>() {
                @Override
                public  void onResponse(Call<MainObjectSpecialist> call, Response<MainObjectSpecialist> response) {

                    if(response.code() == 200){
                        Log.e("AnhLe", "success: " + response.body());
                        MainObjectSpecialist mainObjectSpecialist = response.body();
                        specialists = (ArrayList<Specialist>) mainObjectSpecialist.getSpecialist();
                        LoadDefaultModel.getInstance().setSpecialists(specialists);
                        setupViewPager(vpDoctorRanking);
                        vpDoctorRanking.setCurrentItem(0);
                        progessBar.setVisibility(View.GONE);
                    }else if(response.code() == 401){
                        Utils.backToLogin(getContext());
                    }

                }

                @Override
                public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                    Toast.makeText(getContext(), "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                    progessBar.setVisibility(View.GONE);
                }
            });

        }else {
            setupViewPager(vpDoctorRanking);
            vpDoctorRanking.setCurrentItem(0);
            progessBar.setVisibility(View.GONE);
        }

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

