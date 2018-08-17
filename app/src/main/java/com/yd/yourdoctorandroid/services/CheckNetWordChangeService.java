package com.yd.yourdoctorandroid.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.CheckStatusChatService;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.ListNotDoneResponse;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.ListRequest;
import com.yd.yourdoctorandroid.networks.sendListChatToCheck.ResponDoneChat;
import com.yd.yourdoctorandroid.networks.sendListChatToCheck.SendListChatToCheckDoneService;
import com.yd.yourdoctorandroid.utils.LoadDefaultModel;
import com.yd.yourdoctorandroid.utils.NetworkUtils;
import com.yd.yourdoctorandroid.utils.SharedPrefs;
import com.yd.yourdoctorandroid.utils.SocketUtils;

import java.net.Socket;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckNetWordChangeService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkUtils.isOnline(context)) {
            if (SharedPrefs.getInstance().get("USER_INFO", Patient.class) != null) {
                if(!SocketUtils.getInstance().checkIsConnected()){
                    SocketUtils.getInstance().reConnect();
                }
                LoadDefaultModel.getInstance().loadAllChatPending(SharedPrefs.getInstance().get("USER_INFO", Patient.class).getId());
            }

        }
    }

}
