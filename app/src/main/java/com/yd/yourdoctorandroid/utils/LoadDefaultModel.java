package com.yd.yourdoctorandroid.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.yd.yourdoctorandroid.activities.MainActivity;
import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.GetAllTypesAdvisoryService;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.MainObjectTypeAdivosry;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.GetListIDFavoriteDoctor;
import com.yd.yourdoctorandroid.networks.getListDoctorFavorite.MainObjectIDFavorite;
import com.yd.yourdoctorandroid.networks.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.models.Patient;
import com.yd.yourdoctorandroid.models.Specialist;
import com.yd.yourdoctorandroid.models.TypeAdvisory;
import com.yd.yourdoctorandroid.services.TimeOutChatService;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadDefaultModel {

    private List<Specialist> specialists;
    private List<TypeAdvisory> typeAdvisories;

    private static LoadDefaultModel loadDefaultModel;

    public LoadDefaultModel() {
        loadSpecialist();
        loadTypeAdvisory();
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

    public void loadSpecialist() {
        GetSpecialistService getSpecialistService = RetrofitFactory.getInstance().createService(GetSpecialistService.class);
        getSpecialistService.getMainObjectSpecialist().enqueue(new Callback<MainObjectSpecialist>() {
            @Override
            public  void onResponse(Call<MainObjectSpecialist> call, Response<MainObjectSpecialist> response) {
                if(response.code() == 200){
                    Log.e("AnhLe", "success: " + response.body());
                    MainObjectSpecialist mainObjectSpecialist = response.body();
                    specialists = (ArrayList<Specialist>) mainObjectSpecialist.getListSpecialist();
                }
            }

            @Override
            public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                //Toast.makeText(null, "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadTypeAdvisory() {
        GetAllTypesAdvisoryService getAllTypesAdvisoryService = RetrofitFactory.getInstance().createService(GetAllTypesAdvisoryService.class);
        getAllTypesAdvisoryService.getMainObjectTypeAdvisories(SharedPrefs.getInstance().get("JWT_TOKEN", String.class)).enqueue(new Callback<MainObjectTypeAdivosry>() {
            @Override
            public void onResponse(Call<MainObjectTypeAdivosry> call, Response<MainObjectTypeAdivosry> response) {
                if(response.code() == 200){
                    Log.e("AnhLe", "success: " + response.body());
                    MainObjectTypeAdivosry mainObjectTypeAdivosry = response.body();
                    typeAdvisories = (ArrayList<TypeAdvisory>) mainObjectTypeAdivosry.getTypeAdvisories();
                }

            }

            @Override
            public void onFailure(Call<MainObjectTypeAdivosry> call, Throwable t) {
                //Toast.makeText(null, "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static LoadDefaultModel getInstance() {
        if (loadDefaultModel == null) {
            loadDefaultModel = new LoadDefaultModel();
        }
        return loadDefaultModel;
    }

    public void loadFavoriteDoctor(final Patient currentPatient , final FragmentActivity fragmentActivity , final CircularProgressButton btnLogin) {
        GetListIDFavoriteDoctor getListIDFavoriteDoctor = RetrofitFactory.getInstance().createService(GetListIDFavoriteDoctor.class);
        getListIDFavoriteDoctor.getMainObjectIDFavorite(SharedPrefs.getInstance().get("JWT_TOKEN", String.class),currentPatient.getId()).enqueue(new Callback<MainObjectIDFavorite>() {
            @Override
            public void onResponse(Call<MainObjectIDFavorite> call, Response<MainObjectIDFavorite> response) {
                if(response.code() == 200){
                    MainObjectIDFavorite mainObject = response.body();
                    if (mainObject != null) {
                        currentPatient.setFavoriteDoctors(mainObject.getListIDFavoriteDoctor());
                        SharedPrefs.getInstance().put("USER_INFO", currentPatient);
                        Intent intent = new Intent(fragmentActivity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        fragmentActivity.startActivity(intent);
                    }
                }
                btnLogin.revertAnimation();
            }

            @Override
            public void onFailure(Call<MainObjectIDFavorite> call, Throwable t) {
                //Toast.makeText(null, "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
                btnLogin.revertAnimation();
            }
        });

    }

    public void startServiceTimeOut(Context context , String idChat){
        Intent intent = new Intent(context, TimeOutChatService.class);
        intent.putExtra("idChat",idChat);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (Config.TIME_OUT_CHAT_CONVERSATION * 1000), pendingIntent);
    }

    public void addIdChatToListTimeOut(String idChat){
        List<String> listChatTimeOut =SharedPrefs.getInstance().get("listChatTimeOutNot", List.class );
        if(listChatTimeOut == null){
            listChatTimeOut = new ArrayList<>();
            listChatTimeOut.add(idChat);
            SharedPrefs.getInstance().put("listChatTimeOutNot", listChatTimeOut);
        }else {
            listChatTimeOut.add(idChat);
            SharedPrefs.getInstance().put("listChatTimeOutNot", listChatTimeOut);
            Log.e("HelloA", "put success");
        }
    }

    public void getListPendingChat(){
        //TODO
    }



}
