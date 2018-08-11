package com.yd.yourdoctorandroid.networks.getPaymentHistory;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetPaymentHistoryListService {
    @GET("paymentshistorys/getPaymentHistoryByUser/{id_patient}?")
    Call<MainHistoryPaymentResponse> getPaymentHistoryListService(@Header("Authorization") String jwt,
                                                                @Path("id_patient") String id_patient,
                                                                @Query("pageSize") String pageSize,
                                                                @Query("page") String page);
}
