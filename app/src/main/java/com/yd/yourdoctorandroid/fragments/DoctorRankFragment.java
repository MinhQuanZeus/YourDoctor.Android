package com.yd.yourdoctorandroid.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
        butterKnife = ButterKnife.bind(this, view);
        specialists = LoadDefaultModel.getInstance().getSpecialists();
        setupViewPager(vp_doctorRanking);
        vp_doctorRanking.setCurrentItem(0);

//        ((AppCompatActivity) getActivity()).setSupportActionBar(tb_logo_specialist);
//        final ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
//        actionbar.setTitle(R.string.ranking_doctor);
//
//        tb_logo_specialist.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        tb_logo_specialist.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tb_logo_specialist.setTitle(getResources().getString(R.string.ranking_doctor));
        tb_logo_specialist.setTitleTextColor(getResources().getColor(R.color.primary_text));
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

