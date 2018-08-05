package com.yd.yourdoctorandroid.networks.getLinkImageService;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GetLinkeImageService {
    @POST("uploadImageChat")
    @Multipart
    Call<MainGetLink> uploadImageToGetLink(@Header("Authorization") String jwt, @Part MultipartBody.Part imageFile);
}
