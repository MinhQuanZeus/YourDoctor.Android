package com.yd.yourdoctorandroid.networks.getSpecialistService;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetSpecialistService {

    @GET("http://192.168.124.98:3000/api/specialists")
    Call<MainObjectSpecialist> getMainObjectSpecialist();
}
