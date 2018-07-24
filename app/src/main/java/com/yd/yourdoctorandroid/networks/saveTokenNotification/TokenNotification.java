package com.yd.yourdoctorandroid.networks.saveTokenNotification;

public class TokenNotification {
    private String userId;
    private String tokenDevice;

    public TokenNotification(String userId, String tokenDevice) {
        this.userId = userId;
        this.tokenDevice = tokenDevice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTokenDevice() {
        return tokenDevice;
    }

    public void setTokenDevice(String tokenDevice) {
        this.tokenDevice = tokenDevice;
    }
}
