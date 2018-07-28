package com.yd.yourdoctorandroid.networks.saveTokenNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SaveTokenNotificationService {
    @POST("tokennotifications")
    Call<TokenResponse> saveTokenNotification(@Body TokenNotification tokenNotification);
}
