package com.yd.yourdoctorandroid.models;

public class VideoCallSession {
    private String callerId;
    private String callerName;
    private String calleeId;
    private String calleeName;
    private String calleeAvatar;
    private TypeCall type;

    public VideoCallSession(String callerId, String callerName, String calleeId, String calleeName, String calleeAvatar, TypeCall type) {
        this.callerId = callerId;
        this.callerName = callerName;
        this.calleeId = calleeId;
        this.calleeName = calleeName;
        this.calleeAvatar = calleeAvatar;
        this.type = type;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCalleeId() {
        return calleeId;
    }

    public void setCalleeId(String calleeId) {
        this.calleeId = calleeId;
    }

    public String getCalleeName() {
        return calleeName;
    }

    public void setCalleeName(String calleeName) {
        this.calleeName = calleeName;
    }

    public String getCalleeAvatar() {
        return calleeAvatar;
    }

    public void setCalleeAvatar(String calleeAvatar) {
        this.calleeAvatar = calleeAvatar;
    }

    public TypeCall getType() {
        return type;
    }

    public void setType(TypeCall type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VideoCallSession{" +
                "callerId='" + callerId + '\'' +
                ", callerName='" + callerName + '\'' +
                ", calleeId='" + calleeId + '\'' +
                ", calleeName='" + calleeName + '\'' +
                ", calleeAvatar='" + calleeAvatar + '\'' +
                ", type=" + type +
                '}';
    }
}
