package com.yd.yourdoctorandroid.networks.reportConversation;

import com.yd.yourdoctorandroid.networks.ratingService.MainResponRating;
import com.yd.yourdoctorandroid.networks.ratingService.RatingRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ReportConversation {
    @POST("ReportConversations")
    Call<ResponseReportConversation> reportConversations(@Header("Authorization") String jwt, @Body RequestReportConversation requestReportConversation);
}
