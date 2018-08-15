package com.yd.yourdoctorandroid.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
    private int gender;
    private int status;
    private float remainMoney;
    private float currentRating;
    private ArrayList<Certification> certificates;
    private ArrayList<String> idSpecialist;
    private String universityGraduate;
    private String yearGraduate;
    private String placeWorking;
    private boolean isFavorite = false;
    private boolean isOnline = false;

    public Doctor() {
    }

    public Doctor(String id, String phoneNumber, String password, String firstName, String middleName, String lastName, String avatar, String birthday, String address, int status, float remainMoney, float currentRating, ArrayList<Certification> certificates, ArrayList<String> idSpecialist, String universityGraduate, String yearGraduate, String placeWorking, boolean isFavorite, boolean isOnline) {
        this.id = id;
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
        this.isFavorite = isFavorite;
        this.isOnline = isOnline;
    }

    public String getDoctorId() {
        return id;
    }

    public void setDoctorId(String id) {
        this.id = id;
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

    public int getRemainMoney() {
        return (int) Math.round(remainMoney);
    }

    public void setRemainMoney(float remainMoney) {
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getFullName() {
        if(!this.middleName.isEmpty()) {
            return this.firstName + " " + this.middleName + " " + this.lastName;
        }else {
            return this.firstName + " " + this.lastName;
        }
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id='" + id + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", remainMoney=" + remainMoney +
                ", currentRating=" + currentRating +
                ", certificates=" + certificates +
                ", idSpecialist=" + idSpecialist +
                ", universityGraduate='" + universityGraduate + '\'' +
                ", yearGraduate='" + yearGraduate + '\'' +
                ", placeWorking='" + placeWorking + '\'' +
                ", isFavorite=" + isFavorite +
                ", isOnline=" + isOnline +
                '}';
    }
}
