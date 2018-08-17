package com.yd.yourdoctorandroid.utils;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.CheckStatusChatService;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.ListNotDoneResponse;
import com.yd.yourdoctorandroid.networks.checkStatusChatService.ListRequest;
import com.yd.yourdoctorandroid.networks.getListPendingChatService.GetListPendingChatService;
import com.yd.yourdoctorandroid.networks.getListPendingChatService.IDPending;
import com.yd.yourdoctorandroid.networks.getListPendingChatService.MainPendingResponse;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.models.TypeAdvisory;
import com.yd.yourdoctorandroid.networks.sendListChatToCheck.ResponDoneChat;
import com.yd.yourdoctorandroid.networks.sendListChatToCheck.SendListChatToCheckDoneService;
import com.yd.yourdoctorandroid.services.CheckNetWordChangeService;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadDefaultModel {

    private List<Specialist> specialists;
    private List<TypeAdvisory> typeAdvisories;

    private static LoadDefaultModel loadDefaultModel;

    private CheckNetWordChangeService checkNetWordChangeService;
    private IntentFilter intentFilter;

    public LoadDefaultModel() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        checkNetWordChangeService = new CheckNetWordChangeService();
    }

    public void setSpecialists(List<Specialist> specialists) {
        this.specialists = specialists;
    }

    public void setTypeAdvisories(List<TypeAdvisory> typeAdvisories) {
        this.typeAdvisories = typeAdvisories;
    }

    public List<Specialist> getSpecialists() {

        return specialists;
    }

    public List<TypeAdvisory> getTypeAdvisories() {

        return typeAdvisories;
    }

    public static LoadDefaultModel getInstance() {
        if (loadDefaultModel == null) {
            loadDefaultModel = new LoadDefaultModel();
        }
        return loadDefaultModel;
    }

    public void registerServiceCheckNetwork(Context context){
        context.registerReceiver(checkNetWordChangeService, intentFilter);
    }

    public void unregisterServiceCheckNetwork(Context context){
        context.unregisterReceiver(checkNetWordChangeService);
    }

    public void loadAllChatPending(final String currentPatientId){
        GetListPendingChatService getListPendingChatService = RetrofitFactory.getInstance().createService(GetListPendingChatService.class);
        getListPendingChatService.getListPendingChatService(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),currentPatientId).enqueue(new Callback<MainPendingResponse>() {
            @Override
            public void onResponse(Call<MainPendingResponse> call, Response<MainPendingResponse> response) {
                if(response.code() == 200){
                    MainPendingResponse mainObject = response.body();
                    //check lại time nhe
                    if(mainObject.getListPending() != null && mainObject.getListPending().size() > 0){
                        for (IDPending idPending:mainObject.getListPending()) {
                            handleChatIsPending(idPending);
                        }
                        checking(SharedPrefs.getInstance().get("listChatTimeOutNot", List.class));
                    }

                }
            }

            @Override
            public void onFailure(Call<MainPendingResponse> call, Throwable t) {
               Log.e("ErrorPending", "Anh le");
            }
        });
    }

    public void handleChatIsPending(IDPending idPending){
        if((idPending.getTimeRemain() / 1000) >= Config.TIME_OUT_CHAT_CONVERSATION){
            Utils.addIdChatToListTimeOut(idPending.getId());
        }
    }

    public void checking(final List<String> listChatTimeOut) {
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
                    Log.e("Anhle ","Checking List Response");
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
