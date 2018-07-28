package com.yd.yourdoctorandroid.networks.checkStatusChatService;

public class StatusResponse {
    private String message;
    private Boolean statusDone;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatusDone() {
        return statusDone;
    }

    public void setStatusDone(Boolean statusDone) {
        this.statusDone = statusDone;
    }
}
