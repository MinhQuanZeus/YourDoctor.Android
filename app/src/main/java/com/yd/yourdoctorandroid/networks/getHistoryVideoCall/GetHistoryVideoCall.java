package com.yd.yourdoctorandroid.networks.getHistoryVideoCall;

import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.MainObjectRanking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetHistoryVideoCall {
    @GET("videcallhistories/getHistoryVideoCallPatient/{id_patient}?")
    Call<MainObjectHistoryVideo> getHistoryVideoCall(@Header("Authorization") String jwt,
                                                       @Path("id_patient") String id_patient,
                                                       @Query("pageSize") String number_item,
                                                       @Query("page") String number_page);
}
