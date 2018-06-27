package com.yd.yourdoctorandroid.networks;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private static Retrofit retrofit;
    private static RetrofitFactory retrofitFactory;

    public static RetrofitFactory getInstance(){
        if(retrofitFactory == null){
            return new RetrofitFactory();
        }
        return null;
    }

    private RetrofitFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://your-doctor-auth.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass){
        return retrofit.create(serviceClass);
    }
}
