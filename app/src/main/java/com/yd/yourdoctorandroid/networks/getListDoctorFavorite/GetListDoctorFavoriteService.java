package com.yd.yourdoctorandroid.networks.getListDoctorFavorite;

import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.MainObjectRanking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetListDoctorFavoriteService {
    //10.22.116.199
    @GET("patients/getListFavoriteDoctor/{patientId}?")
    Call<MainObjectFavoriteList> getMainObjectFavoriteList(@Path("patientId") String patientId, @Query("skip") String skip,
                                                           @Query("pageSize") String pageSize);
}
