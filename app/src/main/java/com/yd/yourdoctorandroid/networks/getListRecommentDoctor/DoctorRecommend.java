package com.yd.yourdoctorandroid.networks.getListRecommentDoctor;

public class DoctorRecommend {
    private String doctorId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String avatar;
    private float currentRating;

    public DoctorRecommend(String doctorId, String firstName, String middleName, String lastName, String avatar, float currentRating) {
        this.doctorId = doctorId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.currentRating = currentRating;
    }

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
}
