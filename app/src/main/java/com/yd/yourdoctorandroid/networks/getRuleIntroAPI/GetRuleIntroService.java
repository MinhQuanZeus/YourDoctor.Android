package com.yd.yourdoctorandroid.networks.getRuleIntroAPI;

import com.yd.yourdoctorandroid.networks.getSpecialistService.MainObjectSpecialist;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetRuleIntroService {
    @GET("IntroduceAndRule")
    Call<MainObjectRule> getIntroduceAndRule();
}
