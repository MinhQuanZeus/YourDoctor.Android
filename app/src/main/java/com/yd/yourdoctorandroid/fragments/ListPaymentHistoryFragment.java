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
import android.widget.Toast;

import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.ChatHistoryAdapter;
import com.yd.yourdoctorandroid.adapters.PaymentHistoryAdapter;
import com.yd.yourdoctorandroid.managers.PaginationScrollListener;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getAllChatHistory.ChatHistoryInfoResponse;
import com.yd.yourdoctorandroid.networks.getAllChatHistory.GetAllHistoryChatService;
import com.yd.yourdoctorandroid.networks.getAllChatHistory.MainObjectHistoryChat;
import com.yd.yourdoctorandroid.networks.getPaymentHistory.GetPaymentHistoryListService;
import com.yd.yourdoctorandroid.networks.getPaymentHistory.MainHistoryPaymentResponse;
import com.yd.yourdoctorandroid.networks.getPaymentHistory.ObjectPaymentResponse;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.Utils;

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
public class ListPaymentHistoryFragment extends Fragment {

    private Context context;
    Unbinder butterKnife;

    private PaymentHistoryAdapter paymentHistoryAdapter;

    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 0;


    @BindView(R.id.rvListPaymentHistory)
    RecyclerView rvListPaymentHistory;
    @BindView(R.id.pbListPaymentHistory)
    ProgressBar pbListPaymentHistory;

    Patient currentPatient;


    public ListPaymentHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_payment_history, container, false);
        ButterKnife.bind(this, view);
        paymentHistoryAdapter = new PaymentHistoryAdapter(getContext());
        currentPatient = SharedPrefs.getInstance().get("USER_INFO", Patient.class);
        setUpListPaymentHistory();
        return view;
    }


    private void setUpListPaymentHistory() {

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvListPaymentHistory.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvListPaymentHistory.addItemDecoration(dividerItemDecoration);
        rvListPaymentHistory.setItemAnimator(new DefaultItemAnimator());
        rvListPaymentHistory.setAdapter(paymentHistoryAdapter);

        rvListPaymentHistory.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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
        GetPaymentHistoryListService getPaymentHistoryListService = RetrofitFactory.getInstance().createService(GetPaymentHistoryListService.class);
        getPaymentHistoryListService.getPaymentHistoryListService(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), currentPatient.getId(), 10 + "", currentPage + "").enqueue(new Callback<MainHistoryPaymentResponse>() {
            @Override
            public void onResponse(Call<MainHistoryPaymentResponse> call, Response<MainHistoryPaymentResponse> response) {
                if (response.code() == 200) {
                    MainHistoryPaymentResponse mainObject = response.body();
                    Log.e("haha", response.body().toString());
                    List<ObjectPaymentResponse> objectPaymentResponses = mainObject.getListPaymentHistory();
                    Log.e("objectPaymentResponses",objectPaymentResponses.size() + "");
                    if (objectPaymentResponses != null && objectPaymentResponses.size() > 0) {
                        paymentHistoryAdapter.addAll(objectPaymentResponses);

                        if (objectPaymentResponses.size() == 10)
                            paymentHistoryAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }

                } else if (response.code() == 401) {
                    Utils.backToLogin(getActivity().getApplicationContext());
                }
                if(pbListPaymentHistory != null){
                    pbListPaymentHistory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MainHistoryPaymentResponse> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
                Toast.makeText(getContext(),"Không thể tải được lịch sử thanh toán", Toast.LENGTH_LONG).show();
                if(pbListPaymentHistory != null){
                    pbListPaymentHistory.setVisibility(View.GONE);
                }
            }
        });

    }

    private void loadNextPage() {
        GetPaymentHistoryListService getPaymentHistoryListService = RetrofitFactory.getInstance().createService(GetPaymentHistoryListService.class);
        getPaymentHistoryListService.getPaymentHistoryListService(SharedPrefs.getInstance().get("JWT_TOKEN", String.class), currentPatient.getId(), 10 + "", currentPage + "").enqueue(new Callback<MainHistoryPaymentResponse>() {
            @Override
            public void onResponse(Call<MainHistoryPaymentResponse> call, Response<MainHistoryPaymentResponse> response) {
                if (response.code() == 200) {
                    MainHistoryPaymentResponse mainObject = response.body();
                    // Log.e("haha" , response.body().toString());
                    List<ObjectPaymentResponse> objectPaymentResponses = mainObject.getListPaymentHistory();

                    if (objectPaymentResponses != null && objectPaymentResponses.size() > 0) {


                        paymentHistoryAdapter.removeLoadingFooter();  // 2
                        isLoading = false;   // 3

                        paymentHistoryAdapter.addAll(objectPaymentResponses);   // 4

                        if (objectPaymentResponses.size() == 5)
                            paymentHistoryAdapter.addLoadingFooter();  // 5
                        else isLastPage = true;
                    }
                } else if (response.code() == 401) {
                    Utils.backToLogin(getActivity().getApplicationContext());
                }
                if(pbListPaymentHistory != null){
                    pbListPaymentHistory.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<MainHistoryPaymentResponse> call, Throwable t) {
                Log.d("Anhle", "Fail: " + t.getMessage());
                Toast.makeText(getContext(),"Không thể tải thêm được lịch sử thanh toán", Toast.LENGTH_LONG).show();
                if(pbListPaymentHistory != null){
                    pbListPaymentHistory.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
