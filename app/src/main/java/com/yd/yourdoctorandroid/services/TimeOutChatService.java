package com.yd.yourdoctorandroid.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.CheckStatusChatService;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.StatusResponse;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeOutChatService extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        String idChat = intent.getStringExtra("idChat");
        if (NetworkUtils.isOnline(context)) {
            //Toast.makeText(context, intent.getStringExtra("idChat") + "hello", Toast.LENGTH_LONG).show();
            checking(idChat);
        } else {

            LoadDefaultModel.getInstance().addIdChatToListTimeOut(idChat);
        }

        this.clearAbortBroadcast();
    }

    private void checking(final String idChat){
        CheckStatusChatService checkStatusChatService = RetrofitFactory.getInstance().createService(CheckStatusChatService.class);
        checkStatusChatService.checkStatusChatService(idChat).enqueue(new Callback<StatusResponse>() {
            @Override
            public  void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                Log.e("AnhLe", "success: " + response.body());
                StatusResponse mainObject = response.body();
                if(response.code() == 200 && mainObject != null){
                    if(!mainObject.getStatusDone()){
                        LoadDefaultModel.getInstance().addIdChatToListTimeOut(idChat);
                        //ban luon cho 1 list server check
                        //tru but trong share

                    }
                }else {
                    LoadDefaultModel.getInstance().addIdChatToListTimeOut(idChat);
                }

            }

            @Override
            public void onFailure(Call<StatusResponse> call, Throwable t) {
                LoadDefaultModel.getInstance().addIdChatToListTimeOut(idChat);
            }
        });
    }

//    private void addIdChatToListTimeOut(String idChat){
//        List<String> listChatTimeOut =SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );
//        if(listChatTimeOut == null){
//            listChatTimeOut = new ArrayList<>();
//            listChatTimeOut.add(idChat);
//            SharedPrefs.getInstance().put("listChatTimeOutNot", listChatTimeOut);
//        }else {
//            listChatTimeOut.add(idChat);
//            SharedPrefs.getInstance().put("listChatTimeOutNot", listChatTimeOut);
//        }
//    }

}
