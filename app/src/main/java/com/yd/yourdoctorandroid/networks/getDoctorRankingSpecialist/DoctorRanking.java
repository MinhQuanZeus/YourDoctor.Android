package com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist;

public class DoctorRanking {

    double currentRating;
    DoctorID doctorId;

    public double getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(double currentRating) {
        this.currentRating = currentRating;
    }

    public DoctorID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(DoctorID doctorId) {
        this.doctorId = doctorId;
    }
}
