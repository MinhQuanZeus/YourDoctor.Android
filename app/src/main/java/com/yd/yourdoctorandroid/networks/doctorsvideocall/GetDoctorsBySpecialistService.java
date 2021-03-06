package com.yd.yourdoctorandroid.networks.doctorsvideocall;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface GetDoctorsBySpecialistService {
    @GET("doctors/doctors-by-specialist/")
    Call<DoctorsBySpecialist> getDoctorsBySpecialist(@Header("Authorization") String jwt, @Query("specialistId") String specialistId, @Query("patientId") String patientId);
}
