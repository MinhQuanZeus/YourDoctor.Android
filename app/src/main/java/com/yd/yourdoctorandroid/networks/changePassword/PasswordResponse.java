package com.yd.yourdoctorandroid.networks.changePassword;

public class PasswordResponse {
    private String message;
    private boolean changePasswordSuccess;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isChangePasswordSuccess() {
        return changePasswordSuccess;
    }

    public void setChangePasswordSuccess(boolean changePasswordSuccess) {
        this.changePasswordSuccess = changePasswordSuccess;
    }
}

