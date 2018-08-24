package com.yd.yourdoctorandroid.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.yd.yourdoctorandroid.networks.getListHistoryChat.DoctorIdHistory;

public class HistoryChat {
    @SerializedName("_id")
    @Expose
    private String id;
    private int status;
    private DoctorIdHistory doctorId;
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DoctorIdHistory getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(DoctorIdHistory doctorId) {
        this.doctorId = doctorId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
