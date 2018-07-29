package com.yd.yourdoctorandroid.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.microsoft.azure.storage.blob.ListBlobItem;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.NetworkUtils;
import com.yd.yourdoctorandroid.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

public class CheckNetWordChangeService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtils.isOnline(context)) {
            handleCheckingListTimeOutChat();
        } else {

        }
    }

    private void handleCheckingListTimeOutChat(){
        Log.e("hello", "co mang roi");
        List<String> listTimeOutChat = SharedPrefs.getInstance().get("listChatTimeOutNot", List.class);
        if(listTimeOutChat != null){
            //ban luon cho 1 list server check
            //tru but trong share
        }
    }


}
