package com.yd.yourdoctorandroid.networks.getDoctorRankingSpecialist;

import com.yd.yourdoctorandroid.models.Doctor;
import com.yd.yourdoctorandroid.networks.changePassword.PasswordResponse;
import com.yd.yourdoctorandroid.networks.getDoctorDetailProfile.DoctorID;

public class DoctorRanking {

    private DoctorID doctorId;

    private float currentRating;

    public DoctorID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(DoctorID doctorId) {
        this.doctorId = doctorId;
    }

    public float getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(float currentRating) {
        this.currentRating = currentRating;
    }
}
