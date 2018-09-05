package com.yd.yourdoctorandroid.networks.getListDoctorFavorite;

public class FavoriteDoctor {
    private String doctorId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String avatar;
    private float currentRating;
    private String specialist;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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

    public float getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(float currentRating) {
        this.currentRating = currentRating;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public String getFullName() {
        if(!this.middleName.isEmpty()) {
            return this.firstName + " " + this.middleName + " " + this.lastName;
        }else {
            return this.firstName + " " + this.lastName;
        }
    }
}
