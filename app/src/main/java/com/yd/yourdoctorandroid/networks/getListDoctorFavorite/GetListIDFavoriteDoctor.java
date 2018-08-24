package com.yd.yourdoctorandroid.networks.getListDoctorFavorite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GetListIDFavoriteDoctor {
    @GET("patients/getListIDFavoriteDoctor/{patientId}?")
    Call<MainObjectIDFavorite> getMainObjectIDFavorite(@Header("Authorization") String jwt, @Path("patientId") String patientId);
}
