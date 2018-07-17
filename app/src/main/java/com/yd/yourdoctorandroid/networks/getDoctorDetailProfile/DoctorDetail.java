package com.yd.yourdoctorandroid.networks.getDoctorDetailProfile;

import com.yd.yourdoctorandroid.networks.models.Certification;
import com.yd.yourdoctorandroid.networks.models.Specialist;

import java.util.List;

public class DoctorDetail {

    DoctorID doctorId;
    float currentRating;
    List<Certification> certificates;
    List<SpecialistDetail> idSpecialist;
    String universityGraduate;
    String yearGraduate;
    String placeWorking;

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

    public List<Certification> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certification> certificates) {
        this.certificates = certificates;
    }

    public List<SpecialistDetail> getIdSpecialist() {
        return idSpecialist;
    }

    public void setIdSpecialist(List<SpecialistDetail> idSpecialist) {
        this.idSpecialist = idSpecialist;
    }

    public String getUniversityGraduate() {
        return universityGraduate;
    }

    public void setUniversityGraduate(String universityGraduate) {
        this.universityGraduate = universityGraduate;
    }

    public String getYearGraduate() {
        return yearGraduate;
    }

    public void setYearGraduate(String yearGraduate) {
        this.yearGraduate = yearGraduate;
    }

    public String getPlaceWorking() {
        return placeWorking;
    }

    public void setPlaceWorking(String placeWorking) {
        this.placeWorking = placeWorking;
    }
}
