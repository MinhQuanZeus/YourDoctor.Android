package com.yd.yourdoctorandroid.networks.changePassword;

import com.yd.yourdoctorandroid.networks.favoriteDoctor.FavoriteRequest;
import com.yd.yourdoctorandroid.networks.favoriteDoctor.MainResponseFavorite;
import com.yd.yourdoctorandroid.utils.SharedPrefs;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface ChangePasswordService {
    //http://192.168.124.109:3000/api/
    @PUT("users/changePassword")
    Call<PasswordResponse> changePasswordService(@Header("Authorization") String jwt, @Body PasswordRequest passwordRequest);
}
