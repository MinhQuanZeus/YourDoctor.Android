package com.yd.yourdoctorandroid.networks.saveTokenNotification;

public class TokenResponse {
    private String message;

    public TokenResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
