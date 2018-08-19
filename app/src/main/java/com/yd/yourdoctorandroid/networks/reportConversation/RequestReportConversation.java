package com.yd.yourdoctorandroid.networks.reportConversation;

public class RequestReportConversation {

    private String idReporter;
    private String idPersonBeingReported;
    private String reason;
    private String idConversation;
    private String type;

    public RequestReportConversation(String idReporter, String idPersonBeingReported, String reason, String idConversation, String type) {
        this.idReporter = idReporter;
        this.idPersonBeingReported = idPersonBeingReported;
        this.reason = reason;
        this.idConversation = idConversation;
        this.type = type;
    }

    public String getIdReporter() {
        return idReporter;
    }

    public void setIdReporter(String idReporter) {
        this.idReporter = idReporter;
    }

    public String getIdPersonBeingReported() {
        return idPersonBeingReported;
    }

    public void setIdPersonBeingReported(String idPersonBeingReported) {
        this.idPersonBeingReported = idPersonBeingReported;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIdConversationReported() {
        return idConversation;
    }

    public void setIdConversationReported(String idConversationReported) {
        this.idConversation = idConversationReported;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
