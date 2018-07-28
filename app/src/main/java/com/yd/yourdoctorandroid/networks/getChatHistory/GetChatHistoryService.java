package com.yd.yourdoctorandroid.networks.getChatHistory;

import com.yd.yourdoctorandroid.networks.getListRecommentDoctor.MainObjectRecommend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetChatHistoryService {
    @GET("chatshistorys/getConversationByID/{id}?")
    Call<MainObjectChatHistory> getChatHistory(@Path("id") String id_chathistory);

}
