package com.yd.yourdoctorandroid.networks.getSpecialistService;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetSpecialistService {

    @GET("specialists/getListSpecialist")
    Call<MainObjectSpecialist> getMainObjectSpecialist();
}
