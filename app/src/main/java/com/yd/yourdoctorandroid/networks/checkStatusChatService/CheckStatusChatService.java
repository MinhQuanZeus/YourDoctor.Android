package com.yd.yourdoctorandroid.networks.checkStatusChatService;

import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.MainObjectRanking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CheckStatusChatService {
    //http://192.168.124.109:3000/api/
    @POST("http://192.168.124.109:3000/api/chatshistorys/checkStatusChatsHistory")
    Call<ListNotDoneResponse> checkStatusChatService(@Header("Authorization") String jwt, @Body ListRequest listRequest);

}
