package com.yd.yourdoctorandroid.networks;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2000, TimeUnit.SECONDS)
                .writeTimeout(2000, TimeUnit.SECONDS)
                .readTimeout(3000, TimeUnit.SECONDS)
                .build();
        //https://your-doctor-test2.herokuapp.com/api/
        //your-doctor.herokuapp.com
        //192.168.124.104
        retrofit = new Retrofit.Builder()
                .baseUrl("https://your-doctor-test2.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static <ServiceClass> ServiceClass createService(Class<ServiceClass> serviceClass){
        return retrofit.create(serviceClass);
    }
}
