package com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDoctorRankingSpecialist {
    //perPage={number_item}&page={number_page}

    @GET("http://192.168.124.98:3000/api/doctors/get_list_specialist_doctor/{id_specialist}?")
    Call<MainObjectRanking> getMainObjectRanking(@Path("id_specialist") String id_specialist, @Query("perPage") String number_item, @Query("page") String number_page);
}
