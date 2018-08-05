package com.yd.yourdoctorandroid.networks.getDoctorDetailProfile;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GetDoctorDetailService {
    @GET("doctors/getInformationDoctorById/{doctorID}?")
    Call<MainObjectDetailDoctor> getMainObjectDoctorDetail(@Header("Authorization") String jwt, @Path("doctorID") String doctorID);
}
