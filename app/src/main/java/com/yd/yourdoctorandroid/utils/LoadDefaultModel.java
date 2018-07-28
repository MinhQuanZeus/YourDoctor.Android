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
    //    List<Specialist> specialists;
//    List<TypeAdvisory> typeAdvisories;
//
//    private static LoadDefaultModel loadDefaultModel;
//
//    public LoadDefaultModel() {
//
//    }
//
//    public List<Specialist> getSpecialists() {
//
//        if(specialists != null){
//            loadSpecialist();
//        }
//
//        return specialists;
//    }
//
//    public List<TypeAdvisory> getTypeAdvisories() {
//
//        return typeAdvisories;
//    }
//
//    public List<Specialist> loadSpecialist(final int countProcess , final int totalProcess, final CircularProgressButton circularProgressButton) {
//        GetSpecialistService getSpecialistService = RetrofitFactory.getInstance().createService(GetSpecialistService.class);
//        getSpecialistService.getMainObjectSpecialist().enqueue(new Callback<MainObjectSpecialist>() {
//            @Override
//            public void onResponse(Call<MainObjectSpecialist> call, Response<MainObjectSpecialist> response) {
//                Log.e("AnhLe", "success: " + response.body());
//                MainObjectSpecialist mainObjectSpecialist = response.body();
//                specialists = (ArrayList<Specialist>) mainObjectSpecialist.getSpecialist();
//                checkLoad(countProcess,totalProcess,circularProgressButton);
//            }
//
//            @Override
//            public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
//                Log.e("AnhLe", "Fail: " + t.getMessage());
//                checkLoad(countProcess,totalProcess,circularProgressButton);
//            }
//        });
//        return  specialists;
//    }
//
//    public void loadTypeAdvisory(final int countProcess , final int totalProcess, final CircularProgressButton circularProgressButton) {
//        GetAllTypesAdvisoryService getAllTypesAdvisoryService = RetrofitFactory.getInstance().createService(GetAllTypesAdvisoryService.class);
//        getAllTypesAdvisoryService.getMainObjectTypeAdvisories().enqueue(new Callback<MainObjectTypeAdivosry>() {
//            @Override
//            public void onResponse(Call<MainObjectTypeAdivosry> call, Response<MainObjectTypeAdivosry> response) {
//                Log.e("AnhLe", "success: " + response.body());
//                MainObjectTypeAdivosry mainObjectTypeAdivosry = response.body();
//                 typeAdvisories = (ArrayList<TypeAdvisory>) mainObjectTypeAdivosry.getTypeAdvisories();
//                checkLoad(countProcess,totalProcess,circularProgressButton);
//
//
//            }
//
//            @Override
//            public void onFailure(Call<MainObjectTypeAdivosry> call, Throwable t) {
//                Log.e("AnhLe", "Fail: " + t.getMessage());
//                checkLoad(countProcess,totalProcess,circularProgressButton);
//            }
//        });
//    }
//
//    public static LoadDefaultModel getInstance() {
//        if (loadDefaultModel == null) {
//            loadDefaultModel = new LoadDefaultModel();
//        }
//        return loadDefaultModel;
//    }
//
//    public void saveToken(String userId, String tokenId, final int countProcess , final int totalProcess, final CircularProgressButton circularProgressButton){
//        TokenNotification tokenNotification = new TokenNotification(userId,tokenId);
//        SaveTokenNotificationService saveTokenNotificationService = RetrofitFactory.getInstance().createService(SaveTokenNotificationService.class);
//        saveTokenNotificationService.saveTokenNotification(tokenNotification).enqueue(new Callback<TokenResponse>() {
//            @Override
//            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
//                //TODO
//                checkLoad(countProcess,totalProcess,circularProgressButton);
//            }
//
//            @Override
//            public void onFailure(Call<TokenResponse> call, Throwable t) {
//                checkLoad(countProcess,totalProcess,circularProgressButton);
//            }
//        });
//    }
//
//    private void checkLoad(int countProcess , int totalProcess, CircularProgressButton circularProgressButton ){
//
//        countProcess++;
//        if(countProcess == totalProcess && circularProgressButton != null){
//            circularProgressButton.revertAnimation();
//
//        }
//    }
//
//    public void loadFavoriteDoctor(final Patient currentPatient, final int countProcess , final int totalProcess, final CircularProgressButton circularProgressButton){
//        GetListIDFavoriteDoctor getListIDFavoriteDoctor = RetrofitFactory.getInstance().createService(GetListIDFavoriteDoctor.class);
//        getListIDFavoriteDoctor.getMainObjectIDFavorite(currentPatient.getId()).enqueue(new Callback<MainObjectIDFavorite>() {
//            @Override
//            public void onResponse(Call<MainObjectIDFavorite> call, Response<MainObjectIDFavorite> response) {
//                MainObjectIDFavorite mainObject = response.body();
//                if(mainObject != null){
//                    currentPatient.setFavoriteDoctors(mainObject.getListIDFavoriteDoctor());
//                    SharedPrefs.getInstance().put("USER_INFO", currentPatient);
//                }
//                checkLoad(countProcess,totalProcess,circularProgressButton);
//            }
//
//            @Override
//            public void onFailure(Call<MainObjectIDFavorite> call, Throwable t) {
//                checkLoad(countProcess,totalProcess,circularProgressButton);
//
//            }
//        });
//    }
    private List<Specialist> specialists;
    private List<TypeAdvisory> typeAdvisories;

    private static LoadDefaultModel loadDefaultModel;

    public LoadDefaultModel() {
        loadSpecialist();
        loadTypeAdvisory();
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
                Log.e("AnhLe", "success: " + response.body());
                MainObjectSpecialist mainObjectSpecialist = response.body();
                specialists = (ArrayList<Specialist>) mainObjectSpecialist.getSpecialist();
            }

            @Override
            public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                //Toast.makeText(null, "Kết nốt mạng có vấn đề , không thể tải dữ liệu", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void loadTypeAdvisory() {
        GetAllTypesAdvisoryService getAllTypesAdvisoryService = RetrofitFactory.getInstance().createService(GetAllTypesAdvisoryService.class);
        getAllTypesAdvisoryService.getMainObjectTypeAdvisories().enqueue(new Callback<MainObjectTypeAdivosry>() {
            @Override
            public void onResponse(Call<MainObjectTypeAdivosry> call, Response<MainObjectTypeAdivosry> response) {
                Log.e("AnhLe", "success: " + response.body());
                MainObjectTypeAdivosry mainObjectTypeAdivosry = response.body();
                typeAdvisories = (ArrayList<TypeAdvisory>) mainObjectTypeAdivosry.getTypeAdvisories();
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
        getListIDFavoriteDoctor.getMainObjectIDFavorite(currentPatient.getId()).enqueue(new Callback<MainObjectIDFavorite>() {
            @Override
            public void onResponse(Call<MainObjectIDFavorite> call, Response<MainObjectIDFavorite> response) {
                MainObjectIDFavorite mainObject = response.body();
                if (mainObject != null) {
                    currentPatient.setFavoriteDoctors(mainObject.getListIDFavoriteDoctor());
                    SharedPrefs.getInstance().put("USER_INFO", currentPatient);
                    Intent intent = new Intent(fragmentActivity, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    fragmentActivity.startActivity(intent);
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
        intent.putExtra("idChat","theanh");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (360 * 1000), pendingIntent);
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
