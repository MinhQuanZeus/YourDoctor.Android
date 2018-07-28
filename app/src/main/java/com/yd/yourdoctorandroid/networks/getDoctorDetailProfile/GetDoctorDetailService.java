package com.yd.yourdoctorandroid.networks.getDoctorDetailProfile;

import com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist.MainObjectRanking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDoctorDetailService {
    @GET("doctors/getInformationDoctorById/{doctorID}?")
    Call<MainObjectDetailDoctor> getMainObjectDoctorDetail(@Path("doctorID") String doctorID);
}
