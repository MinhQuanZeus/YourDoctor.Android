package com.yd.yourdoctorandroid.networks.getListPendingChatService;

import com.yd.yourdoctorandroid.networks.getListNotification.MainObjectNotification;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetListPendingChatService {
    @GET("chatshistorys/getListConversationPending/{id_patient}?")
    Call<MainObjectNotification> getListPendingChatService(@Header("Authorization") String jwt,
                                                            @Path("id_patient") String id_patient);
}
