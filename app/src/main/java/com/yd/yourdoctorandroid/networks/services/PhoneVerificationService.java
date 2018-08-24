package com.yd.yourdoctorandroid.networks.services;

import com.yd.yourdoctorandroid.networks.models.CommonSuccessResponse;
import com.yd.yourdoctorandroid.networks.models.PhoneVerification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PhoneVerificationService {
    @POST("phone/sms")
    Call<CommonSuccessResponse> register(@Body PhoneVerification patient);
}
