package com.yd.yourdoctorandroid.networks.getChatHistory;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GetChatHistoryService {
    @GET("chatshistorys/getConversationByID/{id}?")
    Call<MainObjectChatHistory> getChatHistory(@Header("Authorization") String jwt, @Path("id") String id_chathistory);

}
