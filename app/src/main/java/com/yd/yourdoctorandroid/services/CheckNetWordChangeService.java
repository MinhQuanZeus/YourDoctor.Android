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
import com.yd.yourdoctorandroid.utils.NetworkUtils;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.SocketUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckNetWordChangeService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtils.isOnline(context)) {
            SocketUtils.getInstance().reConnect();
            checking(SharedPrefs.getInstance().get("listChatTimeOutNot", List.class));
        }else {
            SocketUtils.getInstance().disconnectConnect();
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
                    Log.e("AnhLe", "success: " + response.toString());
                    ListNotDoneResponse mainObject = response.body();
                    if (response.code() == 200 && mainObject != null) {
                        SharedPrefs.getInstance().put("listChatTimeOutNot", mainObject.getListID());
                        sendToCheckout();
                    }
                }
                @Override
                public void onFailure(Call<ListNotDoneResponse> call, Throwable t) {

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
                    Log.e("AnhLe", "success: " + response.toString());
                    ResponDoneChat mainObject = response.body();
                    if (response.code() == 200 && mainObject != null) {

                        SharedPrefs.getInstance().put("listChatTimeOutNot", mainObject.getArrayChatHistoryCheckFailed());
                    }
                }
                @Override
                public void onFailure(Call<ResponDoneChat> call, Throwable t) {

                }
            });
        }
    }


}
