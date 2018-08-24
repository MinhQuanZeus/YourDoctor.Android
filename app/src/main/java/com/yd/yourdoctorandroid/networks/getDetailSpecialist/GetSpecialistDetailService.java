package com.yd.yourdoctorandroid.networks.getDetailSpecialist;


import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.MainObjectDetailDoctor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GetSpecialistDetailService {
    @GET("specialists/getDetailSpecialist/{specialistId}?")
    Call<MainObjectSpecialistDetail> getSpecialistDetailService(@Path("specialistId") String specialistId);
}
