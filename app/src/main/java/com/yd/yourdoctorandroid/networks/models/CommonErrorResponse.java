package com.yd.yourdoctorandroid.networks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonErrorResponse {
    @SerializedName("success")
    @Expose()
    private boolean isSuccess;
    private String error;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public CommonErrorResponse() {
    }

    public CommonErrorResponse(boolean isSuccess, String error) {

        this.isSuccess = isSuccess;
        this.error = error;
    }
}
