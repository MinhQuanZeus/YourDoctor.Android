package com.yd.yourdoctorandroid.networks.getListPendingChatService;

import java.util.List;

public class MainPendingResponse {
    private List<IDPending> listPending;

    public List<IDPending> getListPending() {
        return listPending;
    }

    public void setListPending(List<IDPending> listPending) {
        this.listPending = listPending;
    }
}
