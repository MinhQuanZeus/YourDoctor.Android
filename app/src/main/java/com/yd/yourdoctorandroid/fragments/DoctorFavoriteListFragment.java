package com.yd.yourdoctorandroid.fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.DoctorFavoriteListAdapter;
import com.yd.yourdoctorandroid.managers.PaginationScrollListener;
import com.yd.yourdoctorandroid.managers.ScreenManager;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.FavoriteDoctor;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.GetListDoctorFavoriteService;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.MainObjectFavoriteList;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.models.Patient;
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
public class DoctorFavoriteListFragment extends Fragment {

    private Context context;
    Unbinder butterKnife;

    @BindView(R.id.rvListDoctorFavorite)
    RecyclerView rvListDoctorFavorite;

    @BindView(R.id.tbFavorite)
    Toolbar tbFavorite;

    @BindView(R.id.pbFavorite)
    ProgressBar pbFavorite;

    private DoctorFavoriteListAdapter doctorFavoriteListAdapter;

    LinearLayoutManager linearLayoutManager;
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
        ButterKnife.bind(this, view);
        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        doctorFavoriteListAdapter = new DoctorFavoriteListAdapter(getContext());

        setUpSpecialists();


        tbFavorite.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        tbFavorite.setTitle(getResources().getString(R.string.favorite_doctor));
        tbFavorite.setTitleTextColor(getResources().getColor(R.color.primary_text));
        tbFavorite.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScreenManager.backFragment(getFragmentManager());
            }
        });

        return view;
    }

    private void setUpSpecialists() {
        if (LoadDefaultModel.getInstance().getSpecialists() == null) {
            GetSpecialistService getSpecialistService = RetrofitFactory.getInstance().createService(GetSpecialistService.class);
            getSpecialistService.getMainObjectSpecialist().enqueue(new Callback<MainObjectSpecialist>() {
                @Override
                public void onResponse(Call<MainObjectSpecialist> call, Response<MainObjectSpecialist> response) {
                    Log.e("AnhLe", "success: " + response.body());
                    MainObjectSpecialist mainObjectSpecialist = response.body();
                    LoadDefaultModel.getInstance().setSpecialists((ArrayList<Specialist>) mainObjectSpecialist.getListSpecialist());
                    setDoctorFavoriteList(rvListDoctorFavorite);

                }

                @Override
                public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                    Log.e("loi mang" ,t.toString());
                    Toast.makeText(getContext(), "Kết nối mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                    pbFavorite.setVisibility(View.GONE);
                }
            });

        } else {
            setDoctorFavoriteList(rvListDoctorFavorite);
        }

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
                        pbFavorite.setVisibility(View.VISIBLE);
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
        Log.e("haha", currentPatient.getId());
        GetListDoctorFavoriteService getListDoctorFavoriteService = RetrofitFactory.getInstance().createService(GetListDoctorFavoriteService.class);
        getListDoctorFavoriteService.getMainObjectFavoriteList(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),currentPatient.getId(), currentPage + "", "5").enqueue(new Callback<MainObjectFavoriteList>() {
            @Override
            public void onResponse(Call<MainObjectFavoriteList> call, Response<MainObjectFavoriteList> response) {
                if(response.code() == 200){
                    MainObjectFavoriteList mainObject = response.body();
                    Log.e("haha", response.body().toString());
                    List<FavoriteDoctor> favoriteDoctors = mainObject.getListFavoriteDoctor().getFavoriteDoctors();
                    List<Doctor> doctorList = new ArrayList<>();
                    if (favoriteDoctors != null && favoriteDoctors.size() > 0) {
                        for (FavoriteDoctor favoriteDoctor : favoriteDoctors) {
                            Doctor doctor = new Doctor();
                            doctor.setAvatar(favoriteDoctor.getAvatar());
                            doctor.setFirstName(favoriteDoctor.getFirstName());
                            doctor.setMiddleName(favoriteDoctor.getMiddleName());
                            doctor.setLastName(favoriteDoctor.getLastName());
                            doctor.setCurrentRating((float) 3.5);
                            doctor.setDoctorId(favoriteDoctor.get_id());
                            doctorList.add(doctor);
                        }

                        doctorFavoriteListAdapter.addAll(doctorList);

                        if (doctorList.size() == 5) doctorFavoriteListAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                    pbFavorite.setVisibility(View.GONE);
                }else if(response.code() == 401){
                    Utils.backToLogin(getContext());
                }

            }

            @Override
            public void onFailure(Call<MainObjectFavoriteList> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
                pbFavorite.setVisibility(View.GONE);
            }
        });

    }

    private void loadNextPage() {
        GetListDoctorFavoriteService getListDoctorFavoriteService = RetrofitFactory.getInstance().createService(GetListDoctorFavoriteService.class);
        getListDoctorFavoriteService.getMainObjectFavoriteList(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),currentPatient.getId(), currentPage + "", "5").enqueue(new Callback<MainObjectFavoriteList>() {
            @Override
            public void onResponse(Call<MainObjectFavoriteList> call, Response<MainObjectFavoriteList> response) {
                if(response.code() == 200){
                    MainObjectFavoriteList mainObject = response.body();
                    // Log.e("haha" , response.body().toString());
                    List<FavoriteDoctor> favoriteDoctors = mainObject.getListFavoriteDoctor().getFavoriteDoctors();
                    List<Doctor> doctorList = new ArrayList<>();
                    if (favoriteDoctors != null && favoriteDoctors.size() > 0) {
                        for (FavoriteDoctor favoriteDoctor : favoriteDoctors) {
                            Doctor doctor = new Doctor();
                            doctor.setAvatar(favoriteDoctor.getAvatar());
                            doctor.setFirstName(favoriteDoctor.getFirstName());
                            doctor.setMiddleName(favoriteDoctor.getLastName());
                            doctor.setLastName(favoriteDoctor.getLastName());
                            doctor.setCurrentRating((float) 3.5);
                            doctorList.add(doctor);
                        }

                        doctorFavoriteListAdapter.removeLoadingFooter();  // 2
                        isLoading = false;   // 3

                        doctorFavoriteListAdapter.addAll(doctorList);   // 4

                        if (doctorList.size() == 5) doctorFavoriteListAdapter.addLoadingFooter();  // 5
                        else isLastPage = true;
                    }
                    pbFavorite.setVisibility(View.GONE);
                }else if(response.code() == 401){
                    Utils.backToLogin(getContext());
                }

            }

            @Override
            public void onFailure(Call<MainObjectFavoriteList> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
                pbFavorite.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
