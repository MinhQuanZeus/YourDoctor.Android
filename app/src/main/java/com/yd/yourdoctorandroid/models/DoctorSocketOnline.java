package com.yd.yourdoctorandroid.models;

public class DoctorSocketOnline {
    private String doctorId;
    private String socketId;

    public DoctorSocketOnline(String doctorId, String socketId) {
        this.doctorId = doctorId;
        this.socketId = socketId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    @Override
    public String toString() {
        return "DoctorSocketOnline{" +
                "doctorId='" + doctorId + '\'' +
                ", socketId='" + socketId + '\'' +
                '}';
    }
}
