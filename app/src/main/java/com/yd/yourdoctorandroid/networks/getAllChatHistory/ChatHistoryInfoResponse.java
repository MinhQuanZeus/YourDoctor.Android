package com.yd.yourdoctorandroid.networks.getAllChatHistory;


import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.DoctorID;

public class ChatHistoryInfoResponse {

    private String _id;
    private DoctorID doctorId;
    private int status;
    private String contentTopic;
    private long createdAt;
    private long updatedAt;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public DoctorID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(DoctorID doctorId) {
        this.doctorId = doctorId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getContentTopic() {
        return contentTopic;
    }

    public void setContentTopic(String contentTopic) {
        this.contentTopic = contentTopic;
    }
}
