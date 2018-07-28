package com.yd.yourdoctorandroid.networks.getListDoctorFavorite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetListIDFavoriteDoctor {
    @GET("patients/getListIDFavoriteDoctor/{patientId}?")
    Call<MainObjectIDFavorite> getMainObjectIDFavorite(@Path("patientId") String patientId);
}
