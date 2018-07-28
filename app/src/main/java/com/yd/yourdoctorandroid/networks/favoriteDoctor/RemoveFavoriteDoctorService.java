package com.yd.yourdoctorandroid.networks.favoriteDoctor;

import com.yd.yourdoctorandroid.networks.models.CommonSuccessResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RemoveFavoriteDoctorService {
    @PUT("patients/removeFavoriteDoctor")
    Call<MainResponseFavorite> addFavoriteDoctor(@Body FavoriteRequest favoriteRequest);
}
