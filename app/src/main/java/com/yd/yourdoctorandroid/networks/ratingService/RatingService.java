package com.yd.yourdoctorandroid.networks.ratingService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RatingService {
    @POST("ratings")
    Call<MainResponRating> ratingService(@Header("Authorization") String jwt, @Body RatingRequest ratingRequest);
}
