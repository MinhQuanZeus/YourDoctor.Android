package com.yd.yourdoctorandroid.networks.getListRecommentDoctor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetListRecommentDoctorService {
    @GET("doctors/getListSpecialistDoctor/{id_specialist}?")
    Call<MainObjectRecommend> getListRecommentDoctor(@Path("id_specialist") String id_specialist, @Query("patientId") String patientId);
}
