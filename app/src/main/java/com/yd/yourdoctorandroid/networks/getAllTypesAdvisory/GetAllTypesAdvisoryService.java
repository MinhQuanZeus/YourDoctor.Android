package com.yd.yourdoctorandroid.networks.getAllTypesAdvisory;

import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetAllTypesAdvisoryService {
    @GET("typeadvisorys/getAllTypeAdvisories")
    Call<MainObjectTypeAdivosry> getMainObjectTypeAdvisories();
}
