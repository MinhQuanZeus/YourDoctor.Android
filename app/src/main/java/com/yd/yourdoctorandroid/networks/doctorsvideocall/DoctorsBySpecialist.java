package com.yd.yourdoctorandroid.networks.doctorsvideocall;

import com.yd.yourdoctorandroid.models.Doctor;

import java.util.List;

public class DoctorsBySpecialist {

    private List<Doctor> doctorList;

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public DoctorsBySpecialist(List<Doctor> doctorList) {

        this.doctorList = doctorList;
    }
}
