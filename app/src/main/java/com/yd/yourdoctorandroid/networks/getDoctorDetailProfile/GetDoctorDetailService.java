package com.yd.yourdoctorandroid.networks.getDoctorDetailProfile;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDoctorDetailService {
    @GET("doctors/getInformationDoctorById/{doctorID}?")
    Call<MainObjectDetailDoctor> getMainObjectDoctorDetail(@Path("doctorID") String doctorID);
}
