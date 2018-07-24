package com.yd.yourdoctorandroid.networks.models;

import java.util.ArrayList;
import java.util.Date;
public class Doctor {
    private String id;
    private String phoneNumber;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String avatar;
    private String birthday;
    private String address;
    private int status;
    private long remainMoney;
    private float currentRating;
    private ArrayList<Certification> certificates;
    private ArrayList<String> idSpecialist;
    private String universityGraduate;
    private String yearGraduate;
    private String placeWorking;

    public Doctor() {
    }

    public Doctor(String doctorId, String phoneNumber, String password, String firstName, String middleName, String lastName, String avatar, String birthday, String address, int status, long remainMoney, float currentRating, ArrayList<Certification> certificates, ArrayList<String> idSpecialist, String universityGraduate, String yearGraduate, String placeWorking) {
        this.id = doctorId;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.birthday = birthday;
        this.address = address;
        this.status = status;
        this.remainMoney = remainMoney;
        this.currentRating = currentRating;
        this.certificates = certificates;
        this.idSpecialist = idSpecialist;
        this.universityGraduate = universityGraduate;
        this.yearGraduate = yearGraduate;
        this.placeWorking = placeWorking;
    }

    public String getDoctorId() {
        return id;
    }

    public void setDoctorId(String doctorId) {
        this.id = doctorId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRemainMoney() {
        return remainMoney;
    }

    public void setRemainMoney(long remainMoney) {
        this.remainMoney = remainMoney;
    }

    public float getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(float currentRating) {
        this.currentRating = currentRating;
    }

    public ArrayList<Certification> getCertificates() {
        return certificates;
    }

    public void setCertificates(ArrayList<Certification> certificates) {
        this.certificates = certificates;
    }

    public ArrayList<String> getIdSpecialist() {
        return idSpecialist;
    }

    public void setIdSpecialist(ArrayList<String> idSpecialist) {
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
