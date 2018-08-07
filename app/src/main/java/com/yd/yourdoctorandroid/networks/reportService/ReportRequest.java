package com.yd.yourdoctorandroid.networks.reportService;

public class ReportRequest {

    private String idReporter;
    private String idPersonBeingReported;
    private String reason;

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
}
