package com.yd.yourdoctorandroid.networks.getAllChatHistory;

import java.util.List;

public class MainObjectHistoryChat {

    private List <ChatHistoryInfoResponse> listChatsHistory;

    public List<ChatHistoryInfoResponse> getListChatsHistory() {
        return listChatsHistory;
    }

    public void setListChatsHistory(List<ChatHistoryInfoResponse> listChatsHistory) {
        this.listChatsHistory = listChatsHistory;
    }
}

