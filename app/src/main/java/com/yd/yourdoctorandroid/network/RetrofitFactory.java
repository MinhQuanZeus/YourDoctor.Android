package com.yd.yourdoctorandroid.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitFactory {
    public static RetrofitFactory retrofitFactory = new RetrofitFactory();
    private static Retrofit retrofit;

    private RetrofitFactory() {
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.124.98:3000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitFactory getInstence() {
        return retrofitFactory;
    }

    public static <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass) {
        return retrofit.create(serviceClass);
    }
}

