package com.yd.yourdoctorandroid.networks.getListRecommentDoctor;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetListRecommentDoctorService {
    @GET("doctors/getListSpecialistDoctor?")
    Call<MainObjectRecommend> getListRecommentDoctor(@Header("Authorization") String jwt, @Query("specialistId") String specialistId, @Query("patientId") String patientId);
}
