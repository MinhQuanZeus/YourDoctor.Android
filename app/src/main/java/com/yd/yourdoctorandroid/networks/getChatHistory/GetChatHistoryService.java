package com.yd.yourdoctorandroid.networks.getChatHistory;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetChatHistoryService {
    @GET("chatshistorys/getConversationByID/{id}?")
    Call<MainObjectChatHistory> getChatHistory(@Path("id") String id_chathistory);

}
