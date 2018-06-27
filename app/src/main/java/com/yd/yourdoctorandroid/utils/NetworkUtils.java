package com.yd.yourdoctorandroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yd.yourdoctorandroid.networks.models.CommonErrorResponse;
import com.yd.yourdoctorandroid.networks.models.CommonSuccessResponse;

import java.io.IOException;

import retrofit2.Response;

public class NetworkUtils {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static CommonErrorResponse parseToCommonError(Response<CommonSuccessResponse> response) {
        CommonErrorResponse commonErrorResponse = new CommonErrorResponse();
        Gson gson = new GsonBuilder().create();
        try {
            commonErrorResponse = gson.fromJson(response.errorBody().string(), CommonErrorResponse.class);
        } catch (IOException e) {
            // handle failure to read error
        }
        return commonErrorResponse;
    }
}
