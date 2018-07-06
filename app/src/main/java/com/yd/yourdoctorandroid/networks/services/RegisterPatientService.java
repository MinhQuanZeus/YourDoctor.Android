package com.yd.yourdoctorandroid.networks.services;

import com.yd.yourdoctorandroid.networks.models.AuthResponse;
import com.yd.yourdoctorandroid.networks.models.Patient;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RegisterPatientService {
    @POST("auth/register")
    @Multipart
    Call<AuthResponse> register(@Part MultipartBody.Part imageFile,  @Part("user") Patient patient);
}
