package com.yd.yourdoctorandroid.networks.getListHistoryChat;

import com.yd.yourdoctorandroid.models.HistoryChat;

import java.util.List;

public class MainObjectHistoryChat {
    private List<HistoryChat> listChatsHistory;

    public List<HistoryChat> getListChatsHistory() {
        return listChatsHistory;
    }

    public void setListChatsHistory(List<HistoryChat> listChatsHistory) {
        this.listChatsHistory = listChatsHistory;
    }
}
