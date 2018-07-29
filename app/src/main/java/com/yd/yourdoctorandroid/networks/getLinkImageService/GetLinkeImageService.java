package com.yd.yourdoctorandroid.networks.getLinkImageService;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GetLinkeImageService {
    @POST("uploadImageChat")
    @Multipart
    Call<MainGetLink> uploadImageToGetLink(@Part MultipartBody.Part imageFile);
}
