package com.yd.yourdoctorandroid.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.CheckStatusChatService;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.ListNotDoneResponse;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.ListRequest;
import com.yd.yourdoctorandroid.networks.sendListChatToCheck.ResponDoneChat;
import com.yd.yourdoctorandroid.networks.sendListChatToCheck.SendListChatToCheckDoneService;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.NetworkUtils;
import com.yd.yourdoctorandroid.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeOutChatService extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        String idChat = intent.getStringExtra("idChat");
        LoadDefaultModel.getInstance().addIdChatToListTimeOut(idChat);

        if (NetworkUtils.isOnline(context)) {
            List<String> listChatTimeOut = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class);
            checking(listChatTimeOut);
        }

    }

    private void checking(final List<String> listChatTimeOut) {
        ListRequest listRequest = new ListRequest();
        listRequest.setListId(listChatTimeOut);
        if (listRequest.getListId() != null && listRequest.getListId().size() != 0) {
            CheckStatusChatService checkStatusChatService = RetrofitFactory.getInstance().createService(CheckStatusChatService.class);
            checkStatusChatService.checkStatusChatService(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),listRequest).enqueue(new Callback<ListNotDoneResponse>() {
                @Override
                public void onResponse(Call<ListNotDoneResponse> call, Response<ListNotDoneResponse> response) {
                    Log.e("AnhLe", "checking: " + response.body());
                    ListNotDoneResponse mainObject = response.body();
                    if (response.code() == 200 && mainObject != null) {
                        SharedPrefs.getInstance().put("listChatTimeOutNot", mainObject.getListID());
                        sendToCheckout();
                    }
                }

                @Override
                public void onFailure(Call<ListNotDoneResponse> call, Throwable t) {
                    TimeOutChatService.this.clearAbortBroadcast();
                }
            });
        }

    }

    private void sendToCheckout() {
        ListRequest listRequest = new ListRequest();
        listRequest.setListId(SharedPrefs.getInstance().get("listChatTimeOutNot", List.class));
        if (listRequest.getListId() != null && listRequest.getListId().size() != 0) {
            SendListChatToCheckDoneService sendListChatToCheckDoneService = RetrofitFactory.getInstance().createService(SendListChatToCheckDoneService.class);
            sendListChatToCheckDoneService.sendListChatToCheckDoneService(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),listRequest).enqueue(new Callback<ResponDoneChat>() {
                @Override
                public void onResponse(Call<ResponDoneChat> call, Response<ResponDoneChat> response) {
                    Log.e("AnhLe", "send : " + response.body());
                    ResponDoneChat mainObject = response.body();
                    if (response.code() == 200 && mainObject != null) {

                        SharedPrefs.getInstance().put("listChatTimeOutNot", mainObject.getArrayChatHistoryCheckFailed());
                    }
                    TimeOutChatService.this.clearAbortBroadcast();
                }

                @Override
                public void onFailure(Call<ResponDoneChat> call, Throwable t) {
                    TimeOutChatService.this.clearAbortBroadcast();
                }
            });
        }
    }

}
