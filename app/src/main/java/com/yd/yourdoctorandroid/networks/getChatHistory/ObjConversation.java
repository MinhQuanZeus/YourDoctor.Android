package com.yd.yourdoctorandroid.networks.getChatHistory;

import java.util.List;

public class ObjConversation {

    private String _id;
    private String contentTopic;
    private PatientID patientId;
    private DoctorID doctorId;
    private List<MainRecord> records;
    private int status;
    private String typeAdvisoryID;
    private String paymentPatientID;

    public ObjConversation(String _id, String contentTopic, PatientID patientId, DoctorID doctorId, List<MainRecord> records, int status, String typeAdvisoryID, String paymentPatientID) {
        this._id = _id;
        this.contentTopic = contentTopic;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.records = records;
        this.status = status;
        this.typeAdvisoryID = typeAdvisoryID;
        this.paymentPatientID = paymentPatientID;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContentTopic() {
        return contentTopic;
    }

    public void setContentTopic(String contentTopic) {
        this.contentTopic = contentTopic;
    }

    public PatientID getPatientId() {
        return patientId;
    }

    public void setPatientId(PatientID patientId) {
        this.patientId = patientId;
    }

    public DoctorID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(DoctorID doctorId) {
        this.doctorId = doctorId;
    }

    public List<MainRecord> getRecords() {
        return records;
    }

    public void setRecords(List<MainRecord> records) {
        this.records = records;
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
}
