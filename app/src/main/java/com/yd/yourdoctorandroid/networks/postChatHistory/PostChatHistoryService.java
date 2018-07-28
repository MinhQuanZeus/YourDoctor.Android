package com.yd.yourdoctorandroid.networks.postChatHistory;

import com.yd.yourdoctorandroid.networks.favoriteDoctor.MainResponseFavorite;
import com.yd.yourdoctorandroid.networks.postPaymentHistory.PaymentHistory;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostChatHistoryService {
    @POST("chatshistorys")
    Call<ChatHistoryResponse> addChatHistory(@Body ChatHistory chatHistory);
}
