package com.yd.yourdoctorandroid.networks.getAllChatHistory;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetAllHistoryChatService {
    @GET("chatshistorys/getAllConversationByPatient/{id_patient}?")
    Call<MainObjectHistoryChat> getAllHistoryChatService(@Header("Authorization") String jwt,
                                                           @Path("id_patient") String id_patient,
                                                           @Query("pageSize") String pageSize,
                                                           @Query("page") String page);
}
