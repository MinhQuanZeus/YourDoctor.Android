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
import com.yd.yourdoctorandroid.adapters.DoctorFavoriteListAdapter;
import com.yd.yourdoctorandroid.adapters.NotificationAdapter;
import com.yd.yourdoctorandroid.events.EventSend;
import com.yd.yourdoctorandroid.managers.PaginationScrollListener;
import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.models.Notification;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.FavoriteDoctor;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.GetListDoctorFavoriteService;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.MainObjectFavoriteList;
import com.yd.yourdoctorandroid.networks.getListNotification.GetListNotificationService;
import com.yd.yourdoctorandroid.networks.getListNotification.MainObjectNotification;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;
import com.yd.yourdoctorandroid.utils.ZoomImageViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
public class NotifyFragment extends Fragment {

    private Context context;
    Unbinder butterKnife;

    private NotificationAdapter notificationAdapter;

    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 0;

    Patient currentPatient;

    @BindView(R.id.rvListNotification)
    RecyclerView rvListNotification;
    @BindView(R.id.pbNotificaton)
    ProgressBar pbNotificaton;


    public NotifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notify, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        notificationAdapter = new NotificationAdapter(getContext());
        setUpListNotification();
        return view;
    }


    private void setUpListNotification() {

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvListNotification.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvListNotification.addItemDecoration(dividerItemDecoration);
        rvListNotification.setItemAnimator(new DefaultItemAnimator());
        rvListNotification.setAdapter(notificationAdapter);

        rvListNotification.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1; //Increment page index to load the next one
                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //pbNotificaton.setVisibility(View.VISIBLE);
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
        if(pbNotificaton != null){
            pbNotificaton.setVisibility(View.VISIBLE);
        }
        GetListNotificationService getListNotificationService = RetrofitFactory.getInstance().createService(GetListNotificationService.class);
        getListNotificationService.getListNotificationService(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),currentPatient.getId(), 10 +"", currentPage + "").enqueue(new Callback<MainObjectNotification>() {
            @Override
            public void onResponse(Call<MainObjectNotification> call, Response<MainObjectNotification> response) {
                if(response.code() == 200){
                    MainObjectNotification mainObject = response.body();
                    Log.e("haha", response.body().toString());
                    List<Notification> notifications = mainObject.getListNotification();
                    if (notifications != null && notifications.size() > 0) {
                        notificationAdapter.addAll(notifications);

                        if (notifications.size() == 10) notificationAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }

                }else if(response.code() == 401){
                    Utils.backToLogin(getActivity().getApplicationContext());
                }
                if(pbNotificaton != null){
                    pbNotificaton.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<MainObjectNotification> call, Throwable t) {
                if(pbNotificaton != null){
                    pbNotificaton.setVisibility(View.GONE);
                }
            }
        });

    }

    private void loadNextPage() {
        GetListNotificationService notificationService = RetrofitFactory.getInstance().createService(GetListNotificationService.class);
        notificationService.getListNotificationService(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),currentPatient.getId() , "10",currentPage + "").enqueue(new Callback<MainObjectNotification>() {
            @Override
            public void onResponse(Call<MainObjectNotification> call, Response<MainObjectNotification> response) {
                if(response.code() == 200){
                    MainObjectNotification mainObject = response.body();
                    // Log.e("haha" , response.body().toString());
                    List<Notification> notifications = mainObject.getListNotification();

                    if (notifications != null && notifications.size() > 0) {

                        notificationAdapter.removeLoadingFooter();  // 2
                        isLoading = false;   // 3

                        notificationAdapter.addAll(notifications);   // 4

                        if (notifications.size() == 5) notificationAdapter.addLoadingFooter();  // 5
                        else isLastPage = true;
                    }
                }else if(response.code() == 401){
                    Utils.backToLogin(getActivity().getApplicationContext());
                }

            }

            @Override
            public void onFailure(Call<MainObjectNotification> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventSend eventSend) {
        if(eventSend.getType() == 3){
            try{
                notificationAdapter.clear();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadFirstPage();
                    }
                }, 1000);
            }catch(Exception e){

            }

        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
