package com.yd.yourdoctorandroid.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.DoctorRankingSpecialistAdapter;
import com.yd.yourdoctorandroid.managers.PaginationScrollListener;
import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.DoctorRanking;
import com.yd.yourdoctorandroid.networks.models.Doctor;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.GetDoctorRankingSpecialist;
import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.MainObjectRanking;

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
public class ListDoctorRankingSpecialistFragment extends Fragment {

    private String specialistId;
    private Context context;
    Unbinder butterKnife;

    @BindView(R.id.rv_list_doctor_ranking)
    RecyclerView rv_list_doctor_ranking;

    @BindView(R.id.pb_ranking)
    ProgressBar progressBar;

    private DoctorRankingSpecialistAdapter doctorRankingAdapter;

    LinearLayoutManager linearLayoutManager;

    // private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 0;


    public ListDoctorRankingSpecialistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_doctor_ranking_specialist, container, false);
        butterKnife = ButterKnife.bind(this, view);
        doctorRankingAdapter = new DoctorRankingSpecialistAdapter(getContext());
        setDoctorRankList(specialistId, rv_list_doctor_ranking);

        return view;
    }

    public ListDoctorRankingSpecialistFragment setSpecialistId(String specialistId, Context context) {
        this.specialistId = specialistId;
        this.context = context;
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
        //butterKnife = ButterKnife.bind(this, view);
    }

    private void setDoctorRankList(String specialistId, RecyclerView rv_list_doctor_ranking) {


        // DoctorRankingSpecialistAdapter doctorRankingSpecialistAdaptert = new DoctorRankingSpecialistAdapter(null, context);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_doctor_ranking.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rv_list_doctor_ranking.addItemDecoration(dividerItemDecoration);
        rv_list_doctor_ranking.setItemAnimator(new DefaultItemAnimator());
        rv_list_doctor_ranking.setAdapter(doctorRankingAdapter);

        rv_list_doctor_ranking.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1; //Increment page index to load the next one
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadFirstPage();
            }
        }, 1000);

        //doctorRankingAdapter.notifyDataSetChanged();
    }

    private void loadFirstPage() {
        GetDoctorRankingSpecialist getDoctorRankingSpecialist = RetrofitFactory.getInstance().createService(GetDoctorRankingSpecialist.class);
        getDoctorRankingSpecialist.getMainObjectRanking(specialistId, "5", currentPage + "").enqueue(new Callback<MainObjectRanking>() {
            @Override
            public void onResponse(Call<MainObjectRanking> call, Response<MainObjectRanking> response) {
                MainObjectRanking mainObject = response.body();
                Log.e("haha" , response.body().toString());
                List<DoctorRanking> doctorRankingList = mainObject.getListDoctor();
                List<Doctor> doctorList = new ArrayList<>();
                if (doctorRankingList != null && doctorRankingList.size() > 0) {
                    for (DoctorRanking doctorRanking : doctorRankingList) {
                        Doctor doctor = new Doctor();
                        doctor.setAvatar(doctorRanking.getAvatar());
                        doctor.setFirstName(doctorRanking.getFirstName());
                        doctor.setLastName(doctorRanking.getLastName());
                        doctor.setMiddleName(doctorRanking.getMiddleName());
                        doctor.setCurrentRating((float) doctorRanking.getCurrentRating());
                        doctor.setDoctorId(doctorRanking.getDoctorId());
                        doctorList.add(doctor);
                    }


                    doctorRankingAdapter.addAll(doctorList);


                    if (doctorRankingList.size()==5) doctorRankingAdapter.addLoadingFooter();
                    else isLastPage = true;
                }
                //progressBar.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MainObjectRanking> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });


    }

    private void loadNextPage() {

        GetDoctorRankingSpecialist getDoctorRankingSpecialist = RetrofitFactory.getInstance().createService(GetDoctorRankingSpecialist.class);
        getDoctorRankingSpecialist.getMainObjectRanking(specialistId, "5", currentPage + "").enqueue(new Callback<MainObjectRanking>() {
            @Override
            public void onResponse(Call<MainObjectRanking> call, Response<MainObjectRanking> response) {
                MainObjectRanking mainObject = response.body();
                List<DoctorRanking> doctorRankingList = mainObject.getListDoctor();
                List<Doctor> doctorList = new ArrayList<>();
                for (DoctorRanking doctorRanking : doctorRankingList) {
                    Doctor doctor = new Doctor();
                    doctor.setAvatar(doctorRanking.getAvatar());
                    doctor.setFirstName(doctorRanking.getFirstName());
                    doctor.setLastName(doctorRanking.getLastName());
                    doctor.setMiddleName(doctorRanking.getMiddleName());
                    doctor.setCurrentRating((float) doctorRanking.getCurrentRating());
                    doctor.setDoctorId(doctorRanking.getDoctorId());
                    doctorList.add(doctor);
                }
                doctorRankingAdapter.removeLoadingFooter();  // 2
                isLoading = false;   // 3

                doctorRankingAdapter.addAll(doctorList);   // 4

                if (doctorList.size()==5) doctorRankingAdapter.addLoadingFooter();  // 5
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<MainObjectRanking> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
    }
}
