package com.yd.yourdoctorandroid.networks.getChatHistory;

public class MainObjectChatHistory {
    private ObjConversation objConversation;

    public MainObjectChatHistory(ObjConversation objConversation) {
        this.objConversation = objConversation;
    }

    public ObjConversation getObjConversation() {
        return objConversation;
    }

    public void setObjConversation(ObjConversation objConversation) {
        this.objConversation = objConversation;
    }
}
