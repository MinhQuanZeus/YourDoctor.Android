package com.yd.yourdoctorandroid.networks.postPaymentHistory;

import com.yd.yourdoctorandroid.networks.favoriteDoctor.FavoriteRequest;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.MainResponseFavorite;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface PostPaymentHistoryService {
    //192.168.124.106
    //http://192.168.124.106:3000/api/
    @POST("paymentshistorys")
    Call<PaymentResponse> addPaymentHistory(@Body PaymentHistory paymentHistory);
}
