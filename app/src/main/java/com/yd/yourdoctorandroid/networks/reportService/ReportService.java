package com.yd.yourdoctorandroid.networks.reportService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ReportService {
    @POST("reports")
    Call<MainResponReport> reportService(@Header("Authorization") String jwt, @Body ReportRequest reportRequest);
}
