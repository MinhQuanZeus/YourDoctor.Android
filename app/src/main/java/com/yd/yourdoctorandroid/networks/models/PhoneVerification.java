package com.yd.yourdoctorandroid.networks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneVerification {
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    private String code;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PhoneVerification(String phoneNumber, String code) {

        this.phoneNumber = phoneNumber;
        this.code = code;
    }
}
