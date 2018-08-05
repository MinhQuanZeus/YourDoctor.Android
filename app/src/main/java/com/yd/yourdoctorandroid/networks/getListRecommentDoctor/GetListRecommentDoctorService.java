package com.yd.yourdoctorandroid.networks.getListRecommentDoctor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetListRecommentDoctorService {
    @GET("doctors/getListSpecialistDoctor/{id_specialist}?")
    Call<MainObjectRecommend> getListRecommentDoctor(@Header("Authorization") String jwt, @Path("id_specialist") String id_specialist, @Query("patientId") String patientId);
}
