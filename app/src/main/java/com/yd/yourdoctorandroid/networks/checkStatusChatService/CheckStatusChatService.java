package com.yd.yourdoctorandroid.networks.checkStatusChatService;

import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.MainObjectRanking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CheckStatusChatService {
    @GET("chatshistorys/checkStatusChatsHistory/{id_chat}?")
    Call<StatusResponse> checkStatusChatService(@Path("id_chat") String id_chat);

}
