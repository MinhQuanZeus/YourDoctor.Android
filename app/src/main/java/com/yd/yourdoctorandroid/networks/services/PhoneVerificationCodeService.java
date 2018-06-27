package com.yd.yourdoctorandroid.networks.services;

import com.yd.yourdoctorandroid.networks.models.PhoneVerification;
import com.yd.yourdoctorandroid.networks.models.CommonSuccessResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PhoneVerificationCodeService {
    @POST("phone/verify")
    Call<CommonSuccessResponse> register(@Body PhoneVerification patient);
}
