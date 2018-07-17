package com.yd.yourdoctorandroid.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.DoctorFavoriteListAdapter;
import com.yd.yourdoctorandroid.adapters.DoctorRankingSpecialistAdapter;
import com.yd.yourdoctorandroid.managers.PaginationScrollListener;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.FavoriteDoctor;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.GetListDoctorFavoriteService;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.MainObjectFavoriteList;
import com.yd.yourdoctorandroid.networks.models.Doctor;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.DoctorRanking;
import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.GetDoctorRankingSpecialist;
import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.MainObjectRanking;
import com.yd.yourdoctorandroid.networks.models.Patient;
import com.yd.yourdoctorandroid.utils.SharedPrefs;

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
public class DoctorFavoriteListFragment extends Fragment {

    private Context context;
    Unbinder butterKnife;

    @BindView(R.id.rv_list_doctor_favorite)
    RecyclerView rv_list_doctor_favorite;

    @BindView(R.id.tb_favorite)
    Toolbar tb_favorite;

    @BindView(R.id.pb_favorite)
    ProgressBar pb_favorite;

    private DoctorFavoriteListAdapter doctorFavoriteListAdapter;

    LinearLayoutManager linearLayoutManager;

    // private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 0;

    Patient currentPatient;


    public DoctorFavoriteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_favorite_list, container, false);
        butterKnife = ButterKnife.bind(this, view);
        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        doctorFavoriteListAdapter = new DoctorFavoriteListAdapter(getContext());

        setDoctorFavoriteList(rv_list_doctor_favorite);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tb_favorite);
        final ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        actionbar.setTitle(R.string.ranking_doctor);

        tb_favorite.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScreenManager.backFragment(getFragmentManager());

            }
        });

        return view;
    }

    public DoctorFavoriteListFragment setSpecialistId(String specialistId, Context context) {
        this.context = context;
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setDoctorFavoriteList(RecyclerView rv_list_doctor_ranking) {

        // DoctorRankingSpecialistAdapter doctorRankingSpecialistAdaptert = new DoctorRankingSpecialistAdapter(null, context);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_list_doctor_ranking.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rv_list_doctor_ranking.addItemDecoration(dividerItemDecoration);
        rv_list_doctor_ranking.setItemAnimator(new DefaultItemAnimator());
        rv_list_doctor_ranking.setAdapter(doctorFavoriteListAdapter);

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
        Log.e("haha" , currentPatient.getId());
        GetListDoctorFavoriteService getListDoctorFavoriteService = RetrofitFactory.getInstance().createService(GetListDoctorFavoriteService.class);
        getListDoctorFavoriteService.getMainObjectFavoriteList(currentPatient.getId(),currentPage + "","5").enqueue(new Callback<MainObjectFavoriteList>() {
            @Override
            public void onResponse(Call<MainObjectFavoriteList> call, Response<MainObjectFavoriteList> response) {
                MainObjectFavoriteList mainObject = response.body();
                Log.e("haha" , response.body().toString());
                List<FavoriteDoctor> favoriteDoctors = mainObject.getListFavoriteDoctor().getFavoriteDoctors();
                List<Doctor> doctorList = new ArrayList<>();
                if (favoriteDoctors != null && favoriteDoctors.size() > 0) {
                    for (FavoriteDoctor favoriteDoctor : favoriteDoctors) {
                        Doctor doctor = new Doctor();
                        doctor.setAvatar("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg");
                        doctor.setFirstName(favoriteDoctor.getFirstName());
                        doctor.setMiddleName(favoriteDoctor.getLastName());
                        doctor.setLastName( favoriteDoctor.getLastName());
                        doctor.setCurrentRating((float) 3.5);
                        doctor.setDoctorId(favoriteDoctor.get_id());
                        doctorList.add(doctor);
                    }

                    doctorFavoriteListAdapter.addAll(doctorList);


                    if (doctorList.size()==5) doctorFavoriteListAdapter.addLoadingFooter();
                    else isLastPage = true;
                }
                //progressBar.setVisibility(View.GONE);
                pb_favorite.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MainObjectFavoriteList> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
                pb_favorite.setVisibility(View.GONE);
            }
        });

//Fortest

//        List<Doctor> doctorList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            Doctor doctor = new Doctor();
//            doctor.setAvatar("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg");
//            doctor.setFirstName("Lê");
//            doctor.setLastName("Anh");
//            doctor.setMiddleName("Thế");
//            doctor.setCurrentRating((float) 3.5);
//            doctorList.add(doctor);
//        }
//
//
//        doctorFavoriteListAdapter.addAll(doctorList);
//
//
//        if (doctorList.size() == 5) doctorFavoriteListAdapter.addLoadingFooter();
//        else isLastPage = true;
//
//        //progressBar.setVisibility(View.GONE);
//        pb_favorite.setVisibility(View.GONE);
    }

    private void loadNextPage() {
        GetListDoctorFavoriteService getListDoctorFavoriteService = RetrofitFactory.getInstance().createService(GetListDoctorFavoriteService.class);
        getListDoctorFavoriteService.getMainObjectFavoriteList(currentPatient.getId(), currentPage + "","5").enqueue(new Callback<MainObjectFavoriteList>() {
            @Override
            public void onResponse(Call<MainObjectFavoriteList> call, Response<MainObjectFavoriteList> response) {
                MainObjectFavoriteList mainObject = response.body();
                // Log.e("haha" , response.body().toString());
                List<FavoriteDoctor> favoriteDoctors = mainObject.getListFavoriteDoctor().getFavoriteDoctors();
                List<Doctor> doctorList = new ArrayList<>();
                if (favoriteDoctors != null && favoriteDoctors.size() > 0) {
                    for (FavoriteDoctor favoriteDoctor : favoriteDoctors) {
                        Doctor doctor = new Doctor();
                        doctor.setAvatar("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg");
                        doctor.setFirstName(favoriteDoctor.getFirstName());
                        doctor.setMiddleName(favoriteDoctor.getLastName());
                        doctor.setLastName( favoriteDoctor.getLastName());
                        doctor.setCurrentRating((float) 3.5);
                        doctorList.add(doctor);
                    }

                    doctorFavoriteListAdapter.removeLoadingFooter();  // 2
                    isLoading = false;   // 3

                    doctorFavoriteListAdapter.addAll(doctorList);   // 4

                    if (doctorList.size()==5) doctorFavoriteListAdapter.addLoadingFooter();  // 5
                    else isLastPage = true;
                }
                //pb_favorite.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MainObjectFavoriteList> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
                //pb_favorite.setVisibility(View.GONE);
            }
        });


//        GetDoctorRankingSpecialist getDoctorRankingSpecialist = RetrofitFactory.getInstance().createService(GetDoctorRankingSpecialist.class);
//        getDoctorRankingSpecialist.getMainObjectRanking(specialistId, "5", currentPage + "").enqueue(new Callback<MainObjectRanking>() {
//            @Override
//            public void onResponse(Call<MainObjectRanking> call, Response<MainObjectRanking> response) {
//                MainObjectRanking mainObject = response.body();
//                List<DoctorRanking> doctorRankingList = mainObject.getListDoctor();
//                List<Doctor> doctorList = new ArrayList<>();
//                for (DoctorRanking doctorRanking : doctorRankingList) {
//                    Doctor doctor = new Doctor();
//                    doctor.setAvatar("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg");
//                    doctor.setFirst_name(doctorRanking.getDoctorId().getFirstName());
//                    doctor.setLast_name(doctorRanking.getDoctorId().getLastName());
//                    doctor.setCurrent_rating((float) doctorRanking.getCurrentRating());
//                    doctorList.add(doctor);
//                }
//                doctorRankingAdapter.removeLoadingFooter();  // 2
//                isLoading = false;   // 3
//
//                doctorRankingAdapter.addAll(doctorList);   // 4
//
//                if (doctorList.size()==5) doctorRankingAdapter.addLoadingFooter();  // 5
//                else isLastPage = true;
//            }
//
//            @Override
//            public void onFailure(Call<MainObjectRanking> call, Throwable t) {
//                Log.d("Anhle", "Fail: " + t.getMessage());
//            }
//        });

//        List<Doctor> doctorList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            Doctor doctor = new Doctor();
//            doctor.setAvatar("https://kenh14cdn.com/2016/160722-star-tzuyu-1469163381381-1473652430446.jpg");
//            doctor.setFirstName("Lê");
//            doctor.setLastName("Anh");
//            doctor.setMiddleName("Thế");
//            doctor.setCurrentRating((float) 3.5);
//            doctorList.add(doctor);
//        }
//
//        doctorFavoriteListAdapter.removeLoadingFooter();  // 2
//        isLoading = false;   // 3
//
//        doctorFavoriteListAdapter.addAll(doctorList);   // 4
//
//        if (doctorList.size() == 5) doctorFavoriteListAdapter.addLoadingFooter();  // 5
//        else isLastPage = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        butterKnife.unbind();
    }
}
