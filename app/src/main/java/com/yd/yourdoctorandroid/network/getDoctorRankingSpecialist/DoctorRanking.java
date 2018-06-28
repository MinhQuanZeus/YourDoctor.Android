package com.yd.yourdoctorandroid.network.getDoctorRankingSpecialist;

public class DoctorRanking {

    double current_rating;
    DoctorID doctor_id;

    public double getCurrent_rating() {
        return current_rating;
    }

    public void setCurrent_rating(double current_rating) {
        this.current_rating = current_rating;
    }

    public DoctorID getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(DoctorID doctor_id) {
        this.doctor_id = doctor_id;
    }
}
