package com.yd.yourdoctorandroid.networks.getLinkImageService;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GetLinkeImageService {
    @POST("http://192.168.124.100:3000/api/uploadImageChat")
    @Multipart
    Call<MainGetLink> uploadImageToGetLink(@Part MultipartBody.Part imageFile);
}
