package com.yd.yourdoctorandroid.networks.services;

import com.yd.yourdoctorandroid.networks.models.AuthResponse;
import com.yd.yourdoctorandroid.networks.models.Patient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterPatientService {
    @POST("auth/register")
    Call<AuthResponse> register(@Body Patient patient);
}
