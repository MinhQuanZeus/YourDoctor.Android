package com.yd.yourdoctorandroid.networks.getListNotification;

import com.yd.yourdoctorandroid.networks.getListRecommentDoctor.MainObjectRecommend;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetListNotificationService {
    @GET("notifications/getAllNotificationByPatient/{id_patient}?")
    Call<MainObjectNotification> getListNotificationService(@Header("Authorization") String jwt,
                                                            @Path("id_patient") String id_patient,
                                                            @Query("pageSize") String pageSize,
                                                            @Query("page") String page );
}
