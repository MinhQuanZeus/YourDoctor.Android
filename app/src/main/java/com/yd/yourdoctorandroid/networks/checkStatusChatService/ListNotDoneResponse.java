package com.yd.yourdoctorandroid.networks.checkStatusChatService;

import java.util.ArrayList;
import java.util.List;

public class ListNotDoneResponse {
    private List<ObjectChat> arrayResult;

    public List<ObjectChat> getArrayResult() {
        return arrayResult;
    }

    public void setArrayResult(List<ObjectChat> arrayResult) {
        this.arrayResult = arrayResult;
    }

    public List<String> getListID(){
        List<String> listId = new ArrayList<>();
        for (ObjectChat objectChat: arrayResult) {
            listId.add(objectChat.getChatsHistoryID());
        }
        return listId;
    }
}
