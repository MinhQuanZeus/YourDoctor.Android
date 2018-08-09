package com.yd.yourdoctorandroid.networks.getPaymentHistory;

import com.yd.yourdoctorandroid.models.Notification;

import java.util.List;

public class MainHistoryPaymentResponse {
    private List<ObjectPaymentResponse> listPaymentHistory;

    public List<ObjectPaymentResponse> getListPaymentHistory() {
        return listPaymentHistory;
    }

    public void setListPaymentHistory(List<ObjectPaymentResponse> listPaymentHistory) {
        this.listPaymentHistory = listPaymentHistory;
    }
}
