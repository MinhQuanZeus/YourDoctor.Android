package com.yd.yourdoctorandroid.utils;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.yd.yourdoctorandroid.networks.RetrofitFactory;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.GetAllTypesAdvisoryService;
import com.yd.yourdoctorandroid.networks.getAllTypesAdvisory.MainObjectTypeAdivosry;
import com.yd.yourdoctorandroid.networks.getSpecialistService.GetSpecialistService;
import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;
import com.yd.yourdoctorandroid.networks.models.Specialist;
import com.yd.yourdoctorandroid.networks.models.TypeAdvisory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadDefaultModel {
    List<Specialist> specialists;
    List<TypeAdvisory> typeAdvisories;

    private static LoadDefaultModel loadDefaultModel;

    public LoadDefaultModel() {
        loadSpecialist();
        loadTypeAdvisory();
    }

    public List<Specialist> getSpecialists() {
        if (specialists == null) {
            loadSpecialist();
        }
        return specialists;
    }

    public List<TypeAdvisory> getTypeAdvisories() {
        if (typeAdvisories == null) {
            loadTypeAdvisory();
        }
        return typeAdvisories;
    }

    public List<Specialist> loadSpecialist() {
        GetSpecialistService getSpecialistService = RetrofitFactory.getInstance().createService(GetSpecialistService.class);
        getSpecialistService.getMainObjectSpecialist().enqueue(new Callback<MainObjectSpecialist>() {
            @Override
            public void onResponse(Call<MainObjectSpecialist> call, Response<MainObjectSpecialist> response) {
                Log.e("AnhLe", "success: " + response.body());
                MainObjectSpecialist mainObjectSpecialist = response.body();
                specialists = (ArrayList<Specialist>) mainObjectSpecialist.getSpecialist();
            }

            @Override
            public void onFailure(Call<MainObjectSpecialist> call, Throwable t) {
                Log.e("AnhLe", "Fail: " + t.getMessage());
            }
        });
        return specialists;
    }

    public List<TypeAdvisory> loadTypeAdvisory() {
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
                Log.e("AnhLe", "Fail: " + t.getMessage());
            }
        });
        return typeAdvisories;
    }

    public static LoadDefaultModel getInstance() {
        if (loadDefaultModel == null) {
            loadDefaultModel = new LoadDefaultModel();
        }
        return loadDefaultModel;
    }


}
