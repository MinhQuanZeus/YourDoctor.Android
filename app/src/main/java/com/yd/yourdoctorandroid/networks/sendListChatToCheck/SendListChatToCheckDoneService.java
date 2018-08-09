package com.yd.yourdoctorandroid.networks.sendListChatToCheck;

import com.yd.yourdoctorandroid.networks.checkStatusChatService.ListNotDoneResponse;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.ListRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SendListChatToCheckDoneService {
    //http://192.168.124.109:3000/api/
    @POST("chatshistorys/checkDoctorReply")
    Call<ResponDoneChat> sendListChatToCheckDoneService(@Header("Authorization") String jwt, @Body ListRequest listRequest);
}
