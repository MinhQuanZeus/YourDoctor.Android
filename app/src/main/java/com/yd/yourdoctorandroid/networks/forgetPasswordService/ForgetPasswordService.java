package com.yd.yourdoctorandroid.networks.forgetPasswordService;

import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.MainObjectDetailDoctor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ForgetPasswordService {
    @GET("users/forgotPassword/{userID}?")
    Call<MainForgetResponse> forgetPasswordService(@Path("userID") String userID);
}
