package com.yd.yourdoctorandroid.networks.getDoctorDetailProfile;

import com.yd.yourdoctorandroid.models.Certification;

import java.util.List;

public class DoctorDetail {

    private DoctorID doctorId;
    private float currentRating;
    private List<Certification> certificates;
    private List<SpecialistDetail> idSpecialist;
    private int gender;
    private String universityGraduate;
    private String yearGraduate;
    private String placeWorking;

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

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
