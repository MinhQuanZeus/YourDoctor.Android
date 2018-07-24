package com.yd.yourdoctorandroid.networks.postChatHistory;

public class ChatHistory {
    private String _id;
    private String contentTopic;
    private String patientId;
    private String doctorId;
    private int status;
    private String typeAdvisoryID;
    private String paymentPatientID;
    private String paymentDoctorID;

    public ChatHistory() {
    }

    public ChatHistory(String contentTopic, String patientId, String doctorId, int status, String typeAdvisoryID, String paymentPatientID, String paymentDoctorID) {
        this.contentTopic = contentTopic;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.status = status;
        this.typeAdvisoryID = typeAdvisoryID;
        this.paymentPatientID = paymentPatientID;
        this.paymentDoctorID = paymentDoctorID;
    }

    public String getContentTopic() {
        return contentTopic;
    }

    public void setContentTopic(String contentTopic) {
        this.contentTopic = contentTopic;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTypeAdvisoryID() {
        return typeAdvisoryID;
    }

    public void setTypeAdvisoryID(String typeAdvisoryID) {
        this.typeAdvisoryID = typeAdvisoryID;
    }

    public String getPaymentPatientID() {
        return paymentPatientID;
    }

    public void setPaymentPatientID(String paymentPatientID) {
        this.paymentPatientID = paymentPatientID;
    }

    public String getPaymentDoctorID() {
        return paymentDoctorID;
    }

    public void setPaymentDoctorID(String paymentDoctorID) {
        this.paymentDoctorID = paymentDoctorID;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
