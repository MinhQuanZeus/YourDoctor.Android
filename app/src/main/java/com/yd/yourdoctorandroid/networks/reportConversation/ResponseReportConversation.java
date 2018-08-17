package com.yd.yourdoctorandroid.networks.reportConversation;

public class ResponseReportConversation {
    private boolean status;
    private String message;
    private boolean success;

    public ResponseReportConversation(boolean status, String message, boolean success) {
        this.status = status;
        this.message = message;
        this.success = success;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
